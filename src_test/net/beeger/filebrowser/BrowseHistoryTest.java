package net.beeger.filebrowser;

import static org.easymock.classextension.EasyMock.*;
import org.junit.Test;
import static org.junit.Assert.*;
import com.intellij.openapi.vfs.VirtualFile;

public class BrowseHistoryTest
{
  @Test
  public void testHistoryNavigation()
  {
    FileSystemHandler fileSystemHandler = createMock(FileSystemHandler.class);

    VirtualFile f1 = createMock(VirtualFile.class);
    expect(f1.getPath()).andReturn("f1").anyTimes();
    VirtualFile f2 = createMock(VirtualFile.class);
    expect(f2.getPath()).andReturn("f2").anyTimes();
    VirtualFile f3 = createMock(VirtualFile.class);
    expect(f3.getPath()).andReturn("f3").anyTimes();
    VirtualFile f4 = createMock(VirtualFile.class);
    expect(f4.getPath()).andReturn("f4").anyTimes();
    VirtualFile f5 = createMock(VirtualFile.class);
    expect(f5.getPath()).andReturn("f5").anyTimes();

    expect(fileSystemHandler.findFileByPath("f1")).andReturn(f1).anyTimes();
    expect(fileSystemHandler.findFileByPath("f2")).andReturn(f2).anyTimes();
    expect(fileSystemHandler.findFileByPath("f3")).andReturn(f3).anyTimes();
    expect(fileSystemHandler.findFileByPath("f4")).andReturn(f4).anyTimes();
    expect(fileSystemHandler.findFileByPath("f5")).andReturn(null);
    expect(fileSystemHandler.findFileByPath("f5")).andReturn(f5);
    expect(fileSystemHandler.findFileByPath("f5")).andReturn(null);

    replay(fileSystemHandler, f1, f2, f3, f4, f5);

    BrowseHistory testObject = new BrowseHistory(fileSystemHandler);

    assertFalse(testObject.canGoBack());
    assertFalse(testObject.canGoForward());

    testObject.add(f1);

    assertSame(f1, testObject.getCurrentFolder());
    assertFalse(testObject.canGoBack());
    assertFalse(testObject.canGoForward());

    testObject.add(f2);

    assertSame(f2, testObject.getCurrentFolder());
    assertTrue(testObject.canGoBack());
    assertFalse(testObject.canGoForward());

    testObject.goBack();
    assertSame(f1, testObject.getCurrentFolder());
    assertFalse(testObject.canGoBack());
    assertTrue(testObject.canGoForward());

    testObject.goForward();
    assertSame(f2, testObject.getCurrentFolder());
    assertTrue(testObject.canGoBack());
    assertFalse(testObject.canGoForward());

    testObject.add(f3);
    assertSame(f3, testObject.getCurrentFolder());
    assertTrue(testObject.canGoBack());
    assertFalse(testObject.canGoForward());

    testObject.goBack();
    assertSame(f2, testObject.getCurrentFolder());
    testObject.goBack();
    assertSame(f1, testObject.getCurrentFolder());
    assertFalse(testObject.canGoBack());
    assertTrue(testObject.canGoForward());

    testObject.add(f4);
    assertSame(f4, testObject.getCurrentFolder());
    assertTrue(testObject.canGoBack());
    assertFalse(testObject.canGoForward());

    testObject.goBack();
    assertSame(f1, testObject.getCurrentFolder());
    assertFalse(testObject.canGoBack());
    assertTrue(testObject.canGoForward());

    testObject.add(f5);
    testObject.add(f2);
    testObject.goBack();
    assertSame(f1, testObject.getCurrentFolder());

    testObject.add(f5);
    testObject.add(f2);
    testObject.goBack();
    testObject.goBack();
    assertSame(f1, testObject.getCurrentFolder());
    testObject.goForward();
    assertSame(f2, testObject.getCurrentFolder());

    verify(fileSystemHandler, f1, f2, f3, f4, f5);
  }
}
