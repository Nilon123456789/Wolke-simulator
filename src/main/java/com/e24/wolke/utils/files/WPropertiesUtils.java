package com.e24.wolke.utils.files;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WPropertiesUtils} permet de definir les methodes utilitaires liees aux
 * proprietes
 *
 * @author MeriBouisri
 */
public final class WPropertiesUtils {

  /** Le {@code Logger} de cette classe */
  private static final Logger logger = LogManager.getLogger(WPropertiesUtils.class.getSimpleName());

  /**
   * Methode utilitaire permettant d'ecrire les donnees du {@code Properties} passe en parametre a
   * un fichier dans un dossier specifie
   *
   * @param parentFolder Le {@code File} qui represente le dossier dans lequel le {@code Properties}
   *     sera ecrit
   * @param filename Le nom du fichier a creer
   * @param properties Le {@code Properties} a ecrire
   * @return L'objet {@code File} qui contient les donnees de {@code Properties}
   */
  public static File exportProperties(File parentFolder, String filename, Properties properties) {
    if (!parentFolder.isDirectory()) {
      WPropertiesUtils.logger.error("Folder expected : " + parentFolder.getPath());
      return null;
    }

    try {
      if (!parentFolder.exists())
        WPropertiesUtils.logger.debug("Creating new folder : " + parentFolder.getPath());
      parentFolder.createNewFile();
    } catch (IOException e) {
      WPropertiesUtils.logger.debug(e.getMessage());
      return null;
    }

    if (!WFileUtils.getExtension(filename).equals("properties")) filename += ".properties";

    File propertiesFile = new File(parentFolder, filename);

    try {
      FileOutputStream out = new FileOutputStream(propertiesFile);
      properties.store(out, "Properties");
      out.close();
    } catch (IOException e) {
      WPropertiesUtils.logger.error(e.getMessage());
      return null;
    }

    WPropertiesUtils.logger.debug("File created successfully : " + propertiesFile.getPath());
    return propertiesFile;
  }

  /**
   * Methode permettant d'importer les proprietes a partir d'un fichier
   *
   * @param propertiesFile Le fichier qui contient des donnees de {@code Properties}
   * @return Le {@code Properties} qui contient les donnees du fichier
   */
  public static Properties importProperties(File propertiesFile) {
    Properties properties = new Properties();

    FileInputStream in;
    try {
      in = new FileInputStream(propertiesFile);
      properties.load(in);
      in.close();
    } catch (IOException e) {
      WPropertiesUtils.logger.error(e.getMessage());
      return null;
    }

    return properties;
  }

  /**
   * Retourne la propriete demandee
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static Object getProperty(String key, Properties properties) {
    if (properties.containsKey(key)) return properties.get(key);

    return null;
  }

  /**
   * Retourne la propriete demandee, ou la valeur par defaut en case d'erreur
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue La valeur par defaut a retourner en cas d'erreur
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static Object getProperty(String key, Properties properties, Object defaultValue) {
    return properties.getOrDefault(key, defaultValue);
  }

  /**
   * Modifie la propriete demandee
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setProperty(String key, Object value, Properties properties) {
    if (key.isEmpty()) return false;

    properties.setProperty(key, value.toString());

    return true;
  }

  /**
   * Retourne la propriete demandee, de type {@code String}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static String getStringProperty(String key, Properties properties) {
    Object property = WPropertiesUtils.getProperty(key, properties);

    if (property == null) return null;

    return property.toString();
  }

  /**
   * Retourne la propriete demandee, de type {@code String}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static String getStringProperty(String key, Properties properties, String defaultValue) {
    String property = WPropertiesUtils.getStringProperty(key, properties);

    return property == null ? defaultValue : property;
  }

  /**
   * Modifie la propriete demandee, de type {@code String}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setStringProperty(String key, String value, Properties properties) {
    return WPropertiesUtils.setProperty(key, value, properties);
  }

  /**
   * Retourne la propriete demandee, de type {@code boolean}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou false si elle n'existe pas dans le {@code Properties} passe
   *     en parametre
   */
  public static boolean getBooleanProperty(String key, Properties properties) {
    return Boolean.parseBoolean(WPropertiesUtils.getStringProperty(key, properties));
  }

