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

import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationListener;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.components.BaseComponent;
import com.intellij.openapi.components.ComponentConfig;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.Key;
import com.intellij.util.messages.MessageBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.picocontainer.PicoContainer;

import java.awt.Component;
import java.util.concurrent.Future;

public class TestApplication implements Application
{

  public void runReadAction(Runnable action)
  {
    throw new UnsupportedOperationException();
  }

  public <T> T runReadAction(Computable<T> computation)
  {
    throw new UnsupportedOperationException();
  }

  public void runWriteAction(Runnable action)
  {
    action.run();
  }

  public <T> T runWriteAction(Computable<T> computation)
  {
    throw new UnsupportedOperationException();
  }

  @Nullable
  public Object getCurrentWriteAction(Class actionClass)
  {
    throw new UnsupportedOperationException();
  }

  public void assertReadAccessAllowed()
  {
    throw new UnsupportedOperationException();
  }

  public void assertWriteAccessAllowed()
  {
    throw new UnsupportedOperationException();
  }

  public void assertIsDispatchThread()
  {
    throw new UnsupportedOperationException();
  }

  public void addApplicationListener(ApplicationListener listener)
  {
    throw new UnsupportedOperationException();
  }

  public void removeApplicationListener(ApplicationListener listener)
  {
    throw new UnsupportedOperationException();
  }

  public void saveAll()
  {
    throw new UnsupportedOperationException();
  }

  public void saveSettings()
  {
    throw new UnsupportedOperationException();
  }

  public void exit()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isWriteAccessAllowed()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isReadAccessAllowed()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isDispatchThread()
  {
    throw new UnsupportedOperationException();
  }

  public void invokeLater(Runnable runnable)
  {
    throw new UnsupportedOperationException();
  }

  public void invokeLater(Runnable runnable, @NotNull ModalityState state)
  {
    throw new UnsupportedOperationException();
  }

  public void invokeAndWait(Runnable runnable, @NotNull ModalityState modalityState)
  {
    throw new UnsupportedOperationException();
  }

  public ModalityState getCurrentModalityState()
  {
    throw new UnsupportedOperationException();
  }

  public ModalityState getModalityStateForComponent(Component c)
  {
    throw new UnsupportedOperationException();
  }

  public ModalityState getDefaultModalityState()
  {
    throw new UnsupportedOperationException();
  }

  public ModalityState getNoneModalityState()
  {
    throw new UnsupportedOperationException();
  }

  public long getStartTime()
  {
    throw new UnsupportedOperationException();
  }

  public long getIdleTime()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isUnitTestMode()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isHeadlessEnvironment()
  {
    throw new UnsupportedOperationException();
  }

  public IdeaPluginDescriptor getPlugin(PluginId id)
  {
    throw new UnsupportedOperationException();
  }

  public IdeaPluginDescriptor[] getPlugins()
  {
    throw new UnsupportedOperationException();
  }

  public BaseComponent getComponent(String name)
  {
    throw new UnsupportedOperationException();
  }

  public <T> T getComponent(Class<T> interfaceClass)
  {
    throw new UnsupportedOperationException();
  }

  public <T> T getComponent(Class<T> interfaceClass, T defaultImplementationIfAbsent)
  {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public Class[] getComponentInterfaces()
  {
    throw new UnsupportedOperationException();
  }

  public boolean hasComponent(@NotNull Class interfaceClass)
  {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public <T> T[] getComponents(Class<T> baseClass)
  {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public PicoContainer getPicoContainer()
  {
    throw new UnsupportedOperationException();
  }

  public MessageBus getMessageBus()
  {
    throw new UnsupportedOperationException();
  }

  public boolean isDisposed()
  {
    throw new UnsupportedOperationException();
  }

  @NotNull
  public ComponentConfig[] getComponentConfigurations()
  {
    throw new UnsupportedOperationException();
  }

  @Nullable
  public Object getComponent(final ComponentConfig componentConfig)
  {
    throw new UnsupportedOperationException();
  }

  public <T> T[] getExtensions(ExtensionPointName<T> extensionPointName)
  {
    throw new UnsupportedOperationException();
  }

  public ComponentConfig getConfig(Class componentImplementation)
  {
    throw new UnsupportedOperationException();
  }

  public Future<?> executeOnPooledThread(Runnable action)
  {
    throw new UnsupportedOperationException();
  }

  public boolean isDisposeInProgress()
  {
    return false;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public <T> T getUserData(Key<T> key)
  {
    throw new UnsupportedOperationException();
  }

  public <T> void putUserData(Key<T> key, T value)
  {
    throw new UnsupportedOperationException();
  }

  public void dispose()
  {
    throw new UnsupportedOperationException();
  }
}
