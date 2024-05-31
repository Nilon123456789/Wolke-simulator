package com.e24.wolke.backend.models.editor.layers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * La classe {@code WLayerGenerator} permet de creer des
 *
 * @author MeriBouisri
 */
public class WLayerGenerator {

  /** La longueur horizontale des images de {@code WLayer} */
  private int width;

  /** La longueur horizontale des images de {@code WLayer} */
  private int height;

  /** Le type de {@code BufferedImage} */
  private int type;

  /**
   * Construction d'un {@code WLayerFactory} avec les parametres des images de calques
   *
   * @param width La longueur horizontale de l'image des calques
   * @param height La longueur verticale de l'image des calques
   * @param type Le type de {@code BufferedImage} de l'image des calques
   */
  public WLayerGenerator(int width, int height, int type) {
    this.width = width;
    this.height = height;
    this.type = type;
  }

  /**
   * Methode permettant de creer un {@code BufferedImage} compatible avec les parametres de cette
   * instance
   *
   * @return Un {@code BufferedImage} compatible avec les parametres de cette instance
   */
  public BufferedImage createBufferedImage() {
    return new BufferedImage(this.width, this.height, this.type);
  }

  /**
   * Methode permettant de creer un {@code WLayer} avec une image compatible avec les parametres de
   * cette instance
   *
   * @return Un {@code WLayer} compatible avec les parametres de cette instance
   */
  public WLayer createLayer() {
    return new WLayer(this.createBufferedImage());
  }

  /**
   * Methode permettant de creer un {@code WLayer} avec une image compatible avec les parametres de
   * cette instance, et avec un identifiant
   *
   * @param id L'identifiant du calque cree
   * @return Un {@code WLayer} compatible avec les parametres de cette instance
   */
  public WLayer createLayer(int id) {
    return new WLayer(this.createBufferedImage(), id);
  }

  /**
   * Methode permettant de determiner si deux {@code WLayer} sont compatibles, selon leurs
   * dimensions
   *
   * @param layer1 Le {@code WLayer} a comparer
   * @param layer2 Le {@code WLayer} a comparer
   * @return {@code true} si les {@code WLayer} ont les memes dimensions d'images
   */
  public static boolean isCompatibleLayer(WLayer layer1, WLayer layer2) {
    return WLayerGenerator.isCompatibleBufferedImage(layer1.getImage(), layer2.getImage());
  }

  /**
   * Methode permettant de determiner si un {@code BufferedImage} est compatible avec les parametres
   * de cette instance
   *
   * @param image Le {@code BufferedImage} a verifier
   * @return {@code true} si le {@code BufferedImage} est compatible avec les parametres de cette
   *     instance
   */
  public boolean isCompatibleBufferedImage(BufferedImage image) {
    return image.getWidth() == this.width && image.getHeight() == this.height;
  }

  /**
   * Methode permettant de modifier un {@code BufferedImage} pour le rendre compatible avec les
   * parametres de cette instance
   *
   * @param image L'image a rendre compatible.
   * @return Un {@code BufferedImage} compatible avec cette instance. Si l'image est deja
   *     compatible, l'instance passee en parametre est retournee
   */
  public BufferedImage fixUncompatibleBufferedImage(BufferedImage image) {
    if (image == null) return null;
    if (this.isCompatibleBufferedImage(image)) return image;

    Image temp = image.getScaledInstance(this.width, this.height, Image.SCALE_SMOOTH);

    BufferedImage compatibleImage = this.createBufferedImage();

    Graphics2D g2d = compatibleImage.createGraphics();
    g2d.drawImage(temp, 0, 0, null);
    g2d.dispose();

    return compatibleImage;
  }

  /**
   * Methode permettant de determiner si deux {@code BufferedImage} sont compatibles, selon leurs
   * dimensions
   *
   * @param image1 Le {@code BufferedImage} a comparer
   * @param image2 Le {@code BufferedImage} a comparer
   * @return {@code true} si les {@code BufferedImage} ont les memes dimensions
   */
  public static boolean isCompatibleBufferedImage(BufferedImage image1, BufferedImage image2) {
    return image1.getWidth() == image2.getWidth() && image1.getHeight() == image2.getHeight();
  }

  /**
   * Methode permettant de determiner si un {@code WLayer} est compatible avec les parametres de
   * cette instance
   *
   * @param layer Le {@code WLayer} a verifier
   * @return {@code true} si le {@code WLayer} est compatible avec les parametres de cette instance
   */
  public boolean isCompatibleLayer(WLayer layer) {
    return this.isCompatibleBufferedImage(layer.getImage());
  }

  /**
   * Getter pour {@code this#width}, la longueur horizontale des images des calques
   *
   * @return La longueur horizontale des images des calques
   */
  public int getWidth() {
    return width;
  }

  /**
   * Setter pour {@code this#width}, la longueur horizontale des images des calques
   *
   * @param width La longueur horizontale des images des calques
   */
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * Getter pour {@code this#height}, la longueur verticale des images des calques
   *
   * @return La longueur verticale des images des calques
   */
  public int getHeight() {
    return height;
  }

  /**
   * Getter pour la taille de l'image des calques
   *
   * @return La taille de l'image des calques
   */
  public int getSize() {
    return this.height * this.width;
  }

  /**
   * Setter pour {@code this#height}, la longueur verticale des images des calques
   *
   * @param height La longueur verticale des images des calques
   */
  public void setHeight(int height) {
    this.height = height;
  }

  /**
   * Getter pour {@code this#type}, le type du {@code BufferedImage} des calques
   *
   * @return Le type du {@code BufferedImage} des calques
   */
  public int getType() {
    return type;
  }

  /**
   * Setter pour {@code this#type}, le type du {@code BufferedImage} des calques
   *
   * @param type Le type du {@code BufferedImage} des calques
   */
  public void setType(int type) {
    this.type = type;
  }
}
