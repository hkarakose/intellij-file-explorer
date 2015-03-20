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
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ComboboxWithBrowseButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

@SuppressWarnings({"WeakerAccess"}) public class DestinationChooserImpl extends JDialog implements DestinationChooser
{
  public DestinationChooserImpl(FolderChooserHandlerFactory folderChooserHandlerFactory, Project project)
  {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(buttonOK);

    buttonOK.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        onOK();
      }
    });

    buttonCancel.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        onCancel();
      }
    });

// call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        onCancel();
      }
    });

// call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


    _folderChooserHandler = folderChooserHandlerFactory.createFolderChooserHandler(_destinationFolder);

    pack();
    setLocationRelativeTo(null);
  }

  public boolean askForDestination(@NotNull DestinationChooserConfiguration configuration, @NotNull List<VirtualFile> files)
  {
    if (files.size() == 0)
    {
      throw new IllegalArgumentException("No files provided for operation");
    }
    if (files.get(0).getParent() == null)
    {
      throw new IllegalArgumentException("Cannot choose a destination for a root directory");
    }
    setTitle(configuration.getOperation());
    setNewName(files.get(0).getName());
    //noinspection ConstantConditions
    setDestinationFolder(files.get(0).getParent().getPath());
    setSingleFileOperation(files.size() == 1);
    _newNameLabel.setVisible(configuration.isShowNewName() && isSingleFileOperation());
    _newName.setVisible(configuration.isShowNewName() && isSingleFileOperation());

    _newNameLabel.setText(configuration.getNewNameLabel());
    _newNameLabel.setDisplayedMnemonic(configuration.getNewNameLabelMnemonic());
    _destinationFolderLabel.setText(configuration.getDestinationFolderLabel());
    _destinationFolderLabel.setDisplayedMnemonic(configuration.getDestinationFolderLabelMnemonic());
    setVisible(true);

    return isConfirmed();
  }

  public boolean askForDestination(@NotNull DestinationChooserConfiguration configuration, @Nullable VirtualFile initialFolder)
  {
    setTitle(configuration.getOperation());
    if (initialFolder != null)
    {
      setDestinationFolder(initialFolder.getPath());
    }
    _newNameLabel.setVisible(configuration.isShowNewName());
    _newName.setVisible(configuration.isShowNewName());

    _newNameLabel.setText(configuration.getNewNameLabel());
    _newNameLabel.setDisplayedMnemonic(configuration.getNewNameLabelMnemonic());
    _destinationFolderLabel.setText(configuration.getDestinationFolderLabel());
    _destinationFolderLabel.setDisplayedMnemonic(configuration.getDestinationFolderLabelMnemonic());
    setVisible(true);

    return isConfirmed();
  }

  private void onOK()
  {
    _confirmed = true;
    dispose();
  }

  private void onCancel()
  {
    _confirmed = false;
    dispose();
  }

  public boolean isConfirmed()
  {
    return _confirmed;
  }

  public boolean isSingleFileOperation()
  {
    return _singleFileOperation;
  }

  public void setSingleFileOperation(boolean singleFileOperation)
  {
    _singleFileOperation = singleFileOperation;
  }

  public void setNewName(String newName)
  {
    _newName.setText(newName);
  }

  public void setDestinationFolder(String path)
  {
    _destinationFolder.getComboBox().getEditor().setItem(path);
  }

  public String getNewName()
  {
    return _newName.getText();
  }

  public VirtualFile getDestinationFolder()
  {
    return _folderChooserHandler.getChosenFolder();
  }

  private boolean _singleFileOperation;
  private boolean _confirmed;
  private JPanel contentPane;
  private JButton buttonOK;
  private JButton buttonCancel;
  private JTextField _newName;
  private ComboboxWithBrowseButton _destinationFolder;
  private JLabel _newNameLabel;
  private JLabel _destinationFolderLabel;
  private final FolderChooserHandler _folderChooserHandler;
}
