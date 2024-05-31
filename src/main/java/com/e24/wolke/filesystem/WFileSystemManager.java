package com.e24.wolke.filesystem;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code WFileSystemManager} permet de gerer la sauvegarde et le chargement de fichiers.
 *
 * @author MeriBouisri
 */
public class WFileSystemManager {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(WFileSystemManager.class.getClass().getSimpleName());

  /** L'emplacement par defaut des fichiers */
  public static final String DEFAULT_PATH = WFileSystemConstant.SAVE_PATH;

  /** Constructeur privé pour empêcher l'instanciation de la classe */
  private WFileSystemManager() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Sauvegarder un objet dans un fichier
   *
   * @param filename Le nom du fichier
   * @param obj L'objet dont les donnees seront sauvegardees
   * @return {@code true} si l'objet a ete sauvegarde avec succes
   */
  public static boolean saveObject(String filename, Object obj) {
    try {
      FileOutputStream file = new FileOutputStream(WFileSystemManager.DEFAULT_PATH + filename);
      ObjectOutputStream out = new ObjectOutputStream(file);

      out.writeObject(obj);
      out.close();
      file.close();
    } catch (IOException e) {
      WFileSystemManager.LOGGER.error("Failed to save file");
      return false;
    }

    return true;
  }

  /**
   * Sauvegarder un objet dans un fichier
   *
   * @param filename Le nom du fichier
   * @param obj L'objet dont les donnees seront sauvegardees
   * @return {@code true} si l'objet a ete sauvegarde avec succes
   */
  public static boolean loadObject(String filename, Object obj) {
    try {
      FileInputStream file = new FileInputStream(WFileSystemManager.DEFAULT_PATH + filename);
      ObjectInputStream in = new ObjectInputStream(file);

      obj = in.readObject();
      in.close();
      file.close();
    } catch (IOException err) {
      WFileSystemManager.LOGGER.error("Failed to load file");
      err.printStackTrace();
      return false;
    } catch (ClassNotFoundException err) {
      WFileSystemManager.LOGGER.error("Class not found");
      return false;
    }

    return true;
  }
}