  /**
   * Retourne la propriete demandee, de type {@code boolean}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou defaultValue si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static boolean getBooleanProperty(
      String key, Properties properties, boolean defaultValue) {
    String strProperty = WPropertiesUtils.getStringProperty(key, properties);

    if (strProperty == null) return defaultValue;

    return WPropertiesUtils.getBooleanProperty(key, properties);
  }

  /**
   * Modifie la propriete demandee, de type {@code boolean}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setBooleanProperty(String key, boolean value, Properties properties) {
    return WPropertiesUtils.setProperty(key, value, properties);
  }

  /**
   * Retourne la propriete demandee, de type {@code int}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou MIN_VALUE si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static int getIntProperty(String key, Properties properties) {
    return WPropertiesUtils.getIntProperty(key, properties, Integer.MIN_VALUE);
  }

  /**
   * Retourne la propriete demandee, de type {@code int}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou defaultValue si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static int getIntProperty(String key, Properties properties, int defaultValue) {
    try {
      return Integer.parseInt(WPropertiesUtils.getStringProperty(key, properties));
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * Modifie la propriete demandee, de type {@code int}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setIntProperty(String key, int value, Properties properties) {
    return WPropertiesUtils.setProperty(key, value, properties);
  }

  /**
   * Retourne la propriete demandee, de type {@code double}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou NaN si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static double getDoubleProperty(String key, Properties properties) {
    return WPropertiesUtils.getDoubleProperty(key, properties, Double.NaN);
  }

  /**
   * Retourne la propriete demandee, de type {@code double}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou defaultValue si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static double getDoubleProperty(String key, Properties properties, double defaultValue) {
    try {
      return Double.parseDouble(WPropertiesUtils.getStringProperty(key, properties));
    } catch (NumberFormatException | NullPointerException e) {
      return defaultValue;
    }
  }

  /**
   * Modifie la propriete demandee, de type {@code double}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setDoubleProperty(String key, double value, Properties properties) {
    return WPropertiesUtils.setProperty(key, value, properties);
  }

  /**
   * Retourne la propriete demandee, de type {@code String[]}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static String[] getArrayProperty(String key, Properties properties) {
    String property = WPropertiesUtils.getStringProperty(key, properties);

    if (property == null) return null;

    return property.toString().split(",");
  }

  /**
   * Retourne la propriete demandee, de type {@code String[]}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou defaultValue si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static String[] getArrayProperty(
      String key, Properties properties, String[] defaultValue) {
    String[] property = WPropertiesUtils.getArrayProperty(key, properties);

    return property == null ? defaultValue : property;
  }

  /**
   * Modifie la propriete demandee, de type {@code String[]}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setArrayProperty(String key, String[] value, Properties properties) {
    return WPropertiesUtils.setProperty(key, String.join(",", value), properties);
  }

  /**
   * Retourne la propriete demandee, de type {@code int[]}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static int[] getIntArrayProperty(String key, Properties properties) {
    String[] strArray = (String[]) WPropertiesUtils.getArrayProperty(key, properties);

    if (strArray == null) return null;

    int[] intArray = new int[strArray.length];

    try {
      for (int i = 0; i < intArray.length; i++) intArray[i] = Integer.parseInt(strArray[i].trim());
    } catch (NumberFormatException e) {
      return null;
    }

    return intArray;
  }

  /**
   * Retourne la propriete demandee, de type {@code String[]}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou defaultValue si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static int[] getIntArrayProperty(String key, Properties properties, int[] defaultValue) {
    int[] property = WPropertiesUtils.getIntArrayProperty(key, properties);

    return property == null ? defaultValue : property;
  }

  /**
   * Modifie la propriete demandee, de type {@code int[]}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setIntArrayProperty(String key, int[] value, Properties properties) {
    String[] strArray = new String[value.length];

    for (int i = 0; i < value.length; i++) strArray[i] = Integer.toString(value[i]);

    return WPropertiesUtils.setArrayProperty(key, strArray, properties);
  }

  /**
   * Retourne la propriete demandee, de type {@code double[]}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static double[] getDoubleArrayProperty(String key, Properties properties) {
    String[] strArray = (String[]) WPropertiesUtils.getArrayProperty(key, properties);

    if (strArray == null) return null;

    double[] doubleArray = new double[strArray.length];

    try {
      for (int i = 0; i < doubleArray.length; i++)
        doubleArray[i] = Integer.parseInt(strArray[i].trim());
    } catch (NumberFormatException | NullPointerException e) {
      return null;
    }

    return doubleArray;
  }

  /**
   * Retourne la propriete demandee, de type {@code double[]}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou defaultValue si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static double[] getDoubleArrayProperty(
      String key, Properties properties, double[] defaultValue) {
    double[] property = WPropertiesUtils.getDoubleArrayProperty(key, properties);

    return property == null ? defaultValue : property;
  }

  /**
   * Modifie la propriete demandee, de type {@code int[]}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setDoubleArrayProperty(String key, double[] value, Properties properties) {
    String[] strArray = new String[value.length];

    for (int i = 0; i < value.length; i++) strArray[i] = Double.toString(value[i]);

    return WPropertiesUtils.setArrayProperty(key, strArray, properties);
  }

  /**
   * Retourne la propriete demandee, de type {@code Color}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @return La propriete demandee, ou null si elle n'existe pas dans le {@code Properties} passe en
   *     parametre
   */
  public static Color getColorProperty(String key, Properties properties) {
    int[] colorArray = WPropertiesUtils.getIntArrayProperty(key, properties);

    if (colorArray == null) return null;

    return new Color(colorArray[0], colorArray[1], colorArray[2]);
  }

  /**
   * Retourne la propriete demandee, de type {@code Color}
   *
   * @param key cle de la propriete
   * @param properties Le {@code Properties} duquel recuperer une propriete
   * @param defaultValue valeur a retourner en cas d'erreur
   * @return La propriete demandee, ou defaultValue si elle n'existe pas dans le {@code Properties}
   *     passe en parametre
   */
  public static Color getColorProperty(String key, Properties properties, Color defaultValue) {
    Color property = WPropertiesUtils.getColorProperty(key, properties);

    return property == null ? defaultValue : property;
  }

  /**
   * Modifie la propriete demandee, de type {@code Color}
   *
   * @param key cle de la propriete
   * @param value La valeur de la propriete
   * @param properties Le {@code Properties} dans lequel modifier une propriete
   * @return {@code true} si la modification a ete effectuee avec succes
   */
  public static boolean setColorProperty(String key, Color value, Properties properties) {
    int[] colorArray = {value.getRed(), value.getGreen(), value.getBlue()};

    return WPropertiesUtils.setIntArrayProperty(key, colorArray, properties);
  }
}
