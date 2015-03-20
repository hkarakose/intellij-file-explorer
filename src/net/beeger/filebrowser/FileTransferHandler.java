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

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileSystem;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

class FileTransferHandler extends TransferHandler
{
  public FileTransferHandler(LocalFileSystem fileSystem, FolderDisplayManager folderDisplayManager)
  {
    _fileSystem = fileSystem;
    _folderDisplayManager = folderDisplayManager;
  }

  public int getSourceActions(JComponent c)
  {
    return COPY;
  }

  @SuppressWarnings({"unchecked"})
  public boolean importData(JComponent comp, Transferable t)
  {
    boolean success = false;
    try
    {
      List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
      success = false;
      if (files.size() > 0)
      {
        File file = files.get(0);
        VirtualFile vfile = _fileSystem.findFileByPath(file.getPath().replace(File.separatorChar, '/'));
        if (vfile != null)
        {
          _folderDisplayManager.select(vfile);
          success = true;
        }

      }
    }
    catch (UnsupportedFlavorException e)
    {
      LOG.error(e);
    }
    catch (IOException e)
    {
      LOG.error(e);
    }
    return success;
  }

  public boolean canImport(JComponent comp, DataFlavor[] transferFlavors)
  {
    return Arrays.asList(transferFlavors).contains(DataFlavor.javaFileListFlavor);
  }

  protected Transferable createTransferable(JComponent c)
  {
    Transferable transferable = null;
    if (((JList) c).getSelectedValue() != null)
    {
      transferable = new FileTransferable((VirtualFile) ((JList) c).getSelectedValue());
    }
    return transferable;
  }

  private final VirtualFileSystem _fileSystem;
  private final FolderDisplayManager _folderDisplayManager;
  private static final Logger LOG = Logger.getInstance("net.beeger.filebrowser");

}
