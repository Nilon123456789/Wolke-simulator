package com.e24.wolke.frontend.editor;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.ApplicationConstants;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.frontend.canvas.EditorCanvasPane;
import com.e24.wolke.frontend.help.HelpButtonComponent;
import com.e24.wolke.frontend.input.EditorLayerInputPane;
import com.e24.wolke.utils.interfaces.Reinitializable;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

/**
 * Panneau d'édition
 *
 * @author adrienles
 * @author MeriBouisri
 */
public class EditorPane extends JPanel implements WEventComponent, Reinitializable {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Panneau d'outils de l'éditeur */
  EditorToolPane editorToolPane;

  /** Panneau de dessin de l'éditeur */
  EditorCanvasPane editorCanvasPane;

  /** Composant de bouton déroulant pour le choix de formes */
  EditorButtonDropdownComponent buttonDropdownComponent;

  /** Composant permettant l'affichage et l'intéraction avec les calques de l'éditeur */
  EditorLayerInputPane editorLayerInputPane;

  /** Composant de bouton d'aide pour le panneau d'outils */
  HelpButtonComponent toolPaneHelpButtonComponent;

  /** L'identifiant des abonnements */
  private int subscriptionID;

  /** Espace entre les composants */
  private static final int GAP = 10;

  /** Créer le panneau */
  public EditorPane() {}

  /**
   * Construction d'un {@code EditorPane} avec un {@code Controller}
   *
   * @param controller Le {@code Controller} de l'application
   */
  public EditorPane(Controller controller) {
    this.controller = controller;
    subscriptionID = hashCode();

    setLayout(new GridLayout(0, 1, 0, 0));
    JLayeredPane layeredPane = new JLayeredPane();

    add(layeredPane);
    layeredPane.setLayout(null);

    editorCanvasPane = new EditorCanvasPane(this.controller);
    editorCanvasPane.setBounds(
        0,
        0,
        ApplicationConstants.DEFAULT_WINDOW_SIZE.width,
        ApplicationConstants.DEFAULT_WINDOW_SIZE.height);
    layeredPane.add(editorCanvasPane);

    editorToolPane = new EditorToolPane(this.controller);
    editorToolPane.setBounds(
        (ApplicationConstants.DEFAULT_WINDOW_SIZE.width - editorToolPane.getPreferredSize().width)
            / 2,
        GAP,
        editorToolPane.getPreferredSize().width,
        editorToolPane.getPreferredSize().height);
    layeredPane.setLayer(editorToolPane, JLayeredPane.MODAL_LAYER);
    layeredPane.add(editorToolPane);

    toolPaneHelpButtonComponent = new HelpButtonComponent(controller, "editor_toolpane");
    layeredPane.setLayer(toolPaneHelpButtonComponent, JLayeredPane.MODAL_LAYER);
    layeredPane.add(toolPaneHelpButtonComponent);

    ArrayList<EditorButtonComponent> buttonList = new ArrayList<EditorButtonComponent>();
    buttonList.add(
        new EditorButtonComponent(
            controller,
            new Subject[] {Subject.EDITOR_TOOLPANE_ELLIPSE_PRESSED},
            "ui.tooltips.shapes.circle",
            "icons/circle.svg",
            false));
    buttonList.add(
        new EditorButtonComponent(
            controller,
            new Subject[] {Subject.EDITOR_TOOLPANE_RECTANGLE_PRESSED},
            "ui.tooltips.shapes.rectangle",
            "icons/square.svg",
            false));

    buttonDropdownComponent = new EditorButtonDropdownComponent(controller, buttonList);
    layeredPane.setLayer(buttonDropdownComponent, JLayeredPane.MODAL_LAYER);
    layeredPane.add(buttonDropdownComponent);
    buttonDropdownComponent.setVisible(
        this.controller.getEditorModel().getShapesDropdownVisibility());
    // TODO : Faire en sorte que le positionnement du menu déroulant soit dynamique
    buttonDropdownComponent.setBounds(
        (ApplicationConstants.DEFAULT_WINDOW_SIZE.width
                - buttonDropdownComponent.getPreferredSize().width)
            / 2,
        GAP,
        buttonDropdownComponent.getPreferredSize().width,
        buttonDropdownComponent.getPreferredSize().height);

    editorLayerInputPane = new EditorLayerInputPane(this.controller);
    layeredPane.setLayer(editorLayerInputPane, JLayeredPane.MODAL_LAYER);
    layeredPane.add(editorLayerInputPane);
    editorLayerInputPane.setBounds(
        GAP,
        GAP,
        editorLayerInputPane.getPreferredSize().width,
        editorLayerInputPane.getPreferredSize().height);

    addComponentListener(
        new ComponentAdapter() {
          public void componentResized(ComponentEvent e) {
            handleResize();
          }
        });

    setupSubscribers();
  }

