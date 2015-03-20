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

import java.util.List;
import java.util.ArrayList;

public class BrowseHistory
{
  public BrowseHistory(FileSystemHandler fileSystemHandler)
  {
    _fileSystemHandler = fileSystemHandler;
    _browseHistory = new ArrayList<String>();
  }

  public void add(String path)
  {
    add(_fileSystemHandler.findFileByPath(path));
  }

  public void add(VirtualFile file)
  {
    String path = file.getPath();
    if (_currentPosition < 0 || !path.equals(_browseHistory.get(_currentPosition)))
    {
      _currentPosition++;
      if (_browseHistory.size() < _currentPosition + 1)
      {
        _browseHistory.add(path);
      }
      else
      {
        _browseHistory.set(_currentPosition, path);
      }
      _lastHistoryEntryIndex = _currentPosition;
      _currentFolder = file;
    }
  }

  public void goBack()
  {
    assert canGoBack() : "canGoBack()";

    _currentPosition--;
    if (_currentPosition < 0)
    {
      _currentPosition = 0;
    }

    determineCurrentFolder(true);
  }

  public void goForward()
  {
    assert canGoForward() : "canGoForward()";
    _currentPosition++;
    if (_currentPosition > _lastHistoryEntryIndex)
    {
      _currentPosition = _lastHistoryEntryIndex;
    }

    determineCurrentFolder(false);
  }

  private void determineCurrentFolder(boolean back)
  {
    _currentFolder = _fileSystemHandler.findFileByPath(_browseHistory.get(_currentPosition));
    while (_currentFolder == null)
    {
      _browseHistory.remove(_currentPosition);
      _currentPosition -= back ? 1 : 0;
      if (_currentPosition < 0)
      {
        _currentPosition = 0;
      }
      _lastHistoryEntryIndex--;
      if (_currentPosition > _lastHistoryEntryIndex)
      {
        _currentPosition = _lastHistoryEntryIndex;
      }
      if (_lastHistoryEntryIndex < 0)
      {
        break;
      }
      _currentFolder = _fileSystemHandler.findFileByPath(_browseHistory.get(_currentPosition));
    }
  }

  public VirtualFile getCurrentFolder()
  {
    return _currentFolder;
  }

  public boolean canGoBack()
  {
    return _currentPosition > 0;
  }

  public boolean canGoForward()
  {
    return _currentPosition < _lastHistoryEntryIndex;
  }

  private final List<String> _browseHistory;
  private int _currentPosition = -1;
  private int _lastHistoryEntryIndex = -1;
  private final FileSystemHandler _fileSystemHandler;
  private VirtualFile _currentFolder;
}
