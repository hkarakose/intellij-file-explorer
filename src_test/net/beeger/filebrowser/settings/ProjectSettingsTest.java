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

import static org.junit.Assert.*;
import org.junit.Test;

public class ProjectSettingsTest implements SettingsChangedListener
{
  @Test
  public void testSettingsChanged()
  {
    _changeCount = 0;
    ProjectSettings testObject = new ProjectSettings();
    testObject.addSettingsChangedListener(this);
    testObject.setAutoScrollFromSource(true);
    assertTrue(testObject.isAutoScrollFromSource());
    assertEquals(1, _changeCount);
    _changeCount = 0;
    testObject.setAutoScrollFromSource(true);
    assertTrue(testObject.isAutoScrollFromSource());
    assertEquals(0, _changeCount);
    _changeCount = 0;
    testObject.setAutoScrollFromSource(false);
    assertFalse(testObject.isAutoScrollFromSource());
    assertEquals(1, _changeCount);
    _changeCount = 0;

    testObject.setAutoScrollToSource(true);
    assertTrue(testObject.isAutoScrollToSource());
    assertEquals(1, _changeCount);
    _changeCount = 0;
    testObject.setAutoScrollToSource(true);
    assertTrue(testObject.isAutoScrollToSource());
    assertEquals(0, _changeCount);
    _changeCount = 0;
    testObject.setAutoScrollToSource(false);
    assertFalse(testObject.isAutoScrollToSource());
    assertEquals(1, _changeCount);
    _changeCount = 0;

    testObject.setCurrentFolder("test");
    assertEquals("test", testObject.getCurrentFolder());
    assertEquals(1, _changeCount);
    _changeCount = 0;
    testObject.setCurrentFolder("test");
    assertEquals("test", testObject.getCurrentFolder());
    assertEquals(0, _changeCount);
    _changeCount = 0;
    testObject.setCurrentFolder("west");
    assertEquals("west", testObject.getCurrentFolder());
    assertEquals(1, _changeCount);
    _changeCount = 0;

    testObject.setDontShowUnknown(true);
    assertTrue(testObject.isDontShowUnknown());
    assertEquals(1, _changeCount);
    _changeCount = 0;
    testObject.setDontShowUnknown(true);
    assertTrue(testObject.isDontShowUnknown());
    assertEquals(0, _changeCount);
    _changeCount = 0;
    testObject.setDontShowUnknown(false);
    assertFalse(testObject.isDontShowUnknown());
    assertEquals(1, _changeCount);
    _changeCount = 0;

    testObject.setFocusEditor(true);
    assertTrue(testObject.isFocusEditor());
    assertEquals(1, _changeCount);
    _changeCount = 0;
    testObject.setFocusEditor(true);
    assertTrue(testObject.isFocusEditor());
    assertEquals(0, _changeCount);
    _changeCount = 0;
    testObject.setFocusEditor(false);
    assertFalse(testObject.isFocusEditor());
    assertEquals(1, _changeCount);
    _changeCount = 0;

    testObject.setGroupFolders(true);
    assertTrue(testObject.isGroupFolders());
    assertEquals(1, _changeCount);
    _changeCount = 0;
    testObject.setGroupFolders(true);
    assertTrue(testObject.isGroupFolders());
    assertEquals(0, _changeCount);
    _changeCount = 0;
    testObject.setGroupFolders(false);
    assertFalse(testObject.isGroupFolders());
    assertEquals(1, _changeCount);
    _changeCount = 0;

    testObject.removeSettingsChangedListener(this);
    testObject.setGroupFolders(true);
    assertEquals(0, _changeCount);
    _changeCount = 0;
  }

  @Test
  public void testLoad()
  {
    ProjectSettings testObject1 = new ProjectSettings();
    testObject1.setAutoScrollFromSource(true);
    testObject1.setGroupFolders(true);
    assertSame(testObject1, testObject1.getState());

    _changeCount = 0;
    ProjectSettings testObject2 = new ProjectSettings();
    testObject2.addSettingsChangedListener(this);
    testObject2.loadState(testObject1);
    assertEquals(1, _changeCount);
    assertTrue(testObject2.isAutoScrollFromSource());
    assertTrue(testObject2.isGroupFolders());
    _changeCount = 0;
  }

  public void settingsChanged()
  {
    _changeCount++;
  }

  private int _changeCount = 0;
}
