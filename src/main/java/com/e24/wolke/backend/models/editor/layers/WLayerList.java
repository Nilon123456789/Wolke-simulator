package com.e24.wolke.backend.models.editor.layers;

import com.e24.wolke.backend.models.editor.EditorConstants;
import com.e24.wolke.utils.images.WImageUtils;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.List;

/**
 * La classe {@code WLayerList} permet de gerer une liste de {@code WLayer}
 *
 * @author MeriBouisri
 * @author adrienles
 */
public class WLayerList {

  /** Le compteur de calques crees par cette instance */
  private int layerCounter = 0;

  /** Le nombre maximal de calques */
  private int maxLayers;

  /** Le nombre minimal de calques */
  private int minLayers = 1;

  /** La liste de {@code WLayer} */
  private ArrayList<WLayer> layers;

  /** Le {@code WLayerFactory} de cette instance */
  private WLayerGenerator layerGenerator;

  /** L'indice du {@code WLayer} selectionne */
  private int selectedLayerIndex;

  /** Le {@code Raster} servant a effacer les donnees d'un {@code BufferedImage} */
  private Raster emptyRaster;

  /**
   * Construction d'un {@code WLayerList}, avec un nombre maximal de calques
   *
   * @param layerFactory le {@code WLayerFactory} qui genere les {@code WLayer}
   * @param maxLayers Le nombre maximal de calques
   * @param minLayers Le nombre minimal de calques
   */
  public WLayerList(WLayerGenerator layerFactory, int maxLayers, int minLayers) {
    this.maxLayers = maxLayers;
    this.minLayers = minLayers;
    this.layerGenerator = layerFactory;

    this.layers = new ArrayList<WLayer>();

    this.emptyRaster = this.layerGenerator.createBufferedImage().getData();
  }

  /**
   * Construction d'un {@code WLayerList}, avec un nombre maximal de calques
   *
   * @param layerFactory le {@code WLayerFactory} qui genere les {@code WLayer}
   * @param maxLayers Le nombre maximal de calques
   */
  public WLayerList(WLayerGenerator layerFactory, int maxLayers) {
    this(layerFactory, maxLayers, 1);
  }

  /**
   * Construction d'un {@code WLayerList}.
   *
   * @param layerFactory le {@code WLayerFactory} qui genere les {@code WLayer}
   */
  public WLayerList(WLayerGenerator layerFactory) {
    this(layerFactory, EditorConstants.MAX_TOTAL_LAYERS);
  }

  /**
   * Construction d'un {@code WLayerList} par defaut.
   *
   * @param imgWidth La longueur horizontale des images des calques
   * @param imgHeight La longueur verticale des images des calques
   * @param imgType Le type de l'image des calques
   * @param maxLayers Le nombre maximal de calques
   */
  public WLayerList(int imgWidth, int imgHeight, int imgType, int maxLayers) {
    this(new WLayerGenerator(imgWidth, imgHeight, imgType), maxLayers);
  }

  /**
   * Construction d'un {@code WLayerList} par defaut.
   *
   * @param imgWidth La longueur horizontale des images des calques
   * @param imgHeight La longueur verticale des images des calques
   * @param imgType Le type de l'imagedes calques
   */
  public WLayerList(int imgWidth, int imgHeight, int imgType) {
    this(new WLayerGenerator(imgWidth, imgHeight, imgType));
  }

  /**
   * Methode permettant d'ajouter un calque vide a la liste des {@code WLayer} de cette instance.
   * Cette methode ne modifie pas {@code this#selectedLayerIndex}
   *
   * @return {@code true} si l'ajout a ete effectue avec succes
   */
  public boolean addLayer() {
    if (this.isFull()) return false;

    return this.layers.add(layerGenerator.createLayer(this.layerCounter++));
  }

  /**
   * Methode permettant d'inserer un calque vide a la liste des {@code WLayer} de cette instance a
   * l'indice donne. Cette methode ne modifie pas {@code this#selectedLayerIndex}
   *
   * @param index La position a laquelle le nouveau calque sera insere
   * @return {@code true} si l'ajout a ete effectue avec succes
   */
  public boolean addLayer(int index) {
    return this.insertLayerAt(this.layerGenerator.createLayer(this.layerCounter++), index);
  }

  /**
   * Methode permettant d'ajouter un {@code WLayer} a la fin de la liste de calques
   *
   * @param layer Le {@code WLayer} a ajouter
   * @return {@code true} si l'ajout a ete effectue avec succes
   */
  public boolean addLayer(WLayer layer) {
    return this.insertLayerAt(layer, this.size());
  }

