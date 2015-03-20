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

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

/**
 * Project component for file browser
 */
public class FileBrowserProjectComponent implements ProjectComponent
{
  public FileBrowserProjectComponent(ToolWindowManager toolWindowManager, FileBrowser fileBrowser)
  {
    _toolWindowManager = toolWindowManager;
    _fileBrowser = fileBrowser;
  }

  public void initComponent()
  {
  }

  public void disposeComponent()
  {
  }

  @NotNull
  public String getComponentName()
  {
    return "FileBrowser";
  }

  public void projectOpened()
  {
    ToolWindow window = _toolWindowManager.registerToolWindow(FileBrowser.FILEBROWSER_TOOLWINDOW_ID, _fileBrowser.getUI(), ToolWindowAnchor.LEFT);
    window.setIcon(IconLoader.getIcon("/net/beeger/filebrowser/images/filebrowser.png"));
    _fileBrowser.setToolWindow(window);
    _fileBrowser.start();
  }

  public void projectClosed()
  {
    _fileBrowser.shutdown();
    _toolWindowManager.unregisterToolWindow(FileBrowser.FILEBROWSER_TOOLWINDOW_ID);
  }

  private final ToolWindowManager _toolWindowManager;
  private final FileBrowser _fileBrowser;
}
