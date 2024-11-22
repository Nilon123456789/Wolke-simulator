package com.e24.wolke.backend.models.editor;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModel;
import com.e24.wolke.backend.models.editor.layers.LayerModel;
import com.e24.wolke.backend.models.editor.tools.WToolConstants;
import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import com.e24.wolke.backend.models.editor.tools.WToolImage;
import com.e24.wolke.backend.models.editor.tools.WToolbox;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.filesystem.scenes.WSceneMember;
import com.e24.wolke.frontend.editor.EditorColorPicker;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * La classe {@code EditorModel} sert a gerer la logique du mode d'edition de scene de l'application
 * et de l'exposer au {@code Controller}.
 *
 * @author MeriBouisri
 * @author adrienles
 */
public class EditorModel extends WModel implements WSceneMember<BufferedImage> {

  /** Le {@code Logger} de la classe */
  private static final Logger LOGGER =
      LogManager.getLogger(EditorModel.class.getClass().getSimpleName());

  /** L'image fusionnee */
  private BufferedImage editorImage;

  /** Le {@code WLayerManager} de l'instance */
  // private WLayerManager layerManager;
  private LayerModel layerModel;

  /** Le {@code WToolbox} utilise pour dessiner */
  private WToolbox toolbox;

  /** Le {@code EditorColorPickerFrame} utilisé pour choisir la couleur */
  private EditorColorPicker editorColorPicker;

  /** Indique si le menu deroulant des formes est visible */
  private boolean shapesDropdownVisibility = EditorConstants.DEFAULT_SHAPES_DROPDOWN_VISIBILITY;

