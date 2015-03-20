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

import org.junit.Test;
import static org.easymock.classextension.EasyMock.*;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;

public class FileOperationHandlerImplTest
{
  @Test
  public void testMove() throws IOException
  {
    VirtualFile file1 = createMock(VirtualFile.class);
    VirtualFile parentFolder = createMock(VirtualFile.class);
    VirtualFile otherFolder = createMock(VirtualFile.class);

    expect(file1.getParent()).andReturn(parentFolder).anyTimes();
    String file1Name = "test1.txt";
    expect(file1.getName()).andReturn(file1Name).anyTimes();
    expect(parentFolder.isDirectory()).andReturn(true).anyTimes();
    expect(otherFolder.isDirectory()).andReturn(true).anyTimes();

    file1.move(anyObject(), same(otherFolder));

    replay(file1, parentFolder, otherFolder);

    FileOperationHandler testObject = new FileOperationHandlerImpl();
    testObject.move(file1, otherFolder);

    verify(file1, parentFolder, otherFolder);
  }

  @Test
  public void testCopy() throws IOException
  {
    VirtualFile file1 = createMock(VirtualFile.class);
    VirtualFile parentFolder = createMock(VirtualFile.class);
    VirtualFile otherFolder = createMock(VirtualFile.class);

    expect(file1.getParent()).andReturn(parentFolder).anyTimes();
    String file1Name = "test1.txt";
    expect(file1.getName()).andReturn(file1Name).anyTimes();
    expect(parentFolder.isDirectory()).andReturn(true).anyTimes();
    expect(otherFolder.isDirectory()).andReturn(true).anyTimes();

    expect(file1.copy(anyObject(), same(otherFolder), eq("test2.txt"))).andReturn(null);

    replay(file1, parentFolder, otherFolder);

    FileOperationHandler testObject = new FileOperationHandlerImpl();
    testObject.copy(file1, otherFolder, "test2.txt");

    verify(file1, parentFolder, otherFolder);
  }

  @Test
  public void testRename() throws IOException
  {
    VirtualFile file1 = createMock(VirtualFile.class);
    String copyName = "copy.txt";
    VirtualFile parentFolder = createMock(VirtualFile.class);

    expect(file1.getParent()).andReturn(parentFolder).anyTimes();
    expect(file1.getName()).andReturn("test1.txt").anyTimes();
    expect(parentFolder.getName()).andReturn("p.txt").anyTimes();
    expect(file1.isDirectory()).andReturn(false).anyTimes();
    expect(parentFolder.isDirectory()).andReturn(true).anyTimes();

    file1.rename(anyObject(), eq(copyName));

    replay(file1, parentFolder);

    FileOperationHandler testObject = new FileOperationHandlerImpl();

    testObject.rename(file1, copyName);

    verify(file1, parentFolder);
  }

  @Test
  public void testDelete() throws IOException
  {
    VirtualFile file1 = createMock(VirtualFile.class);
    VirtualFile file2 = createMock(VirtualFile.class);
    VirtualFile parentFolder = createMock(VirtualFile.class);
    VirtualFile otherFolder = createMock(VirtualFile.class);

    expect(file1.getParent()).andReturn(parentFolder).anyTimes();
    expect(file2.getParent()).andReturn(parentFolder).anyTimes();
    expect(file1.getName()).andReturn("test1.txt").anyTimes();
    expect(file2.getName()).andReturn("test2.txt").anyTimes();
    expect(parentFolder.getName()).andReturn("p.txt").anyTimes();
    expect(otherFolder.getName()).andReturn("o.txt").anyTimes();
    expect(file1.isDirectory()).andReturn(false).anyTimes();
    expect(file2.isDirectory()).andReturn(false).anyTimes();
    expect(parentFolder.isDirectory()).andReturn(true).anyTimes();
    expect(otherFolder.isDirectory()).andReturn(true).anyTimes();

    file1.delete(anyObject());
    file2.delete(anyObject());

    replay(file1, file2, parentFolder, otherFolder);

    List<VirtualFile> files = new ArrayList<VirtualFile>();
    files.add(file1);
    files.add(file2);


    FileOperationHandler testObject = new FileOperationHandlerImpl();

    testObject.delete(files);

    verify(file1, file2, parentFolder, otherFolder);
  }
}
