package com.e24.wolke.frontend.editor;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.eventsystem.Subject;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGIcon.ColorFilter;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.UIManager;

/**
 * Classe EditorButtonComponent qui agence les boutons de l'éditeur
 *
 * @author n-o-o-d-l-e
 */
class EditorButtonComponent extends JButton {

  /** Numéro de série de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code Controller} de l'application */
  protected Controller controller;

  /** Taille du bouton */
  public static final Dimension BUTTON_SIZE = new Dimension(40, 40);

  /** Les {@code Subject} des événements que le bouton doit déclencher */
  private Subject[] subjects;

  /** L'icône du bouton */
  protected FlatSVGIcon icon;

  /** Constructeur par defaut. */
  public EditorButtonComponent() {
    super();
    setButtonSizes();
  }

  /**
   * Constructeur de la classe EditorButtonComponent
   *
   * @param controller le {@code Controller} de l'application
   * @param subjects les {@code Subject} des événements que le bouton doit déclencher
   * @param tooltipLocaleKey la clé de localisation du tooltip
   * @param iconPath le chemin de l'icône du bouton
   * @param hideButton le boolean pour cacher le bouton
   */
  public EditorButtonComponent(
      Controller controller,
      Subject[] subjects,
      String tooltipLocaleKey,
      String iconPath,
      boolean hideButton) {
    super();
    this.controller = controller;
    setEnabled(!hideButton);
    if (hideButton) {
      setToolTipText(
          LocaleManager.getLocaleResourceBundle().getString("ui.tooltips.component_disabled"));
    }
    if (!tooltipLocaleKey.isEmpty())
      setToolTipText(LocaleManager.getLocaleResourceBundle().getString(tooltipLocaleKey));
    setButtonSizes();
    setupPublisher(subjects);
    this.icon = new FlatSVGIcon(iconPath);
    this.icon.setColorFilter(new ColorFilter(color -> UIManager.getColor("Button.foreground")));
    setIcon(this.icon);
  }

  /** Méthode qui s'occupe de la taille du bouton */
  private void setButtonSizes() {
    setPreferredSize(EditorButtonComponent.BUTTON_SIZE);
    setMinimumSize(EditorButtonComponent.BUTTON_SIZE);
    setMaximumSize(EditorButtonComponent.BUTTON_SIZE);
  }

  /**
   * Méthode qui s'occupe de changer l'icône du bouton
   *
   * @param icon l'icône du bouton
   */
  protected void setIcon(FlatSVGIcon icon) {
    super.setIcon(icon);
  }

  /**
   * Méthode qui s'occupe de l'initialisation des abonnements aux événements
   *
   * @param subjects les {@code Subject} des événements que le bouton doit déclencher
   */
  private void setupPublisher(Subject[] subjects) {
    if (subjects == null) {
      return;
    }
    this.subjects = subjects;
    for (Subject subject : this.subjects) {
      addActionListener(
          e -> this.controller.getEditorModel().getPublisher().publish(subject, true));
    }
  }

  /** Retourne la taille préférée du bouton */
  @Override
  public Dimension getPreferredSize() {
    return super.getPreferredSize();
  }

  /**
   * Retourne les {@code Subject} des événements que le bouton doit déclencher
   *
   * @return les {@code Subject} des événements que le bouton doit déclencher
   */
  public Subject[] getSubjects() {
    return subjects;
  }
}
