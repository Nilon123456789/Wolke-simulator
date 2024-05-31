package com.e24.wolke.filesystem.properties;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.filesystem.WFileSystemConstant;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Classe permettant de charger et modifier les propriétés de l'application
 *
 * @author Nilon123456789
 */
public class PropertiesManager {

  /** Logger de la classe */
  protected static final Logger LOGGER =
      LogManager.getLogger(PropertiesManager.class.getSimpleName());

  /** nom du fichier */
  protected final String fileName;

  /** Chemin complet du fichier utilisateur */
  private final String userFileFullPath;

  /** Chemin complet du fichier par defaut */
  private final String defaultFileFullPath;

  /** Version du fichier attendue */
  private final String fileVersion;

  /** Si le fichier peut être modifié */
  private final boolean editable;

  /** Commentaire du fichier */
  private final String fileComment;

  /** Si le fichier a été chargé */
  private boolean loaded = false;

  /** Propriétés par défaut */
  private Properties defaultProperties;

  /** Propriétés utilisateur */
  private Properties userProperties;

  /** Propriétés utilisateur sans défaut */
  private Properties rawUserProperties;

  /**
   * Constructeur de la classe
   *
   * @param defaultFilePath chemin du fichier par défaut
   * @param fileName nom du fichier
   * @param fileVersion version du fichier attendue
   * @param editable si le fichier peut être modifié
   * @param fileComment commentaire du fichier pour la sauvegarde
   */
  protected PropertiesManager(
      String defaultFilePath,
      String fileName,
      String fileVersion,
      boolean editable,
      String fileComment) {
    this.fileName = fileName;
    this.userFileFullPath = WFileSystemConstant.SAVE_PATH + fileName;
    defaultFileFullPath = defaultFilePath + fileName;
    this.fileVersion = fileVersion.trim();
    this.editable = editable;
    this.fileComment = fileComment;

    loadPropertiesFiles();
  }

  /**
   * Constructeur de la classe
   *
   * @param defaultFilePath chemin du fichier par défaut
   * @param fileName nom du fichier
   * @param fileVersion version du fichier attendue
   * @param editable si le fichier peut être modifié
   */
  protected PropertiesManager(
      String defaultFilePath, String fileName, String fileVersion, boolean editable) {
    this(defaultFilePath, fileName, fileVersion, editable, "");
  }

  /**
   * Constructeur de la classe sans modification possible
   *
   * @param defaultFilePath chemin du fichier par défaut
   * @param fileName nom du fichier
   * @param fileVersion version du fichier attendue
   */
  protected PropertiesManager(String defaultFilePath, String fileName, String fileVersion) {
    this(defaultFilePath, fileName, fileVersion, false);
  }

