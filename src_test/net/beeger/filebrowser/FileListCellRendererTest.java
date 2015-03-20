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

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

public class FileListCellRendererTest
{
  @Test
  public void testParentDirectory()
  {
    Icon iconMock = createMock(Icon.class);
    expect(iconMock.getIconHeight()).andReturn(7).anyTimes();
    expect(iconMock.getIconWidth()).andReturn(7).anyTimes();
    iconMock.paintIcon((Component) anyObject(), (Graphics) anyObject(), anyInt(), anyInt());
    expectLastCall().anyTimes();
    VirtualFile parentMock = createMock(VirtualFile.class);
    expect(parentMock.getName()).andReturn("p").anyTimes();
    expect(parentMock.getIcon()).andReturn(iconMock).anyTimes();
    expect(parentMock.isDirectory()).andReturn(true).anyTimes();
    VirtualFile folderMock = createMock(VirtualFile.class);
    expect(folderMock.getName()).andReturn("test").anyTimes();
    expect(folderMock.getIcon()).andReturn(iconMock).anyTimes();
    expect(folderMock.isDirectory()).andReturn(true).anyTimes();
    expect(folderMock.getParent()).andReturn(parentMock).anyTimes();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(folderMock).anyTimes();
    JList listMock = createMock(JList.class);
    expect(listMock.getForeground()).andReturn(Color.BLACK).anyTimes();
    expect(listMock.getBackground()).andReturn(Color.WHITE).anyTimes();
    expect(listMock.getSelectionForeground()).andReturn(Color.YELLOW).anyTimes();
    expect(listMock.getSelectionBackground()).andReturn(Color.BLUE).anyTimes();

    replay(folderDisplayManagerMock, folderMock, parentMock, listMock);
    FileListCellRenderer testObject = new FileListCellRenderer(folderDisplayManagerMock);

    JLabel label = (JLabel) testObject.getListCellRendererComponent(listMock, parentMock, 0, true, true);
    assertEquals("[..]", label.getText());

    verify(folderDisplayManagerMock, folderMock, parentMock, listMock);
  }

  @Test
  public void testDirectory()
  {
    Icon iconMock = createMock(Icon.class);
    expect(iconMock.getIconHeight()).andReturn(7).anyTimes();
    expect(iconMock.getIconWidth()).andReturn(7).anyTimes();
    iconMock.paintIcon((Component) anyObject(), (Graphics) anyObject(), anyInt(), anyInt());
    expectLastCall().anyTimes();
    VirtualFile parentMock = createMock(VirtualFile.class);
    expect(parentMock.getName()).andReturn("p").anyTimes();
    expect(parentMock.getIcon()).andReturn(iconMock).anyTimes();
    expect(parentMock.isDirectory()).andReturn(true).anyTimes();
    expect(parentMock.getParent()).andReturn(null).anyTimes();
    VirtualFile fileMock = createMock(VirtualFile.class);
    expect(fileMock.getName()).andReturn("test").anyTimes();
    expect(fileMock.getIcon()).andReturn(iconMock).anyTimes();
    expect(fileMock.isDirectory()).andReturn(true).anyTimes();
    expect(fileMock.getParent()).andReturn(parentMock).anyTimes();
    FolderDisplayManager folderDisplayManagerMock = createMock(FolderDisplayManager.class);
    expect(folderDisplayManagerMock.getCurrentBaseFolder()).andReturn(parentMock).anyTimes();
    JList listMock = createMock(JList.class);
    expect(listMock.getForeground()).andReturn(Color.BLACK).anyTimes();
    expect(listMock.getBackground()).andReturn(Color.WHITE).anyTimes();
    expect(listMock.getSelectionForeground()).andReturn(Color.YELLOW).anyTimes();
    expect(listMock.getSelectionBackground()).andReturn(Color.BLUE).anyTimes();

    replay(folderDisplayManagerMock, fileMock, listMock, parentMock);
    FileListCellRenderer testObject = new FileListCellRenderer(folderDisplayManagerMock);

    JLabel label = (JLabel) testObject.getListCellRendererComponent(listMock, fileMock, 1, true, false);
    assertEquals("test", label.getText());

    verify(folderDisplayManagerMock, fileMock, listMock, parentMock);
  }

}
