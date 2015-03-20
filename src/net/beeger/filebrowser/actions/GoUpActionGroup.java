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
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import net.beeger.filebrowser.FolderChangeListener;
import net.beeger.filebrowser.FolderDisplayManager;

@SuppressWarnings({"ComponentNotRegistered"})
public class GoUpActionGroup extends ShutdownAbleActionGroup implements FolderChangeListener
{
  public GoUpActionGroup(
      FolderDisplayManager folderDisplayManager)
  {
    super("Favorites", true);
    _folderDisplayManager = folderDisplayManager;
    _folderDisplayManager.addFolderChangeListener(this);

    _currentFolder = _folderDisplayManager.getCurrentBaseFolder();
    refresh();
  }

  public void shutdown()
  {
    super.shutdown();
    _folderDisplayManager.removeFolderChangeListener(this);
  }

  private void refresh()
  {
    removeAll();
    VirtualFile parent = _currentFolder != null ? _currentFolder.getParent() : null;
    while (parent != null)
    {
      final String favorite = parent.getPath();
      add(new AnAction(favorite, new StringBuilder().append("Go to ").append(favorite).toString(),
          IconLoader.getIcon("/nodes/folder.png"))
      {
        public void actionPerformed(AnActionEvent event)
        {
          _folderDisplayManager.activate(favorite, false);
//          _folderDisplayManager.showFolderContents(favorite);
//          _browseHistory.add(favorite);
        }
      });
      parent = parent.getParent();
    }
  }

  public void folderChanged()
  {
    VirtualFile currentBaseFolder = _folderDisplayManager.getCurrentBaseFolder();
    if (currentBaseFolder != null && !currentBaseFolder.equals(_currentFolder) ||
        currentBaseFolder == null && _currentFolder != null)
    {
      _currentFolder = currentBaseFolder;
      refresh();
    }
  }

  private final FolderDisplayManager _folderDisplayManager;
  private VirtualFile _currentFolder;
}