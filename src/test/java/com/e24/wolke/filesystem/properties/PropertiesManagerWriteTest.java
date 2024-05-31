package com.e24.wolke.filesystem.properties;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.filesystem.WFileSystemConstant;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Locale;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

/**
 * Classe de test pour la classe {@code PropertiesManagerWriteTest}
 *
 * @author Nilon123456789
 */
public class PropertiesManagerWriteTest {

  /** version actuel du fichier */
  private static final String FILE_VERSION = "valid";

  /** Nom du fichier de configuration */
  private static final String FILE_NAME = "test-defaults.properties";

  /** Chemin du fichier de configuration par défaut */
  private static final String DEFAULT_FILE_PATH = "";

  /** Instance de la classe à tester */
  private static PropertiesManager writer;

  /**
   * Règle JUnit permettant de sauvegarder le fichier de configuration avant tout les tests et de le
   * restaurer après tout les tests
   */
  @ClassRule
  public static ExternalResource resource =
      new ExternalResource() {

        private final File sourceFile = new File(WFileSystemConstant.SAVE_PATH + FILE_NAME);
        private final File testBackupFile =
            new File(WFileSystemConstant.SAVE_PATH + FILE_NAME + ".test.bak");
        private final File backupFile =
            new File(WFileSystemConstant.SAVE_PATH + FILE_NAME + ".bak");
        private boolean exists = false;

        /** Sauvegarde le fichier de configuration */
        @Override
        protected void before() throws Throwable {
          if (sourceFile.exists()) {
            exists = sourceFile.exists();
            try {
              Files.copy(
                  sourceFile.toPath(),
                  testBackupFile.toPath(),
                  StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
              throw new RuntimeException("Failed to copy file", e);
            }
          }
        }

        /** Restaure le fichier de configuration */
        @Override
        protected void after() {
          try {
            if (testBackupFile.exists() && exists) {

              Files.copy(
                  testBackupFile.toPath(),
                  sourceFile.toPath(),
                  StandardCopyOption.REPLACE_EXISTING);
              Files.delete(testBackupFile.toPath());

            } else if (sourceFile.exists()) {
              Files.delete(sourceFile.toPath());
              if (testBackupFile.exists()) Files.delete(testBackupFile.toPath());
            }

            if (backupFile.exists())
              Files.delete(
                  backupFile.toPath()); // On supprime le fichier de backup puisqu'il continent les
            // données du test
          } catch (IOException e) {
            throw new RuntimeException("Failed to copy file", e);
          }
        }
      };

  /** Initialisation des propriétés utilisateur */
  @BeforeClass
  public static void setUp() {
    LocaleManager.update(Locale.FRENCH); // Initialisation de la locale pour les log
    writer = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION, true);
    writer.resetUserProperties();
  }

  /** Reinitalise les propriétés utilisateur */
  @Before
  public void resetProperties() {
    writer.resetUserProperties();
  }

  /** Test de la méthode {@code isEditable} */
  @Test
  public void testIsEditable() {
    Assert.assertTrue(writer.isEditable());
  }

