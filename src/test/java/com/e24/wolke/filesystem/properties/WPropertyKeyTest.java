package com.e24.wolke.filesystem.properties;

import com.e24.wolke.backend.models.application.LocaleManager;
import java.awt.Color;
import java.util.Locale;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

/**
 * Classe permettant de tester les fonctionnalites de {@code WPropertyKey}
 *
 * @author MeriBouisri
 * @author Nilon123456789
 */
public class WPropertyKeyTest {

  /** version actuel du fichier */
  private static final String FILE_VERSION = "valid";

  /** Nom du fichier de configuration */
  private static final String FILE_NAME = "test-defaults.properties";

  /** Chemin du fichier de configuration par défaut */
  private static final String DEFAULT_FILE_PATH = "";

  /** Instance de la classe à tester */
  private static PropertiesManager manager;

  /**
   * Règle JUnit permettant de sauvegarder le fichier de configuration avant tout les tests et de le
   * restaurer après tout les tests
   */
  @ClassRule public static ExternalResource resource = PropertiesManagerWriteTest.resource;

  /** Initialisation des proprietes utilisateur */
  @BeforeClass
  public static void setup() {
    LocaleManager.update(Locale.FRENCH); // Initialisation de la locale pour les log
    manager = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION, true);
    manager.resetUserProperties();

    WPropertyKey.setPropertiesManager(manager);
  }

  /** Test pour {@code WPropertyKey#write(Object)} */
  @Test
  public void testWriteInteger() {
    int expected = 2;
    Assert.assertTrue(WPropertyKey.TEST_INTEGER.write(expected));
    int actual = manager.getIntProperty(WPropertyKey.TEST_INTEGER.getKey());
    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#write(Object)} */
  @Test
  public void testWriteDouble() {
    double expected = 2.0;
    Assert.assertTrue(WPropertyKey.TEST_DOUBLE.write(expected));
    double actual = manager.getDoubleProperty(WPropertyKey.TEST_DOUBLE.getKey());
    Assert.assertEquals(expected, actual, 0);
  }

  /** Test pour {@code WPropertyKey#write(Object)} */
  @Test
  public void testWriteBoolean() {
    boolean expected = true;
    Assert.assertTrue(WPropertyKey.TEST_BOOLEAN.write(expected));
    boolean actual = manager.getBooleanProperty(WPropertyKey.TEST_BOOLEAN.getKey());
    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#write(Object)} */
  @Test
  public void testWriteString() {
    String expected = "Hello World!";
    Assert.assertTrue(WPropertyKey.TEST_STRING.write(expected));
    String actual = manager.getStringProperty(WPropertyKey.TEST_STRING.getKey());
    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#write(Object)} */
  @Test
  public void testWriteIntegerArray() {
    int[] expected = {1, 2, 3};
    Assert.assertTrue(WPropertyKey.TEST_INTEGER_ARRAY.write(expected));
    int[] actual = manager.getIntArrayProperty(WPropertyKey.TEST_INTEGER_ARRAY.getKey());
    Assert.assertArrayEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#write(Object)} */
  @Test
  public void testWriteDoubleArray() {
    double[] expected = {1.0, 2.0, 3.0};
    Assert.assertTrue(WPropertyKey.TEST_DOUBLE_ARRAY.write(expected));
    double[] actual = manager.getDoubleArrayProperty(WPropertyKey.TEST_DOUBLE_ARRAY.getKey());
    Assert.assertArrayEquals(expected, actual, 0);
  }

  /** Test pour {@code WPropertyKey#write(Object)} */
  @Test
  public void testWriteColor() {
    Color expected = Color.BLACK;
    Assert.assertTrue(WPropertyKey.TEST_COLOR.write(expected));
    Color actual = manager.getColorProperty(WPropertyKey.TEST_COLOR.getKey());
    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#read(Object)} */
  @Test
  public void testReadInteger() {
    int expected = 2;
    manager.setIntProperty(WPropertyKey.TEST_INTEGER.getKey(), expected);
    int actual = (int) WPropertyKey.TEST_INTEGER.read();
    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#read(Object)} */
  @Test
  public void testReadDouble() {
    double expected = 2.0;
    manager.setDoubleProperty(WPropertyKey.TEST_DOUBLE.getKey(), expected);
    double actual = (double) WPropertyKey.TEST_DOUBLE.read();
    Assert.assertEquals(expected, actual, 0);
  }

  /** Test pour {@code WPropertyKey#read(Object)} */
  @Test
  public void testReadBoolean() {
    boolean expected = true;
    manager.setBooleanProperty(WPropertyKey.TEST_BOOLEAN.getKey(), expected);
    boolean actual = (boolean) WPropertyKey.TEST_BOOLEAN.read();
    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#read(Object)} */
  @Test
  public void testReadString() {
    String expected = "Hello World!";
    manager.setStringProperty(WPropertyKey.TEST_STRING.getKey(), expected);
    String actual = (String) WPropertyKey.TEST_STRING.read();
    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#read(Object)} */
  @Test
  public void testReadIntegerArray() {
    int[] expected = {1, 2, 3};
    manager.setIntArrayProperty(WPropertyKey.TEST_INTEGER_ARRAY.getKey(), expected);
    int[] actual = (int[]) WPropertyKey.TEST_INTEGER_ARRAY.read();
    Assert.assertArrayEquals(expected, actual);
  }

  /** Test pour {@code WPropertyKey#read(Object)} */
  @Test
  public void testReadDoubleArray() {
    double[] expected = {1.0, 2.0, 3.0};
    manager.setDoubleArrayProperty(WPropertyKey.TEST_DOUBLE_ARRAY.getKey(), expected);
    double[] actual = (double[]) WPropertyKey.TEST_DOUBLE_ARRAY.read();
    Assert.assertArrayEquals(expected, actual, 0);
  }

  /** Test pour {@code WPropertyKey#read(Object)} */
  @Test
  public void testReadColor() {
    Color expected = Color.BLACK;

    manager.setColorProperty(WPropertyKey.TEST_COLOR.getKey(), expected);
    Color actual = (Color) WPropertyKey.TEST_COLOR.read();
    Assert.assertEquals(expected, actual);
  }

  /** Test pour voir si toutes les clés existes */
  @Test
  public void testAllKeysExist() {
    for (WPropertyKey key : WPropertyKey.values()) {
      if (key.isTest()) continue; // On ignore les clés de test

      Assert.assertNotNull(key.read(SettingsPropertiesManager.INSTANCE));
    }
  }
}
