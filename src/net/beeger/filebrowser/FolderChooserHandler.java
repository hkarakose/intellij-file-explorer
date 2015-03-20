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

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.ComboboxWithBrowseButton;
import net.beeger.filebrowser.settings.ApplicationSettings;
import net.beeger.filebrowser.settings.ProjectSettings;

import javax.swing.JComboBox;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.util.ArrayList;
import java.util.List;

public class FolderChooserHandler implements PopupMenuListener
{
  public FolderChooserHandler(ComboboxWithBrowseButton comboboxWithBrowseButton, Project project,
                              FileSystemHandler fileSystemHandler, ProjectSettings projectSettings,
                              ApplicationSettings applicationSettings)
  {
    _fileSystemHandler = fileSystemHandler;
    _comboboxWithBrowseButton = comboboxWithBrowseButton;
    _projectSettings = projectSettings;
    _applicationSettings = applicationSettings;

    FileChooserDescriptor fileChooserDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
    _comboboxWithBrowseButton.addBrowseFolderListener(project, fileChooserDescriptor);
    _comboboxWithBrowseButton.getComboBox().setEditable(true);

    _history = new ArrayList<String>();

    _comboboxWithBrowseButton.getComboBox().addPopupMenuListener(this);
  }

  public VirtualFile getChosenFolder()
  {
    JComboBox comboBox = _comboboxWithBrowseButton.getComboBox();
    VirtualFile chosenFolder = _fileSystemHandler.findFileByPath((String) comboBox.getEditor().getItem());
    if (chosenFolder != null)
    {
      String folderPath = chosenFolder.getPath();
      _history.add(0, folderPath);
      for (int i = _history.size() - 1; i > 0; i--)
      {
        if (folderPath.equals(_history.get(i)))
        {
          _history.remove(i);
        }
      }
      while (_history.size() > 10)
      {
        _history.remove(10);
      }
    }

    return chosenFolder;
  }

  public void popupMenuWillBecomeVisible(PopupMenuEvent e)
  {
    JComboBox comboBox = _comboboxWithBrowseButton.getComboBox();
    comboBox.removeAllItems();
    for (String entry : _history)
    {
      comboBox.addItem(entry);
    }

    for (String favorite : _projectSettings.getFavorites())
    {
      comboBox.addItem(favorite);
    }

    for (String favorite : _applicationSettings.getFavorites())
    {
      comboBox.addItem(favorite);
    }
  }

  public void popupMenuWillBecomeInvisible(PopupMenuEvent e)
  {
  }

  public void popupMenuCanceled(PopupMenuEvent e)
  {
  }

  private final List<String> _history;
  private final ComboboxWithBrowseButton _comboboxWithBrowseButton;
  private final ProjectSettings _projectSettings;
  private final ApplicationSettings _applicationSettings;
  private final FileSystemHandler _fileSystemHandler;
}
