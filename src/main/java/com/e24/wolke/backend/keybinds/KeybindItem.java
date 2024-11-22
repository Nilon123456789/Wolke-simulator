package com.e24.wolke.backend.keybinds;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.backend.keybinds.KeybindConstants.KeybindScope;
import com.e24.wolke.backend.models.WModel;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.editor.EditorModel;
import com.e24.wolke.backend.models.editor.layers.LayerModel;
import com.e24.wolke.eventsystem.Subject;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

/**
 * La classe {@code KeybindItem} regroupe le {@code KeybindScope} et le {@code Subject} qui doivent
 * être liés à un raccourci clavier. Un nom localisé est également associé à l'élément.
 *
 * @author adrienles
 */
public class KeybindItem {

  /** Le {@code KeybindScope} de l'élément */
  private final KeybindConstants.KeybindScope scope;

  /** Le {@code Controller} de l'application */
  private Controller controller;

  /** La classe du modèle de l'élément */
  private final Class<?> modelClass;

  /** Le {@code Subject} de l'élément */
  private final Subject subject;

  /** Le nom localisé de l'élément */
  private final String localeKey;

  /** L'état d'appui de la touche du raccourci clavier */
  private boolean state = false;

  /** L'événement de touche du raccourci clavier */
  private final int keyEvent;

  /** L'événement de touche modificateur du raccourci clavier */
  private final int modifierKeyEvent;

  /**
   * L'événement de touche modificateur du raccourci clavier si un second modificateur est
   * nécessaire
   */
  private final int secondModifierKeyEvent;

  /** Si le raccourci clavier doit être déclenché de manière répétée */
  private final boolean fireRepeatedly;

  /**
   * Créer un élément de raccourci clavier avec deux modificateurs
   *
   * @param keyEvent L'événement de touche du raccourci clavier
   * @param modifierKeyEvent L'événement de touche modificateur du raccourci clavier
   * @param secondModifierKeyEvent L'événement de touche modificateur du raccourci clavier si un
   *     second modificateur est nécessaire
   * @param scope Le {@code KeybindScope} de l'élément
   * @param fireRepeatedly Si le raccourci clavier doit être déclenché de manière répétée
   * @param modelClass La classe du modèle de l'élément
   * @param subject Le {@code Subject} de l'élément
   * @param localeKey Le nom localisé de l'élément
   */
  public KeybindItem(
      int keyEvent,
      int modifierKeyEvent,
      int secondModifierKeyEvent,
      KeybindConstants.KeybindScope scope,
      boolean fireRepeatedly,
      Class<?> modelClass,
      Subject subject,
      String localeKey) {
    this.keyEvent = keyEvent;
    this.modifierKeyEvent = modifierKeyEvent;
    this.secondModifierKeyEvent = secondModifierKeyEvent;
    this.scope = scope;
    this.modelClass = modelClass;
    this.subject = subject;
    this.localeKey = localeKey;
    this.fireRepeatedly = fireRepeatedly;
  }

  /**
   * Créer un élément de raccourci clavier avec un modificateur
   *
   * @param keyEvent L'événement de touche du raccourci clavier
   * @param modifierKeyEvent L'événement de touche modificateur du raccourci clavier
   * @param scope Le {@code KeybindScope} de l'élément
   * @param fireRepeatedly Si le raccourci clavier doit être déclenché de manière répétée
   * @param modelClass La classe du modèle de l'élément
   * @param subject Le {@code Subject} de l'élément
   * @param localeKey Le nom localisé de l'élément
   */
  public KeybindItem(
      int keyEvent,
      int modifierKeyEvent,
      KeybindConstants.KeybindScope scope,
      boolean fireRepeatedly,
      Class<?> modelClass,
      Subject subject,
      String localeKey) {
    this(
        keyEvent,
        modifierKeyEvent,
        KeyEvent.VK_UNDEFINED,
        scope,
        fireRepeatedly,
        modelClass,
        subject,
        localeKey);
  }

  /**
   * Configurer le {@code Controller} de l'application
   *
   * @param controller Le {@code Controller} de l'application
   */
  public void setController(Controller controller) {
    this.controller = controller;
  }

  /**
   * Configurer l'action du raccourci clavier
   *
   * @param keyManager Le gestionnaire de focus clavier
   */
  public void setupAction(KeyboardFocusManager keyManager) {
    if (this.subject == null) {
      return;
    }
    keyManager.addKeyEventDispatcher(
        e -> {
          if (scope.getTabIndex() != KeybindScope.GLOBAL.getTabIndex()) {
            if (controller.getApplicationModel().getCurrentTab().getIndex() != scope.getTabIndex())
              return false;
          }
          if (e.getID() == KeyEvent.KEY_PRESSED) {
            if (e.getKeyCode() == keyEvent
                && e.getModifiersEx() == (modifierKeyEvent | secondModifierKeyEvent)) {
              if (!fireRepeatedly) {
                if (state == true) return true;
              }
              state = true;
              publishEventToRespectiveModel();
              return true;
            }
          }
          if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (e.getKeyCode() == keyEvent
                && e.getModifiersEx() == (modifierKeyEvent | secondModifierKeyEvent)) {
              state = false;
              return true;
            }
          }

          return false;
        });
  }

  /** Publier l'événement à son modèle respectif */
  private void publishEventToRespectiveModel() {
    WModel[] models = controller.getModels();

    for (WModel model : models) {
      if (modelClass == LayerModel.class) {
        if (model.getClass().getName().equals(EditorModel.class.getName())) {
          ((EditorModel) model).getLayerModel().getPublisher().publish(subject, null);
          return;
        }
      }
      if (model.getClass().getName().equals(modelClass.getName())) {
        model.getPublisher().publish(subject, null);
        return;
      }
    }
  }

  /**
   * Obtenir le {@code KeybindScope} du raccourci clavier
   *
   * @return Le {@code KeybindScope} du raccourci clavier
   */
  public KeybindConstants.KeybindScope getScope() {
    return scope;
  }

  /**
   * Obtenir le {@code Subject} du raccourci clavier
   *
   * @return Le {@code Subject} du raccourci clavier
   */
  public Subject getSubject() {
    return subject;
  }

  /**
   * Obtenir le nom localisé du raccourci clavier
   *
   * @return Le nom localisé du raccourci clavier
   */
  @Override
  public String toString() {
    return LocaleManager.getLocaleResourceBundle().getString(localeKey);
  }

  /**
   * Obtenir l'état d'appui de la touche du raccourci clavier
   *
   * @return L'état d'appui de la touche du raccourci clavier
   */
  public boolean getState() {
    return state;
  }

  /**
   * Obtenir une chaîne de caractères représentant le raccourci clavier
   *
   * @return Une chaîne de caractères représentant le raccourci clavier
   */
  public String getKeybindString() {
    if (modifierKeyEvent == 0) {
      return KeyEvent.getKeyText(keyEvent);
    } else {
      String modifierString =
          KeyEvent.getModifiersExText(modifierKeyEvent | secondModifierKeyEvent);
      modifierString = modifierString.replace("+", " + ");
      return modifierString + " + " + KeyEvent.getKeyText(keyEvent);
    }
  }
}
