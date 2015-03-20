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

import com.intellij.openapi.vfs.VirtualFile;
import static org.junit.Assert.*;
import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileTransferableTest
{
  @Test
  public void testGetTransferDataFileList() throws Exception
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    expect(fileMock.getPath()).andReturn("test.txt");
    replay(fileMock);

    FileTransferable testObject = new FileTransferable(fileMock);
    List<File> files = (List<File>) testObject.getTransferData(DataFlavor.javaFileListFlavor);
    assertEquals(1, files.size());
    assertEquals("test.txt", files.get(0).getPath());

    verify(fileMock);
  }

  @Test
  public void testGetTransferDataString() throws Exception
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    expect(fileMock.getPath()).andReturn("test.txt");
    replay(fileMock);

    FileTransferable testObject = new FileTransferable(fileMock);
    String path = (String) testObject.getTransferData(DataFlavor.stringFlavor);
    assertEquals(new File("test.txt").getAbsolutePath(), path);

    verify(fileMock);
  }

  @Test(expected = UnsupportedFlavorException.class)
  public void testGetTransferDataUnsupportedFlavour() throws Exception
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    replay(fileMock);

    FileTransferable testObject = new FileTransferable(fileMock);
    testObject.getTransferData(DataFlavor.imageFlavor);

    verify(fileMock);
  }

  @Test
  public void testDefault() throws Exception
  {
    VirtualFile fileMock = createMock(VirtualFile.class);
    replay(fileMock);

    FileTransferable testObject = new FileTransferable(fileMock);
    assertEquals(2, testObject.getTransferDataFlavors().length);
    assertTrue(Arrays.asList(testObject.getTransferDataFlavors()).contains(DataFlavor.javaFileListFlavor));
    assertTrue(Arrays.asList(testObject.getTransferDataFlavors()).contains(DataFlavor.stringFlavor));
    assertTrue(testObject.isDataFlavorSupported(DataFlavor.javaFileListFlavor));
    assertTrue(testObject.isDataFlavorSupported(DataFlavor.stringFlavor));

    verify(fileMock);
  }
}
