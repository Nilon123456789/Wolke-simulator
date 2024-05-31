package com.e24.wolke.backend.models.editor.layers;

import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.utils.images.WImageUtils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Simple classe stockant les données nécessaires pour former un calque de l'éditeur.
 *
 * @author n-o-o-d-l-e
 * @author MeriBouisri
 */
public class WLayer {

  /** {@code BufferedImage} du calque. */
  private BufferedImage layerImage;

  /** Le nom du calque */
  private String layerName;

  /** L'identifiant unique d'un calque */
  private int layerID;

  /** Si l'acces en ecriture du calque est permis */
  private boolean isWritable = true;

  /** Si l'acces en lecture du calque est permis */
  private boolean isReadable = true;

  /** Si la calque doit etre accessible a la selection par l'utilisateur */
  private boolean isSelectable = true;

  /** La longueur maximale du nom d'un calque */
  private static final int MAX_NAME_LENGTH = 22;

  /**
   * Constructeur d'un calque, qui incrémente le compteur du nombre de calques
   *
   * @param image Le {@code BufferedImage} du calque
   */
  public WLayer(BufferedImage image) {
    this.layerImage = image;
    this.setID(this.hashCode(), true);
  }

  /**
   * Construction d'un {@code WLayer} avec un identifiant
   *
   * @param image Le {@code BufferedImage} du calque
   * @param name Le nom du calque
   * @param isWritable {@code true} si l'acces en ecriture est permis
   * @param isReadable {@code true} si l'acces en lecture est permis
   * @param id L'identifiant du calque
   */
  public WLayer(BufferedImage image, String name, boolean isWritable, boolean isReadable, int id) {
    this(image);
    this.setID(id);
    this.setName(name);
    this.isWritable = isWritable;
    this.isReadable = isReadable;
  }

  /**
   * Construction d'un {@code WLayer}
   *
   * @param image Le {@code BufferedImage} du calque
   * @param name Le nom du calque
   * @param isWritable {@code true} si l'acces en ecriture est permis
   * @param isReadable {@code true} si l'acces en lecture est permis
   */
  public WLayer(BufferedImage image, String name, boolean isWritable, boolean isReadable) {
    this(image);
    this.setName(name);
    this.isWritable = isWritable;
    this.isReadable = isReadable;
  }

  /**
   * Construction d'un {@code WLayer} avec un identifiant
   *
   * @param image Le {@code BufferedImage} du calque
   * @param id L'identifiant du calque
   */
  public WLayer(BufferedImage image, int id) {
    this(image);
    this.setID(id, true);
  }

  /**
   * Construction d'un {@code WLayer}
   *
   * @param image Le {@code BufferedImage} du calque
   * @param id L'identifiant du calque
   * @param isWritable {@code true} si l'acces en ecriture est permis
   * @param isReadable {@code true} si l'acces en lecture est permis
   */
  public WLayer(BufferedImage image, int id, boolean isWritable, boolean isReadable) {
    this(image, id);
    this.isWritable = isWritable;
    this.isReadable = isReadable;
  }

  /**
   * Getter pour le {@code BufferedImage} du calque
   *
   * @return Le {@code BufferedImage} du calque
   */
  public BufferedImage getImage() {
    return layerImage;
  }

  /**
   * Getter pour le nom du calque
   *
   * @return Le nom du calque
   */
  public String getName() {
    return layerName;
  }

  /**
   * Setter pour le nom du calque
   *
   * @param name Le nom du calque
   */
  public void setName(String name) {
    layerName = name;
    this.layerName = layerName.substring(0, Math.min(layerName.length(), MAX_NAME_LENGTH));
    if (layerName.length() == MAX_NAME_LENGTH) layerName += "...";
  }

  /**
   * Setter pour le {@code BufferedImage} du calque
   *
   * @param image Le {@code BufferedImage} du calque
   */
  public void setImage(BufferedImage image) {
    layerImage = image;
  }

