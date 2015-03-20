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
import com.intellij.openapi.util.IconLoader;
import net.beeger.filebrowser.FolderDisplayManager;
import net.beeger.filebrowser.FileListHandler;
import net.beeger.filebrowser.settings.ApplicationSettings;
import net.beeger.filebrowser.settings.FavoritesSettings;
import net.beeger.filebrowser.settings.ProjectSettings;
import net.beeger.filebrowser.settings.SettingsChangedListener;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"ComponentNotRegistered"})
public class FavoritesActionGroup extends ShutdownAbleActionGroup implements SettingsChangedListener, FileListHandlerAware
{
  public FavoritesActionGroup(FavoriteManagementActionGroup favoriteManagementActionGroup,
                              ProjectSettings projectSettings, ApplicationSettings applicationSettings,
                              FolderDisplayManager folderDisplayManager)
  {
    super("Favorites", true);
    _favoriteManagementActionGroup = favoriteManagementActionGroup;
    _projectSettings = projectSettings;
    _projectSettings.addSettingsChangedListener(this);
    _applicationSettings = applicationSettings;
    _applicationSettings.addSettingsChangedListener(this);
    _folderDisplayManager = folderDisplayManager;

    _projectFavoriteActions = createFavoritesActions(_projectSettings);
    _applicationFavoriteActions = createFavoritesActions(_applicationSettings);

    refreshFavoritesActionGroup();
  }

  public void shutdown()
  {
    super.shutdown();
    _projectSettings.removeSettingsChangedListener(this);
    _applicationSettings.removeSettingsChangedListener(this);
  }

  private void refreshFavoritesActionGroup()
  {
    removeAll();
    addAll(_favoriteManagementActionGroup);
    addSeparator();
    for (AnAction favoriteAction : _projectFavoriteActions)
    {
      add(favoriteAction);
    }
    addSeparator();
    for (AnAction favoriteAction : _applicationFavoriteActions)
    {
      add(favoriteAction);
    }
  }

  private List<AnAction> createFavoritesActions(FavoritesSettings favoritesSettings)
  {
    List<AnAction> result = new ArrayList<AnAction>();
    for (final String favorite : favoritesSettings.getFavorites())
    {
      addFavoriteToMenu(result, favorite);
    }
    return result;
  }


  private void addFavoriteToMenu(List<AnAction> actionGroup, final String favorite)
  {
    insertFavoriteToMenu(actionGroup, favorite, actionGroup.size());
  }

  private void insertFavoriteToMenu(List<AnAction> actionGroup, final String favorite, int position)
  {
    actionGroup.add(position, new AnAction(favorite, new StringBuilder().append("Go to ").append(favorite).toString(), IconLoader.getIcon("/nodes/folder.png"))
    {
      public void actionPerformed(AnActionEvent event)
      {
        _folderDisplayManager.activate(favorite, false);
      }
    });
  }

  private void refreshFavoriteActiions(FavoritesSettings favoritesSettings, List<AnAction> favoriteActions)
  {
    if (favoriteActions != null)
    {
      for (int i = 0; i < favoritesSettings.getFavorites().size(); i++)
      {
        String favorite = favoritesSettings.getFavorites().get(i);
        if (favoriteActions.size() < (i + 1))
        {
          addFavoriteToMenu(favoriteActions, favorite);
        }
        else if (!favoriteActions.get(i).getTemplatePresentation().getText().equals(favorite))
        {
          favoriteActions.remove(i);
          insertFavoriteToMenu(favoriteActions, favorite, i);
        }
      }
      while (favoriteActions.size() > favoritesSettings.getFavorites().size())
      {
        favoriteActions.remove(favoriteActions.size() - 1);
      }
    }
  }


  public void settingsChanged()
  {
    refreshFavoriteActiions(_projectSettings, _projectFavoriteActions);
    refreshFavoriteActiions(_applicationSettings, _applicationFavoriteActions);
    refreshFavoritesActionGroup();
  }

  public void setFileListHandler(FileListHandler fileListHandler)
  {
    _favoriteManagementActionGroup.setFileListHandler(fileListHandler);
  }

  private final FavoriteManagementActionGroup _favoriteManagementActionGroup;
  private final ProjectSettings _projectSettings;
  private final ApplicationSettings _applicationSettings;
  private final FolderDisplayManager _folderDisplayManager;
  private final List<AnAction> _projectFavoriteActions;
  private final List<AnAction> _applicationFavoriteActions;
}
