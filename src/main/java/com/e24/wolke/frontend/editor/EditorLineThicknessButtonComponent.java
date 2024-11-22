package com.e24.wolke.frontend.editor;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import javax.swing.UIManager;

/**
 * Composant de bouton pour choisir l'épaisseur de ligne dans l'éditeur
 *
 * @author adrienles
 */
public class EditorLineThicknessButtonComponent extends EditorButtonComponent
    implements Reinitializable, WEventComponent {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Identifiant des abonnements */
  private int subscriptionID;

  /** Chemins des icônes de l'épaisseur de ligne */
  private String[] thicknessIconPaths = {
    "icons/pencil_size_1.svg",
    "icons/pencil_size_2.svg",
    "icons/pencil_size_3.svg",
    "icons/pencil_size_4.svg",
    "icons/pencil_size_5.svg"
  };

  /** Icônes de l'épaisseur de ligne */
  private FlatSVGIcon[] thicknessIcons = new FlatSVGIcon[this.thicknessIconPaths.length];

  /** Index de l'épaisseur de ligne */
  private int thicknessIndex = 0;

  /** Constructeur par défaut */
  public EditorLineThicknessButtonComponent() {
    super();
  }

  /**
   * Constructeur avec initialisation du {@code Controller}
   *
   * @param controller le {@code Controller} de l'application
   */
  public EditorLineThicknessButtonComponent(Controller controller) {
    super(controller, null, "ui.tooltips.line_thickness", "icons/pencil_size_2.svg", false);
    subscriptionID = hashCode();

    for (int i = 0; i < this.thicknessIconPaths.length; i++) {
      this.thicknessIcons[i] = new FlatSVGIcon(this.thicknessIconPaths[i]);
      this.thicknessIcons[i].setColorFilter(
          new ColorFilter(color -> UIManager.getColor("Button.foreground")));
    }

    addActionListener(e -> changeThickness());
    setupSubscribers();
  }

  /** {@inheritDoc} */
  public void setupSubscribers() {
    super.controller
        .getEditorModel()
        .getSubscriber()
        .subscribeWithID(
            Subject.KEYBIND_EDITOR_LINE_THICKNESS_PRESSED, e -> changeThickness(), subscriptionID);
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
  }

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getEditorModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }

  /** Méthode qui change l'épaisseur de ligne ainsi que l'icône du bouton */
  public void changeThickness() {
    super.controller.getEditorModel().getToolbox().cycleThroughThickness();
    this.thicknessIndex = super.controller.getEditorModel().getToolbox().getCurrentThicknessIndex();
    super.setIcon(this.thicknessIcons[this.thicknessIndex]);
  }
}
