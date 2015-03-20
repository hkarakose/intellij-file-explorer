package net.beeger.filebrowser.settings;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class ApplicationSettingsEditor implements ApplicationComponent, Configurable
{
  public ApplicationSettingsEditor(ApplicationSettings applicationSettings)
  {
    _applicationSettings = applicationSettings;
  }

  @NonNls @NotNull public String getComponentName()
  {
    return "ApplicationSettingsEditor";
  }

  public void initComponent()
  {
  }

  public void disposeComponent()
  {
  }

  @Nls public String getDisplayName()
  {
    return "FileBrowser";
  }

  @Nullable public Icon getIcon()
  {
    return IconLoader.getIcon("/net/beeger/filebrowser/images/filebrowser32.png");
  }

  @Nullable @NonNls public String getHelpTopic()
  {
    return null;
  }

  public JComponent createComponent()
  {
    _panel = new JPanel(new BorderLayout(3,3));
    _showParent = (JCheckBox) _panel.add(new JCheckBox("Show parent in file list"));
    return _panel;
  }

  public boolean isModified()
  {
    return _applicationSettings.isShowParent() != _showParent.isSelected(); 
  }

  public void apply() throws ConfigurationException
  {
    _applicationSettings.setShowParent(_showParent.isSelected());
  }

  public void reset()
  {
    _showParent.setSelected(_applicationSettings.isShowParent());
  }

  public void disposeUIResources()
  {
    _panel = null;
    _showParent = null;
  }

  private final ApplicationSettings _applicationSettings;
  private JPanel _panel;
  private JCheckBox _showParent;
}
