package com.e24.wolke.eventsystem;

import com.e24.wolke.backend.models.application.ApplicationConstants.*;
import com.e24.wolke.utils.logger.WLogEvent;
import java.awt.Color;
import java.util.Locale;

/**
 * La classe enum {@code Subject} regroupe les sujets/evenements que le {@code EventPublisher} peut
 * diffuser et auquels les {@code EventSubscribers} peuvent s'abonner. Chaque element de {@code
 * Subject} possède un champ {@code messageType} permettant de definir le type d'objet que les
 * {@code EventPublisher} ont le droit de diffuser pour un {@code Subject} donné.
 *
 * <p>Le système de diffusion/abonnement opère selon le contrat que le {@code message} diffusé par
 * un {@code EventPublisher} et le type de paramètre accepté par un {@code Consumer} abonné par un
 * {@code EventSubscriber} respectent tous deux le typage defini par le {@code
 * Subject#getMessageType()} d'un {@code Subject} donné.
 *
 * @author MeriBouisri
 * @author Nilon123456789
 */
public enum Subject {

  /**
   * Sujet a diffuser pour avertir que la fenetre d'introduction a ete fermee. Le type du message
   * est {@code null}.
   */
  ON_INTRO_FRAME_CLOSED(null),

  /** Sujet a diffuser pour avertir que le rendu est fini. Le type du message est {@code null}. */
  ON_RENDERING_DONE(null),

  /**
   * Sujet a diffuser pour avertir que le dessin du rendu est fini. Le type du message est {@code
   * null}.
   */
  ON_BUFFER_IMAGE_DONE(null),

  /**
   * Sujet a diffuser pour avertir que le dessin de l'editeur est fini. Le type du message est
   * {@code null}.
   */
  ON_EDITOR_PAINTING_DONE(null),

  // ============= UI RELATED SUBJECTS ============== //

  /**
   * Sujet à diffuser pour avertir que le panneau d'entrées à été minimisé. Le type du message est
   * {@code boolean}.
   */
  ON_UI_INPUT_PANE_MINIMIZED(Boolean.class),

  // ========== APPLICATION MODEL SUBJECTS ========== //

  /**
   * Sujet a diffuser pour avertir que la localisation de l'application a ete modifiee. Le type du
   * message est {@code Locale}.
   */
  ON_APP_LANGUAGE_LOCALE_CHANGED(Locale.class),

  /**
   * Sujet a diffuser pour avertir que la resolution de l'application a ete modifiee. Le type du
   * message est {@code Resolution}.
   */
  ON_APP_RESOLUTION_CHANGED(Resolution.class),

  /**
   * Sujet a diffuser pour avertir que le mode de couleur de l'application a ete modifie. Le type du
   * message est {@code ColorMode}.
   */
  ON_APP_COLORMODE_CHANGED(ColorMode.class),

  /**
   * Sujet a diffuser pour avertir que la visibilité des boutons d'aide à été modifiée. Le type du
   * message est {@code Boolean}.
   */
  ON_APP_HELP_BUTTON_VISIBILITY_CHANGED(Boolean.class),

  /**
   * Sujet a diffuser lors de la reinitialisation de l'application. Le type du message est {@code
   * null}.
   */
  ON_APP_REINITIALIZE(null),

  /**
   * Sujet a diffuser pour avertir que la visibilité de la légende a ete modifiee. Le type du
   * message est {@code Boolean}.
   */
  ON_APP_LEGEND_VISIBILITY_CHANGED(Boolean.class),

  /**
   * Sujet a diffuser pour avertir que la visibilité de la console a ete modifiee. Le type du
   * message est {@code Boolean}.
   */
  ON_APP_CONSOLE_VISIBILITY_CHANGED(Boolean.class),

  /**
   * Sujet a diffuser pour avertir que la visibilité du mode zen a ete modifiee. Le type du message
   * est {@code Boolean}.
   */
  ON_APP_ZEN_MODE_VISIBILITY_CHANGED(Boolean.class),

  /**
   * Sujet a diffuser pour avertir que le mode de simulation a ete modifie. Le type du message est
   * {@code ApplicationTab}.
   */
  ON_APP_TAB_CHANGED(ApplicationTab.class),

  /**
   * Sujet a diffuser pour avertir que la visibilité de l'inspecteur a ete modifiee. Le type du
   * message est {@code Boolean}.
   */
  ON_APP_INSPECTOR_VISIBILITY_CHANGED(Boolean.class),

  /**
   * Sujet a diffuser pour avertir qu'une scène vient d'être chargée. Le type du message est {@code
   * null}.
   */
  ON_APP_SCENE_LOADED(null),

