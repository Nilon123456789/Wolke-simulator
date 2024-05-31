package com.e24.wolke.filesystem.properties;

import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les fonctionnalites de {@code SettingsPropertiesManager}
 *
 * @author Nilon123456789
 */
public class SettingsPropertiesManagerTest {

  /** Test la version du fichier */
  @Test
  public void testFileVersion() {
    Assert.assertEquals(
        SettingsPropertiesManager.FILE_VERSION,
        SettingsPropertiesManager.INSTANCE.getStringProperty("version"));
  }
}
