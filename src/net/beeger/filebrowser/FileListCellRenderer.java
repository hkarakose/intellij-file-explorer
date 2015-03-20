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

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Icons;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 * Cell renderer for file entries in the file list.
 */
class FileListCellRenderer implements ListCellRenderer
{
  public FileListCellRenderer(FolderDisplayManager folderDisplayManager)
  {
    _folderDisplayManager = folderDisplayManager;
    _label = new JLabel();
    _label.setOpaque(true);
  }

  public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
  {
    VirtualFile file = (VirtualFile) value;
    _label.setIcon(FileTypeManager.getInstance().getFileTypeByFile(file).getIcon());
    _label.setText(file.getName());

    if (file.isDirectory())
    {
      _label.setIcon(Icons.DIRECTORY_CLOSED_ICON);
    }
    VirtualFile currentBaseFolder = _folderDisplayManager.getCurrentBaseFolder();
    if (currentBaseFolder != null && file.equals(currentBaseFolder.getParent()))
    {
      _label.setText("[..]");
    }
    _label.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
    _label.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
    if (cellHasFocus)
    {
      _label.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.BLACK), BorderFactory.createEmptyBorder(1, 1, 1, 1)));
    }
    else
    {
      _label.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
    }
    return _label;
  }

  private final JLabel _label;
  private final FolderDisplayManager _folderDisplayManager;
}
