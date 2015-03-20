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
package net.beeger.filebrowser.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.actionSystem.ActionManager;
import net.beeger.filebrowser.ShutdownAble;
import net.beeger.filebrowser.FileListHandler;

import java.awt.Component;

public abstract class AbstractPopupTriggerAction extends AnAction
    implements ShutdownAble, FileListHandlerAware
{
  @SuppressWarnings({"WeakerAccess", "SameParameterValue"}) public AbstractPopupTriggerAction(String text, String description, javax.swing.Icon icon, ActionManager actionManager, ActionGroup actionGroup)
  {
    super(text, description, icon);
    _actionGroup = actionGroup;
    _popupMenu = actionManager.createActionPopupMenu("", _actionGroup);
  }

  public void actionPerformed(AnActionEvent e)
  {
    Component component = e.getInputEvent().getComponent();
    _popupMenu.getComponent().show(component, 0, 10);
  }

  public void shutdown()
  {
    if (_actionGroup instanceof ShutdownAble)
    {
      ((ShutdownAble) _actionGroup).shutdown();
    }
  }

  public void setFileListHandler(FileListHandler fileListHandler)
  {
    if (_actionGroup instanceof FileListHandlerAware)
    {
      ((FileListHandlerAware) _actionGroup).setFileListHandler(fileListHandler);
    }
  }

  private final ActionPopupMenu _popupMenu;
  private final ActionGroup _actionGroup;
}
