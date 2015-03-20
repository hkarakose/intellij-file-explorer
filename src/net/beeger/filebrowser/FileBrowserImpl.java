/*
  Copyright (c) 2007, Robert F. Beeger (robert at beeger dot net)
  All rights reserved.

  Redistribution and use in source and binary forms, with or without modification,
  are permitted provided that the following conditions are met:

      * Redistributions of source code must retain the above copyright notice, this list
        of conditions and the following disclaimer.
      * Redistributions in binary form must reproduce the above copyright notice, this
        list of conditions and the following disclaimer in the documentation and/or other
        materials provided with the distribution.
      * Neither the name of 'Robert F. Beeger' nor the names of his contributors may be
        used to endorse or promote products derived from this software without specific
        prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
  EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
  MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
  THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
  OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
  TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package net.beeger.filebrowser;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerAdapter;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.application.Application;
import net.beeger.filebrowser.actions.FileListHandlerAware;
import net.beeger.filebrowser.actions.ListPopupActionGroup;
import net.beeger.filebrowser.actions.ToolBarActionGroup;
import net.beeger.filebrowser.settings.ApplicationSettings;
import net.beeger.filebrowser.settings.ProjectSettings;
import net.beeger.filebrowser.settings.SettingsChangedListener;
import org.jetbrains.annotations.NotNull;

import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.TransferHandler;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * File Browser.
 */
public class FileBrowserImpl implements SettingsChangedListener, FileBrowser, FileListHandler, ShutdownAble
{
  public FileBrowserImpl(Project project,
                         ApplicationSettings applicationSettings, ProjectSettings projectSettings,
                         FileEditorManager fileEditorManager, ActionManager actionManager,
                         FolderDisplayManager folderDisplayManager,
                         FileTransferHandler fileTransferHandler, FileSystemHandler fileSystemHandler,
                         ListPopupActionGroup listPopupActionGroup,
                         ToolBarActionGroup toolBarActionGroup, FileListCellRenderer fileListCellRenderer, Application application
  )
  {
    _project = project;
    _projectSettings = projectSettings;
    _fileTransferHandler = fileTransferHandler;
    _fileSystemHandler = fileSystemHandler;
    _fileListCellRenderer = fileListCellRenderer;
    _application = application;
    _projectSettings.addSettingsChangedListener(this);
    _applicationSettings = applicationSettings;
    _applicationSettings.addSettingsChangedListener(this);
    _folderDisplayManager = folderDisplayManager;


    _listPopupActionGroup = listPopupActionGroup;
    checkFileListHandlerAwareness(_listPopupActionGroup);
    ActionPopupMenu popupMenu = actionManager.createActionPopupMenu("", listPopupActionGroup);

    configureFileList(popupMenu);
    _toolBarActionGroup = toolBarActionGroup;
    checkFileListHandlerAwareness(_toolBarActionGroup);
//    registerActions(_toolBarActionGroup, actionManager, defaultKeyMap);
    ActionToolbar actionToolbar = actionManager.createActionToolbar("", _toolBarActionGroup, true);
    _filter.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    _ui = new JPanel(new BorderLayout(3, 3));
    _ui.add(actionToolbar.getComponent(), BorderLayout.NORTH);
    _ui.add(_main);

    fileEditorManager.addFileEditorManagerListener(new FileEditorManagerAdapter()
    {
      public void selectionChanged(FileEditorManagerEvent event)
      {
        onCurrentlyEditedFileChanged(event.getNewFile() != null ? event.getNewFile().getPath() : null);
      }
    });
  }

  private void checkFileListHandlerAwareness(Object object)
  {
    if (object instanceof FileListHandlerAware)
    {
      ((FileListHandlerAware) object).setFileListHandler(this);
    }
  }

  public void start()
  {
    _folderDisplayManager.start(_project.getBaseDir(), this);
    _toolWindow.setTitle(_folderDisplayManager.getCurrentFolderName());
  }

  public void shutdown()
  {
    _projectSettings.removeSettingsChangedListener(this);
    _applicationSettings.removeSettingsChangedListener(this);

    _listPopupActionGroup.shutdown();
    _toolBarActionGroup.shutdown();
  }

  public JComponent getUI()
  {
    return _ui;
  }

  public void select(@NotNull VirtualFile file)
  {
    _folderDisplayManager.select(file);
  }

