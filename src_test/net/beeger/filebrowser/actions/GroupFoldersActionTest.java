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

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import net.beeger.filebrowser.settings.ProjectSettings;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class GroupFoldersActionTest
{
  @Test
  public void testDefault()
  {
    DataContext dataContextMock = createMock(DataContext.class);
    replay(dataContextMock);

    ProjectSettings settings = new ProjectSettings();
    GroupFoldersAction testObject = new GroupFoldersAction(settings);
    AnActionEvent event = new AnActionEvent(null, dataContextMock, "", testObject.getTemplatePresentation(), null, 0);

    assertFalse(testObject.isSelected(event));
    settings.setGroupFolders(true);
    assertTrue(testObject.isSelected(event));
    testObject.setSelected(event, false);
    assertFalse(testObject.isSelected(event));
    assertFalse(settings.isGroupFolders());
    testObject.setSelected(event, true);
    assertTrue(testObject.isSelected(event));
    assertTrue(settings.isGroupFolders());

    verify(dataContextMock);
  }
}