  /**
   * Sujet a diffuser pour avertir que le bouton d'aide d'un composant a été appuyé. Le type du
   * message est {@code String}, soit la clé de la page à laquelle on veut naviguer.
   */
  ON_COMPONENT_HELP_BUTTON_PRESSED(String.class),

  /**
   * Sujet a diffuser pour avertir que le {@code SimulationCanvasPane} a ete clique. Le type du
   * message est {@code Integer}.
   */
  ON_SIMULATION_CANVAS_PANE_CLICKED(Integer[].class),

  /**
   * Sujet a diffuser pour avertir que le theme de l'application a ete modifie. Le type du message
   * est {@code String}.
   */
  ON_APP_THEME_CHANGED(String.class),

  // ========== SIMULATION MODEL SUBJECTS ========== //

  /**
   * Sujet à diffuser lors de la modification du paramètre de la simulation visualisé. Le type du
   * message est {@code VisualizationType}.
   */
  ON_SIMULATION_VISUALIZATION_TYPE_CHANGED(null),

  /**
   * Sujet a diffuser lorsque la simulation à été réinitialisée. Le type du message est {@code
   * null}.
   */
  ON_SIMULATION_RESTARTED(null),

  /**
   * Sujet a diffuser pour avertir que l'état de la simulation a été modifié. Le type du message est
   * {@code Boolean}.
   */
  ON_SIMULATION_STATE_CHANGED(Boolean.class),

  /**
   * Sujet à diffuser afin d'avertir que la position du panel de l'inspecteur devrait être mise à
   * jour après le redimensionnement de la fenêtre. Le type du message est {@code null}.
   */
  INSPECTOR_REPOSITION_NEEDED(null),

  // ========== RENDERER MODEL SUBJECTS ========== //

  /**
   * Sujet a diffuser pour avertir que le minimum et le maximum des valeurs de la simulation ont été
   * modifiés. Le type du message est {@code double[]}.
   */
  // TODO : Souscription directe dans SimulationLegendPane, mais envoie des
  // erreurs en ce moment
  ON_SIMULATION_MINMAX_CHANGED(double[].class),

  /**
   * Sujet a diffuser pour avertir que les couleurs du dégradé ont été modifiées. Le type du message
   * est {@code float[][]}.
   */
  ON_SIMULATION_GRADIENT_COLORS_CHANGED(float[][].class),

  /**
   * Sujet a diffuser pour avertir que la visualisation en nuances de gris a été modifiée. Le type
   * du message est {@code null}.
   */
  ON_GRAYSCALE_VISUALISATION_CHANGED(null),

  /**
   * Sujet a diffuser pour avertir que la barre à outils devrait actualiser sa sélection d'outils.
   * Le type du message est {@code null}.
   */
  EDITOR_SELECT_CURRENT_TOOL(null),

  /**
   * Sujet à diffuser pour avertir que le bouton annuler a été appuyé. Le type du message est {@code
   * null}.
   */
  EDITOR_TOOLPANE_UNDO_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que le bouton refaire a été appuyé. Le type du message est {@code
   * null}.
   */
  EDITOR_TOOLPANE_REDO_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que le bouton importer a été appuyé. Le type du message est
   * {@code null}.
   */
  EDITOR_TOOLPANE_IMPORT_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que l'importation d'image a échouée. Le type du message est
   * {@code null}.
   */
  EDITOR_TOOLPANE_IMPORT_FAILED(null),

  /**
   * Sujet à diffuser pour avertir que le bouton crayon a été appuyé. Le type du message est {@code
   * null}.
   */
  EDITOR_TOOLPANE_PENCIL_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que le bouton gomme a été appuyé. Le type du message est {@code
   * null}.
   */
  EDITOR_TOOLPANE_ERASER_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que le bouton formes a été appuyé. Le type du message est {@code
   * null}.
   */
  EDITOR_TOOLPANE_SHAPES_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que le bouton ellipse a été appuyé. Le type du message est {@code
   * null}.
   */
  EDITOR_TOOLPANE_ELLIPSE_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que le bouton rectangle a été appuyé. Le type du message est
   * {@code null}.
   */
  EDITOR_TOOLPANE_RECTANGLE_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que l'état d'affichage du menu déroulant des formes a été
   * modifié. Le type du message est {@code Boolean}.
   */
  EDITOR_TOOLPANE_SHAPES_DROPDOWN_STATE_CHANGED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que le bouton choix de couleur a été appuyé. Le type du message
   * est {@code null}.
   */
  EDITOR_TOOLPANE_COLORPICKER_PRESSED(Boolean.class),

