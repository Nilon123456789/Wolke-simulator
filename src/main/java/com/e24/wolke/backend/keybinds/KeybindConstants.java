package com.e24.wolke.backend.keybinds;

import com.e24.wolke.backend.models.application.ApplicationModel;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.EditorModel;
import com.e24.wolke.backend.models.editor.layers.LayerModel;
import com.e24.wolke.backend.models.renderer.RendererModel;
import com.e24.wolke.backend.models.simulation.SimulationModel;
import com.e24.wolke.eventsystem.Subject;
import java.awt.event.KeyEvent;

/**
 * Classe contenant les constantes des raccourcis clavier, soit les combinaisons de touches.
 *
 * @author adrienles
 */
public final class KeybindConstants {

  /** Constructeur privé pour empêcher l'instanciation de la classe */
  private KeybindConstants() {
    throw new IllegalStateException("Utility class");
  }

  /** Le modificateur de touche pour les raccourcis clavier */
  public static final int KEYBIND_MODIFIER;

  static {
    int modifier = KeyEvent.CTRL_DOWN_MASK;
    try {
      if (System.getProperty("os.name").toLowerCase().contains("mac")) {
        modifier = KeyEvent.ALT_DOWN_MASK;
      } else {
        modifier = KeyEvent.CTRL_DOWN_MASK;
      }
    } catch (SecurityException | NullPointerException | IllegalArgumentException e) {
      e.printStackTrace();
    }
    KEYBIND_MODIFIER = modifier;
  }