  /**
   * Methode permettant d'inserer un {@code WLayer} a une position donnee. Le {@code WLayer} doit
   * etre compatible avec le {@code WLayerFactory} de cette instance
   *
   * @param layer Le {@code WLayer} a inserer
   * @param index La position ou inserer le {@code WLayer}
   * @return {@code true} si l'insertion a ete effectuee avec succes
   */
  public boolean insertLayerAt(WLayer layer, int index) {
    if (this.isFull()) return false;

    if (!this.layerGenerator.isCompatibleLayer(layer)) return false;

    // Dans le cas ou l'insertion doit se faire a la fin de la liste
    if (index == this.size()) {
      this.layers.add(null);
      this.layers.set(index, layer);
      return true;
    }

    if (!this.isValidLayerIndex(index))
      throw new IndexOutOfBoundsException("i=" + index + ", size=" + this.size());

    this.layers.add(index, layer);

    return true;
  }

  /**
   * Méthode permettant d'ajouter un calque vide par dessus le calque actuel et de le sélectionner.
   *
   * @return {@code true} si l'ajout a ete effectue avec succes
   */
  public boolean addLayerAboveSelection() {
    if (this.isFull()) return false;

    int aboveLayerIndex = this.getSelectedLayerIndex() + 1;

    if (!this.addLayer(aboveLayerIndex)) return false;

    this.setSelectedLayerIndex(aboveLayerIndex);

    return true;
  }

  /**
   * Methode permettant de deplacer le calque selectionne a une position donnee
   *
   * @param newIndex La nouvelle position du calque
   * @return {@code true} si le deplacement a ete effectue avec succes
   */
  public boolean moveSelectedLayer(int newIndex) {
    if (!this.isValidLayerIndex(newIndex)) throw new IndexOutOfBoundsException(newIndex);

    // On fait rien si meme index
    if (newIndex == this.selectedLayerIndex) return true;

    WLayer layer = this.removeLayerAt(this.selectedLayerIndex, true);

    if (!this.insertLayerAt(layer, newIndex)) return false;

    this.setSelectedLayerIndex(newIndex);
    return true;
  }

  /**
   * Methode permettant de deplacer le {@code WLayer} selectionne a la position n + 1
   *
   * @return {@code true} si le deplacement a ete effectue avec succes
   */
  public boolean moveSelectedLayerUp() {
    if (!this.getLayerAt(this.selectedLayerIndex).isWritable()) return false;

    if (this.selectedLayerIndex >= this.size() - 1) return false;

    return this.moveSelectedLayer(this.selectedLayerIndex + 1);
  }

  /**
   * Methode permettant de deplacer le {@code WLayer} selectionne a la position n - 1
   *
   * @return {@code true} si le deplacement a ete effectue avec succes
   */
  public boolean moveSelectedLayerDown() {
    if (!this.getLayerAt(this.selectedLayerIndex).isWritable()
        || !this.getLayerAt(this.selectedLayerIndex - 1).isWritable()) return false;

    if (this.selectedLayerIndex <= 0) return false;

    return this.moveSelectedLayer(this.selectedLayerIndex - 1);
  }

  /**
   * Methode permettant de retourner le {@code WLayer} a l'indice donne
   *
   * @param index l'indice de la position du {@code WLayer} dans la liste
   * @return le {@code WLayer} a l'indice passe en parametre
   */
  public WLayer getLayerAt(int index) {
    if (!this.isValidLayerIndex(index))
      throw new IndexOutOfBoundsException("Indice du calque invalide : " + index);

    return this.layers.get(index);
  }

  /**
   * Methode permettant de retirer un {@code WLayer} de la liste.
   *
   * @param index L'indice du {@code WLayer} a retirer
   * @param force {@code true} si le champ {@code isWritable} du {@code WLayer} doit etre ignore
   * @return Le {@code WLayer} retire de la liste, {@code null} si l'ecriture au {@code WLayer} est
   *     non-permise
   */
  protected WLayer removeLayerAt(int index, boolean force) {
    if (!this.isValidLayerIndex(index)) throw new IndexOutOfBoundsException(index);

    if (!this.getLayerAt(index).isWritableByForce(force)) return null;

    return this.layers.remove(index);
  }

  /**
   * Methode permettant de retirer un {@code WLayer} de la liste.
   *
   * @param index L'indice du {@code WLayer} a retirer
   * @return Le {@code WLayer} retire de la liste, {@code null} si l'ecriture au {@code WLayer} est
   *     non-permise
   */
  public WLayer removeLayerAt(int index) {
    return this.removeLayerAt(index, false);
  }