  /** Charge les fichiers de l'application et de l'utilisateur */
  protected void loadAppAndUserFilesProperties() {
    if (!hasUserFile()) {
      createUserFile();
    }

    File file = new File(defaultFileFullPath);
    try {
      InputStream inputDefault =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultFileFullPath);
      this.defaultProperties = new Properties();
      defaultProperties.load(inputDefault);
      inputDefault.close();

      file = new File(userFileFullPath);
      FileInputStream input = new FileInputStream(new File(userFileFullPath));
      FileInputStream inputRaw = new FileInputStream(new File(userFileFullPath));
      this.userProperties = new Properties(defaultProperties);
      this.rawUserProperties = new Properties();
      this.userProperties.load(input);
      this.rawUserProperties.load(inputRaw);
      input.close();
      inputRaw.close();

    } catch (FileNotFoundException e) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.fileNotFound"),
          file.getPath(),
          e);
    } catch (IOException e) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.loadError"),
          file.getPath(),
          e);
    }

    if (!isFileVersionValid()) {
      LOGGER.warn(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.versionMismatch"),
          this.rawUserProperties.getProperty("version"),
          fileVersion);
      createUserFile();
    }

    this.loaded = true;
  }

  /** Charge les fichiers de l'application */
  protected void loadAppPropertiesFiles() {
    File file = new File(defaultFileFullPath);
    try {

      InputStream inputDefault =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultFileFullPath);
      this.defaultProperties = new Properties();
      defaultProperties.load(inputDefault);
      inputDefault.close();

      inputDefault =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultFileFullPath);

      this.userProperties = new Properties();
      this.userProperties.load(inputDefault);
      inputDefault.close();

      this.rawUserProperties = new Properties(userProperties);
      inputDefault =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultFileFullPath);
      this.rawUserProperties.load(inputDefault);
      inputDefault.close();

    } catch (FileNotFoundException e) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.fileNotFound"),
          file.getPath(),
          e);
    } catch (IOException e) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.loadError"),
          file.getPath(),
          e);
    }

    if (!isFileVersionValid()) {
      LOGGER.warn(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.versionMismatch"),
          this.rawUserProperties.getProperty("version"),
          fileVersion);
    }

    this.loaded = true;
  }

  /** Charge les fichiers de l'utilisateur */
  protected void loadPropertiesFiles() {
    if (editable) loadAppAndUserFilesProperties();
    else loadAppPropertiesFiles();
  }

  /** Réinitialise les propriétés utilisateur */
  public void resetUserProperties() {
    createUserFile();
    loadPropertiesFiles();
  }

  /** Crée un fichier utilisateur en utilisant celui par défaut dans les ressources */
  private void createUserFile() {
    LOGGER.trace(
        LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.creatingUserFile"),
        getClass().getName());
    try {
      InputStream defautlFileInputStream =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(defaultFileFullPath);
      File destinationFile = new File(this.userFileFullPath);

      // Création du dossier
      File dir = new File(WFileSystemConstant.SAVE_PATH);
      dir.mkdirs();

      // Création du fichier
      if (destinationFile.exists()) { // Si le fichier existe déjà, on le sauvegarde
        File backupFile = new File(destinationFile.getPath() + ".bak");
        Files.copy(
            destinationFile.toPath(), backupFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      Files.copy(
          defautlFileInputStream, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

      LOGGER.info(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.userConfigCreated"),
          destinationFile.toPath());
    } catch (IOException e) {
      throw new RuntimeException("Failed to copy file", e);
    }
  }

  /** Supprime le fichier utilisateur */
  public void deleteUserFile() {
    File file = new File(userFileFullPath);
    if (file.exists()) {
      file.delete();
    }
  }

  /**
   * Retourne si le fichier utilisateur est modifiable
   *
   * @return true si le fichier utilisateur est modifiable
   */
  public boolean isEditable() {
    return this.editable;
  }

  /**
   * Sauvegarde les propriétés utilisateur dans le fichier utilisateur
   *
   * @return true si la sauvegarde a réussi
   */
  public boolean saveUserProperties() {
    if (!this.editable) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.notEditable"),
          fileName);
      return false;
    }

    try {
      OutputStream os = Files.newOutputStream(new File(userFileFullPath).toPath());
      this.userProperties.store(os, fileComment);
      os.close();
    } catch (IOException e) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.saveError"),
          userFileFullPath,
          e);
      return false;
    }

    LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.saveSuccess"),
        userFileFullPath);

    return true;
  }

  /**
   * Retourne si l'utilisateur a un fichier
   *
   * @return true si l'utilisateur a le fichier
   */
  private boolean hasUserFile() {
    return new File(userFileFullPath).exists();
  }

  /**
   * Retourne si la version du fichier est valide
   *
   * @return true si la version du fichier est la même que celle de l'application
   */
  protected boolean isFileVersionValid() {
    return fileVersion.equalsIgnoreCase(
        this.rawUserProperties.getProperty("version", "0.0.0").trim());
  }

  /**
   * Retourne la propriété demandée
   *
   * @param key clé de la propriété
   * @return la propriété demandée ou null si elle n'existe pas dans les fichiers
   */
  public Object getProperty(String key) {
    if (!this.loaded) loadPropertiesFiles();

    if (this.userProperties.containsKey(key)) return this.userProperties.get(key);

    if (this.defaultProperties.containsKey(key)) return this.defaultProperties.get(key);

    LOGGER.warn(
        LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.keyNotFound"), key);
    return null;
  }

  /**
   * Modifie la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setProperty(String key, Object value, boolean save) {
    if (!this.editable) {
      LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.notEditable"),
          fileName);
      return false;
    }

    if (key.isEmpty()) {
      LOGGER.error(LocaleManager.getLocaleResourceBundle().getString("propertiesLoader.emptyKey"));
      return false;
    }

    this.userProperties.setProperty(key, value.toString());

    if (save) return saveUserProperties();

    return true;
  }

  /**
   * Modifie la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setProperty(String key, Object value) {
    return setProperty(key, value, false);
  }

  /**
   * Retourne le string de la propriété demandée
   *
   * @param key clé de la propriété
   * @return le string de la propriété demandée ou null si elle n'existe pas dans les fichiers
   */
  public String getStringProperty(String key) {
    Object property = getProperty(key);
    if (property == null) return null;
    return getProperty(key).toString();
  }

  /**
   * Modifie la chaîne de caractères de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setStringProperty(String key, String value, boolean save) {
    return setProperty(key, value, save);
  }

  /**
   * Modifie la chaîne de caractères de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setStringProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setStringProperty(String key, String value) {
    return setStringProperty(key, value, false);
  }

  /**
   * Retourne le boolean de la propriété demandée
   *
   * @param key clé de la propriété
   * @return le boolean de la propriété demandée ou false si elle n'existe pas dans les fichiers
   */
  public boolean getBooleanProperty(String key) {
    return Boolean.parseBoolean(getStringProperty(key));
  }

  /**
   * Modifie le boolean de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setBooleanProperty(String key, boolean value, boolean save) {
    return setProperty(key, value, save);
  }

  /**
   * Modifie le boolean de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setBooleanProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setBooleanProperty(String key, boolean value) {
    return setBooleanProperty(key, value, false);
  }

  /**
   * Retourne le int de la propriété demandée
   *
   * @param key clé de la propriété
   * @return le int de la propriété demandée ou {@code Interger.MIN_VALUE} si elle n'existe pas dans
   *     les fichiers
   */
  public int getIntProperty(String key) {
    try {
      return Integer.parseInt(getStringProperty(key));
    } catch (NumberFormatException e) {
      return Integer.MIN_VALUE;
    }
  }

  /**
   * Modifie le int de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setIntProperty(String key, int value, boolean save) {
    return setProperty(key, value, save);
  }

  /**
   * Modifie le int de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setIntProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setIntProperty(String key, int value) {
    return setIntProperty(key, value, false);
  }

  /**
   * Retourne le double de la propriété demandée
   *
   * @param key clé de la propriété
   * @return le double de la propriété demandée ou {@code Double.NaN} si elle n'existe pas dans les
   *     fichiers
   */
  public double getDoubleProperty(String key) {
    try {
      return Double.parseDouble(getStringProperty(key));
    } catch (NumberFormatException e) {
      return Double.NaN;
    } catch (NullPointerException e) {
      return Double.NaN;
    }
  }

  /**
   * Modifie le double de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setDoubleProperty(String key, double value, boolean save) {
    return setProperty(key, value, save);
  }

  /**
   * Modifie le double de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setDoubleProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setDoubleProperty(String key, double value) {
    return setDoubleProperty(key, value, false);
  }

  /**
   * Retourne le tableau de string de la propriété demandée
   *
   * @param key clé de la propriété
   * @return le tableau de string de la propriété demandée ou null si elle n'existe pas dans les
   *     fichiers
   */
  public String[] getArrayProperty(String key) {
    String property = getStringProperty(key);
    if (property == null) return null;
    return property.toString().split(",");
  }

  /**
   * Modifie le tableau de string de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setArrayProperty(String key, String[] value, boolean save) {
    return setProperty(key, String.join(",", value), save);
  }

  /**
   * Modifie le tableau de string de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setArrayProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setArrayProperty(String key, String[] value) {
    return setArrayProperty(key, value, false);
  }

  /**
   * Retourne le tableau d'entiers de la propriété demandée
   *
   * @param key clé de la propriété
   * @return le tableau d'entiers de la propriété demandée ou null si elle n'existe pas dans les
   *     fichiers
   */
  public int[] getIntArrayProperty(String key) {
    String[] array = (String[]) getArrayProperty(key);

    if (array == null) return null;

    int[] intArray = new int[array.length];
    try {
      for (int i = 0; i < array.length; i++) {
        intArray[i] = Integer.parseInt(array[i].trim());
      }
    } catch (NumberFormatException e) {
      return null;
    }
    return intArray;
  }

  /**
   * Modifie le tableau d'entiers de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setIntArrayProperty(String key, int[] value, boolean save) {
    String[] stringArray = new String[value.length];
    for (int i = 0; i < value.length; i++) {
      stringArray[i] = Integer.toString(value[i]);
    }
    return setArrayProperty(key, stringArray, save);
  }

  /**
   * Modifie le tableau d'entiers de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setIntArrayProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setIntArrayProperty(String key, int[] value) {
    return setIntArrayProperty(key, value, false);
  }

  /**
   * Retourne le tableau de doubles de la propriété demandée
   *
   * @param key clé de la propriété
   * @return le tableau de doubles de la propriété demandée ou null si elle n'existe pas dans les
   */
  public double[] getDoubleArrayProperty(String key) {
    String[] array = (String[]) getArrayProperty(key);

    if (array == null) return null;

    double[] doubleArray = new double[array.length];
    try {
      for (int i = 0; i < array.length; i++) {
        doubleArray[i] = Double.parseDouble(array[i].trim());
      }
    } catch (NumberFormatException e) {
      return null;
    } catch (NullPointerException e) {
      return null;
    }
    return doubleArray;
  }

  /**
   * Modifie le tableau de doubles de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setDoubleArrayProperty(String key, double[] value, boolean save) {
    String[] stringArray = new String[value.length];
    for (int i = 0; i < value.length; i++) {
      stringArray[i] = Double.toString(value[i]);
    }
    return setArrayProperty(key, stringArray, save);
  }

  /**
   * Modifie le tableau de doubles de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setDoubleArrayProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setDoubleArrayProperty(String key, double[] value) {
    return setDoubleArrayProperty(key, value, false);
  }

  /**
   * Retourne la couleur de la propriété demandée
   *
   * @param key clé de la propriété
   * @return la couleur de la propriété demandée ou null si elle n'existe pas dans les fichiers
   */
  public Color getColorProperty(String key) {
    int[] colorArray = getIntArrayProperty(key);
    if (colorArray == null) return null;

    return new Color(colorArray[0], colorArray[1], colorArray[2]);
  }

  /**
   * Modifie la couleur de la propriété demandée
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @param save si le fichier doit être enregistré après la modification
   * @return true si la modification a réussi
   */
  public boolean setColorProperty(String key, Color value, boolean save) {
    int[] colorArray = {value.getRed(), value.getGreen(), value.getBlue()};
    return setIntArrayProperty(key, colorArray, save);
  }

  /**
   * Modifie la couleur de la propriété demandée
   *
   * <p>Le fichier n'est pas enregistré automatiquement, il faut appeler {@code
   * saveUserProperties()} ou {@code setColorProperty(key, value, true)}
   *
   * @param key clé de la propriété
   * @param value valeur de la propriété
   * @return true si la modification a réussi
   */
  public boolean setColorProperty(String key, Color value) {
    return setColorProperty(key, value, false);
  }
}
