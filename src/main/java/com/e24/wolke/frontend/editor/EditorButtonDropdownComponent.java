package com.e24.wolke.frontend.editor;

import com.e24.wolke.backend.controller.Controller;
import com.formdev.flatlaf.FlatClientProperties;
import java.util.ArrayList;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Classe EditorButtonDropdownComponent qui agence les listes des boutons de l'éditeur
 *
 * @author adrienles
 */
public class EditorButtonDropdownComponent extends JPanel {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Nombre maximum de boutons par ligne */
  private int MAX_BUTTONS_PER_ROW = 3;

  /** Constructeur par defaut. */
  public EditorButtonDropdownComponent() {
    super();
  }

  /**
   * Constructeur de la classe EditorButtonDropdownComponent
   *
   * @param controller le {@code Controller} de l'application
   * @param buttonList la liste des boutons {@code EditorButtonComponent} à afficher
   */
  public EditorButtonDropdownComponent(
      Controller controller, ArrayList<EditorButtonComponent> buttonList) {
    super();
    putClientProperty(FlatClientProperties.STYLE_CLASS, "layeredPanel");
    setLayout(new MigLayout("ins 5, gap 5", "", ""));
    if (buttonList.size() <= MAX_BUTTONS_PER_ROW) {
      for (EditorButtonComponent button : buttonList) {
        button.addActionListener(
            e -> controller.getEditorModel().setShapesDropdownVisibility(false));
        add(button);
      }
    } else {
      // 3 boutons par ligne
      for (int i = 0; i < buttonList.size(); i++) {
        // Si on est à la fin de la ligne, on passe à la ligne suivante
        if ((i + 1) % MAX_BUTTONS_PER_ROW == 0 && (i != buttonList.size() - 1)) {
          add(buttonList.get(i), "wrap");
        } else {
          add(buttonList.get(i));
        }
      }
    }
    // TODO : Faire en sorte que les boutons soient centrés dans le cas de lignes non complètes
  }
}