  /** La liste des éléments de raccourcis clavier */
  public static final KeybindItem[] KEYBIND_ITEMS = {
    new KeybindItem(
        KeyEvent.VK_ESCAPE,
        KeyEvent.KEY_LOCATION_UNKNOWN,
        KeybindScope.GLOBAL,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_ZEN_MODE_EXIT_PRESSED,
        "ui.keybinds.item.zen_mode_exit"),
    new KeybindItem(
        KeyEvent.VK_SPACE,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        SimulationModel.class,
        Subject.KEYBIND_SIMULATION_TOGGLE_PRESSED,
        "ui.keybinds.item.play_pause"),
    new KeybindItem(
        KeyEvent.VK_O,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_IMPORT_SCENE_PRESSED,
        "ui.menu_bar.file.import_scene"),
    new KeybindItem(
        KeyEvent.VK_S,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_EXPORT_SCENE_PRESSED,
        "ui.menu_bar.file.export_scene"),
    new KeybindItem(
        KeyEvent.VK_RIGHT,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        true,
        SimulationModel.class,
        Subject.KEYBIND_SIMULATION_NEXT_FRAME_PRESSED,
        "ui.keybinds.item.next"),
    new KeybindItem(
        KeyEvent.VK_R,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        SimulationModel.class,
        Subject.KEYBIND_SIMULATION_RESET_PRESSED,
        "ui.keybinds.item.reset"),
    new KeybindItem(
        KeyEvent.VK_F,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_ZEN_MODE_TOGGLE_PRESSED,
        "ui.keybinds.item.zen_mode"),
    new KeybindItem(
        KeyEvent.VK_L,
        KEYBIND_MODIFIER,
        KeybindScope.SIMULATION,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_LEGEND_TOGGLE_PRESSED,
        "ui.keybinds.item.legend"),
    new KeybindItem(
        KeyEvent.VK_SLASH,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_SHOW_INSTRUCTIONS_PRESSED,
        "ui.keybinds.item.instructions"),
    new KeybindItem(
        KeyEvent.VK_K,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_SHOW_KEYBIND_MENU_PRESSED,
        "ui.keybinds.item.keybind_menu"),
    new KeybindItem(
        KeyEvent.VK_P,
        KEYBIND_MODIFIER,
        KeybindScope.SIMULATION,
        false,
        ApplicationModel.class,
        Subject.KEYBIND_INSPECTOR_TOGGLE_PRESSED,
        "ui.keybinds.item.inspector"),
    new KeybindItem(
        KeyEvent.VK_1,
        KEYBIND_MODIFIER,
        KeybindScope.EDITOR,
        false,
        EditorModel.class,
        Subject.KEYBIND_TOGGLE_PENCIL_PRESSED,
        "ui.keybinds.item.pencil"),
    new KeybindItem(
        KeyEvent.VK_2,
        KEYBIND_MODIFIER,
        KeybindScope.EDITOR,
        false,
        EditorModel.class,
        Subject.KEYBIND_TOGGLE_ERASER_PRESSED,
        "ui.keybinds.item.eraser"),
    new KeybindItem(
        KeyEvent.VK_N,
        KEYBIND_MODIFIER,
        KeybindScope.EDITOR,
        false,
        LayerModel.class,
        Subject.EDITOR_LAYER_ADD_PRESSED,
        "ui.keybinds.item.add"),
    new KeybindItem(
        KeyEvent.VK_R,
        KEYBIND_MODIFIER,
        KeyEvent.SHIFT_DOWN_MASK,
        KeybindScope.EDITOR,
        false,
        LayerModel.class,
        Subject.KEYBIND_EDITOR_RENAME_PRESSED,
        "ui.keybinds.item.rename"),
    new KeybindItem(
        KeyEvent.VK_BACK_SPACE,
        KEYBIND_MODIFIER,
        KeybindScope.EDITOR,
        false,
        LayerModel.class,
        Subject.EDITOR_LAYER_DELETE_PRESSED,
        "ui.keybinds.item.delete"),
    new KeybindItem(
        KeyEvent.VK_BACK_SPACE,
        KEYBIND_MODIFIER,
        KeyEvent.SHIFT_DOWN_MASK,
        KeybindScope.EDITOR,
        false,
        LayerModel.class,
        Subject.EDITOR_LAYER_CLEAR_PRESSED,
        "ui.keybinds.item.clear"),
    new KeybindItem(
        KeyEvent.VK_D,
        KEYBIND_MODIFIER,
        KeybindScope.EDITOR,
        false,
        LayerModel.class,
        Subject.EDITOR_LAYER_DUPLICATE_PRESSED,
        "ui.keybinds.item.duplicate"),
    new KeybindItem(
        KeyEvent.VK_UP,
        KEYBIND_MODIFIER,
        KeyEvent.SHIFT_DOWN_MASK,
        KeybindScope.EDITOR,
        false,
        LayerModel.class,
        Subject.EDITOR_LAYER_UP_PRESSED,
        "ui.keybinds.item.layer_up"),
    new KeybindItem(
        KeyEvent.VK_DOWN,
        KEYBIND_MODIFIER,
        KeyEvent.SHIFT_DOWN_MASK,
        KeybindScope.EDITOR,
        false,
        LayerModel.class,
        Subject.EDITOR_LAYER_DOWN_PRESSED,
        "ui.keybinds.item.layer_down"),
    new KeybindItem(
        KeyEvent.VK_UP,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        RendererModel.class,
        Subject.KEYBIND_VISUALIZATION_UP_PRESSED,
        "ui.keybinds.item.vis_up"),
    new KeybindItem(
        KeyEvent.VK_DOWN,
        KEYBIND_MODIFIER,
        KeybindScope.GLOBAL,
        false,
        RendererModel.class,
        Subject.KEYBIND_VISUALIZATION_DOWN_PRESSED,
        "ui.keybinds.item.vis_down"),
    new KeybindItem(
        KeyEvent.VK_T,
        KEYBIND_MODIFIER,
        KeybindScope.EDITOR,
        false,
        EditorModel.class,
        Subject.KEYBIND_EDITOR_LINE_THICKNESS_PRESSED,
        "ui.keybinds.item.line_thickness"),
  };

  /**
   * La portée des raccourcis clavier, soit le contexte dans lequel ils sont actifs.
   *
   * @author adrienles
   */
  public enum KeybindScope {
    /** La portée globale, qui est active en tout temps */
    GLOBAL("ui.keybinds.scope.global", 2),

    /** La portée de la simulation, qui est active lors de la simulation */
    SIMULATION("ui.keybinds.scope.simulation", 0),

    /** La portée de l'éditeur, qui est active lors de l'édition */
    EDITOR("ui.keybinds.scope.editor", 1);

    /** L'identifiant pour le nom de la portée */
    private final String localeKey;

    /** L'index de l'onglet {@code ApplicationTab} associé à la portée */
    private final int tabIndex;

    /**
     * Créer une portée de raccourcis clavier
     *
     * @param localeKey L'identifiant pour le nom
     * @param tabIndex L'index de l'onglet
     */
    KeybindScope(String localeKey, int tabIndex) {
      this.localeKey = localeKey;
      this.tabIndex = tabIndex;
    }

    /**
     * Obtenir Le nom localisé de la portée
     *
     * @return Le nom localisé de la portée
     */
    @Override
    public String toString() {
      return LocaleManager.getLocaleResourceBundle().getString(this.localeKey);
    }

    /**
     * Obtenir l'index de l'onglet associé à la portée
     *
     * @return L'index de l'onglet
     */
    public int getTabIndex() {
      return this.tabIndex;
    }
  }
}
