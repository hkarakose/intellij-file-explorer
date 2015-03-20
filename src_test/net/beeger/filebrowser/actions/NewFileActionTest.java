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
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.vfs.VirtualFile;
import net.beeger.filebrowser.DialogProvider;
import net.beeger.filebrowser.FolderDisplayManager;
import net.beeger.filebrowser.TestApplication;
import net.beeger.filebrowser.FileOperationHandlerImpl;
import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;

import java.io.IOException;

@SuppressWarnings({"ThrowableInstanceNeverThrown"}) public class NewFileActionTest
{
  @Test
  public void testActionPerformedCancel()
  {
    Application application = new TestApplication();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);
    DataContext dataContextMock = createMock(DataContext.class);

    expect(dialogProviderMock.getInputFromUser((String) anyObject())).andReturn(null);

    replay(folderDisplayManagerMock, dialogProviderMock, dataContextMock);

    NewFileAction testObject = new NewFileAction(application, folderDisplayManagerMock, dialogProviderMock, new FileOperationHandlerImpl());
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.actionPerformed(event);

    verify(folderDisplayManagerMock, dialogProviderMock, dataContextMock);
  }

  @Test
  public void testActionPerformedConfirm() throws Exception
  {
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    Application application = new TestApplication();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);
    DataContext dataContextMock = createMock(DataContext.class);

    expect(dialogProviderMock.getInputFromUser((String) anyObject())).andReturn("test.txt");
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock);
    expect(baseFolderMock.createChildData(anyObject(), eq("test.txt"))).andReturn(null);
    folderDisplayManagerMock.refreshCurrentFolderDisplay(false);

    replay(folderDisplayManagerMock, dialogProviderMock, dataContextMock, baseFolderMock);

    NewFileAction testObject = new NewFileAction(application, folderDisplayManagerMock, dialogProviderMock, new FileOperationHandlerImpl());
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.actionPerformed(event);

    verify(folderDisplayManagerMock, dialogProviderMock, dataContextMock, baseFolderMock);
  }

  @Test(expected = RuntimeException.class)
  public void testActionPerformedThrowException() throws Exception
  {
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    Application application = new TestApplication();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);
    DataContext dataContextMock = createMock(DataContext.class);

    expect(dialogProviderMock.getInputFromUser((String) anyObject())).andReturn("test.txt");
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock);
    expect(baseFolderMock.createChildData(anyObject(), eq("test.txt"))).andThrow(new IOException());

    replay(folderDisplayManagerMock, dialogProviderMock, dataContextMock, baseFolderMock);

    NewFileAction testObject = new NewFileAction(application, folderDisplayManagerMock, dialogProviderMock, new FileOperationHandlerImpl());
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.actionPerformed(event);

    verify(folderDisplayManagerMock, dialogProviderMock, dataContextMock, baseFolderMock);
  }
}
