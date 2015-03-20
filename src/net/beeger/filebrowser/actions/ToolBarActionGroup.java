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
public class ToolBarActionGroup extends ShutdownAbleActionGroup implements FileListHandlerAware
{
  public ToolBarActionGroup(SynchronizeAction synchronizeAction,
                            FavoritesAction favoritesAction,
                            AutoscrollToSourceAction autoscrollToSourceAction,
                            AutoScrollFromSourceAction autoScrollFromSourceAction,
                            GroupFoldersAction groupFoldersAction,
                            FocusEditorAction focusEditorAction,
                            DontShowUnknownFilesAction dontShowUnknownFilesAction, GoToAction goToAction,
                            GoUpAction goUpAction, GoToParentAction goToParentAction,
                            ActivateSelectedAction activateSelectedAction, GoBackAction goBackAction, GoForwardAction goForwardAction)
  {
    _synchronizeAction = synchronizeAction;
    _goToAction = goToAction;
    _favoritesAction = favoritesAction;
    _goUpAction = goUpAction;
    _goToParentAction = goToParentAction;
    _activateSelectedAction = activateSelectedAction;
    _goBackAction = goBackAction;
    _goForwardAction = goForwardAction;
    add(_synchronizeAction);
    add(_favoritesAction);
    add(_goToAction);
    add(_goUpAction);
    add(_goBackAction);
    add(_goForwardAction);
    addSeparator();
    add(autoscrollToSourceAction);
    add(autoScrollFromSourceAction);
    add(groupFoldersAction);
    add(focusEditorAction);
    add(dontShowUnknownFilesAction);

  }

  public void setFileListHandler(FileListHandler fileListHandler)
  {
    _favoritesAction.setFileListHandler(fileListHandler);
    _synchronizeAction.setFileListHandler(fileListHandler);
    _goToAction.setFileListHandler(fileListHandler);
    _goUpAction.setFileListHandler(fileListHandler);
    _goToParentAction.setFileListHandler(fileListHandler);
    _activateSelectedAction.setFileListHandler(fileListHandler);
    _goBackAction.setFileListHandler(fileListHandler);
    _goForwardAction.setFileListHandler(fileListHandler);
  }

  private final FavoritesAction _favoritesAction;
  private final GoUpAction _goUpAction;
  private final GoToParentAction _goToParentAction;
  private final ActivateSelectedAction _activateSelectedAction;
  private final GoBackAction _goBackAction;
  private final GoForwardAction _goForwardAction;
  private final SynchronizeAction _synchronizeAction;
  private final GoToAction _goToAction;
}