  /** Test de la méthode {@code isEditable} */
  @Test
  public void testIsNotEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.isEditable());
  }

  /** Test de la méthode {@code setProperty} */
  @Test
  public void testSetProperty() {
    Assert.assertTrue(writer.setProperty("test.string.property", "New Hello, World!"));
    String value = writer.getStringProperty("test.string.property");
    Assert.assertEquals("New Hello, World!", value);
  }

  /** Test de la méthode {@code setProperty} */
  @Test
  public void testSetPropertyWithOutSave() {
    String expected = writer.getStringProperty("test.string.property");

    Assert.assertTrue(writer.setProperty("test.string.property", "New Hello, World!", false));
    writer.loadPropertiesFiles();

    String value = writer.getStringProperty("test.string.property");
    Assert.assertEquals(expected, value);
  }

  /** Test de la méthode {@code setProperty} */
  @Test
  public void testSetPropertyWithSave() {
    Assert.assertTrue(writer.setProperty("test.string.property", "New Hello, World!", true));
    writer.loadPropertiesFiles();

    String value = writer.getStringProperty("test.string.property");
    Assert.assertEquals("New Hello, World!", value);
  }

  /** Test de la méthode {@code setProperty} */
  @Test
  public void testSetPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setProperty("", "New Hello, World!"));
  }

  /** Test de la méthode {@code setProperty} */
  @Test
  public void testSetPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.setProperty("test.string.property", "New Hello, World!"));
  }

  /** Test de la méthode {@code setStringProperty} */
  @Test
  public void testSetStringProperty() {
    Assert.assertTrue(writer.setStringProperty("test.string.property", "New Hello, World!"));
    String value = writer.getStringProperty("test.string.property");
    Assert.assertEquals("New Hello, World!", value);
  }

  /** Test de la méthode {@code setStringProperty} */
  @Test
  public void testSetStringPropertyWithOutSave() {
    String expected = writer.getStringProperty("test.string.property");

    Assert.assertTrue(writer.setStringProperty("test.string.property", "New Hello, World!", false));
    writer.loadPropertiesFiles();

    String value = writer.getStringProperty("test.string.property");
    Assert.assertEquals(expected, value);
  }

  /** Test de la méthode {@code setStringProperty} */
  @Test
  public void testSetStringPropertyWithSave() {
    Assert.assertTrue(writer.setStringProperty("test.string.property", "New Hello, World!", true));
    writer.loadPropertiesFiles();

    String value = writer.getStringProperty("test.string.property");
    Assert.assertEquals("New Hello, World!", value);
  }

  /** Test de la méthode {@code setStringProperty} */
  @Test
  public void testSetStringPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setStringProperty("", "New Hello, World!"));
  }

  /** Test de la méthode {@code setStringProperty} */
  @Test
  public void testSetStringPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.setStringProperty("test.string.property", "New Hello, World!"));
  }

  /** Test de la méthode {@code setIntProperty} */
  @Test
  public void testSetIntProperty() {
    Assert.assertTrue(writer.setIntProperty("test.int.property", 42));
    int value = writer.getIntProperty("test.int.property");
    Assert.assertEquals(42, value);
  }

  /** Test de la méthode {@code setIntProperty} */
  @Test
  public void testSetIntPropertyWithOutSave() {
    int expected = writer.getIntProperty("test.int.property");

    Assert.assertTrue(writer.setIntProperty("test.int.property", 42, false));
    writer.loadPropertiesFiles();

    int value = writer.getIntProperty("test.int.property");
    Assert.assertEquals(expected, value);
  }

  /** Test de la méthode {@code setIntProperty} */
  @Test
  public void testSetIntPropertyWithSave() {
    Assert.assertTrue(writer.setIntProperty("test.int.property", 42, true));
    writer.loadPropertiesFiles();

    int value = writer.getIntProperty("test.int.property");
    Assert.assertEquals(42, value);
  }

  /** Test de la méthode {@code setIntProperty} */
  @Test
  public void testSetIntPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setIntProperty("", 42));
  }

  /** Test de la méthode {@code setIntProperty} */
  @Test
  public void testSetIntPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.setIntProperty("test.int.property", 42));
  }

  /** Test de la méthode {@code setBooleanProperty} */
  @Test
  public void testSetBooleanProperty() {
    Assert.assertTrue(writer.setBooleanProperty("test.boolean.property", true));
    boolean value = writer.getBooleanProperty("test.boolean.property");
    Assert.assertTrue(value);
  }

  /** Test de la méthode {@code setBooleanProperty} */
  @Test
  public void testSetBooleanPropertyWithOutSave() {
    boolean expected = writer.getBooleanProperty("test.boolean.property");

    Assert.assertTrue(writer.setBooleanProperty("test.boolean.property", true, false));
    writer.loadPropertiesFiles();

    boolean value = writer.getBooleanProperty("test.boolean.property");
    Assert.assertEquals(expected, value);
  }

  /** Test de la méthode {@code setBooleanProperty} */
  @Test
  public void testSetBooleanPropertyWithSave() {
    Assert.assertTrue(writer.setBooleanProperty("test.boolean.property", true, true));
    writer.loadPropertiesFiles();

    boolean value = writer.getBooleanProperty("test.boolean.property");
    Assert.assertTrue(value);
  }

  /** Test de la méthode {@code setBooleanProperty} */
  @Test
  public void testSetBooleanPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setBooleanProperty("", true));
  }

  /** Test de la méthode {@code setBooleanProperty} */
  @Test
  public void testSetBooleanPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.setBooleanProperty("test.boolean.property", true));
  }

  /** Test de la méthode {@code setDoubleProperty} */
  @Test
  public void testSetDoubleProperty() {
    Assert.assertTrue(writer.setDoubleProperty("test.double.property", 42.42));
    double value = writer.getDoubleProperty("test.double.property");
    Assert.assertEquals(42.42, value, 0.0);
  }

  /** Test de la méthode {@code setDoubleProperty} */
  @Test
  public void testSetDoublePropertyWithOutSave() {
    double expected = writer.getDoubleProperty("test.double.property");

    Assert.assertTrue(writer.setDoubleProperty("test.double.property", 42.42, false));
    writer.loadPropertiesFiles();

    double value = writer.getDoubleProperty("test.double.property");
    Assert.assertEquals(expected, value, 0.0);
  }

  /** Test de la méthode {@code setDoubleProperty} */
  @Test
  public void testSetDoublePropertyWithSave() {
    Assert.assertTrue(writer.setDoubleProperty("test.double.property", 42.42, true));
    writer.loadPropertiesFiles();

    double value = writer.getDoubleProperty("test.double.property");
    Assert.assertEquals(42.42, value, 0.0);
  }

  /** Test de la méthode {@code setDoubleProperty} */
  @Test
  public void testSetDoublePropertyWithEmptyKey() {
    Assert.assertFalse(writer.setDoubleProperty("", 42.42));
  }

  /** Test de la méthode {@code setDoubleProperty} */
  @Test
  public void testSetDoublePropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.setDoubleProperty("test.double.property", 42.42));
  }

  /** Test de la méthode {@code setArrayProperty} */
  @Test
  public void testSetArrayProperty() {
    String[] value = new String[] {"3", "2", "1"};

    Assert.assertTrue(writer.setArrayProperty("test.array.property", value));
    String[] result = writer.getArrayProperty("test.array.property");
    Assert.assertArrayEquals(value, result);
  }

  /** Test de la méthode {@code setArrayProperty} */
  @Test
  public void testSetArrayPropertyWithOutSave() {
    String[] expected = writer.getArrayProperty("test.array.property");

    Assert.assertTrue(
        writer.setArrayProperty("test.array.property", new String[] {"3", "2", "1"}, false));
    writer.loadPropertiesFiles();

    String[] result = writer.getArrayProperty("test.array.property");
    Assert.assertArrayEquals(expected, result);
  }

  /** Test de la méthode {@code setArrayProperty} */
  @Test
  public void testSetArrayPropertyWithSave() {
    String[] value = new String[] {"3", "2", "1"};

    Assert.assertTrue(writer.setArrayProperty("test.array.property", value, true));
    writer.loadPropertiesFiles();

    String[] result = writer.getArrayProperty("test.array.property");
    Assert.assertArrayEquals(value, result);
  }

  /** Test de la méthode {@code setArrayProperty} */
  @Test
  public void testSetArrayPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setArrayProperty("", new String[] {"3", "2", "1"}));
  }

  /** Test de la méthode {@code setArrayProperty} */
  @Test
  public void testSetArrayPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(
        reader.setArrayProperty("test.array.property", new String[] {"3", "2", "1"}));
  }

  /** Test de la méthode {@code setIntArrayProperty} */
  @Test
  public void testSetIntArrayProperty() {
    int[] value = new int[] {3, 2, 1};

    Assert.assertTrue(writer.setIntArrayProperty("test.int.array.property", value));
    int[] result = writer.getIntArrayProperty("test.int.array.property");
    Assert.assertArrayEquals(value, result);
  }

  /** Test de la méthode {@code setIntArrayProperty} */
  @Test
  public void testSetIntArrayPropertyWithOutSave() {
    int[] expected = writer.getIntArrayProperty("test.int.array.property");

    Assert.assertTrue(
        writer.setIntArrayProperty("test.int.array.property", new int[] {3, 2, 1}, false));
    writer.loadPropertiesFiles();

    int[] result = writer.getIntArrayProperty("test.int.array.property");
    Assert.assertArrayEquals(expected, result);
  }

  /** Test de la méthode {@code setIntArrayProperty} */
  @Test
  public void testSetIntArrayPropertyWithSave() {
    int[] value = new int[] {3, 2, 1};

    Assert.assertTrue(writer.setIntArrayProperty("test.int.array.property", value, true));
    writer.loadPropertiesFiles();

    int[] result = writer.getIntArrayProperty("test.int.array.property");
    Assert.assertArrayEquals(value, result);
  }

  /** Test de la méthode {@code setIntArrayProperty} */
  @Test
  public void testSetIntArrayPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setIntArrayProperty("", new int[] {3, 2, 1}));
  }

  /** Test de la méthode {@code setIntArrayProperty} */
  @Test
  public void testSetIntArrayPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.setIntArrayProperty("test.int.array.property", new int[] {3, 2, 1}));
  }

  /** Test de la méthode {@code setDoubleArrayProperty} */
  @Test
  public void testSetDoubleArrayProperty() {
    double[] value = new double[] {3.3, 2.2, 1.1};

    Assert.assertTrue(writer.setDoubleArrayProperty("test.double.array.property", value));
    double[] result = writer.getDoubleArrayProperty("test.double.array.property");
    Assert.assertArrayEquals(value, result, 0.0);
  }

  /** Test de la méthode {@code setDoubleArrayProperty} */
  @Test
  public void testSetDoubleArrayPropertyWithOutSave() {
    double[] expected = writer.getDoubleArrayProperty("test.double.array.property");

    Assert.assertTrue(
        writer.setDoubleArrayProperty(
            "test.double.array.property", new double[] {3.3, 2.2, 1.1}, false));
    writer.loadPropertiesFiles();

    double[] result = writer.getDoubleArrayProperty("test.double.array.property");
    Assert.assertArrayEquals(expected, result, 0.0);
  }

  /** Test de la méthode {@code setDoubleArrayProperty} */
  @Test
  public void testSetDoubleArrayPropertyWithSave() {
    double[] value = new double[] {3.3, 2.2, 1.1};

    Assert.assertTrue(writer.setDoubleArrayProperty("test.double.array.property", value, true));
    writer.loadPropertiesFiles();

    double[] result = writer.getDoubleArrayProperty("test.double.array.property");
    Assert.assertArrayEquals(value, result, 0.0);
  }

  /** Test de la méthode {@code setDoubleArrayProperty} */
  @Test
  public void testSetDoubleArrayPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setDoubleArrayProperty("", new double[] {3.3, 2.2, 1.1}));
  }

  /** Test de la méthode {@code setDoubleArrayProperty} */
  @Test
  public void testSetDoubleArrayPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(
        reader.setDoubleArrayProperty("test.double.array.property", new double[] {3.3, 2.2, 1.1}));
  }

  /** Test de la méthode {@code setColorProperty} */
  @Test
  public void testSetColorProperty() {
    Assert.assertTrue(writer.setColorProperty("test.color.property", Color.RED));
    Color value = writer.getColorProperty("test.color.property");
    Assert.assertEquals(Color.RED, value);
  }

  /** Test de la méthode {@code setColorProperty} */
  @Test
  public void testSetColorPropertyWithOutSave() {
    Color expected = writer.getColorProperty("test.color.property");

    Assert.assertTrue(writer.setColorProperty("test.color.property", Color.RED, false));
    writer.loadPropertiesFiles();

    Color value = writer.getColorProperty("test.color.property");
    Assert.assertEquals(expected, value);
  }

  /** Test de la méthode {@code setColorProperty} */
  @Test
  public void testSetColorPropertyWithSave() {
    Assert.assertTrue(writer.setColorProperty("test.color.property", Color.RED, true));
    writer.loadPropertiesFiles();

    Color value = writer.getColorProperty("test.color.property");
    Assert.assertEquals(Color.RED, value);
  }

  /** Test de la méthode {@code setColorProperty} */
  @Test
  public void testSetColorPropertyWithEmptyKey() {
    Assert.assertFalse(writer.setColorProperty("", Color.RED));
  }

  /** Test de la méthode {@code setColorProperty} */
  @Test
  public void testSetColorPropertyWithNoneEditable() {
    PropertiesManager reader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    Assert.assertFalse(reader.setColorProperty("test.color.property", Color.RED));
  }

  /** Test de la méthode {@code saveUserProperties} */
  @Test
  public void testSaveUserProperties() {
    Assert.assertTrue(writer.setProperty("test.string.property", "New Hello, World!"));
    Assert.assertTrue(writer.saveUserProperties());
    writer.loadPropertiesFiles();

    String value = writer.getStringProperty("test.string.property");
    Assert.assertEquals("New Hello, World!", value);
  }

  /** Test de la méthode {@code saveUserProperties} */
  @Test
  public void testSaveUserPropertiesWithOutSave() {
    String expected1 = "New Hello, World!";
    int expected2 = 42;
    double[] expected3 = {3.3, 2.2, 1.1};

    Assert.assertTrue(writer.setProperty("test.string.property", "New Hello, World!", false));
    Assert.assertTrue(writer.setIntProperty("test.int.property", 42, false));
    Assert.assertTrue(
        writer.setDoubleArrayProperty(
            "test.double.array.property", new double[] {3.3, 2.2, 1.1}, false));
    Assert.assertTrue(writer.saveUserProperties());
    writer.loadPropertiesFiles();

    String value1 = writer.getStringProperty("test.string.property");
    int value2 = writer.getIntProperty("test.int.property");
    double[] value3 = writer.getDoubleArrayProperty("test.double.array.property");
    Assert.assertEquals(expected1, value1);
    Assert.assertEquals(expected2, value2);
    Assert.assertArrayEquals(expected3, value3, 0.0);
  }
}
