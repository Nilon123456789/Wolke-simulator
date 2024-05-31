package com.e24.wolke.backend.keybinds;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.models.WModel;
import com.e24.wolke.backend.models.renderer.RendererConstants;
import java.awt.KeyboardFocusManager;

/**
 * Modèle de gestion des raccourcis clavier
 *
 * @author n-o-o-d-l-e
 */
public class KeybindModel extends WModel {

  /** Construction d'un {@code KeybindModel} avec parametres par defaut, sans {@code Controller}. */
  public KeybindModel() {
    this(null);
  }

  /**
   * Construction d'un {@code KeybindModel} avec paramètres par défaut.
   *
   * @param controller Le {@code Controller} de l'application
   */
  public KeybindModel(Controller controller) {
    this(controller, KeybindConstants.KEYBIND_ITEMS);
  }

  /**
   * Construction d'un {@code KeybindModel} avec un {@code Controller} et les différents événements
   * de clavier à gérer.
   *
   * @param controller Le {@code Controller} de l'application
   * @param keybindItems Les éléments de raccourcis clavier à gérer
   */
  public KeybindModel(Controller controller, KeybindItem[] keybindItems) {
    super(controller);
    if (RendererConstants.USE_OPENGL) {
      return;
    }
    setupKeybindSubscriptions();

    KeyboardFocusManager keyManager;
    keyManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    for (KeybindItem keybindItem : keybindItems) {
      keybindItem.setController(controller);
      keybindItem.setupAction(keyManager);
    }
  }

  /** {@inheritDoc} */
  public void setupKeybindSubscriptions() {}

  /** {@inheritDoc} */
  @Override
  public boolean setup() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public void softReinitialize() {}
}
