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
package net.beeger.filebrowser.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.vfs.VirtualFile;
import net.beeger.filebrowser.DestinationChooser;
import net.beeger.filebrowser.DestinationChooserConfiguration;
import net.beeger.filebrowser.DialogProvider;
import net.beeger.filebrowser.FileOperationHandler;
import net.beeger.filebrowser.FolderDisplayManager;

import java.io.IOException;
import java.util.List;

@SuppressWarnings({"ComponentNotRegistered"})
public class CopyAction extends AbstractFileListHandlerAwareAction implements WrappableAction
{
  public CopyAction(Application application, FolderDisplayManager folderDisplayManager,
                    FileOperationHandler fileOperationHandler, DestinationChooser destinationChooser,
                    DialogProvider dialogProvider)
  {
    super("Copy...", "Copy the previously marked files into the current folder", null);
    _application = application;
    _fileOperationHandler = fileOperationHandler;
    _folderDisplayManager = folderDisplayManager;
    _destinationChooser = destinationChooser;
    _dialogProvider = dialogProvider;
  }

  public void update(AnActionEvent e)
  {
    e.getPresentation()
        .setEnabled(
            _folderDisplayManager.getCurrentBaseFolder() != null && getFileListHandler().getSelectedFiles().size() > 0);
  }

  public void updateForShortcutAccess(AnActionEvent e)
  {
    e.getPresentation()
        .setEnabled(getFileListHandler().hasListFocus() && _folderDisplayManager.getCurrentBaseFolder() != null &&
            getFileListHandler().getSelectedFiles().size() > 0);
  }

  public void actionPerformed(AnActionEvent e)
  {
    final List<VirtualFile> files = getFileListHandler().getSelectedFiles();
    if (_destinationChooser.askForDestination(DestinationChooserConfiguration.COPY, files))
    {
      final VirtualFile destinationFolder = _destinationChooser.getDestinationFolder();
      if (destinationFolder != null)
      {
        _application.runWriteAction(new Runnable()
        {
          public void run()
          {
            try
            {
              for (VirtualFile file : files)
              {
                String newName = file.getName();
                if (_destinationChooser.isSingleFileOperation())
                {
                  newName = _destinationChooser.getNewName();
                }
                VirtualFile destinationFile = destinationFolder.findChild(newName);
                boolean executeCopy = true;
                if (destinationFile != null)
                {
                  executeCopy = false;
                  if(_dialogProvider.getConfirmationFromUser("A file or folder with the name " + newName +
                      " already exists in folder " + destinationFolder.getPath() + ". Do you want to replace it?"))
                  {
                    destinationFile.delete(this);
                    executeCopy = true;
                  }
                }
                if (executeCopy)
                {
                  _fileOperationHandler.copy(file, destinationFolder, newName);
                }
              }
              _folderDisplayManager.refreshCurrentFolderDisplay(false);
            }
            catch (IOException e1)
            {
              throw new RuntimeException("Could not copy files into " + _folderDisplayManager.getCurrentFolderName(),
                  e1);
            }
          }
        });
      }
      else
      {
        _dialogProvider.showInfo("The chosen folder does not exist. You need to create it first.");
      }
    }
  }

  private final Application _application;
  private final FileOperationHandler _fileOperationHandler;
  private final FolderDisplayManager _folderDisplayManager;
  private final DestinationChooser _destinationChooser;
  private final DialogProvider _dialogProvider;
}