  /**
   * Sujet à diffuser pour avertir que la couleur de l'éditeur a été modifiée. Le type du message
   * est {@code Color}.
   */
  EDITOR_COLOR_CHANGE(Color.class),

  /** Sujet a diffuser pour avertir qu'un {@code WLayer} a change */
  EDITOR_LAYER_UPDATE_IMAGE(null),

  /** Sujet a diffuser lorsque les donnees sauvegardees du {@code ApplicationModel} sont chargees */
  ON_LOAD_APPLICATION_MODEL(null),

  /** Sujet a diffuser lorsque les donnees sauvegardees du {@code EditorModel} sont chargees */
  ON_LOAD_EDITOR_MODEL(null),

  /** Sujet a diffuser lorsque les donnees sauvegardees du {@code EditorModel} sont chargees */
  ON_LOAD_SIMULATION_MODEL(null),

  /** Sujet a diffuser lorsque les donnees du {@code ApplicationModel} sont sauvegardees */
  ON_SAVE_APPLICATION_MODEL(null),

  /** Sujet a diffuser lorsque les donnees du {@code EditorModel} sont sauvegardees */
  ON_SAVE_EDITOR_MODEL(null),

  /** Sujet a diffuser lorsque les donnees du {@code SimulationModel} sont sauvegardees */
  ON_SAVE_SIMULATION_MODEL(null),

  /** Sujet a diffuser lorsque les donnees sauvegardees du {@code RendererModel} sont chargees */
  ON_LOAD_RENDERER_MODEL(null),