  /**
   * Getter pour l'identifiant unique du calque
   *
   * @return L'identifiant unique du calque
   */
  public int getID() {
    return layerID;
  }

  /**
   * Methode permettant de creer un nouveau calque avec la meme image que cette instance. Le {@code
   * BufferedImage} est copié, ainsi que les droits d'acces. Le {@code layerID} du calque duplique
   * n'est pas le meme.
   *
   * @return Une instance de {@code WLayer} avec la meme image que cette instance, mais qui n'est
   *     pas {@code equal} a cette instance.
   */
  public WLayer duplicate() {
    String duplicateLayerName =
        this.layerName
            + " "
            + LocaleManager.getLocaleResourceBundle().getString("ui.editor.layers.layer_copy");
    duplicateLayerName.substring(0, Math.min(layerName.length(), MAX_NAME_LENGTH));
    if (duplicateLayerName.length() == MAX_NAME_LENGTH) duplicateLayerName += "...";
    return new WLayer(
        WImageUtils.copy(this.layerImage), duplicateLayerName, this.isWritable, this.isReadable);
  }

  /**
   * Methode permettant de creer un nouveau calque avec la meme image que cette instance. Le {@code
   * BufferedImage} est copié, ainsi que les droits d'acces. Le {@code layerID} du calque duplique
   * n'est pas le meme.
   *
   * @param id L'identifiant du calque duplique.
   * @return Une instance de {@code WLayer} avec la meme image que cette instance, mais qui n'est
   *     pas {@code equal} a cette instance.
   */
  public WLayer duplicate(int id) {
    WLayer dupe = this.duplicate();
    dupe.setID(id);
    return dupe;
  }

  /**
   * Getter pour {@code this#isWritable}, si l'acces en ecriture de ce calque est permis.
   *
   * @return {@code true} si l'acces en ecriture de ce calque est permis
   */
  public boolean isWritable() {
    return isWritable;
  }

  /**
   * Setter pour {@code this#isWritable}, si l'acces en ecriture de ce calque est permis.
   *
   * @param isWritable {@code true} si l'acces en ecriture de ce calque est permis
   */
  public void setWritable(boolean isWritable) {
    this.isWritable = isWritable;
  }

  /**
   * Getter pour {@code this#isReadable}, si l'acces en lecture de ce calque est permis.
   *
   * @return {@code true} si l'acces en lecture de ce calque est permis
   */
  public boolean isReadable() {
    return isReadable;
  }

  /**
   * Setter pour {@code this#isReadable}, si l'acces en lecture de ce calque est permis.
   *
   * @param isReadable {@code true} si l'acces en lecture de ce calque est permis
   */
  public void setReadable(boolean isReadable) {
    this.isReadable = isReadable;
  }

  /**
   * Getter pour {@code this#isSelectable}, si la selection par l'utilisateur est autorisee
   *
   * @return {@code true} si ce calque peut etre selectionne par l'utilisateur
   */
  public boolean isSelectable() {
    return isSelectable;
  }

  /**
   * Setter pour {@code this#isSelectable}, si la selection par l'utilisateur est autorisee
   *
   * @param isSelectable {@code true} si ce calque peut etre selectionne par l'utilisateur
   */
  public void setSelectable(boolean isSelectable) {
    this.isSelectable = isSelectable;
  }

  /**
   * Methode permettant de determiner si l'acces en lecture de ce calque est permis, selon la valeur
   * de {@code force}
   *
   * @param force {@code true} si les permissions de lecture doivent etre ignorees
   * @return Le resultat de {@code isReadable} OU {@code force}
   */
  public boolean isReadableByForce(boolean force) {
    return WLayer.isAccessibleByForce(this.isReadable, force);
  }

  /**
   * Methode permettant de determiner si l'acces en ecriture de ce calque est permis, selon la
   * valeur de {@code force}
   *
   * @param force {@code true} si les permissions d'ecriture doivent etre ignorees
   * @return Le resultat de {@code isWritable} OU {@code force}
   */
  public boolean isWritableByForce(boolean force) {
    return WLayer.isAccessibleByForce(this.isReadable, force);
  }

