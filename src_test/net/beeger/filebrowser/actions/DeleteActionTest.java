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
import net.beeger.filebrowser.FileListHandler;
import net.beeger.filebrowser.FolderDisplayManager;
import net.beeger.filebrowser.TestApplication;
import net.beeger.filebrowser.FileOperationHandlerImpl;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"ThrowableInstanceNeverThrown"}) public class DeleteActionTest
{
  @Test
  public void testActionPerformedCancel()
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    Application application = new TestApplication();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);
    DataContext dataContextMock = createMock(DataContext.class);

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files).anyTimes();
    expect(fileMock.getName()).andReturn("test.txt").anyTimes();
    expect(dialogProviderMock.getConfirmationFromUser((String) anyObject())).andReturn(false);

    replay(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);

    DeleteAction testObject = new DeleteAction(application, folderDisplayManagerMock, dialogProviderMock, null);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.setFileListHandler(fileListHandlerMock);
    testObject.actionPerformed(event);

    verify(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);
  }

  @Test
  public void testActionPerformedConfirm() throws Exception
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    Application application = new TestApplication();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);
    DataContext dataContextMock = createMock(DataContext.class);

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files).anyTimes();
    expect(fileMock.getName()).andReturn("test.txt").anyTimes();
    expect(dialogProviderMock.getConfirmationFromUser((String) anyObject())).andReturn(true).times(1);
    fileMock.delete(anyObject());
    folderDisplayManagerMock.refreshCurrentFolderDisplay(false);

    replay(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);

    DeleteAction testObject = new DeleteAction(application, folderDisplayManagerMock, dialogProviderMock, new FileOperationHandlerImpl());
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.setFileListHandler(fileListHandlerMock);
    testObject.actionPerformed(event);

    verify(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);
  }

  @Test(expected = RuntimeException.class)
  public void testActionPerformedThrowException() throws Exception
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    Application application = new TestApplication();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);
    DataContext dataContextMock = createMock(DataContext.class);

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files).anyTimes();
    expect(fileMock.getName()).andReturn("test.txt").anyTimes();
    expect(dialogProviderMock.getConfirmationFromUser((String) anyObject())).andReturn(true);
    fileMock.delete(anyObject());
    expectLastCall().andThrow(new IOException());

    replay(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);

    DeleteAction testObject = new DeleteAction(application, folderDisplayManagerMock, dialogProviderMock, null);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.setFileListHandler(fileListHandlerMock);
    testObject.actionPerformed(event);

    verify(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);
  }

  @Test
  public void testUpdate() throws Exception
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    Application application = new TestApplication();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);
    DataContext dataContextMock = createMock(DataContext.class);

    expect(fileListHandlerMock.hasListFocus()).andReturn(true).anyTimes();
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock).anyTimes();
    List<VirtualFile> emptyFileList = new ArrayList<VirtualFile>();
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(emptyFileList);
    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files);

    replay(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);

    DeleteAction testObject = new DeleteAction(application, folderDisplayManagerMock, dialogProviderMock, new FileOperationHandlerImpl());
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.setFileListHandler(fileListHandlerMock);

    testObject.update(event);
    assertFalse(event.getPresentation().isEnabled());
    testObject.update(event);
    assertTrue(event.getPresentation().isEnabled());

    verify(fileMock, fileListHandlerMock, folderDisplayManagerMock, dialogProviderMock, dataContextMock);
  }
}