  /** Gestion du redimensionnement du panneau d'outils */
  private void handleToolBarResize() {
    editorToolPane.setBounds(
        (getWidth() - editorToolPane.getPreferredSize().width) / 2,
        GAP,
        editorToolPane.getPreferredSize().width,
        editorToolPane.getPreferredSize().height);
    toolPaneHelpButtonComponent.setBounds(
        editorToolPane.getX() - toolPaneHelpButtonComponent.getPreferredSize().width - GAP / 2,
        GAP,
        toolPaneHelpButtonComponent.getPreferredSize().width,
        toolPaneHelpButtonComponent.getPreferredSize().height);
  }

  /** Gestion du redimensionnement du panneau de dessin de l'éditeur */
  private void handleCanvasPaneResize() {
    editorCanvasPane.setBounds(0, 0, getWidth(), getHeight());
  }

  /** Gestion du redimensionnement du menu déroulant */
  private void handleDropdownResize() {
    buttonDropdownComponent.setBounds(
        editorToolPane.getX()
            + editorToolPane.getPreferredSize().width / 2
            - EditorButtonComponent.BUTTON_SIZE.width / 2
            - GAP / 4,
        GAP + editorToolPane.getPreferredSize().height + GAP / 2,
        buttonDropdownComponent.getPreferredSize().width,
        buttonDropdownComponent.getPreferredSize().height);
  }

  /** Gestion du redimensionnement du panneau de calques */
  private void handleInputLayerPaneResize() {
    editorLayerInputPane.setMaxComponentHeight(getHeight() - 2 * GAP);
    editorLayerInputPane.setBounds(
        (int) (getWidth() - GAP - editorLayerInputPane.getPreferredSize().getWidth()),
        GAP,
        (int) editorLayerInputPane.getPreferredSize().getWidth(),
        editorLayerInputPane.isMinimized()
            ? editorLayerInputPane.getMinimisedHeight()
            : (editorLayerInputPane.getPreferredSize().height < (getHeight() - 2 * GAP)
                ? editorLayerInputPane.getPreferredSize().height
                : (getHeight() - 2 * GAP)));

    editorLayerInputPane.updateUI();
  }

  /** Gestion de la redimension du panneau de l'éditeur */
  private void handleResize() {
    handleToolBarResize();
    handleCanvasPaneResize();
    handleDropdownResize();
    handleInputLayerPaneResize();
  }

  /** {@inheritDoc} */
  @Override
  public void setupSubscribers() {
    controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_TOOLPANE_SHAPES_DROPDOWN_STATE_CHANGED,
            e -> {
              buttonDropdownComponent.setVisible((Boolean) e);
              editorToolPane.selectCurrentTool();
            },
            subscriptionID);
    controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.EDITOR_SELECT_CURRENT_TOOL,
            e -> {
              editorToolPane.selectCurrentTool();
            },
            subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getEditorModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
    editorCanvasPane.reinitialize();
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    editorCanvasPane.reinitialize();
    editorLayerInputPane.reinitialize();
    editorToolPane.reinitialize();
    removeSubscribers();
  }
}
