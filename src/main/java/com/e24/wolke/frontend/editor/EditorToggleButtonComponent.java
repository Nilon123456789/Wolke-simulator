package com.e24.wolke.frontend.editor;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.tools.WToolConstants.ToolType;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.eventsystem.WEventComponent;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Dimension;
import javax.swing.JToggleButton;
import javax.swing.UIManager;

/**
 * Classe EditorButtonComponent qui agence les boutons de l'éditeur
 *
 * @author n-o-o-d-l-e
 */
class EditorToggleButtonComponent extends JToggleButton
    implements Reinitializable, WEventComponent {

  /** Numéro de série de la classe */
  private static final long serialVersionUID = 1L;

  /** Identifiant des abonnements */
  private int subscriptionID;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** Taille du bouton */
  private static final Dimension BUTTON_SIZE = new Dimension(40, 40);

  /** Le {@code Subject} de l'événement que le bouton doit déclencher */
  private Subject subject;

  /** Le {@code ToolType} de l'outil associé au bouton */
  private ToolType toolType;

  /** Constructeur par defaut. */
  public EditorToggleButtonComponent() {
    super();
    this.setButtonSizes();
  }

  /**
   * Constructeur de la classe EditorButtonComponent
   *
   * @param controller le {@code Controller} de l'application
   * @param subject le {@code Subject} de l'événement que le bouton doit déclencher
   * @param toolType le {@code ToolType} de l'outil associé au bouton
   * @param tooltipLocaleKey la clé de localisation du tooltip
   * @param iconPath le chemin de l'icône du bouton
   * @param hideButton le boolean pour cacher le bouton
   */
  public EditorToggleButtonComponent(
      Controller controller,
      Subject subject,
      ToolType toolType,
      String tooltipLocaleKey,
      String iconPath,
      boolean hideButton) {
    super();
    this.controller = controller;
    subscriptionID = hashCode();
    setEnabled(!hideButton);
    this.toolType = toolType;
    if (hideButton) {
      setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.component_disabled"));
    }
    if (!tooltipLocaleKey.isEmpty())
      setToolTipText(LocaleManager.getLocaleResourceBundle().getString(tooltipLocaleKey));
    this.setButtonSizes();
    this.setupPublisher(subject);
    setSelected(this.isCurrentTool() && !hideButton);
    FlatSVGIcon icon = new FlatSVGIcon(iconPath);
    icon.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));
    this.setIcon(icon);
    this.setupSubscribers();
  }

  /**
   * Méthode permettant de déterminer si l'outil associé au bouton est l'outil actuel, afin de
   * pouvoir l'activé quand le bouton est sélectionné au moment de la construction du composant.
   *
   * @return true si l'outil associé au bouton est l'outil actuel, false sinon
   */
  private boolean isCurrentTool() {
    ToolType currentTool = this.controller.getEditorModel().getToolbox().getCurrentToolType();
    if ((currentTool == ToolType.ELLIPSE || currentTool == ToolType.RECTANGLE)
        && toolType == ToolType.ELLIPSE) return true;
    return currentTool == toolType;
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    removeSubscribers();
  }

  /** {@inheritDoc} */
  public void setupSubscribers() {}

  /** {@inheritDoc} */
  @Override
  public void removeSubscribers() {
    controller.getEditorModel().getSubscriber().unsubscribeAllWithID(subscriptionID);
  }

  /** Méthode qui s'occupe de la taille du bouton */
  private void setButtonSizes() {
    setPreferredSize(EditorToggleButtonComponent.BUTTON_SIZE);
    setMinimumSize(EditorToggleButtonComponent.BUTTON_SIZE);
    setMaximumSize(EditorToggleButtonComponent.BUTTON_SIZE);
  }

  /**
   * Méthode qui s'occupe de l'initialisation des abonnements aux événements
   *
   * @param subject le {@code Subject} de l'événement que le bouton doit déclencher
   */
  private void setupPublisher(Subject subject) {
    this.subject = subject;
    addActionListener(
        e -> {
          this.controller.getEditorModel().getPublisher().publish(subject, isSelected());
        });
  }

  /** Retourne la taille préférée du bouton */
  @Override
  public Dimension getPreferredSize() {
    return super.getPreferredSize();
  }

  /**
   * Retourne le {@code ToolType} que le bouton gère
   *
   * @return le {@code ToolType} que le bouton gère
   */
  public ToolType getToolType() {
    return toolType;
  }

  /**
   * Retourne le {@code Subject} de l'événement que le bouton doit déclencher
   *
   * @return le {@code Subject} de l'événement que le bouton doit déclencher
   */
  public Subject getSubject() {
    return subject;
  }
}
