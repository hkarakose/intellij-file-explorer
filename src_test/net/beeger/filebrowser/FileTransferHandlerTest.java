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

import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;

import javax.swing.JList;
import javax.swing.TransferHandler;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTransferHandlerTest
{
  @Test
  public void testImportData() throws Exception
  {
    VirtualFile virtualFileMock = createMock(VirtualFile.class);
    LocalFileSystem fileSystemMock = createMock(LocalFileSystem.class);
    expect(fileSystemMock.findFileByPath((String) anyObject())).andReturn(virtualFileMock);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    folderDisplayManagerMock.select(virtualFileMock);
    expectLastCall();
    Transferable transferableMock = createMock(Transferable.class);
    List<File> files = new ArrayList<File>();
    files.add(new File("test"));
    expect(transferableMock.getTransferData(DataFlavor.javaFileListFlavor)).andReturn(files);
    replay(virtualFileMock, fileSystemMock, folderDisplayManagerMock, transferableMock);

    FileTransferHandler testObject = new FileTransferHandler(fileSystemMock, folderDisplayManagerMock);
    testObject.importData(null, transferableMock);

    verify(virtualFileMock, fileSystemMock, folderDisplayManagerMock, transferableMock);
  }

  @Test
  public void testCanImport() throws Exception
  {
    LocalFileSystem fileSystemMock = createMock(LocalFileSystem.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    replay(fileSystemMock, folderDisplayManagerMock);

    FileTransferHandler testObject = new FileTransferHandler(fileSystemMock, folderDisplayManagerMock);
    assertTrue(testObject.canImport(null, new DataFlavor[]{DataFlavor.javaFileListFlavor}));

    verify(fileSystemMock, folderDisplayManagerMock);
  }

  @Test
  public void testGetSourceActions() throws Exception
  {
    LocalFileSystem fileSystemMock = createMock(LocalFileSystem.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    replay(fileSystemMock, folderDisplayManagerMock);

    FileTransferHandler testObject = new FileTransferHandler(fileSystemMock, folderDisplayManagerMock);
    assertEquals(TransferHandler.COPY, testObject.getSourceActions(null));

    verify(fileSystemMock, folderDisplayManagerMock);
  }

  @Test
  public void testCreateTransferable() throws Exception
  {
    VirtualFile virtualFileMock = createMock(VirtualFile.class);
    JList listMock = createMock(JList.class);
    expect(listMock.getSelectedValue()).andReturn(virtualFileMock).times(2);
    LocalFileSystem fileSystemMock = createMock(LocalFileSystem.class);
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    replay(fileSystemMock, folderDisplayManagerMock, listMock);

    FileTransferHandler testObject = new FileTransferHandler(fileSystemMock, folderDisplayManagerMock);
    assertTrue(testObject.createTransferable(listMock) instanceof FileTransferable);

    verify(fileSystemMock, folderDisplayManagerMock, listMock);
  }
}
