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
import net.beeger.filebrowser.DestinationChooser;
import net.beeger.filebrowser.DestinationChooserConfiguration;
import net.beeger.filebrowser.DialogProvider;
import net.beeger.filebrowser.FileListHandler;
import net.beeger.filebrowser.FileOperationHandler;
import net.beeger.filebrowser.FolderDisplayManager;
import net.beeger.filebrowser.TestApplication;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MoveActionTest
{
  @Test
  public void testUpdate()
  {
    FileOperationHandler fileOperationHandlerMock = createMock(FileOperationHandler.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    VirtualFile fileMock = createMock(VirtualFile.class);
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    DataContext dataContextMock = createMock(DataContext.class);
    Application application = new TestApplication();
    DestinationChooser destinationChooserMock = createMock(DestinationChooser.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);

    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(null);

    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(new ArrayList<VirtualFile>());

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files);

    replay(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock);

    MoveAction testObject = new MoveAction(application, folderDisplayManagerMock, fileOperationHandlerMock, destinationChooserMock, dialogProviderMock);
    testObject.setFileListHandler(fileListHandlerMock);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.update(event);
    assertFalse(event.getPresentation().isEnabled());
    testObject.update(event);
    assertFalse(event.getPresentation().isEnabled());
    testObject.update(event);
    assertTrue(event.getPresentation().isEnabled());

    verify(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock);
  }

  @Test
  public void testActionPerformed() throws IOException
  {
    FileOperationHandler fileOperationHandlerMock = createMock(FileOperationHandler.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    VirtualFile fileMock = createMock(VirtualFile.class);
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    DataContext dataContextMock = createMock(DataContext.class);
    Application application = new TestApplication();
    DestinationChooser destinationChooserMock = createMock(DestinationChooser.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);

    expect(fileMock.getName()).andReturn("bla.txt").anyTimes();

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files).anyTimes();
    expect(destinationChooserMock.askForDestination(same(DestinationChooserConfiguration.MOVE), same(files))).andReturn(true);
    expect(destinationChooserMock.isSingleFileOperation()).andReturn(true).anyTimes();
    expect(destinationChooserMock.getDestinationFolder()).andReturn(baseFolderMock).anyTimes();
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock).anyTimes();

    expect(baseFolderMock.findChild("bla.txt")).andReturn(null);
    fileOperationHandlerMock.move(same(fileMock), same(baseFolderMock));
    folderDisplayManagerMock.refreshCurrentFolderDisplay(eq(false));

    replay(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock);

    MoveAction testObject = new MoveAction(application, folderDisplayManagerMock, fileOperationHandlerMock, destinationChooserMock, dialogProviderMock);
    testObject.setFileListHandler(fileListHandlerMock);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.actionPerformed(event);

    verify(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock);
  }

  @Test
  public void testActionPerformedReplace() throws IOException
  {
    FileOperationHandler fileOperationHandlerMock = createMock(FileOperationHandler.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    VirtualFile fileMock = createMock(VirtualFile.class);
    VirtualFile destFileMock = createMock(VirtualFile.class);
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    DataContext dataContextMock = createMock(DataContext.class);
    Application application = new TestApplication();
    DestinationChooser destinationChooserMock = createMock(DestinationChooser.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);

    expect(fileMock.getName()).andReturn("bla.txt").anyTimes();

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files).anyTimes();
    expect(destinationChooserMock.askForDestination(same(DestinationChooserConfiguration.MOVE), same(files))).andReturn(true);
    expect(destinationChooserMock.isSingleFileOperation()).andReturn(true).anyTimes();
    expect(destinationChooserMock.getDestinationFolder()).andReturn(baseFolderMock).anyTimes();
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock).anyTimes();
    expect(baseFolderMock.getPath()).andReturn("/r/b");

    expect(baseFolderMock.findChild("bla.txt")).andReturn(destFileMock);
    expect(dialogProviderMock.getConfirmationFromUser((String) anyObject())).andReturn(true);
    destFileMock.delete(anyObject());
    fileOperationHandlerMock.move(same(fileMock), same(baseFolderMock));
    folderDisplayManagerMock.refreshCurrentFolderDisplay(eq(false));

    replay(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock, destFileMock);

    MoveAction testObject = new MoveAction(application, folderDisplayManagerMock, fileOperationHandlerMock, destinationChooserMock, dialogProviderMock);
    testObject.setFileListHandler(fileListHandlerMock);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.actionPerformed(event);

    verify(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock, destFileMock);
  }

  @Test
  public void testActionPerformedDontReplace() throws IOException
  {
    FileOperationHandler fileOperationHandlerMock = createMock(FileOperationHandler.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    VirtualFile fileMock = createMock(VirtualFile.class);
    VirtualFile destFileMock = createMock(VirtualFile.class);
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    DataContext dataContextMock = createMock(DataContext.class);
    Application application = new TestApplication();
    DestinationChooser destinationChooserMock = createMock(DestinationChooser.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);

    expect(fileMock.getName()).andReturn("bla.txt").anyTimes();

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files).anyTimes();
    expect(destinationChooserMock.askForDestination(same(DestinationChooserConfiguration.MOVE), same(files))).andReturn(true);
    expect(destinationChooserMock.isSingleFileOperation()).andReturn(true).anyTimes();
    expect(destinationChooserMock.getDestinationFolder()).andReturn(baseFolderMock).anyTimes();
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock).anyTimes();
    expect(baseFolderMock.getPath()).andReturn("/r/b");


    expect(baseFolderMock.findChild("bla.txt")).andReturn(destFileMock);
    expect(dialogProviderMock.getConfirmationFromUser((String) anyObject())).andReturn(false);
    folderDisplayManagerMock.refreshCurrentFolderDisplay(eq(false));

    replay(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock, destFileMock);

    MoveAction testObject = new MoveAction(application, folderDisplayManagerMock, fileOperationHandlerMock, destinationChooserMock, dialogProviderMock);
    testObject.setFileListHandler(fileListHandlerMock);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.actionPerformed(event);

    verify(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock, destFileMock);
  }

  @Test
  public void testActionPerformedNoDestinationFolder() throws IOException
  {
    FileOperationHandler fileOperationHandlerMock = createMock(FileOperationHandler.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    FileListHandler fileListHandlerMock = createMock(FileListHandler.class);
    VirtualFile fileMock = createMock(VirtualFile.class);
    VirtualFile destFileMock = createMock(VirtualFile.class);
    VirtualFile baseFolderMock = createMock(VirtualFile.class);
    DataContext dataContextMock = createMock(DataContext.class);
    Application application = new TestApplication();
    DestinationChooser destinationChooserMock = createMock(DestinationChooser.class);
    DialogProvider dialogProviderMock = createMock(DialogProvider.class);

    expect(fileMock.getName()).andReturn("bla.txt").anyTimes();

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(fileMock);
    expect(fileListHandlerMock.getSelectedFiles()).andReturn(files).anyTimes();
    expect(destinationChooserMock.askForDestination(same(DestinationChooserConfiguration.MOVE), same(files))).andReturn(true);
    expect(destinationChooserMock.isSingleFileOperation()).andReturn(true).anyTimes();
    expect(destinationChooserMock.getDestinationFolder()).andReturn(null).anyTimes();
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(baseFolderMock).anyTimes();

    dialogProviderMock.showInfo((String) anyObject());

    replay(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock, destFileMock);

    MoveAction testObject = new MoveAction(application, folderDisplayManagerMock, fileOperationHandlerMock, destinationChooserMock, dialogProviderMock);
    testObject.setFileListHandler(fileListHandlerMock);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);
    testObject.actionPerformed(event);

    verify(fileOperationHandlerMock, folderDisplayManagerMock, fileListHandlerMock, fileMock, dataContextMock, baseFolderMock, destinationChooserMock, dialogProviderMock, destFileMock);
  }
}