  private void configureFileList(ActionPopupMenu popupMenu)
  {
    _fileList.setCellRenderer(_fileListCellRenderer);
    _fileList.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    _fileList.addKeyListener(new KeyAdapter()
    {
      public void keyTyped(KeyEvent e)
      {
        onListKeyTyped(e);
      }

      public void keyPressed(KeyEvent e)
      {
        onListKeyPressed(e);
      }
    });

    _fileList.addListSelectionListener(new ListSelectionListener()
    {
      public void valueChanged(ListSelectionEvent e)
      {
        onListSelectionChanged();
      }
    });

    _fileList.setComponentPopupMenu(popupMenu.getComponent());

    _fileList.addMouseListener(new MouseAdapter()
    {
      public void mouseClicked(MouseEvent e)
      {
        if (e.getClickCount() == 2)
        {
          activate(true);
        }
      }
    });
    _fileList.setTransferHandler(_fileTransferHandler);
    _fileList.setDragEnabled(true);
    InputMap imap = _fileList.getInputMap();
    imap.put(KeyStroke.getKeyStroke("ctrl V"), TransferHandler.getPasteAction().getValue(Action.NAME));

	ActionMap map = _fileList.getActionMap();
    map.put(TransferHandler.getPasteAction().getValue(Action.NAME), TransferHandler.getPasteAction());
  }

  private void onCurrentlyEditedFileChanged(String path)
  {
    if (_projectSettings.isAutoScrollFromSource() && path != null)
    {
      final VirtualFile file = _fileSystemHandler.findFileByPath(path);
      if (file != null)
      {
        final VirtualFile parent = file.getParent();
        if (parent != null)
        {
          _application.invokeLater(new Runnable()
          {
            public void run()
            {
              _folderDisplayManager.activate(parent);
              _fileList.setSelectedValue(file, true);
            }
          });
        }
      }
    }
  }

  private void onListSelectionChanged()
  {
    if (_projectSettings.isAutoScrollToSource() && !_folderDisplayManager.isSelectedEntryFolder())
    {
      activate(false);
    }
  }

  private void onListKeyTyped(KeyEvent e)
  {
    if (!Character.isISOControl(e.getKeyChar()))
    {
      _filter.setText(_filter.getText() + e.getKeyChar());
      _folderDisplayManager.updateFileList();
    }
  }

  private void onListKeyPressed(KeyEvent e)
  {
    if (hasListFocus())
    {
      if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
      {
        String filterText = _filter.getText();
        if (filterText.length() > 0)
        {
          filterText = filterText.substring(0, filterText.length() - 1);
        }
        _filter.setText(filterText);
        _folderDisplayManager.updateFileList();
      }
    }
  }

  private void activate(boolean checkAssociation)
  {
    if (_fileList.getSelectedValue() != null)
    {
      VirtualFile file = (VirtualFile) _fileList.getSelectedValue();
      _folderDisplayManager.activate(file, checkAssociation, true);
    }
  }

  public void setToolWindow(ToolWindow toolWindow)
  {
    _toolWindow = toolWindow;
  }

  public void settingsChanged()
  {
    if (_currentlyGroupingFolders != _projectSettings.isGroupFolders())
    {
      _currentlyGroupingFolders = _projectSettings.isGroupFolders();
      _folderDisplayManager.resortFileList();

    }
    if (_currentlyNotShowingUnkown != _projectSettings.isDontShowUnknown())
    {
      _currentlyNotShowingUnkown = _projectSettings.isDontShowUnknown();
      _folderDisplayManager.refreshCurrentFolderDisplay(true);
    }
  }

  public String getFilter()
  {
    return _filter.getText();
  }

  public void resetFilter()
  {
    _filter.setText("");
  }

  @NotNull
  public List<VirtualFile> getSelectedFiles()
  {
    List<VirtualFile> result = new ArrayList<VirtualFile>();
    for (Object file : _fileList.getSelectedValues())
    {
      result.add((VirtualFile) file);
    }
    return result;
  }

  public void setSelectedFile(VirtualFile file)
  {
    _fileList.setSelectedValue(file, true);
  }

  public void setListContents(List<VirtualFile> files)
  {
    _fileList.setListData(files.toArray());
    _toolWindow.setTitle(_folderDisplayManager.getCurrentFolderName());
  }

  public void setFilterValid(boolean valid)
  {
    if (valid)
    {
      _filter.setForeground(Color.BLACK);
    }
    else
    {
      _filter.setForeground(Color.RED);
    }
  }

  public boolean hasListFocus()
  {
    return _fileList.isFocusOwner();
  }

  private JList _fileList;
  private JPanel _main;
  private JTextField _filter;
  private final Project _project;
  private final ProjectSettings _projectSettings;
  private final FileTransferHandler _fileTransferHandler;
  private final FileSystemHandler _fileSystemHandler;
  private final FileListCellRenderer _fileListCellRenderer;
  private final Application _application;
  private final JPanel _ui;
  private ToolWindow _toolWindow;
  private final FolderDisplayManager _folderDisplayManager;
  private final ApplicationSettings _applicationSettings;
  private boolean _currentlyGroupingFolders;
  private boolean _currentlyNotShowingUnkown;
  private final ListPopupActionGroup _listPopupActionGroup;
  private final ToolBarActionGroup _toolBarActionGroup;
}
