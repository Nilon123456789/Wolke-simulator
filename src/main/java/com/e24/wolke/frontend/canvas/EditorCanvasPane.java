package com.e24.wolke.frontend.canvas;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.filesystem.WFileSystemConstant;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JFrame;

/**
 * Panneau de l'éditeur de simulation
 *
 * @author n-o-o-d-l-e
 * @author MeriBouisri
 */
public class EditorCanvasPane extends AbstractCanvasPane {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Image de l'éditeur */
  private BufferedImage image;

  /**
   * Constructeur du panneau de l'éditeur de simulation
   *
   * @param controller Le {@code Controller} de l'application
   */
  public EditorCanvasPane(Controller controller) {
    super(controller);
    subscriptionID = hashCode();
    this.image =
        new BufferedImage(
            this.simulationResolution[0],
            this.simulationResolution[1],
            BufferedImage.TYPE_INT_ARGB);
    for (int i = 0; i < this.simulationResolution[0]; i++) {
      for (int j = 0; j < this.simulationResolution[1]; j++) {
        this.image.setRGB(i, j, Color.WHITE.getRGB());
      }
    }
    addMouseListener(
        new MouseAdapter() {
          @Override
          public void mousePressed(MouseEvent e) {
            handleDropdown();
            onStartPainting(e);
          }

          @Override
          public void mouseReleased(MouseEvent e) {
            onStopPainting(e);
          }
        });
    addMouseMotionListener(
        new MouseMotionAdapter() {
          @Override
          public void mouseDragged(MouseEvent e) {
            onKeepPainting(e);
          }
        });
  }

  /** {@inheritDoc} */
  @Override
  protected void initialize() {
    super.initialize();
  }

  /** Methode a invoquer lorsque le bouton de defaite est clique */
  private void onUndo() {
    controller.getEditorModel().undo();
  }

  /** Methode a invoquer lorsque le bouton de refaite est clique */
  private void onRedo() {
    controller.getEditorModel().redo();
  }

  /**
   * Methode a invoquer lorsque l'utiliateur commence a dessiner
   *
   * @param e {@code MouseEvent}
   */
  private void onStartPainting(MouseEvent e) {
    int[] coords = getScaledPixel(e.getX(), e.getY());

    controller.getEditorModel().startPainting(coords[0], coords[1]);
    repaint();
  }

  /**
   * Methode a invoquer au cours du dessin de l'utilisateur
   *
   * @param e {@code MouseEvent}
   */
  private void onKeepPainting(MouseEvent e) {
    int[] coords = getScaledPixel(e.getX(), e.getY());

    controller.getEditorModel().keepPainting(coords[0], coords[1]);
    repaint();
  }

  /**
   * Methode a invoquer a la fin du dessin
   *
   * @param e {@code MouseEvent}
   */
  private void onStopPainting(MouseEvent e) {
    controller.getEditorModel().stopPainting();
    if (controller.getEditorModel().getToolbox().getCurrentToolType() == ToolType.IMAGE) {
      controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.NONE);
      controller.getEditorModel().getPublisher().publish(Subject.EDITOR_SELECT_CURRENT_TOOL, null);
    }
    controller.getEditorModel().getPublisher().publish(Subject.EDITOR_LAYER_CHANGED, null);
    repaint();

