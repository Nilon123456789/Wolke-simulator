package com.e24.wolke.backend.models.editor.layers;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModel;
import com.e24.wolke.backend.models.application.ApplicationConstants.Resolution;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.EditorConstants;
import com.e24.wolke.backend.models.editor.tools.WToolConstants;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.utils.math.WMatrix1D;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code LayerModel} permet de gerer les calques.
 *
 * @author MeriBouisri
 */
public class LayerModel extends WModel {
  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(LayerModel.class.getClass().getSimpleName());

  /** Nombre minimal de calques */
  private static final int MIN_LAYERS = 2;

  /** Le {@code WLayerMergeProcessor} de cette instance */
  private WLayerMergeProcessor layerMergeProcessor;

  /** L'indice du calque de la bordure */
  private int borderIndex;

  /** Le {@code WLayerGenerator} de cette instance */
  private WLayerGenerator layerGenerator;

  /** Le {@code WLayerManager} de cette instance */
  private WLayerList layerList;

  /**
   * Construction d'un {@code LayerModel} avec un {@code Controller}
   *
   * @param controller Le {@code Controller} de l'application
   */
  public LayerModel(Controller controller) {
    super(controller);

    this.initialize();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean setup() {
    int[] res = this.getController().getSimulationResolution();

    this.layerGenerator =
        new WLayerGenerator(res[0], res[1], EditorConstants.DEFAULT_LAYER_IMAGE_TYPE);
    this.layerList =
        new WLayerList(
            this.layerGenerator, EditorConstants.MAX_TOTAL_LAYERS, LayerModel.MIN_LAYERS);

    this.initializeBorderLayer();
    this.addLayerAboveSelection();

    this.initializeLayerMergeProcessor();

    LayerModel.LOGGER.info("Initialized");

    return true;
  }

  /** Methode permettant d'initialiser la bordure d'obstacles */
  private void initializeBorderLayer() {
    this.borderIndex = 0;

    this.layerList.addLayer();

    for (int i = 0; i < this.layerGenerator.getSize(); i++) {
      if (!WMatrix1D.isBorderPosition(
          i, this.layerGenerator.getWidth(), this.layerGenerator.getHeight())) continue;

      int[] coords = WMatrix1D.getPosition2D(i, this.layerGenerator.getWidth());

      this.layerList
          .getLayerAt(this.borderIndex)
          .getImage()
          .setRGB(coords[0], coords[1], WToolConstants.DEFAULT_COLOR.getRGB());

      this.layerList.getLayerAt(this.borderIndex).setWritable(false);
    }
  }

  /** Methode permettant d'initialiser le {@code WLayerProcessor} */
  private void initializeLayerMergeProcessor() {
    this.layerMergeProcessor = new WLayerMergeProcessor(this.layerList);
    this.layerMergeProcessor.update();
  }

  /** Methode permettant d'ajouter un calque. */
  public void addLayer() {
    this.layerList.addLayer();
    this.onLayerFocusUpdate();
  }

  /** Methode permettant de retirer le calque actuel. */
  public void removeSelectedLayer() {
    if (this.getSelectedLayerIndex() == this.borderIndex) return;

    this.layerList.removeSelectedLayer();

    this.onLayerFocusUpdate();
  }

  /** Methode permettant de retirer tous les calques. */
  public void removeAllLayers() {
    int layerCount = this.layerList.size() - 1;
    for (int i = 0; i < layerCount; i++) {
      this.layerList.removeSelectedLayer();
    }
  }

  /** Methode permettant d'ajouter un calque par-dessus le calque selectionne */
  public void addLayerAboveSelection() {
    this.layerList.addLayerAboveSelection();
    this.onLayerFocusUpdate();
  }

  /** Methode permettant de deplacer le calque actuel a la position superieure (n + 1) */
  public void moveSelectedLayerUp() {
    if (this.getSelectedLayerIndex() + 1 == this.borderIndex) return;

    this.layerList.moveSelectedLayerUp();
    this.onLayerFocusUpdate();
  }

  /** Methode permettant de deplacer le calque actuel a la position inferieure (n - 1) */
  public void moveSelectedLayerDown() {
    if (this.getSelectedLayerIndex() - 1 == this.borderIndex) return;

    this.layerList.moveSelectedLayerDown();
    this.onLayerFocusUpdate();
  }

  /** Methode permettant de dupliquer le calque en selection */
  public void duplicateSelectedLayer() {
    this.layerList.duplicateSelectedLayer();
    this.onLayerFocusUpdate();
  }

  /**
   * Setter pour l'indice du calque selectionne
   *
   * @param index L'indice du calque selectionne
   */
  public void setSelectedLayerIndex(int index) {
    this.layerList.setSelectedLayerIndex(index);

    this.onLayerFocusUpdate();
  }

  /**
   * Getter pour l'indice du calque selectionne
   *
   * @return L'indice du calque selectionne
   */
  public int getSelectedLayerIndex() {
    return this.layerList.getSelectedLayerIndex();
  }

  /**
   * Getter pour le {@code WLayer} actuellement selectionne
   *
   * @return Le {@code WLayer} actuellement selectionne
   */
  public WLayer getSelectedLayer() {
    return this.layerList.getSelectedLayer();
  }

  /**
   * Methode permettant de selectionner un {@code WLayer} selon sa reference, si il est present dans
   * la liste
   *
   * @param layer La reference au {@code WLayer} a selectionner. Aucun changement si le {@code
   *     WLayer} n'est pas present
   */
  public void setSelectedLayer(WLayer layer) {
    int layerIndex = this.layerList.indexOfLayer(layer);
    if (layerIndex == -1) return;

    this.setSelectedLayerIndex(layerIndex);
  }

  /**
   * Retourne le @code WLayerFactory} de la liste de calques, le generateur des calques
   *
   * @return Le generateur des calques du {@code WLayerList}
   */
  public WLayerGenerator getLayerFactory() {
    return this.layerGenerator;
  }

  /**
   * getter pour la liste de {@code WLayerList}
   *
   * @return La liste des calques
   */
  public WLayerList getLayerList() {
    return this.layerList;
  }

  /**
   * Methode permettant de filtrer pour obtenir les {@code WLayer} dont l'acces a l'ecriture est
   * permis
   *
   * @return Une liste des {@code WLayer} dont l'acces a l'ecriture est permis
   */
  public List<WLayer> filterWritableLayers() {
    return this.layerList.filterWritableLayers();
  }

  /**
   * Methode permettant de filtrer pour obtenir les {@code WLayer} dont l'acces a la lecture est
   * permis
   *
   * @return Une liste des {@code WLayer} dont l'acces a la lecture est permis
   */
  public List<WLayer> filterReadableLayers() {
    return this.layerList.filterReadableLayers();
  }

  /**
   * Methode permettant de filter pour obtenir les {@code WLayer} sans le calque de bordure
   *
   * @return La liste de {@code WLayer} sans le calque de bordure
   */
  public List<WLayer> filterBorderlessLayers() {
    return this.layerList.getList().stream()
        .filter(layer -> layer.getID() != this.borderIndex)
        .toList();
  }

  /**
   * Methode permettant de creer l'image des calques fusionnee sans la bordure
   *
   * @return Le {@code BufferedImage} des calques fusionnees sans la bordure
   */
  public BufferedImage getMergedBorderlessImage() {
    WLayer[] layers = this.filterBorderlessLayers().toArray(WLayer[]::new);

    if (layers.length == 0) return this.layerGenerator.createBufferedImage();

    return WLayerList.mergeLayers(layers);
  }

  /** Methode a invoquer lors d'un changement avec le focus des calques */
  public void onLayerFocusUpdate() {
    this.getPublisher().publish(Subject.EDITOR_LAYER_FOCUS_UPDATE, this.getSelectedLayerIndex());
  }

  /** Methode a invoquer lors d'un changement avec les calque */
  public void onLayerUpdateImage() {
    this.getPublisher().publish(Subject.EDITOR_LAYER_UPDATE_IMAGE, null);
    this.getController()
        .getRendererModel()
        .getPublisher()
        .publish(Subject.ON_BUFFER_IMAGE_DONE, null);
  }

  /**
   * Getter pour {@code this#layerProcessor}
   *
   * @return Le {@code WLayerMergeProcessor} de cette instance
   */
  public WLayerMergeProcessor getLayerMergeProcessor() {
    return this.layerMergeProcessor;
  }

  /**
   * Methode permettant d'ajouter une image en tant que {@code WLayer} a la fin de la liste de
   * calques
   *
   * @param image L'image du nouveau {@code WLayer} a ajouter
   * @param layerName Le nom du {@code WLayer}
   * @return {@code true} si l'ajout a ete effectue avec succes
   */
  public boolean addFixedImageLayer(BufferedImage image, String layerName) {
    if (image == null) return false;

    // Fix image compatibility
    if (!this.layerGenerator.isCompatibleBufferedImage(image))
      this.promptUserAboutLayerCompatibility(image.getWidth());

    BufferedImage fixedImage = this.layerGenerator.fixUncompatibleBufferedImage(image);

    WLayer fixedImageLayer = null;

    if (layerName == null)
      fixedImageLayer = new WLayer(fixedImage, this.layerList.getLayerCount() + 1, true, true);
    else fixedImageLayer = new WLayer(fixedImage, layerName, true, true);

    this.layerList.addLayer(fixedImageLayer);

    this.onLayerFocusUpdate();

    return true;
  }

  /**
   * Methode permettant de demander a l'utilisateur comment proceder apres une erreur de
   * compatibilite
   *
   * @param layerWidth La longueur horizontale de l'image du calque
   * @return {@code true} si l'utilisateur a selectionne {@code YES}, {@code false} sinon
   */
  public boolean promptUserAboutLayerCompatibility(int layerWidth) {
    boolean result =
        JOptionPane.showConfirmDialog(
                null,
                LocaleManager.getLocaleResourceBundle().getString("scene.incorrect_resolution"),
                LocaleManager.getLocaleResourceBundle().getString("scene.error_title"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE)
            == JOptionPane.YES_OPTION;

    if (result) {
      Resolution importedResolution;
      for (Resolution resolution : Resolution.values()) {
        if (resolution.getWidth() == layerWidth) {
          importedResolution = resolution;
          this.getController().getApplicationModel().saveResolutionOnRestart(importedResolution);
          break;
        }
      }
    }
    return result;
  }

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {
    this.layerMergeProcessor.stop();
    this.layerMergeProcessor = null;
    this.layerList = null;

    this.setup();
  }

  /** {@inheritDoc} */
  @Override
  public void setupKeybindSubscriptions() {}
}
