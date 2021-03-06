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
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import static org.junit.Assert.*;

import com.kodfarki.intellij.plugins.filebrowser.util.FileBrowserConstants;
import org.easymock.IAnswer;
import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;

public class SelectInFileBrowserTargetTest
{
  @Test
  public void testSelectInWithRequestFocus()
  {
    SelectInContext selectInContextMock = createMock(SelectInContext.class);
    VirtualFile fileMock = createMock(VirtualFile.class);
    expect(selectInContextMock.getVirtualFile()).andReturn(fileMock);
    ToolWindow toolWindowMock = createMock(ToolWindow.class);
    toolWindowMock.activate((Runnable) anyObject());
    expectLastCall().andStubAnswer(new IAnswer<Object>()
    {
      public Object answer() throws Throwable
      {
        ((Runnable) getCurrentArguments()[0]).run();
        return null;
      }
    });
    ToolWindowManager toolWindowManagerMock = createMock(ToolWindowManager.class);
    expect(toolWindowManagerMock.getToolWindow(FileBrowserConstants.FILEBROWSER_TOOLWINDOW_ID)).andReturn(toolWindowMock);
    FileBrowser fileBrowserMock = createMock(FileBrowser.class);
    fileBrowserMock.select(fileMock);
    replay(selectInContextMock, toolWindowMock, toolWindowManagerMock, fileBrowserMock);

    SelectInFileBrowserTarget testObject = new SelectInFileBrowserTarget(fileBrowserMock, toolWindowManagerMock);
    testObject.selectIn(selectInContextMock, true);

    verify(selectInContextMock, toolWindowMock, toolWindowManagerMock, fileBrowserMock);
  }

  @Test
  public void testSelectInWithoutRequestFocus()
  {
    SelectInContext selectInContextMock = createMock(SelectInContext.class);
    VirtualFile fileMock = createMock(VirtualFile.class);
    expect(selectInContextMock.getVirtualFile()).andReturn(fileMock);
    ToolWindow toolWindowMock = createMock(ToolWindow.class);
    toolWindowMock.show((Runnable) anyObject());
    expectLastCall().andStubAnswer(new IAnswer<Object>()
    {
      public Object answer() throws Throwable
      {
        ((Runnable) getCurrentArguments()[0]).run();
        return null;
      }
    });
    ToolWindowManager toolWindowManagerMock = createMock(ToolWindowManager.class);
    expect(toolWindowManagerMock.getToolWindow(FileBrowserConstants.FILEBROWSER_TOOLWINDOW_ID)).andReturn(toolWindowMock);
    FileBrowser fileBrowserMock = createMock(FileBrowser.class);
    fileBrowserMock.select(fileMock);
    replay(selectInContextMock, toolWindowMock, toolWindowManagerMock, fileBrowserMock);

    SelectInFileBrowserTarget testObject = new SelectInFileBrowserTarget(fileBrowserMock, toolWindowManagerMock);
    testObject.selectIn(selectInContextMock, false);

    verify(selectInContextMock, toolWindowMock, toolWindowManagerMock, fileBrowserMock);
  }

  @Test
  public void testGetters()
  {
    SelectInContext selectInContextMock = createMock(SelectInContext.class);
    ToolWindowManager toolWindowManagerMock = createMock(ToolWindowManager.class);
    FileBrowser fileBrowserMock = createMock(FileBrowser.class);
    replay(selectInContextMock, toolWindowManagerMock, fileBrowserMock);

    SelectInFileBrowserTarget testObject = new SelectInFileBrowserTarget(fileBrowserMock, toolWindowManagerMock);

    assertEquals(true, testObject.canSelect(selectInContextMock));
    assertEquals(FileBrowserConstants.FILEBROWSER_TOOLWINDOW_ID, testObject.getToolWindowId());
    assertEquals(null, testObject.getMinorViewId());
    assertEquals(100f, testObject.getWeight(), 0.0f);
    assertEquals("FileBrowser", testObject.toString());

    verify(selectInContextMock, toolWindowManagerMock, fileBrowserMock);
  }
}