    controller
        .getEditorModel()
        .getLayerModel()
        .getPublisher()
        .publish(Subject.EDITOR_LAYER_PREVIEW_REFRESH, null);
  }

  /**
   * Dessin du panneau de l'éditeur de simulation
   *
   * @param g Contexte graphique du panneau
   */
  @Override
  public void paintComponent(Graphics g) {
    if (controller.getEditorModel().getToolbox().isPainting()) return;
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;

    if (!this.isInitialized) {
      initialize();
    }

    g2d.drawImage(
        this.controller.getRendererModel().getSimulationImage(), this.matSimulation, null);
    g2d.drawImage(this.controller.getEditorModel().getEditorImage(), this.matSimulation, null);
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    super.controller
        .getRendererModel()
        .getSubscriber()
        .subscribeWithID(Subject.ON_BUFFER_IMAGE_DONE, msg -> repaint(), subscriptionID);
    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(Subject.ON_EDITOR_PAINTING_DONE, msg -> repaint(), subscriptionID);

    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_TOOLPANE_IMPORT_PRESSED,
            msg -> onImportPressed((Boolean) msg),
            subscriptionID);

    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_TOOLPANE_PENCIL_PRESSED,
            msg -> onPencilPressed((Boolean) msg),
            subscriptionID);
    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_TOOLPANE_ERASER_PRESSED,
            msg -> onEraserPressed((Boolean) msg),
            subscriptionID);

    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_TOOLPANE_SHAPES_PRESSED,
            msg -> onShapesPressed((Boolean) msg),
            subscriptionID);

    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(Subject.EDITOR_TOOLPANE_UNDO_PRESSED, msg -> onUndo(), subscriptionID);

    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(Subject.EDITOR_TOOLPANE_REDO_PRESSED, msg -> onRedo(), subscriptionID);

    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_TOOLPANE_ELLIPSE_PRESSED, msg -> onEllipsePressed(), subscriptionID);

    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_TOOLPANE_RECTANGLE_PRESSED, msg -> onRectanglePressed(), subscriptionID);
  }

  // TODO : Revoir la structure de les handlers pour les boutons. Déplacer dans
  // EditorPane?

  /**
   * Methode a invoquer lorsque l'utilisateur clique sur tout bouton afin de s'assurer que le menu
   * déroulant des formes soit caché
   */
  private void handleDropdown() {
    this.controller.getEditorModel().setShapesDropdownVisibility(false);
  }

  /**
   * Methode a invoquer lorsque l'utilisateur clique sur le bouton d'importation d'image
   *
   * @param state Etat du bouton
   */
  private void onImportPressed(Boolean state) {
    handleDropdown();
    ToolType previousTool = controller.getEditorModel().getToolbox().getCurrentToolType();
    if (!state) {
      super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.NONE);
      return;
    }
    super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.IMAGE);
    super.controller
        .getEditorModel()
        .getPublisher()
        .publish(Subject.EDITOR_SELECT_CURRENT_TOOL, null);
    FileDialog dialog =
        new FileDialog(
            new JFrame(),
            LocaleManager.getLocaleResourceBundle().getString("ui.editor.image_file_picker.title"),
            FileDialog.LOAD);
    dialog.setDirectory(
        WFileSystemConstant.SAVE_PATH
            + ApplicationConstants.SAMPLE_IMAGES_FOLDER_SUBDIR
            + File.separator);
    dialog.setFile("*.svg;*.png");
    dialog.setVisible(true);
    boolean noImageSelected = false;
    File imageFile = null;
    try {
      imageFile = dialog.getFiles()[0];
    } catch (Exception e) {
      noImageSelected = true;
    }
    if (noImageSelected) {
      super.controller.getEditorModel().getToolbox().setCurrentToolType(previousTool);
      super.controller
          .getEditorModel()
          .getPublisher()
          .publish(Subject.EDITOR_SELECT_CURRENT_TOOL, null);

      controller
          .getEditorModel()
          .getPublisher()
          .publish(Subject.EDITOR_TOOLPANE_IMPORT_FAILED, null);
      return;
    }
    this.controller.getEditorModel().importToolImage(imageFile);
  }

  /**
   * Méthode appelée lorsqu'on appuie sur le bouton de crayon
   *
   * @param state Etat du bouton
   */
  private void onPencilPressed(Boolean state) {
    if (!state) {
      super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.NONE);
      return;
    }
    super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.PENCIL);
    handleDropdown();
  }

  /**
   * Méthode appelée lorsqu'on appuie sur le bouton de gomme
   *
   * @param state Etat du bouton
   */
  private void onEraserPressed(Boolean state) {
    if (!state) {
      super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.NONE);
      return;
    }
    super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.ERASER);
    handleDropdown();
  }

  /**
   * Méthode appelée lorsqu'on appuie sur le bouton de formes geometriques
   *
   * @param state Etat du bouton
   */
  private void onShapesPressed(Boolean state) {
    if (!state) {
      super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.NONE);
      super.controller.getEditorModel().setShapesDropdownVisibility(false);

      return;
    }
    super.controller.getEditorModel().setShapesDropdownVisibility(true);
  }

  /** Methode a invoquer lorsque l'utilisateur clique sur le bouton ellipse */
  private void onEllipsePressed() {
    super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.ELLIPSE);
    handleDropdown();
  }

  /** Methode a invoquer lorsque l'utilisateur clique sur le bouton rectangle */
  private void onRectanglePressed() {
    super.controller.getEditorModel().getToolbox().setCurrentToolType(ToolType.RECTANGLE);
    handleDropdown();
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    super.controller.getEditorModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
    super.controller.getRendererModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }
}
