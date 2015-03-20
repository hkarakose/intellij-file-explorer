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

import com.intellij.openapi.application.Application;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.UserFileType;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import net.beeger.filebrowser.settings.ApplicationSettings;
import net.beeger.filebrowser.settings.ProjectSettings;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.easymock.classextension.EasyMock.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"CanBeFinal"}) public class FolderDisplayManagerImplTest
{
  @Before
  public void setUp() throws Exception
  {
    _application = new TestApplication();
    _projectSettings = new ProjectSettings();
    _applicationSetttings = new ApplicationSettings();
    _fileListHandler = new TestFileListHandler();

    _fileTypeManager = createMock(FileTypeManager.class);
    _fileEditorManager = createMock(FileEditorManager.class);

    KnownFileType knownFileType = new KnownFileType();
    UnKnownFileType unKnownFileType = new UnKnownFileType();

    _root = new TestVirtualFile("r", true, null);
    _projectDir = _root.createChildDirectory(this, "p");
    ((TestVirtualFile) _projectDir.createChildData(this, "a.unk")).setFileType(unKnownFileType);
    _projectDir.createChildDirectory(this, "b");
    ((TestVirtualFile) _projectDir.createChildData(this, "c.txt")).setFileType(knownFileType);
    _projectDir.createChildDirectory(this, "d");

    _fileSystemHandler = new TestFileSystemHandler();
    _browseHistory = new BrowseHistory(_fileSystemHandler);

    replay(_fileTypeManager, _fileEditorManager);
  }

  @After
  public void tearDown()
  {
    verify(_fileTypeManager, _fileEditorManager);
  }

  @Test
  public void testNavigation()
  {
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    assertEquals(5, _fileListHandler.getFiles().size());
    assertEquals("r", _fileListHandler.getFiles().get(0).getName());
    assertEquals("a.unk", _fileListHandler.getFiles().get(1).getName());
    assertEquals("b", _fileListHandler.getFiles().get(2).getName());
    assertEquals("c.txt", _fileListHandler.getFiles().get(3).getName());
    assertEquals("d", _fileListHandler.getFiles().get(4).getName());
    assertEquals("/r/p", testObject.getCurrentFolderName());

    testObject.showFolderContents("/r");
    assertEquals(1, _fileListHandler.getFiles().size());
    assertEquals("p", _fileListHandler.getFiles().get(0).getName());
    assertEquals("/r", testObject.getCurrentFolderName());
  }

  @Test
  public void testGroupFolders()
  {
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    _projectSettings.setGroupFolders(true);
    testObject.resortFileList();

    assertEquals(5, _fileListHandler.getFiles().size());
    assertEquals("r", _fileListHandler.getFiles().get(0).getName());
    assertEquals("b", _fileListHandler.getFiles().get(1).getName());
    assertEquals("d", _fileListHandler.getFiles().get(2).getName());
    assertEquals("a.unk", _fileListHandler.getFiles().get(3).getName());
    assertEquals("c.txt", _fileListHandler.getFiles().get(4).getName());
  }

  @Test
  public void testDontShowUnknown()
  {
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    _projectSettings.setDontShowUnknown(true);
    testObject.refreshCurrentFolderDisplay(false);

    assertEquals(4, _fileListHandler.getFiles().size());
    assertEquals("r", _fileListHandler.getFiles().get(0).getName());
    assertEquals("b", _fileListHandler.getFiles().get(1).getName());
    assertEquals("c.txt", _fileListHandler.getFiles().get(2).getName());
    assertEquals("d", _fileListHandler.getFiles().get(3).getName());
  }

  @Test
  public void testFilter()
  {
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    _fileListHandler.setFilter("a");
    testObject.refreshCurrentFolderDisplay(true);

    assertEquals(2, _fileListHandler.getFiles().size());
    assertEquals("r", _fileListHandler.getFiles().get(0).getName());
    assertEquals("a.unk", _fileListHandler.getFiles().get(1).getName());
    assertTrue(_fileListHandler.isFilterValid());

    _fileListHandler.setFilter("aaaaa");
    testObject.refreshCurrentFolderDisplay(true);
    assertEquals(1, _fileListHandler.getFiles().size());
    assertEquals("r", _fileListHandler.getFiles().get(0).getName());
    assertFalse(_fileListHandler.isFilterValid());
  }

  @Test
  public void testIsSelectedEntryFolder()
  {
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    _fileListHandler.setSelectedFile(_projectDir);
    assertTrue(testObject.isSelectedEntryFolder());

    _fileListHandler.setSelectedFile(_root.find("/r/p/a.unk"));
    assertFalse(testObject.isSelectedEntryFolder());
  }

  @Test
  public void testCurrentFolderSet()
  {
    _projectSettings.setCurrentFolder("/r");
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    assertEquals(1, _fileListHandler.getFiles().size());
    assertEquals("p", _fileListHandler.getFiles().get(0).getName());
  }

  @Test
  public void testShowRoots()
  {
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    testObject.showFolderContents((VirtualFile) null);

    assertEquals(1, _fileListHandler.getFiles().size());
    assertEquals("r", _fileListHandler.getFiles().get(0).getName());
  }

  @Test
  public void testSelect()
  {
    FolderDisplayManagerImpl testObject = new FolderDisplayManagerImpl(_application, _projectSettings, _applicationSetttings, _fileSystemHandler, _fileTypeManager, _fileEditorManager, _browseHistory);
    testObject.start(_projectDir, _fileListHandler);

    testObject.select(_root.find("/r/p"));

    assertEquals(1, _fileListHandler.getFiles().size());
    assertEquals("p", _fileListHandler.getFiles().get(0).getName());
  }


  private Application _application;
  private ProjectSettings _projectSettings;
  private TestFileListHandler _fileListHandler;
  private TestVirtualFile _root;
  private VirtualFile _projectDir;
  private FileSystemHandler _fileSystemHandler;
  private ApplicationSettings _applicationSetttings;
  private FileTypeManager _fileTypeManager;
  private FileEditorManager _fileEditorManager;
  private BrowseHistory _browseHistory;

  private static class TestVirtualFile extends LightVirtualFile
  {
    public TestVirtualFile(@NonNls String name, boolean directory, VirtualFile parent)
    {
      super(name);
      _isDirectory = directory;
      _parent = parent;
      _children = new ArrayList<TestVirtualFile>();
    }

    public boolean isDirectory()
    {
      return _isDirectory;
    }

    public VirtualFile createChildDirectory(Object requestor, @NonNls String name) throws IOException
    {
      if (!isDirectory())
      {
        throw new IOException("is not directory");
      }
      TestVirtualFile child = new TestVirtualFile(name, true, this);
      _children.add(child);
      return child;
    }

    public VirtualFile createChildData(Object requestor, @NotNull @NonNls String name) throws IOException
    {
      if (!isDirectory())
      {
        throw new IOException("is not directory");
      }
      TestVirtualFile child = new TestVirtualFile(name, false, this);
      _children.add(child);
      return child;
    }

    public VirtualFile[] getChildren()
    {
      return _children.toArray(new VirtualFile[_children.size()]);
    }

    public String getPath()
    {
      return (_parent != null ? _parent.getPath() : "") + "/" + getName();
    }

    public VirtualFile getParent()
    {
      return _parent;
    }

    public void refresh(boolean asynchronous, boolean recursive, Runnable postRunnable)
    {
      postRunnable.run();
    }

    public VirtualFile find(final String path)
    {
      VirtualFile result = null;
      if (path.startsWith("/" + getName()))
      {
        String newPath = path.substring(1 + getName().length());
        if (newPath.length() == 0)
        {
          result = this;
        }
        else if (isDirectory())
        {
          for (TestVirtualFile child : _children)
          {
            result = child.find(newPath);
            if (result != null)
            {
              break;
            }
          }
        }
      }
      return result;
    }

    @NotNull
    public FileType getFileType()
    {
      return _fileType;
    }

    public void setFileType(FileType fileType)
    {
      _fileType = fileType;
    }

    private boolean _isDirectory;
    private List<TestVirtualFile> _children;
    private VirtualFile _parent;
    private FileType _fileType;
  }

  private class TestFileSystemHandler implements FileSystemHandler
  {

    public List<VirtualFile> getRoots()
    {
      List<VirtualFile> result = new ArrayList<VirtualFile>();
      result.add(_root);
      return result;
    }

    public boolean hasUnknownFileType(VirtualFile virtualFile)
    {
      return virtualFile.getFileType() instanceof UnKnownFileType;
    }

    public VirtualFile findFileByPath(String path)
    {
      return _root.find(path);
    }
  }

  private class TestFileListHandler implements FileListHandler
  {
    public TestFileListHandler()
    {
      _filter = "";
    }

    public String getFilter()
    {
      return _filter;
    }

    public void setFilter(String filter)
    {
      _filter = filter;
    }

    public void resetFilter()
    {
      _filter = "";
    }

    @NotNull
    public List<VirtualFile> getSelectedFiles()
    {
      List<VirtualFile> result = new ArrayList<VirtualFile>();
      result.add(_selectedFile);
      return result;
    }

    public void setSelectedFile(VirtualFile file)
    {
      _selectedFile = file;
    }

    public void setListContents(List<VirtualFile> files)
    {
      _files = files;
    }

    public void setFilterValid(boolean valid)
    {
      _filterValid = valid;
    }

    public boolean hasListFocus()
    {
      return false; 
    }

    public List<VirtualFile> getFiles()
    {
      return _files;
    }

    public boolean isFilterValid()
    {
      return _filterValid;
    }

    private String _filter;
    private VirtualFile _selectedFile;
    private List<VirtualFile> _files;
    private boolean _filterValid;
  }

  private static class KnownFileType extends UserFileType
  {
    public SettingsEditor getEditor()
    {
      return null;
    }

    public boolean isBinary()
    {
      return false;
    }

    @Nullable
    public SyntaxHighlighter getHighlighter(@Nullable Project project, final VirtualFile virtualFile)
    {
      return null;
    }
  }

  private static class UnKnownFileType extends UserFileType
  {

    public SettingsEditor getEditor()
    {
      return null;
    }

    public boolean isBinary()
    {
      return false;
    }

    @Nullable
    public SyntaxHighlighter getHighlighter(@Nullable Project project, final VirtualFile virtualFile)
    {
      return null;
    }
  }

}
