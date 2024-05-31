package com.e24.wolke.frontend.editor;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import com.e24.wolke.backend.models.editor.tools.WToolbox;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;

/**
 * Panneau d'outils de l'éditeur
 *
 * @author n-o-o-d-l-e
 * @author MeriBouisri
 */
public class EditorToolPane extends JPanel implements WEventComponent, Reinitializable {

  /** Numéro de série de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Liste des boutons et séparateurs du panneau d'outils */
  private JComponent[] toolPaneItems = null;

  /** Identifiant des abonnements */
  private int subscriptionID;

  /** Création de la liste des boutons et séparateurs */
  private void createButtonList() {
    this.toolPaneItems =
        new JComponent[] {
          new EditorToggleButtonComponent(
              this.controller,
              Subject.EDITOR_TOOLPANE_IMPORT_PRESSED,
              ToolType.IMAGE,
              "ui.tooltips.image_import",
              "icons/import.svg",
              false),
          new JSeparator(SwingConstants.VERTICAL),
          new EditorToggleButtonComponent(
              this.controller,
              Subject.EDITOR_TOOLPANE_PENCIL_PRESSED,
              ToolType.PENCIL,
              "ui.tooltips.pen",
              "icons/pencil.svg",
              false),
          new EditorToggleButtonComponent(
              this.controller,
              Subject.EDITOR_TOOLPANE_ERASER_PRESSED,
              ToolType.ERASER,
              "ui.tooltips.eraser",
              "icons/eraser.svg",
              false),
          new EditorToggleButtonComponent(
              this.controller,
              Subject.EDITOR_TOOLPANE_SHAPES_PRESSED,
              ToolType.ELLIPSE,
              "ui.tooltips.shapes",
              "icons/shapes.svg",
              false),
          new EditorColorPickerButtonComponent(
              this.controller,
              new Subject[] {Subject.EDITOR_TOOLPANE_COLORPICKER_PRESSED},
              "icons/colorpicker.svg"),
          new JSeparator(SwingConstants.VERTICAL),
          new EditorLineThicknessButtonComponent(this.controller)
        };
  }

  /** Constructeur par defaut. */
  public EditorToolPane() {}

  /**
   * Créer le panneau d'outils de l'éditeur
   *
   * @param controller Le {@code Controller} de l'application
   */
  public EditorToolPane(Controller controller) {
    this.controller = controller;
    subscriptionID = hashCode();

    createButtonList();

    putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");

    setLayout(new MigLayout("ins 5, gap 5", "", ""));

    for (int i = 0; i < this.toolPaneItems.length; i++) {
      add(toolPaneItems[i]);
    }

    setupSubscribers();
  }

