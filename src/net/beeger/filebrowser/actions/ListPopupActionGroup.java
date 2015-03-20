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

import net.beeger.filebrowser.FileListHandler;

@SuppressWarnings({"ComponentNotRegistered"})
public class ListPopupActionGroup extends ShutdownAbleActionGroup implements FileListHandlerAware
{
  public ListPopupActionGroup(NewFolderAction newFolderAction, NewFileAction newFileAction,
                              DeleteAction deleteAction,
                              RenameAction renameAction, CopyAction copyAction,
                              MoveAction moveAction)
  {
    _deleteAction = deleteAction;
    _renameAction = renameAction;
    _copyAction = copyAction;
    _moveAction1 = moveAction;
    _newFolderAction = newFolderAction;
    _newFileAction = newFileAction;

    add(_newFolderAction);
    add(_newFileAction);
    addSeparator();
    add(_deleteAction);
    add(_renameAction);
    addSeparator();
    add(_copyAction);
    add(_moveAction1);
  }

  public void setFileListHandler(FileListHandler fileListHandler)
  {
    _deleteAction.setFileListHandler(fileListHandler);
    _renameAction.setFileListHandler(fileListHandler);
    _copyAction.setFileListHandler(fileListHandler);
    _moveAction1.setFileListHandler(fileListHandler);
    _newFileAction.setFileListHandler(fileListHandler);
    _newFolderAction.setFileListHandler(fileListHandler);
  }

  private final DeleteAction _deleteAction;
  private final RenameAction _renameAction;
  private final CopyAction _copyAction;
  private final MoveAction _moveAction1;
  private final NewFolderAction _newFolderAction;
  private final NewFileAction _newFileAction;
}