  /**
   * Methode permettant de retourner le {@code WLayer} a l'indice determine par {@code
   * this#selectedLayerIndex}
   *
   * @return Le {@code WLayer} a l'indice determine par {@code this#selectedLayerIndex}
   */
  public WLayer getSelectedLayer() {
    return this.getLayerAt(this.selectedLayerIndex);
  }

  /**
   * Setter pour {@code this#selectedLayerIndex}, l'indice du calque selectionne
   *
   * @param index L'indice du calque a selectionner. L'
   */
  public void setSelectedLayerIndex(int index) {
    if (!this.isValidLayerIndex(index))
      throw new IndexOutOfBoundsException("Indice invalide : " + index);

    this.selectedLayerIndex = index;
  }

  /**
   * Getter pour {@code this#selectedLayerIndex}, l'indice du calque selectionne
   *
   * @return L'indice du calque a selectionner
   */
  public int getSelectedLayerIndex() {
    return this.selectedLayerIndex;
  }

  /**
   * Methode permettant de retirer le {@code WLayer} a la position indiquee par {@code
   * this#selectedIndex}.
   *
   * @return Le {@code WLayer} retire de la liste, {@code null} si le retrait a echoue
   */
  public WLayer removeSelectedLayer() {
    WLayer removed = this.removeLayerAt(this.selectedLayerIndex);

    // Si retrait du WLayer a echoue, selectedIndex ne sera pas modifie
    if (removed == null) return null;

    // S'il reste seulement 1 layer
    if (this.size() == this.minLayers - 1) {
      this.addLayer();
      this.setSelectedLayerIndex(this.size() - 1);
    }

    if (this.selectedLayerIndex >= this.size()) this.setSelectedLayerIndex(this.size() - 1);

    // Retourne null si retrait echoue
    return removed;
  }

  /**
   * Methode permettant de dupliquer le calque actuel et de l'ajouter a la liste des calques.
   * Celui-ci sera ensuite selectionne
   */
  public void duplicateSelectedLayer() {
    if (this.isFull()) return;

    this.insertLayerAt(this.getSelectedLayer().duplicate(), this.selectedLayerIndex + 1);

    this.setSelectedLayerIndex(this.selectedLayerIndex + 1);
  }

  /**
   * Methode permettant de renommer le nom du calque actuel
   *
   * @param newName Le nouveau nom du calque actuel
   * @return L'ancien nom du calque actuel
   */
  public String setSelectedLayerName(String newName) {
    String oldName = this.getSelectedLayer().getName();
    this.getSelectedLayer().setName(newName);
    return oldName;
  }

  /**
   * Methode permettant d'effacer le contenu d'un calque specifie par {@code index}. Le calque ne
   * sera pas efface si l'acces en ecriture est non-permis.
   *
   * @param index L'indice du calque a effacer
   * @return {@code true} Si le calque a ete efface avec succes, {@code false} si l'acces en
   *     ecriture est non-permis
   */
  public boolean clearLayerAt(int index) {
    if (!this.isValidLayerIndex(index)) throw new IndexOutOfBoundsException(index);

    if (!this.getLayerAt(index).isWritable()) return false;

    this.getLayerAt(index).getImage().setData(this.emptyRaster);
    return true;
  }

  /**
   * Methode permettant de recuperer l'indice du {@code WLayer} dans la liste selon sa reference
   *
   * @param layer Le {@code WLayer} dont l'indice est a retourner
   * @return L'indice du {@code WLayer} passe en parametre, -1 si le {@code WLayer} n'est pas
   *     present
   */
  public int indexOfLayer(WLayer layer) {
    return this.layers.indexOf(layer);
  }

  /**
   * Methode permettant d'effacer le contenu du calque actuel
   *
   * @return {@code true} Si le calque a ete efface avec succes, {@code false} si l'acces en
   *     ecriture est non-permis
   */
  public boolean clearSelectedLayer() {
    return this.clearLayerAt(this.selectedLayerIndex);
  }

  /** Methode permettant d'effacer tous les calques dont l'acces en ecriture est permis */
  public void clearAllLayers() {
    this.layers.forEach(layer -> this.clearLayerAt(this.layers.indexOf(layer)));
  }

  /**
   * Methode permettant de filtrer pour obtenir les {@code WLayer} dont l'acces a l'ecriture est
   * permis
   *
   * @return Une liste des {@code WLayer} dont l'acces a l'ecriture est permis
   */
  public List<WLayer> filterWritableLayers() {
    return this.layers.stream().filter(layer -> layer.isWritable()).toList();
  }

