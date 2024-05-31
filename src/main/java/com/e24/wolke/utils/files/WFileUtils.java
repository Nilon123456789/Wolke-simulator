package com.e24.wolke.utils.files;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;

/**
 * La classe {@code WFileUtils} permet de definir les fonctions utilitaires pour la gestion de
 * fichiers
 *
 * @author MeriBouisri
 */
public class WFileUtils {

  /** Extension d'un fichier de format {@code Wolke} */
  public static final String WOLKE_EXTENSION = "wlks";

  /** Classe non instanciable */
  private WFileUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode permettant d'ecrire une image a un dossier
   *
   * @param parentFolder Le dossier vers lequel ecrire l'image
   * @param basename Le nom de base du fichier
   * @param format Le format du fichier
   * @param image Image a export
   * @return Le {@code File} qui contient l'image
   * @throws IOException IOException
   */
  public static File exportImageFile(
      File parentFolder, String basename, String format, BufferedImage image) throws IOException {
    if (!parentFolder.isDirectory()) return null;

    File file = new File(parentFolder, basename);

    if (!file.exists()) file.createNewFile();

    ImageIO.write(image, format, file);

    return file;
  }

  /**
   * Methode permettant de determiner si un fichier est un fichier {@code Wolke}
   *
   * @param file Le {@code File} a verifier
   * @return {@code true} si le fichier possede l'extension d'un fichier {@code Wolke}
   */
  public static boolean isWolkeFile(File file) {
    if (file.isDirectory()) return false;

    return WFileUtils.getExtension(file.getName()).equals(WFileUtils.WOLKE_EXTENSION);
  }

  /**
   * Methode permettant de modifier l'extension d'un fichier
   *
   * @param file Le {@code File} dont le nom est a modifier
   * @param ext La nouvelle extension du fichier
   * @return Le {@code Path} qui represente le fichier modifie
   */
  public static Path modifyExtension(File file, String ext) {
    String oldName = WFileUtils.getBasename(file.getName());

    if (ext.charAt(0) == '.') ext = ext.substring(1);

    String newName = oldName + "." + ext;

    Path source = Paths.get(file.getPath());
    try {
      return Files.move(source, source.resolveSibling(newName));
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Methode permettant d'extraire l'extension du nom du fichier
   *
   * @param fileName Le nom du fichier
   * @return Le {@code String} contenant l'extension du fichier. Ou un {@code String} vide si le nom
   *     du fichier ne possede pas d'extension
   */
  public static String getExtension(String fileName) {
    int pos = fileName.lastIndexOf(".");

    if (pos == -1) return "";

    // On prend le substring apres le point
    pos++;

    if (pos < 0 || pos >= fileName.length()) return "";

    return fileName.substring(pos, fileName.length());
  }

  /**
   * Methode permettant d'extraire le nom de base (sans extension) du nom d'un fichier
   *
   * @param fileName Le nom du fichier
   * @return Le nom du fichier sans l'extension. Retourne le nom du fichier tel quel s'il n'y a pas
   *     d'extension
   */
  public static String getBasename(String fileName) {
    int pos = fileName.lastIndexOf(".");

    if (pos < 0 || pos >= fileName.length()) return fileName;

    return fileName.substring(0, pos);
  }

  /**
   * Methode pour extraire fichier d'un zip.
   *
   * @param zipFilePath le path du fichier zip
   * @param destDirectory dossier ou extraire les fichiers
   * @throws IOException IOException
   *     https://www.codejava.net/java-se/file-io/programmatically-extract-a-zip-file-using-java
   */
  public static void unzipToDirectory(String zipFilePath, String destDirectory) throws IOException {
    File destDir = new File(destDirectory);
    if (!destDir.exists()) {
      destDir.mkdir();
    }
    ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
    ZipEntry entry = zipIn.getNextEntry();
    // iterates over entries in the zip file
    while (entry != null) {
      String filePath = destDirectory + File.separator + entry.getName();
      if (!entry.isDirectory()) {
        // if the entry is a file, extracts it
        extractFile(zipIn, filePath);
      } else {
        // if the entry is a directory, make the directory
        File dir = new File(filePath);
        dir.mkdirs();
      }
      zipIn.closeEntry();
      entry = zipIn.getNextEntry();
    }
    zipIn.close();
  }

  /**
   * Extraire fichier d'un zip
   *
   * @param zipIn ZipInputStream
   * @param filePath path du fichier
   * @throws IOException IOException
   */
  public static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
    byte[] bytesIn = new byte[4096];
    int read = 0;
    while ((read = zipIn.read(bytesIn)) != -1) {
      bos.write(bytesIn, 0, read);
    }
    bos.close();
  }
}
