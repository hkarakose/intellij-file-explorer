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
import net.beeger.filebrowser.settings.ProjectSettings;
import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;

public class VirtualFileComparatorTest
{
  @Test
  public void testCompareTwoFiles()
  {
    VirtualFile file1Mock = createMock(VirtualFile.class);
    expect(file1Mock.isDirectory()).andReturn(false).times(2);
    expect(file1Mock.getName()).andReturn("a");
    VirtualFile file2Mock = createMock(VirtualFile.class);
    expect(file2Mock.isDirectory()).andReturn(false);
    expect(file2Mock.getName()).andReturn("b");
    ProjectSettings projectSettingsMock = createMock(ProjectSettings.class);
    expect(projectSettingsMock.isGroupFolders()).andReturn(true);
    replay(file1Mock, file2Mock, projectSettingsMock);

    VirtualFileComparator comparator = new VirtualFileComparator(projectSettingsMock);
    assertEquals(-1, comparator.compare(file1Mock, file2Mock));

    verify(file1Mock, file2Mock, projectSettingsMock);
  }

  @Test
  public void testCompareTwoDirectories()
  {
    VirtualFile file1Mock = createMock(VirtualFile.class);
    expect(file1Mock.isDirectory()).andReturn(true).times(2);
    expect(file1Mock.getName()).andReturn("a");
    VirtualFile file2Mock = createMock(VirtualFile.class);
    expect(file2Mock.isDirectory()).andReturn(true);
    expect(file2Mock.getName()).andReturn("b");
    ProjectSettings projectSettingsMock = createMock(ProjectSettings.class);
    expect(projectSettingsMock.isGroupFolders()).andReturn(true);
    replay(file1Mock, file2Mock, projectSettingsMock);

    VirtualFileComparator comparator = new VirtualFileComparator(projectSettingsMock);
    assertEquals(-1, comparator.compare(file1Mock, file2Mock));

    verify(file1Mock, file2Mock, projectSettingsMock);
  }

  @Test
  public void testCompareFileAndDirectory()
  {
    VirtualFile file1Mock = createMock(VirtualFile.class);
    expect(file1Mock.isDirectory()).andReturn(false).times(2);
    VirtualFile file2Mock = createMock(VirtualFile.class);
    expect(file2Mock.isDirectory()).andReturn(true);
    ProjectSettings projectSettingsMock = createMock(ProjectSettings.class);
    expect(projectSettingsMock.isGroupFolders()).andReturn(true);
    replay(file1Mock, file2Mock, projectSettingsMock);

    VirtualFileComparator comparator = new VirtualFileComparator(projectSettingsMock);
    assertEquals(1, comparator.compare(file1Mock, file2Mock));

    verify(file1Mock, file2Mock, projectSettingsMock);
  }

  @Test
  public void testCompareDirectoryAndFile()
  {
    VirtualFile file1Mock = createMock(VirtualFile.class);
    expect(file1Mock.isDirectory()).andReturn(true);
    VirtualFile file2Mock = createMock(VirtualFile.class);
    expect(file2Mock.isDirectory()).andReturn(false);
    ProjectSettings projectSettingsMock = createMock(ProjectSettings.class);
    expect(projectSettingsMock.isGroupFolders()).andReturn(true);
    replay(file1Mock, file2Mock, projectSettingsMock);

    VirtualFileComparator comparator = new VirtualFileComparator(projectSettingsMock);
    assertEquals(-1, comparator.compare(file1Mock, file2Mock));

    verify(file1Mock, file2Mock, projectSettingsMock);
  }

  @Test
  public void testCompareFileAndDirectoryWithoutGrouping()
  {
    VirtualFile file1Mock = createMock(VirtualFile.class);
    expect(file1Mock.getName()).andReturn("a");
    VirtualFile file2Mock = createMock(VirtualFile.class);
    expect(file2Mock.getName()).andReturn("b");
    ProjectSettings projectSettingsMock = createMock(ProjectSettings.class);
    expect(projectSettingsMock.isGroupFolders()).andReturn(false);
    replay(file1Mock, file2Mock, projectSettingsMock);

    VirtualFileComparator comparator = new VirtualFileComparator(projectSettingsMock);
    assertEquals(-1, comparator.compare(file1Mock, file2Mock));

    verify(file1Mock, file2Mock, projectSettingsMock);
  }
}
