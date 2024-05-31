package com.e24.wolke.utils.images;

import com.github.weisj.jsvg.SVGDocument;
import com.github.weisj.jsvg.attributes.ViewBox;
import com.github.weisj.jsvg.geometry.size.FloatSize;
import com.github.weisj.jsvg.parser.SVGLoader;
import com.jogamp.nativewindow.util.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.net.URL;

/**
 * La classe {@code WImageUtils} regroupe les methode utilitaires pour la manipulation d'images
 *
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public class WImageUtils {

  /** Classe non instanciable */
  private WImageUtils() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode permettant de convertir un tableau de donnees en {@code BufferedImage}. Dans le
   * tableau, un element represente un pixel de valeurs 1 ou 0.
   *
   * @param data Les donnees a dessiner sur l'image
   * @param image Le {@code BufferedImage} sur lequel dessiner.
   * @return Le {@code BufferedImage} passe en parametre, apres l'operation
   */
  public static BufferedImage toBufferedImage(int[] data, BufferedImage image) {
    // TODO : Implementation
    return image;
  }

  /**
   * Methode permettant de convertir un tableau de donnees en {@code BufferedImage}. Dans le
   * tableau, un element represente un pixel de valeurs 1 ou 0.
   *
   * @param data Les donnees a dessiner sur l'image
   * @param width La longueur horizontale de l'image a retourner
   * @param height La longueur verticale de l'image a retourner
   * @return Un nouveau {@code BufferedImage} avec les donnees dessinees
   */
  public static BufferedImage toBufferedImage(int[] data, int width, int height) {
    // TODO : Implementation
    return new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
  }

  /**
   * Methode permettant de copier les donnees d'un {@code BufferedImage} et retourner un nouveau
   * {@code BufferedImage} identique dont les changements ne vont pas affecter le {@code
   * BufferedImage} original.
   *
   * @param image L'image a copier
   * @return Le nouveau {@code BufferedImage} dont les donnees sont copiees du {@code BufferedImage}
   */
  public static BufferedImage copy(BufferedImage image) {
    BufferedImage imageCopy =
        new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    imageCopy.setData(image.getData());

    return imageCopy;
  }

  /**
   * Methode permettant de creer un nouveau {@code BufferedImage} avec les memes dimensions et type
   * que le {@code BufferedImage} passe en parametre
   *
   * @param image L'image a partir de laquelle le nouveau {@code BufferedImage} vide doit etre cree
   * @return Un nouveau {@code BufferedImage} compatible avec celui passe en parametre
   */
  public static BufferedImage createCompatibleEmptyBufferedImage(BufferedImage image) {
    return new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
  }

  /**
   * Methode permettant de creer un {@code Raster} representant une image vide
   *
   * @param width La longueur horizontale de l'image
   * @param height La longueur verticale de l'image
   * @param imageType Le type de l'image
   * @return Le {@code Raster} vide d'une image compatible avec les valeurs passees en parametre
   */
  public static Raster createCompatibleEmptyRaster(int width, int height, int imageType) {
    BufferedImage image = new BufferedImage(width, height, imageType);
    return image.getData();
  }

  /**
   * Methode permettant de creer un {@code Raster} representant une image vide
   *
   * @param image L'image avec laquelle le {@code Raster} doit etre compatible
   * @return Le {@code Raster} vide d'une image compatible avec les valeurs passees en parametre
   */
  public static Raster createCompatibleEmptyRaster(BufferedImage image) {
    BufferedImage compatibleImage =
        new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    return compatibleImage.getData();
  }

  /**
   * Méthode permettant de charger une image SVG.
   *
   * @param imagePath Le chemin du fichier SVG
   * @param width La longueur horizontale de l'image
   * @param height La longueur verticale de l'image
   * @return Le {@code BufferedImage}
   */
  public static BufferedImage loadSVGImage(URL imagePath, int width, int height) {
    SVGLoader loader = new SVGLoader();
    SVGDocument svgImageDocument = loader.load(imagePath);
    FloatSize size = svgImageDocument.size();
    Dimension canvasSize = new Dimension(width, height);

    BufferedImage image;
    // On redimensionne l'image pour qu'elle rentre dans le canvas
    if (size.width > size.height) {
      image =
          new BufferedImage(
              (int) canvasSize.getWidth(),
              (int) (canvasSize.getWidth() * size.height / size.width),
              BufferedImage.TYPE_INT_ARGB);
    } else {
      image =
          new BufferedImage(
              (int) (canvasSize.getHeight() * size.width / size.height),
              (int) canvasSize.getHeight(),
              BufferedImage.TYPE_INT_ARGB);
    }
    Graphics2D g2d = image.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    svgImageDocument.render(null, g2d, new ViewBox(0, 0, image.getWidth(), image.getHeight()));
    g2d.dispose();

    return image;
  }
}
