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

import com.intellij.ide.SelectInContext;
import com.intellij.ide.SelectInTarget;
import com.intellij.openapi.wm.ToolWindowManager;

public class SelectInFileBrowserTarget implements SelectInTarget
{
  public SelectInFileBrowserTarget(FileBrowser fileBrowser, ToolWindowManager toolWindowManager)
  {
    _fileBrowser = fileBrowser;
    _toolWindowManager = toolWindowManager;
  }

  public boolean canSelect(SelectInContext context)
  {
    return true;
  }

  public void selectIn(final SelectInContext context, final boolean requestFocus)
  {
    Runnable runnable = new Runnable()
    {
      public void run()
      {
        _fileBrowser.select(context.getVirtualFile());
      }
    };

    if (requestFocus)
    {
      _toolWindowManager.getToolWindow(getToolWindowId()).activate(runnable);
    }
    else
    {
      _toolWindowManager.getToolWindow(getToolWindowId()).show(runnable);
    }
  }

  public String getToolWindowId()
  {
    return FileBrowser.FILEBROWSER_TOOLWINDOW_ID;
  }

  public String getMinorViewId()
  {
    return null;
  }

  public float getWeight()
  {
    return 100;
  }

  public String toString()
  {
    return "FileBrowser";
  }

  private final FileBrowser _fileBrowser;
  private final ToolWindowManager _toolWindowManager;
}
