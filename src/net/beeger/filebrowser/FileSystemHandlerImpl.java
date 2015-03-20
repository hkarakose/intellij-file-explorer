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

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSystemHandlerImpl implements FileSystemHandler
{
  public FileSystemHandlerImpl(LocalFileSystem fileSystem)
  {
    _fileSystem = fileSystem;
  }

  public List<VirtualFile> getRoots()
  {
    List<VirtualFile> result = new ArrayList<VirtualFile>();
    File[] roots = File.listRoots();
    for (File root : roots)
    {
      if (_fileSystem != null)
      {
        VirtualFile file = _fileSystem.findFileByPath(root.getPath().replace(File.separatorChar, '/'));
        if (file != null)
        {
          result.add(file);
        }
      }
    }

    return result;
  }

  public boolean hasUnknownFileType(VirtualFile virtualFile)
  {
    return StdFileTypes.UNKNOWN.equals(virtualFile.getFileType());
  }

  public VirtualFile findFileByPath(String path)
  {
    return _fileSystem.findFileByPath(path.replace(File.separatorChar, '/'));
  }

  private final LocalFileSystem _fileSystem;
}