  /** Initialiser les abonnements aux événements des différents boutons */
  @Override
  public void setupSubscribers() {
    for (JComponent component : this.toolPaneItems) {
      if (component instanceof EditorToggleButtonComponent) {
        controller
            .getEditorModel()
            .getSubscriber()
            .subscribeWithID(
                ((EditorToggleButtonComponent) component).getSubject(),
                e -> {
                  handleButtonSelected(((EditorToggleButtonComponent) component).getSubject());
                },
                subscriptionID);
      } else if (component instanceof EditorButtonComponent) {
        if (component.getClass().equals(EditorLineThicknessButtonComponent.class)) {
          continue;
        }
        for (Subject subject : ((EditorButtonComponent) component).getSubjects()) {
          controller
              .getEditorModel()
              .getSubscriber()
              .subscribeWithID(
                  subject,
                  e -> {
                    handleButtonSelected(subject);
                  },
                  subscriptionID);
        }
      } else if (component instanceof EditorColorPickerButtonComponent) {
        for (Subject subject : ((EditorColorPickerButtonComponent) component).getSubjects()) {
          controller
              .getEditorModel()
              .getSubscriber()
              .subscribeWithID(
                  subject,
                  e -> {
                    handleButtonSelected(subject);
                  },
                  subscriptionID);
        }
      }
    }
    controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.KEYBIND_TOGGLE_PENCIL_PRESSED,
            msg -> {
              controller
                  .getEditorModel()
                  .getPublisher()
                  .publish(Subject.EDITOR_TOOLPANE_SHAPES_DROPDOWN_STATE_CHANGED, false);
              WToolbox toolbox = controller.getEditorModel().getToolbox();
              this.handleButtonSelected(
                  Subject.EDITOR_TOOLPANE_PENCIL_PRESSED,
                  toolbox.getCurrentToolType() != ToolType.PENCIL);
              toolbox.setCurrentToolType(
                  toolbox.getCurrentToolType() != ToolType.PENCIL
                      ? ToolType.PENCIL
                      : ToolType.NONE);
            },
            this.subscriptionID);
    controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.KEYBIND_TOGGLE_ERASER_PRESSED,
            msg -> {
              controller
                  .getEditorModel()
                  .getPublisher()
                  .publish(Subject.EDITOR_TOOLPANE_SHAPES_DROPDOWN_STATE_CHANGED, false);
              WToolbox toolbox = controller.getEditorModel().getToolbox();
              this.handleButtonSelected(
                  Subject.EDITOR_TOOLPANE_ERASER_PRESSED,
                  toolbox.getCurrentToolType() != ToolType.ERASER);
              toolbox.setCurrentToolType(
                  toolbox.getCurrentToolType() != ToolType.ERASER
                      ? ToolType.ERASER
                      : ToolType.NONE);
            },
            this.subscriptionID);
    controller
        .getApplicationModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.ON_APP_LANGUAGE_LOCALE_CHANGED,
            e -> {
              if (controller.getEditorModel().getColorPickerFrame() == null) {
                this.controller
                    .getEditorModel()
                    .setColorPicker(
                        new EditorColorPicker(
                            this.controller,
                            this.controller.getEditorModel().getToolbox().getColor()));
              }
            },
            subscriptionID);
  }

  /**
   * Gère la désélection des autres boutons lorsqu'un bouton à bascule est activé et déarme l'outil
   *
   * @param subject le {@code Subject} du bouton à activer
   */
  public void handleButtonSelected(Subject subject) {
    if (subject == Subject.EDITOR_TOOLPANE_COLORPICKER_PRESSED) {
      this.controller.getEditorModel().setColorPickerVisibility(true);
      return;
    }
    for (JComponent component : this.toolPaneItems) {
      if (component instanceof EditorToggleButtonComponent) {
        if (((EditorToggleButtonComponent) component).getSubject() != subject) {
          ((EditorToggleButtonComponent) component).setSelected(false);
        }
      }
    }
  }

  /** Sélectionne l'outil actuel. Nécessaire pour retourner à l'état avant le choix annulé */
  public void selectCurrentTool() {
    WToolbox toolbox = controller.getEditorModel().getToolbox();
    ToolType currentTool = toolbox.getCurrentToolType();
    for (JComponent component : this.toolPaneItems) {
      if (component instanceof EditorToggleButtonComponent) {
        if (currentTool == ToolType.RECTANGLE
            && ((EditorToggleButtonComponent) component).getToolType() == ToolType.ELLIPSE) {
          ((EditorToggleButtonComponent) component).setSelected(true);
          continue;
        }
        if (((EditorToggleButtonComponent) component).getToolType() == currentTool) {
          ((EditorToggleButtonComponent) component).setSelected(true);
        } else {
          ((EditorToggleButtonComponent) component).setSelected(false);
        }
      }
    }
  }

  /**
   * Gère la désélection des autres boutons lorsqu'un bouton à bascule est activé et déarme l'outil
   *
   * @param subject le {@code Subject} du bouton à activer
   * @param selected l'état du bouton
   */
  public void handleButtonSelected(Subject subject, boolean selected) {
    if (subject == Subject.EDITOR_TOOLPANE_COLORPICKER_PRESSED) {
      this.controller.getEditorModel().setColorPickerVisibility(true);
      return;
    }
    for (JComponent component : this.toolPaneItems) {
      if (component instanceof EditorToggleButtonComponent) {
        if (((EditorToggleButtonComponent) component).getSubject() != subject) {
          ((EditorToggleButtonComponent) component).setSelected(false);
        } else {
          ((EditorToggleButtonComponent) component).setSelected(selected);
        }
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
    for (JComponent component : this.toolPaneItems) {
      if (component instanceof Reinitializable) {
        ((Reinitializable) component).reinitialize();
      }
    }
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getApplicationModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
    controller.getEditorModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }
}