  /**
   * Construction d'un {@code EditorModel} avec un {@code Controller}. La methode {@code
   * this#initialize()} est appelee.
   *
   * @param controller Le {@code Controller} qui gere cette instance.
   */
  public EditorModel(Controller controller) {
    super(controller);
    setupKeybindSubscriptions();

    initialize();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean setup() {
    toolbox = new WToolbox(getController());
    layerModel = new LayerModel(this.getController());
    setupLayerObservers();

    // Pour dessiner les obstacles deja presents
    retrieveSimulationObstacles();
    updateEditorImage();

    this.layerModel.getLayerMergeProcessor().update();

    EditorModel.LOGGER.debug("Initialized");

    return true;
  }

  /**
   * Methode permettant de dessiner les obstacles actuels dessines a partir du {@code
   * SimulationData}
   */
  private void retrieveSimulationObstacles() {
    getImageLayer()
        .setData(
            getController()
                .getObstacleModel()
                .getObstacleMatrix()
                .toBufferedImage(WToolConstants.DEFAULT_COLOR.getRGB())
                .getData());
  }

  /** Methode permettant d'initialiser les {@code WObserver} */
  private void setupLayerObservers() {
    this.layerModel.getLayerMergeProcessor().addObserver(this::updateEditorImage);

    this.layerModel
        .getLayerMergeProcessor()
        .addObserver(() -> getController().getObstacleModel().onNewObstacles());
  }

  /**
   * Getter pour {@code this#layerModel}, le sous-modele en charge du systeme de calques
   *
   * @return Le modele des calques
   */
  public LayerModel getLayerModel() {
    return layerModel;
  }

  // ========== PAINTING METHODS ========== //

  /**
   * Methode permettant de commencer le dessin, avec les positions initiales de la souris
   *
   * @param mouseStartX La position initiale en x de la souris lors du debut du dessin
   * @param mouseStartY La position initiale en y de la souris lors du debut du dessin
   */
  public void startPainting(int mouseStartX, int mouseStartY) {
    if (this.toolbox.getCurrentToolType() == ToolType.NONE) return;

    this.layerModel.getLayerMergeProcessor().start();

    // Paint with toolbox

    toolbox.setStartPosition(mouseStartX, mouseStartY);
    toolbox.use(getImageLayer(), mouseStartX, mouseStartY, mouseStartX, mouseStartY);
  }

  /**
   * Methode permettant de poursuivre le dessin, avec les positions actuelles de la souris
   *
   * @param mouseCurrentX La position actuelle en x de la souris au cours du dessin
   * @param mouseCurrentY La position actuelle en y de la souris au cours du dessin
   */
  public void keepPainting(int mouseCurrentX, int mouseCurrentY) {
    toolbox.use(getImageLayer(), mouseCurrentX, mouseCurrentY);
  }

  /** Methode a invoquer lorsque le dessin est fini */
  public void stopPainting() {
    if (toolbox.hasIntermediateImage()) {
      Graphics2D g2d = getImageLayer().createGraphics();
      g2d.drawImage(toolbox.pullIntermediateImage(), null, null);
      g2d.dispose();
    }

    this.layerModel.getLayerMergeProcessor().stop();
  }

  /**
   * Getter pour {@code this.toolbox}
   *
   * @return La boite a outils utilisee par l'editeur d'obstacles
   */
  public WToolbox getToolbox() {
    return toolbox;
  }

  /**
   * Getter pour le {@code BufferedImage} des calques fusionnees
   *
   * @return le {@code BufferedImage} des calques fusionnees
   */
  public BufferedImage getEditorImage() {
    return editorImage;
  }

  /**
   * Methode permettant d'actualiser l'image de l'editeur avec toutes les calques, et l'image
   * intermediaire en cours de dessin, s'il y a lieu.
   */
  public void updateEditorImage() {
    BufferedImage image = layerModel.getLayerFactory().createBufferedImage();

    Graphics2D g2d = image.createGraphics();

    g2d.drawImage(layerModel.getLayerMergeProcessor().getCachedUnderLayer(), null, null);
    g2d.drawImage(layerModel.getSelectedLayer().getImage(), null, null);

    if (toolbox.hasIntermediateImage()) g2d.drawImage(toolbox.peekIntermediateImage(), null, null);

    g2d.drawImage(layerModel.getLayerMergeProcessor().getCachedOverLayer(), null, null);
    g2d.dispose();

    editorImage = image;
    image = null;
  }

  /**
   * Getter pour le tableau de donnees binaires qui represente la presence ou l'absence d'un pixel
   * dans l'image fusionnee
   *
   * @return tableau de donnees binaires qui represente la presence ou l'absence d'un pixel dans
   *     l'image fusionnee
   */
  public int[] getMergedImageData() {
    return this.layerModel.getLayerMergeProcessor().getMergedImageData();
  }

  /**
   * Getter pour le {@code BufferedImage} du calque actuel
   *
   * @return Le {@code BufferedImage} du calque actuel
   */
  public BufferedImage getImageLayer() {
    return layerModel.getSelectedLayer().getImage();
  }

  /**
   * Methode permettant de defaire la derniere action effectuee dans l'editeur d'obstacles.
   *
   * @return {@code true} si la defaite de la derniere action a ete effectuee avec succes
   */
  public boolean undo() {
    return false;
  }

  /**
   * Methode permettant de refaire la derniere action defaite dans l'editeur d'obstacles.
   *
   * @return {@code true} si la refaction de la derniere action defaite a ete effectué avec succès
   */
  public boolean redo() {
    return false;
  }

  /**
   * Methode permettant de changer la visibilite du menu deroulant des formes
   *
   * @param visibility La visibilite du menu deroulant des formes
   */
  public void setShapesDropdownVisibility(boolean visibility) {
    shapesDropdownVisibility = visibility;
    getPublisher().publish(Subject.EDITOR_TOOLPANE_SHAPES_DROPDOWN_STATE_CHANGED, visibility);
  }

  /**
   * Getter pour la visibilite du menu deroulant des formes
   *
   * @return La visibilite du menu deroulant des formes
   */
  public boolean getShapesDropdownVisibility() {
    return shapesDropdownVisibility;
  }

  /**
   * Methode permettant d'importer dans l'outil de dessin d'image SVG ou PNG
   *
   * @param imagePath Le chemin de l'image a importer
   */
  public void importToolImage(File imagePath) {
    if (this.toolbox.getTool().getToolType() == ToolType.IMAGE)
      WEditorIO.importToolImage(imagePath, (WToolImage) toolbox.getTool());
  }

  /**
   * Methode permettant d'importer une image fixe en tant que {@code WLayer}
   *
   * @param imagePath Le chemin de l'image a importer
   */
  public void importFixedImage(File imagePath) {

    BufferedImage image =
        WEditorIO.importFixedImage(
            imagePath, this.editorImage.getWidth(), this.editorImage.getHeight());

    if (image == null) EditorModel.LOGGER.error("Import d'image echoue");

    this.importFixedImage(image, imagePath.getName());
  }

  /**
   * Methode permettant d'importer une image fixe en tant que {@code WLayer}
   *
   * @param image L'image a importer
   */
  public void importFixedImage(BufferedImage image) {
    this.importFixedImage(image, null);
  }

  /**
   * Methode permettant d'importer une image fixe en tant que {@code WLayer}
   *
   * @param image L'image a importer
   * @param layerName Le nom du calque
   */
  public void importFixedImage(BufferedImage image, String layerName) {
    // TODO : non hacky way to make image update at start
    ToolType prevTool = this.toolbox.getCurrentToolType();

    this.toolbox.setCurrentToolType(ToolType.ERASER);
    this.startPainting(-20, -20);

    layerModel.addFixedImageLayer(image, layerName);

    this.stopPainting();
    this.toolbox.setCurrentToolType(prevTool);

    this.updateEditorImage();
    this.getController().getObstacleModel().recalculateObstacles();
    this.getPublisher().publish(Subject.ON_EDITOR_PAINTING_DONE, null);
    this.layerModel.onLayerFocusUpdate();
    this.layerModel.onLayerUpdateImage();
  }

  /**
   * Methode permettant d'exporter l'image de l'editeur
   *
   * @param imagePath Le path de l'image
   */
  public void exportImage(File imagePath) {
    WEditorIO.exportImage(imagePath, this.layerModel.getMergedBorderlessImage());
  }

  /**
   * Retourne l'instance de la fenêtre de sélection de couleur afin de pouvoir changer son titre
   *
   * @return L'instance de la fenêtre de sélection de couleur
   */
  public EditorColorPicker getColorPickerFrame() {
    return editorColorPicker;
  }

  /**
   * Setter pour la fenêtre de sélection de couleur de l'éditeur
   *
   * @param newColorPickerFrame La nouvelle fenêtre de sélection de couleur
   */
  public void setColorPicker(EditorColorPicker newColorPickerFrame) {
    editorColorPicker = newColorPickerFrame;
  }

  /**
   * Méthode permettant de changer la visibilité de la fenêtre de sélection de couleur de l'éditeur
   *
   * @param visibility La nouvelle visibilité de la fenêtre
   */
  public void setColorPickerVisibility(boolean visibility) {
    if (editorColorPicker == null) {
      this.editorColorPicker = new EditorColorPicker(getController(), this.toolbox.getColor());
    }
    editorColorPicker.askForColor();
  }

  /** {@inheritDoc} */
  public void setupKeybindSubscriptions() {}

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {
    this.layerModel.getLayerMergeProcessor().stop();
    this.layerModel.softReinitialize();

    toolbox = null;
    editorImage = null;
    shapesDropdownVisibility = EditorConstants.DEFAULT_SHAPES_DROPDOWN_VISIBILITY;

    toolbox = new WToolbox(getController());
    editorColorPicker = null;

    setupLayerObservers();
    retrieveSimulationObstacles();

    updateEditorImage();
  }

  /** {@inheritDoc} */
  @Override
  public BufferedImage getSceneComponent() {
    return this.layerModel.getMergedBorderlessImage();
  }

  /** {@inheritDoc} */
  @Override
  public boolean setSceneComponent(BufferedImage sceneComponent) {
    this.importFixedImage(sceneComponent);
    return true;
  }
}
