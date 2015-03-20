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
package net.beeger.filebrowser.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;

/**
 * The project specific settings of the file browser.
 */
@State(
    name = "FileBrowser",
    storages = {
    @Storage(
        id = "other",
        file = "$WORKSPACE_FILE$"
    )}
)
public class ProjectSettings extends AbstractSettings implements PersistentStateComponent<ProjectSettings>
{
  public boolean isAutoScrollToSource()
  {
    return _autoScrollToSource;
  }

  public void setAutoScrollToSource(boolean autoScrollToSource)
  {
    if (_autoScrollToSource != autoScrollToSource)
    {
      _autoScrollToSource = autoScrollToSource;
      fireSettingsChangedEvent();
    }
  }

  public boolean isAutoScrollFromSource()
  {
    return _autoScrollFromSource;
  }

  public void setAutoScrollFromSource(boolean autoScrollFromSource)
  {
    if (_autoScrollFromSource != autoScrollFromSource)
    {
      _autoScrollFromSource = autoScrollFromSource;
      fireSettingsChangedEvent();
    }
  }

  public boolean isGroupFolders()
  {
    return _groupFolders;
  }

  public void setGroupFolders(boolean groupFolders)
  {
    if (_groupFolders != groupFolders)
    {
      _groupFolders = groupFolders;
      fireSettingsChangedEvent();
    }
  }

  public boolean isFocusEditor()
  {
    return _focusEditor;
  }

  public void setFocusEditor(boolean focusEditor)
  {
    if (_focusEditor != focusEditor)
    {
      _focusEditor = focusEditor;
      fireSettingsChangedEvent();
    }
  }

  public boolean isDontShowUnknown()
  {
    return _dontShowUnknown;
  }

  public void setDontShowUnknown(boolean dontShowUnknown)
  {
    if (_dontShowUnknown != dontShowUnknown)
    {
      _dontShowUnknown = dontShowUnknown;
      fireSettingsChangedEvent();
    }
  }

  public String getCurrentFolder()
  {
    return _currentFolder;
  }

  public void setCurrentFolder(String currentFolder)
  {
    if (_currentFolder != null && !_currentFolder.equals(currentFolder) ||
        currentFolder != null && !currentFolder.equals(_currentFolder))
    {
      _currentFolder = currentFolder;
      fireSettingsChangedEvent();
    }
  }

  public ProjectSettings getState()
  {
    return this;
  }

  public void loadState(ProjectSettings object)
  {
    load(object);
  }

  private boolean _autoScrollToSource;
  private boolean _autoScrollFromSource;
  private boolean _groupFolders;
  private boolean _focusEditor;
  private boolean _dontShowUnknown;
  private String _currentFolder;
}
