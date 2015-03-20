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

import org.jetbrains.annotations.NotNull;

public enum DestinationChooserConfiguration
{
  COPY("Copy"), MOVE("Move", false), GOTO("Go To", false);

  DestinationChooserConfiguration(@NotNull String operation)
  {
    this(operation, true);
  }

  DestinationChooserConfiguration(@NotNull String operation, boolean showNewName)
  {
    this(operation, showNewName, "New name", 'n', "To folder", 'f');
  }

  DestinationChooserConfiguration(
      @NotNull String operation, boolean showNewName,
      @NotNull String newNameLabel, char newNameLabelMnemonic,
      @NotNull String destinationFolderLabel, char destinationFolderLabelMnemonic)
  {
    _destinationFolderLabelMnemonic = destinationFolderLabelMnemonic;
    _newNameLabelMnemonic = newNameLabelMnemonic;
    _showNewName = showNewName;
    _newNameLabel = newNameLabel;
    _destinationFolderLabel = destinationFolderLabel;
    _operation = operation;
  }

  public boolean isShowNewName()
  {
    return _showNewName;
  }

  @NotNull
  public String getNewNameLabel()
  {
    return _newNameLabel;
  }

  @NotNull
  public String getDestinationFolderLabel()
  {
    return _destinationFolderLabel;
  }

  @NotNull
  public String getOperation()
  {
    return _operation;
  }

  public char getNewNameLabelMnemonic()
  {
    return _newNameLabelMnemonic;
  }

  public char getDestinationFolderLabelMnemonic()
  {
    return _destinationFolderLabelMnemonic;
  }

  private final boolean _showNewName;
  private final String _newNameLabel;
  private final char _newNameLabelMnemonic;
  private final String _destinationFolderLabel;
  private final char _destinationFolderLabelMnemonic;
  private final String _operation;
}
