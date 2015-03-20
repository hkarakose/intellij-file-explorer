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

import net.beeger.filebrowser.FolderDisplayManager;
import net.beeger.filebrowser.BrowseHistory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.util.IconLoader;

@SuppressWarnings({"ComponentNotRegistered"})
public class GoForwardAction extends AbstractFileListHandlerAwareAction implements WrappableAction
{
  public GoForwardAction(FolderDisplayManager folderDisplayManager, BrowseHistory browseHistory)
  {
    super("Forward", "Go forward in history", IconLoader.getIcon("/actions/forward.png"));
    _folderDisplayManager = folderDisplayManager;
    _browseHistory = browseHistory;
  }

  public void actionPerformed(AnActionEvent e)
  {
    _browseHistory.goForward();
    VirtualFile toActivate = _browseHistory.getCurrentFolder();
    _folderDisplayManager.activate(toActivate, true, false);
  }

  public void updateForShortcutAccess(AnActionEvent e)
  {
    e.getPresentation()
        .setEnabled(getFileListHandler().hasListFocus() && _browseHistory.canGoForward());
  }

  public void update(AnActionEvent e)
  {
    e.getPresentation().setEnabled(_browseHistory.canGoForward());
  }

  private final FolderDisplayManager _folderDisplayManager;
  private final BrowseHistory _browseHistory;

}