  /**
   * Methode utilitaire permettant de determiner le resultat de {@code isAccessible} selon la valeur
   * de {@code force}
   *
   * @param isAccessible {@code true} si l'acces est permis
   * @param force {@code true} si {@code isAccessible} doit etre ignore
   * @return Le resultat de {@code isAccessible} OU {@code force}
   */
  public static boolean isAccessibleByForce(boolean isAccessible, boolean force) {
    return isAccessible || force;
  }

  /**
   * Methode permettant de fusionner l'image du {@code WLayer} passe en parametre a l'image de cette
   * instance. L'image du {@code WLayer} passe en parametre n'est pas modifiee
   *
   * @param layer Le {@code WLayer} dont l'image est a dessiner par dessus l'image de cette instance
   * @param onTop {@code true} le calque passe en parametre doit etre dessine par-dessus l'image
   *     existente, {@code false} si dessine par en-dessous
   * @return {@code true} si la fusion a ete effectuee avec succes.
   */
  public boolean merge(WLayer layer, boolean onTop) {
    if (!WLayerGenerator.isCompatibleLayer(this, layer)) return false;

    if (!this.isWritable) return false;

    if (!layer.isReadable()) return false;

    return this.merge(layer.getImage(), onTop);
  }

  /**
   * Methode permettant de fusionner l'image passeee en parametre a l'image de cette instance
   *
   * @param image L'image a dessiner par-dessus l'image de ce {@code WLayer}
   * @param onTop {@code true} le calque passe en parametre doit etre dessine par-dessus l'image
   *     existente, {@code false} si dessine par en-dessous
   * @return {@code true} si la fusion a ete effectuee avec succes.
   */
  public boolean merge(BufferedImage image, boolean onTop) {
    if (!WLayerGenerator.isCompatibleBufferedImage(this.getImage(), image)) return false;

    // Dessiner par-dessus
    if (onTop) this.mergeFromAbove(image);
    else this.mergeFromBelow(image);

    return true;
  }

  /**
   * Methode permettant de dessiner un {@code BufferedImage} par-dessus l'image actuelle
   *
   * @param image Le {@code BufferedImage} a dessiner par-dessus
   */
  public void mergeFromAbove(BufferedImage image) {
    Graphics2D g2d = this.layerImage.createGraphics();
    g2d.drawImage(image, null, null);
    g2d.dispose();
  }

  /**
   * Methode permettant de dessiner un {@code BufferedImage} en-dessous de l'image actuelle
   *
   * @param image Le {@code BufferedImage} a dessiner en-dessous
   */
  public void mergeFromBelow(BufferedImage image) {
    BufferedImage newImage = WImageUtils.copy(image);

    Graphics2D g2d = newImage.createGraphics();
    g2d.drawImage(this.layerImage, null, null);
    g2d.dispose();

    this.setImage(newImage);
  }

  /**
   * Setter pour {@code this#layerID}, l'identifiant du calque. Le nom du calque n'est pas modifiee
   *
   * @param id L'identifiant du calque
   */
  protected void setID(int id) {
    this.setID(id, false);
  }

  /**
   * Setter pour {@code this#layerID}, l'identifiant du calque. Le nom du calque peut etre modifie
   * selon le parametre {@code changeName}
   *
   * @param id L'identifiant du calque
   * @param changeName Si le nom du calque doit etre modifie pour correspondre au {@code id}
   */
  protected void setID(int id, boolean changeName) {
    this.layerID = id;
    if (changeName)
      this.setName(
          LocaleManager.getLocaleResourceBundle().getString("ui.editor.layers.layer")
              + " "
              + this.layerID);
  }

  /**
   * Un {@code WLayer} est egal a un autre {@code WLayer} s'ils ont le meme identifiant
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object o) {
    if (o instanceof WLayer) return this.getID() == ((WLayer) o).getID();

    return false;
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "name=" + this.layerName + ", id=" + this.layerID;
  }
}
