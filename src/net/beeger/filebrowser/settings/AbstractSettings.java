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

import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.AbstractCollection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSettings implements FavoritesSettings, ObservableSettings
{
  @SuppressWarnings({"WeakerAccess", "UnusedDeclaration"}) protected AbstractSettings()
  {
    _listeners = new ArrayList<SettingsChangedListener>();
  }

  @AbstractCollection(elementTag = "favorite")
  public List<String> getFavorites()
  {
    if (_favorites == null)
    {
      _favorites = new ArrayList<String>();
    }
    return _favorites;
  }

  public void addFavorite(@NotNull String favorite)
  {
    if (_favorites == null)
    {
      _favorites = new ArrayList<String>();
    }
    _favorites.add(favorite);
    fireSettingsChangedEvent();
  }

  public void removeFavorite(@NotNull String favorite)
  {
    if (_favorites != null)
    {
      _favorites.remove(favorite);
    }
    fireSettingsChangedEvent();
  }

  public boolean hasFavorite(@NotNull String favorite)
  {
    return _favorites != null && _favorites.contains(favorite);
  }

  public void addSettingsChangedListener(SettingsChangedListener listener)
  {
    _listeners.add(listener);
  }

  public void removeSettingsChangedListener(SettingsChangedListener listener)
  {
    _listeners.remove(listener);
  }

  @SuppressWarnings({"WeakerAccess"})
  protected void fireSettingsChangedEvent()
  {
    if (!isCurrentlyLoading())
    {
      for (SettingsChangedListener listener : _listeners)
      {
        listener.settingsChanged();
      }
    }
  }

  @SuppressWarnings({"UnusedDeclaration"})
  public void setFavorites(List<String> favorites)
  {
    _favorites = favorites;
    fireSettingsChangedEvent();
  }

  @SuppressWarnings({"BooleanMethodIsAlwaysInverted"})
  private boolean isCurrentlyLoading()
  {
    return _currentlyLoading;
  }

  private void setCurrentlyLoading(boolean currentlyLoading)
  {
    _currentlyLoading = currentlyLoading;
  }

  @SuppressWarnings({"WeakerAccess"})
  protected void load(Object object)
  {
    setCurrentlyLoading(true);
    XmlSerializerUtil.copyBean(object, this);
    setCurrentlyLoading(false);
    fireSettingsChangedEvent();
  }

  private List<String> _favorites;
  private final List<SettingsChangedListener> _listeners;
  private boolean _currentlyLoading;
}
