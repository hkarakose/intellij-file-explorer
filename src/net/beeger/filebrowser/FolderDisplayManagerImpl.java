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

import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import net.beeger.filebrowser.settings.ApplicationSettings;
import net.beeger.filebrowser.settings.ProjectSettings;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class FolderDisplayManagerImpl implements FolderDisplayManager
{
  public FolderDisplayManagerImpl(Application application,
                                  ProjectSettings projectSettings, ApplicationSettings applicationSettings,
                                  FileSystemHandler fileSystemHandler, FileTypeManager fileTypeManager, FileEditorManager fileEditorManager, BrowseHistory browseHistory)
  {
    _application = application;
    _fileSystemHandler = fileSystemHandler;
    _applicationSettings = applicationSettings;
    _fileTypeManager = fileTypeManager;
    _fileEditorManager = fileEditorManager;
    _browseHistory = browseHistory;
    _unfilteredFiles = new ArrayList<VirtualFile>();
    _projectSettings = projectSettings;
    _virtualFileComparator = new VirtualFileComparator(_projectSettings);
    _listeners = new ArrayList<FolderChangeListener>();
  }

  public void start(VirtualFile projectBaseFolder, FileListHandler fileListHandler)
  {
    _fileListHandler = fileListHandler;
    VirtualFile folder = null;
    if (_projectSettings.getCurrentFolder() != null)
    {
      folder = _fileSystemHandler.findFileByPath(_projectSettings.getCurrentFolder());
    }
    if (folder == null)
    {
      folder = projectBaseFolder;
    }
    showFolderContents(folder);
  }

  @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
  public boolean isSelectedEntryFolder()
  {
    List<VirtualFile> selectedFiles = _fileListHandler.getSelectedFiles();
    return selectedFiles.size() == 1 && selectedFiles.get(0).isDirectory();
  }

  public void showFolderContents(String folder)
  {
    VirtualFile foundFolder = _fileSystemHandler.findFileByPath(folder);
    if (foundFolder != null)
    {
      showFolderContents(foundFolder);
    }
    else
    {
      _projectSettings.removeFavorite(folder);
      _applicationSettings.removeFavorite(folder);
    }
  }

  public void showFolderContents(VirtualFile folder)
  {
    _currentBaseFolder = folder;
    _fileListHandler.resetFilter();
    refreshCurrentFolderDisplay(true);
    for (FolderChangeListener listener : _listeners)
    {
      listener.folderChanged();
    }
  }

  public void activate(@NotNull String fileToActivate, boolean checkAssociation)
  {
    activate(_fileSystemHandler.findFileByPath(fileToActivate), checkAssociation, true);
  }

  public void activate(@NotNull VirtualFile fileToActivate)
  {
    activate(fileToActivate, true, true);
  }

  public void activate(@NotNull VirtualFile fileToActivate, boolean checkAssociation, boolean addToHistory)
  {
    if (fileToActivate.isDirectory())
    {
      VirtualFile currentBaseFolder = getCurrentBaseFolder();
      showFolderContents(fileToActivate);
      if (getCurrentBaseFolder() != null && currentBaseFolder != null
          && getCurrentBaseFolder().equals(currentBaseFolder.getParent()))
      {
        _fileListHandler.setSelectedFile(currentBaseFolder);
      }
      if (addToHistory)
      {
        _browseHistory.add(fileToActivate);
      }
    }
    else
    {
      if (checkAssociation)
      {
        if (_fileTypeManager.getKnownFileTypeOrAssociate(fileToActivate) != null)
        {
          _fileEditorManager.openFile(fileToActivate, _projectSettings.isFocusEditor());
        }
      }
      else
      {
        _fileEditorManager.openFile(fileToActivate, _projectSettings.isFocusEditor());
      }
    }
  }


  public void updateFileList()
  {
    String filterString = _fileListHandler.getFilter().toLowerCase();
    List<VirtualFile> filteredFiles = new ArrayList<VirtualFile>();
    if (filterString.length() > 0)
    {
      for (VirtualFile virtualFile : _unfilteredFiles)
      {
        if (virtualFile.getName().toLowerCase().indexOf(filterString) >= 0)
        {
          filteredFiles.add(virtualFile);
        }
      }
    }
    else
    {
      filteredFiles.addAll(_unfilteredFiles);
    }
    if (_applicationSettings.isShowParent() && _currentBaseFolder != null && _currentBaseFolder.getParent() != null)
    {
      filteredFiles.add(0, _currentBaseFolder.getParent());
    }
    _fileListHandler.setListContents(filteredFiles);
    _fileListHandler.setSelectedFile(filteredFiles.get(0));
    _fileListHandler.setFilterValid(
        !(filteredFiles.size() == 1 && !ROOTS.equals(_currentFolderName) ||
            filteredFiles.size() == 0 && ROOTS.equals(_currentFolderName)));
  }

  public void refreshCurrentFolderDisplay(boolean wrapInWriteAction)
  {
    if (wrapInWriteAction)
    {
      _application.runWriteAction(new Runnable()
      {
        public void run()
        {
          refreshCurrentFolderDisplayBare();
        }
      });
    }
    else
    {
      refreshCurrentFolderDisplayBare();
    }
  }

  private void refreshCurrentFolderDisplayBare()
  {
    if (getCurrentBaseFolder() != null)
    {
      getCurrentBaseFolder().refresh(false, false, new Runnable()
      {
        public void run()
        {
          _unfilteredFiles.clear();
          if (!_projectSettings.isDontShowUnknown())
          {
            _unfilteredFiles.addAll(Arrays.asList(_currentBaseFolder.getChildren()));
          }
          else
          {
            for (VirtualFile file : _currentBaseFolder.getChildren())
            {
              if (file.isDirectory() || !_fileSystemHandler.hasUnknownFileType(file))
              {
                _unfilteredFiles.add(file);
              }
            }
          }
          Collections.sort(_unfilteredFiles, _virtualFileComparator);
          _currentFolderName = _currentBaseFolder.getPath();
          _projectSettings.setCurrentFolder(_currentBaseFolder.getPath());

          updateFileList();
        }
      });
    }
    else
    {
      _unfilteredFiles.clear();
      _unfilteredFiles.addAll(_fileSystemHandler.getRoots());
      Collections.sort(_unfilteredFiles, _virtualFileComparator);
      _currentFolderName = ROOTS;
      _projectSettings.setCurrentFolder(null);
      updateFileList();
    }
  }

  public void select(@NotNull VirtualFile file)
  {
    showFolderContents(file.getParent());
    _fileListHandler.setSelectedFile(file);

  }

  public void resortFileList()
  {
    Collections.sort(_unfilteredFiles, _virtualFileComparator);
    updateFileList();
  }


  public String getCurrentFolderName()
  {
    return _currentFolderName;
  }

  public VirtualFile getCurrentBaseFolder()
  {
    return _currentBaseFolder;
  }

  public void addFolderChangeListener(FolderChangeListener listener)
  {
    _listeners.add(listener);
  }

  public void removeFolderChangeListener(FolderChangeListener listener)
  {
    _listeners.remove(listener);
  }

  private VirtualFile _currentBaseFolder;
  private final Application _application;
  private final FileSystemHandler _fileSystemHandler;
  private final ApplicationSettings _applicationSettings;
  private final FileTypeManager _fileTypeManager;
  private final FileEditorManager _fileEditorManager;
  private final BrowseHistory _browseHistory;
  private FileListHandler _fileListHandler;
  private final List<VirtualFile> _unfilteredFiles;
  private final ProjectSettings _projectSettings;
  private final VirtualFileComparator _virtualFileComparator;
  private String _currentFolderName;
  private final List<FolderChangeListener> _listeners;

  private static final String ROOTS = "[Roots]";
}
