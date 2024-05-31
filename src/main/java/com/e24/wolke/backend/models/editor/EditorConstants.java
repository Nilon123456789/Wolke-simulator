package com.e24.wolke.backend.models.editor;

import com.e24.wolke.filesystem.properties.SettingsPropertiesManager;
import java.awt.image.BufferedImage;

/**
 * La classe {@code ObstacleConstants} regroupe les constantes reliees au {@code ObstacleModel}
 *
 * @author MeriBouisri
 */
public final class EditorConstants {

  /** Le type de {@code BufferedImage} des {@code WLayer} par defaut */
  public static final int DEFAULT_LAYER_IMAGE_TYPE = BufferedImage.TRANSLUCENT;

  /**
   * Le masque permettant de definir l'acces en ecriture (permission de modifier) de chaque calque,
   * par defaut
   */
  public static final int WRITABLE_LAYER_MASK_DEFAULT = 0b1111_1110;

  /**
   * Le masque permettant de definir l'acces en lecture (la visibilite) de chaque calque, par defaut
   */
  public static final int READABLE_LAYER_MASK_DEFAULT = 0b1111_1111;

  /** Nombre maximal de calques */
  public static final int MAX_TOTAL_LAYERS =
      SettingsPropertiesManager.INSTANCE.getIntProperty("editor.maxLayers");

  /** L'indice du calque qui contient la bordure d'obstacles */
  public static final int BORDER_LAYER_INDEX = MAX_TOTAL_LAYERS - 1;

  /** La visibilite par defaut du menu déroulant du choix de formes */
  public static final boolean DEFAULT_SHAPES_DROPDOWN_VISIBILITY = false;

  /** Le code d'erreur indiquant que le calque est invalide */
  public static final int ERROR_CODE_INCOMPATIBLE_LAYER = -2;

  /** L'initialisation de la classe {@code ObstacleConstants} est interdite. */
  private EditorConstants() {
    throw new AssertionError();
  }
}