  /**
   * Methode permettant de filtrer pour obtenir les {@code WLayer} dont l'acces a la lecture est
   * permis
   *
   * @return Une liste des {@code WLayer} dont l'acces a la lecture est permis
   */
  public List<WLayer> filterReadableLayers() {
    return this.layers.stream().filter(layer -> layer.isReadable()).toList();
  }

  /**
   * Methode permettant de valider l'existence d'un {@code WLayer} a l'indice donne
   *
   * @param index L'indice du {@code WLayer} a verifier
   * @return {@code true} si l'indice est valide pour retourner un {@code WLayer}
   */
  public boolean isValidLayerIndex(int index) {
    return index >= 0 && index < this.size();
  }

  /**
   * Methode permettant de determiner si le nombre de {@code WLayer} de la liste a atteint le
   * maximum determine par {@code this#maxLayers}
   *
   * @return {@code true} si la liste de {@code WLayer} a atteint son maximum
   */
  public boolean isFull() {
    return this.layers.size() == this.maxLayers;
  }

  /**
   * Retourne la taille de la liste de {@code WLayer}
   *
   * @return la taille de la liste de {@code WLayer}
   */
  public int size() {
    return this.layers.size();
  }

  /**
   * Getter pour {@code this#layerFactory}, le generateur de calques
   *
   * @return Le generateur de calques
   */
  public WLayerGenerator getLayerGenerator() {
    return this.layerGenerator;
  }

  /**
   * Methode permettant de recuperer tous les {@code WLayer} en dessous du {@code WLayer} courant
   *
   * @return Liste de tous les {@code WLayer} en dessous du {@code WLayer} selectionne
   */
  public WLayer[] getLayersBelowSelection() {
    // Nombre de layers en bas correspond a l'indice de la selection
    int numLayersBelow = this.getSelectedLayerIndex();
    WLayer[] layersBelow = new WLayer[numLayersBelow];

    for (int i = 0; i < layersBelow.length; i++) layersBelow[i] = this.getLayerAt(i);

    return layersBelow;
  }

  /**
   * Methode permettant de recuperer tous les {@code WLayer} par-dessus du {@code WLayer} courant
   *
   * @return Liste de tous les {@code WLayer} par-dessus le {@code WLayer} selectionne
   */
  public WLayer[] getLayersAboveSelection() {
    int numLayersAbove = this.size() - 1 - this.getSelectedLayerIndex();
    WLayer[] layersAbove = new WLayer[numLayersAbove];

    int layerIndex;

    for (int i = 0; i < layersAbove.length; i++) {
      layerIndex = this.getSelectedLayerIndex() + i + 1;
      layersAbove[i] = this.getLayerAt(layerIndex);
    }

    return layersAbove;
  }

  /**
   * Methode permettant d'ajouter un certain nombre de {@code WLayers} a la fin de la liste de cette
   * instance
   *
   * @param numLayers Le nombre de {@code WLayer} a ajouter a la fin de cette liste
   * @return {@code true} si l'ajout de calques, en tout ou en partie, a ete effectue avec succes,
   *     {@code false}
   */
  public boolean fill(int numLayers) {
    if (this.isFull()) return false;

    if (numLayers < 1) return false;

    // Truncate num of layers to fit with max
    if (this.size() + numLayers >= this.maxLayers) numLayers = this.maxLayers - this.size();

    for (int i = 0; i < numLayers; i++) this.addLayer();

    return true;
  }

  /**
   * Methode permettant de fusionner une lsite de {@code WLayer}. La position des calques dans le
   * tableau determine leur ordre de dessin, avec le premier element dessine en premier (le plus
   * en-dessous) et le dernier dessine en dernier (le plus par-dessus)
   *
   * @param layers Liste des {@code WLayer} a fusionner
   * @return Le {@code BufferedImage} qui contient le dessin fusionne des calques, {@code null} si
   *     le tableau est vide
   */
  public static BufferedImage mergeLayers(WLayer[] layers) {
    if (layers.length == 0) return null;

    BufferedImage image = WImageUtils.copy(layers[0].getImage());

    Graphics2D g2d = image.createGraphics();

    for (int i = 1; i < layers.length; i++) g2d.drawImage(layers[i].getImage(), null, null);

    g2d.dispose();

    return image;
  }

  /**
   * Getter pour {@code this#layers}
   *
   * @return La liste de {@code WLayer}
   */
  public ArrayList<WLayer> getList() {
    return this.layers;
  }

  /**
   * Getter pour {@code this#layerCounter}, le compteur de calques
   *
   * @return Le compteur de calques depuis la creation de cette instance
   */
  public int getLayerCount() {
    return this.layerCounter;
  }
}