  /**
   * Sujet à diffuser pour avertir que le bouton pour descendre un calque a été appuyé. Le type du
   * message est {@code null}.
   */
  EDITOR_LAYER_DOWN_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le bouton pour monter un calque a été appuyé. Le type du
   * message est {@code null}.
   */
  EDITOR_LAYER_UP_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le bouton pour ajouter un calque a été appuyé. Le type du
   * message est {@code null}.
   */
  EDITOR_LAYER_ADD_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le bouton pour dupliquer un calque a été appuyé. Le type du
   * message est {@code null}.
   */
  EDITOR_LAYER_DUPLICATE_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le bouton pour supprimer un calque a été appuyé. Le type du
   * message est {@code null}.
   */
  EDITOR_LAYER_DELETE_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le bouton pour effacer tous les calques a été appuyé. Le type
   * du message est {@code null}.
   */
  EDITOR_LAYER_CLEAR_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir qu'un calque à été renommé. Le type du message est {@code
   * String}.
   */
  EDITOR_LAYER_RENAMED(String.class),

  /**
   * Sujet à diffuser pour avertir qu'un nouveau calque est en focus. Le type du message est {@code
   * Interger}.
   */
  EDITOR_LAYER_FOCUSED(Integer.class),

  /**
   * Sujet à diffuser pour avertir qu'un nouveau calque est en focus. Le type du message est l'ID du
   * {@code WLayer} de type {@code Integer}.
   */
  EDITOR_LAYER_FOCUS_UPDATE(Integer.class),

  /**
   * Sujet à diffuser pour avertir que les images d'aperçu du gestionnaire de calques doivent être
   * rafraichies. Le type du message est {@code null}.
   */
  EDITOR_LAYER_PREVIEW_REFRESH(null),

  /**
   * Sujet à diffuser pour avertir que l'agence de calques a été modifiée. Le type du message est
   * {@code null}.
   */
  EDITOR_LAYER_CHANGED(null),

  // ========== CONSOLE MODEL SUBJECTS ========== //

  /**
   * Sujet a diffuser lorsqu'une nouvelle entree de log est ajoutée. Le type du message est {@code
   * WLogEvent}.
   */
  ON_CONSOLE_LOGS_CHANGED(WLogEvent.class),

  // ================
  // KEYBIND SUBJECTS
  // ================

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour partir/arrêter la simulation a été
   * appuyé. Le type du message est {@code null}.
   */
  KEYBIND_SIMULATION_TOGGLE_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour passer à la prochaine image de la
   * simulation a été appuyé. Le type du message est {@code null}.
   */
  KEYBIND_SIMULATION_NEXT_FRAME_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour réinitialiser la simulation a été
   * appuyé. Le type du message est {@code null}.
   */
  KEYBIND_SIMULATION_RESET_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour monter le type de visualisation a
   * été appuyé. Le type du message est {@code null}.
   */
  KEYBIND_VISUALIZATION_UP_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour descendre le type de visualisation
   * a été appuyé. Le type du message est {@code null}.
   */
  KEYBIND_VISUALIZATION_DOWN_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour changer le nom du calque actif a
   * été appuyé. Le type du message est {@code null}.
   */
  KEYBIND_EDITOR_RENAME_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour changer l'épaisseur de trait a été
   * appuyé. Le type du message est {@code null}.
   */
  KEYBIND_EDITOR_LINE_THICKNESS_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour changer l'état du mode zen a été
   * appuyé. Le type du message est {@code null}.
   */
  KEYBIND_ZEN_MODE_TOGGLE_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour désactiver le mode zen a été
   * appuyé. Le type du message est {@code null}.
   */
  KEYBIND_ZEN_MODE_EXIT_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour changer l'état de la légende a été
   * appuyé. Le type du message est {@code null}.
   */
  KEYBIND_LEGEND_TOGGLE_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour ouvrir le menu instruction a été
   * appuyé. Le type du message est {@code null}.
   */
  KEYBIND_SHOW_INSTRUCTIONS_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour ouvrir le menu des raccourcis
   * clavier a été appuyé. Le type du message est {@code null}.
   */
  KEYBIND_SHOW_KEYBIND_MENU_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour activer l'inspecteur a été appuyé.
   * Le type du message est {@code null}.
   */
  KEYBIND_INSPECTOR_TOGGLE_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour activer le crayon a été appuyé. Le
   * type du message est {@code null}.
   */
  KEYBIND_TOGGLE_PENCIL_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour activer la gomme a été appuyé. Le
   * type du message est {@code null}.
   */
  KEYBIND_TOGGLE_ERASER_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour importer une scène a été appuyé. Le
   * type du message est {@code null}.
   */
  KEYBIND_IMPORT_SCENE_PRESSED(null),

  /**
   * Sujet à diffuser pour avertir que le raccourci clavier pour exporter une scène a été appuyé. Le
   * type du message est {@code null}.
   */
  KEYBIND_EXPORT_SCENE_PRESSED(null),

  // =============
  // TEST SUBJECTS
  // =============

  /**
   * Sujet a diffuser pour tester le systeme d'abonnement/diffusion. Le type du message est un
   * {@code Object}
   */
  ON_TEST_PUBSUB_OBJECT(Object.class),
  /**
   * Sujet a diffuser pour tester le systeme d'abonnement/diffusion. Le type du message est un
   * {@code Integer}
   */
  ON_TEST_PUBSUB_INTEGER(Integer.class),
  /**
   * Sujet a diffuser pour tester le systeme d'abonnement/diffusion. Le type du message est un
   * {@code String}
   */
  ON_TEST_PUBSUB_STRING(String.class),

  /**
   * Sujet a diffuser pour tester le systeme d'abonnement/diffusion. Le type du message est {@code
   * null}
   */
  ON_TEST_PUBSUB_NULL(null);

  /** Le type attendu du message diffusé par le {@code EventPublisher}. */
  private final Class<?> messageType;

  /**
   * Construction des elements {@code Subject}.
   *
   * @param messageType Le type attendu du message diffusé par le {@code EventPublisher}
   */
  Subject(Class<?> messageType) {
    this.messageType = messageType;
  }

  /**
   * Getter pour le {@code messageType} d'un element de {@code Subject}. Lors de l'abonnement a un
   * {@code Subject} quelconque, le {@code EventSubscriber} doit s'assurer que le type d'objet passé
   * en paramètre au {@code Consumer<T>} respecte le type defini par {@code messageType}.
   *
   * <p>Le {@code messageType} permet de definir le type d'objet que les {@code EventPublisher} ont
   * le droit de diffuser pour un {@code Subject} donné.
   *
   * @return Le type attendu du message diffusé par le {@code EventPublisher}.
   */
  public Class<?> getMessageType() {
    return messageType;
  }

  /**
   * Methode permettant de determiner si un message donné respecte le {@code messageType} defini
   * pour cet element de {@code Subject}, tel que le {@code message} est d'un type pouvant etre
   * converti au {@code messageType}.
   *
   * @param message Le {@code Object} dont le type doit etre validé
   * @return {@code true} si le {@code message} passé en paramètre respecte le type attendu
   * @see Class#isAssignableFrom(Class) La condition a respecter
   */
  public boolean isValidMessage(Object message) {
    // TODO : tester cette methode

    // Check if only one of them is null
    if (messageType == null ^ message == null) return false;

    // If one is null, then both are null
    if (message == null) return true;

    return messageType.isAssignableFrom(message.getClass());
  }
}
