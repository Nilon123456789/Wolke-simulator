package com.e24.wolke.frontend.editor;

import com.bric.colorpicker.ColorPickerDialog;
import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.eventsystem.Subject;
import java.awt.Color;

/**
 * Fenêtre contenant le panneau de sélection de couleur de l'application
 *
 * <p>Utilise la librairie {@code com.bric.colorpicker} pour afficher la fenêtre de sélection de
 * couleur. Cette librairie a été modifée à sa source par Nilon123456789 afin de supporter la
 * traduction de l'interface utilisateur. Voir <a
 * href="https://github.com/dheid/colorpicker/pull/55">Pull Request #55</a> pour voir les
 * modifications apportées dans la librairie.
 *
 * @author Nilon123456789
 * @author adrienles
 */
public class EditorColorPicker {

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** La couleur actuel */
  private Color currentColor;

  /**
   * Constructeur de la fenêtre de sélection de couleur
   *
   * @param controller Le {@code Controller} de l'application
   * @param initialColor La couleur initialle que devrait avoir le panneau de sélection de couleur
   *     lorsque la fenêtre est crée/recrée
   */
  public EditorColorPicker(Controller controller, Color initialColor) {
    this.controller = controller;
    currentColor = initialColor;
  }

  /** Méthode qui ouvre une fenêtre de sélection de couleur et qui met à jour la couleur actuelle */
  public void askForColor() {
    Color newColor =
        ColorPickerDialog.showDialog(
            null, currentColor, false, controller.getApplicationModel().getCurrentLocale());

    if (newColor == null) return;

    currentColor = newColor;
    controller.getEditorModel().getToolbox().setColor(currentColor);
    controller.getEditorModel().getPublisher().publish(Subject.EDITOR_COLOR_CHANGE, currentColor);
  }
}
