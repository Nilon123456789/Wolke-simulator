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
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.ExternalResource;

/**
 * Classe de test pour la classe {@code PropertiesManagerReadTest}
 *
 * @author Nilon123456789
 */
public class PropertiesManagerReadTest {

  /** version actuel du fichier */
  private static final String FILE_VERSION = "valid";

  /** Nom du fichier de configuration */
  private static final String FILE_NAME = "test-defaults.properties";

  /** Chemin du fichier de configuration par défaut */
  private static final String DEFAULT_FILE_PATH = "";

  /** Instance de la classe à tester */
  private static PropertiesManager loader;

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
          if (this.sourceFile.exists()) {
            this.exists = this.sourceFile.exists();
            try {
              Files.copy(
                  this.sourceFile.toPath(),
                  this.testBackupFile.toPath(),
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
            if (this.testBackupFile.exists() && this.exists) {

              Files.copy(
                  this.testBackupFile.toPath(),
                  this.sourceFile.toPath(),
                  StandardCopyOption.REPLACE_EXISTING);
              Files.delete(this.testBackupFile.toPath());

            } else if (this.sourceFile.exists()) {
              Files.delete(this.sourceFile.toPath());
              if (this.testBackupFile.exists()) Files.delete(this.testBackupFile.toPath());
            }

            if (this.backupFile.exists())
              Files.delete(
                  this.backupFile
                      .toPath()); // On supprime le fichier de backup puisqu'il continent les
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
    loader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, FILE_VERSION);
    loader.resetUserProperties();
  }

  /** Test de la méthode {@code getStringProperty} */
  @Test
  public void testGetStringProperty() {
    String value = loader.getStringProperty("test.string.property");
    Assert.assertEquals("Hello, World!", value);
  }

  /** Test de la méthode {@code getStringProperty} */
  @Test
  public void testGetStringPropertyWithInvalidKey() {
    String value = loader.getStringProperty("invalid.key");
    Assert.assertNull(value);
  }

  /** Test de la méthode {@code getStringProperty} */
  @Test
  public void testGetStringPropertyWithNoUserFile() {
    loader.deleteUserFile();
    String value = loader.getStringProperty("test.string.property");
    Assert.assertEquals("Hello, World!", value);

    loader.resetUserProperties();
  }

  /** Test de la méthode {@code isFileVersionValid} */
  @Test
  public void testIsFileVersionValid() {
    boolean value = loader.isFileVersionValid();
    Assert.assertTrue(value);
  }

  /** Test de la méthode {@code isFileVersionValid} */
  @Test
  public void testIsFileVersionValidWithInvalidVersion() {
    PropertiesManager tempLoader = new PropertiesManager(DEFAULT_FILE_PATH, FILE_NAME, "notVALID");
    boolean value = tempLoader.isFileVersionValid();
    Assert.assertFalse(value);
  }

  /** Test de la méthode {@code getBooleanProperty} */
  @Test
  public void testGetBooleanProperty() {
    boolean value = loader.getBooleanProperty("test.boolean.property");
    Assert.assertTrue(value);
  }

  /** Test de la méthode {@code getBooleanProperty} */
  @Test
  public void testGetBooleanPropertyWithInvalidKey() {
    boolean value = loader.getBooleanProperty("invalid.key");
    Assert.assertFalse(value);
  }

  /** Test de la méthode {@code getIntProperty} */
  @Test
  public void testGetIntProperty() {
    int value = loader.getIntProperty("test.int.property");
    Assert.assertEquals(42, value);
  }

  /** Test de la méthode {@code getIntProperty} */
  @Test
  public void testGetIntPropertyWithInvalidKey() {
    int value = loader.getIntProperty("invalid.key");
    Assert.assertEquals(Integer.MIN_VALUE, value);
  }

  /** Test de la méthode {@code getDoubleProperty} */
  @Test
  public void testGetDoubleProperty() {
    double value = loader.getDoubleProperty("test.double.property");
    Assert.assertEquals(3.14, value, 0.001);
  }

  /** Test de la méthode {@code getDoubleProperty} */
  @Test
  public void testGetDoublePropertyWithInvalidKey() {
    double value = loader.getDoubleProperty("invalid.key");
    Assert.assertEquals(Double.NaN, value, 0.001);
  }

  /** Test de la méthode {@code getArrayProperty} */
  @Test
  public void testGetArrayProperty() {
    String[] value = loader.getArrayProperty("test.array.property");
    Assert.assertArrayEquals(new String[] {"1", "2", "3", "4", "5"}, value);
  }

  /** Test de la méthode {@code getArrayProperty} */
  @Test
  public void testGetArrayPropertyWithInvalidKey() {
    String[] value = loader.getArrayProperty("invalid.key");
    Assert.assertNull(value);
  }

  /** Test de la méthode {@code getIntArrayProperty} */
  @Test
  public void testGetIntArrayProperty() {
    int[] value = loader.getIntArrayProperty("test.int.array.property");
    Assert.assertArrayEquals(new int[] {1, 2, 3, 4, 5}, value);
  }

  /** Test de la méthode {@code getIntArrayProperty} */
  @Test
  public void testGetIntArrayPropertyWithSpace() {
    int[] value = loader.getIntArrayProperty("test.int.array.property.with.spaces");
    Assert.assertArrayEquals(new int[] {1, 2, 3, 4, 5}, value);
  }

  /** Test de la méthode {@code getIntArrayProperty} */
  @Test
  public void testGetIntArrayPropertyWithInvalidKey() {
    int[] value = loader.getIntArrayProperty("invalid.key");
    Assert.assertNull(value);
  }

  /** Test de la méthode {@code getDoubleArrayProperty} */
  @Test
  public void testGetDoubleArrayProperty() {
    double[] value = loader.getDoubleArrayProperty("test.double.array.property");
    Assert.assertArrayEquals(new double[] {1.1, 2.2, 3.3, 4.4, 5.5}, value, 0.001);
  }

  /** Test de la méthode {@code getDoubleArrayProperty} */
  @Test
  public void testGetDoubleArrayPropertyWithInvalidKey() {
    double[] value = loader.getDoubleArrayProperty("invalid.key");
    Assert.assertNull(value);
  }

  /** Test de la méthode {@code getColorProperty} */
  @Test
  public void testGetColorProperty() {
    Color value = loader.getColorProperty("test.color.property");
    Assert.assertEquals(new Color(255, 125, 50), value);
  }

  /** Test de la méthode {@code getColorProperty} */
  @Test
  public void testGetColorPropertyWithInvalidKey() {
    Color value = loader.getColorProperty("invalid.key");
    Assert.assertNull(value);
  }
}
