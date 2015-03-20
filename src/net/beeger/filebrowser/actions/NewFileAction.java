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

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.util.IconLoader;
import net.beeger.filebrowser.DialogProvider;
import net.beeger.filebrowser.FileOperationHandler;
import net.beeger.filebrowser.FolderDisplayManager;

import java.io.IOException;

@SuppressWarnings({"ComponentNotRegistered"})
public class NewFileAction extends AbstractFileListHandlerAwareAction implements WrappableAction
{
  public NewFileAction(Application application, FolderDisplayManager folderDisplayManager, DialogProvider dialogProvider, FileOperationHandler fileOperationHandler)
  {
    super("New file...", "Create a new file", IconLoader.getIcon("/fileTypes/text.png"));
    _folderDisplayManager = folderDisplayManager;
    _application = application;
    _dialogProvider = dialogProvider;
    _fileOperationHandler = fileOperationHandler;
  }

  public void update(AnActionEvent e)
  {
    e.getPresentation().setEnabled(_folderDisplayManager.getCurrentBaseFolder() != null);
  }

  public void updateForShortcutAccess(AnActionEvent e)
  {
    e.getPresentation().setEnabled(getFileListHandler().hasListFocus() && _folderDisplayManager.getCurrentBaseFolder() != null);
  }

  public void actionPerformed(AnActionEvent event)
  {
    _application.runWriteAction(new Runnable()
    {
      public void run()
      {
        String newFileName = _dialogProvider.getInputFromUser("Enter a new file name");
        if (newFileName != null)
        {
          try
          {
            _fileOperationHandler.createNewFile(newFileName, _folderDisplayManager.getCurrentBaseFolder());
            _folderDisplayManager.refreshCurrentFolderDisplay(false);
          }
          catch (IOException e)
          {
            throw new RuntimeException("Error on creating new file " + newFileName, e);
          }
        }
      }
    });
  }

  private final FolderDisplayManager _folderDisplayManager;
  private final Application _application;
  private final DialogProvider _dialogProvider;
  private final FileOperationHandler _fileOperationHandler;
}
