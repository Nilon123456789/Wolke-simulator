package com.e24.wolke.backend.models.obstacles;

/**
 * La classe {@code ObstacleConstants} regroupe les constantes reliees au {@code ObstacleModel}
 *
 * @author MeriBouisri
 */
public final class ObstacleConstants {

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
  public static final int MAX_TOTAL_LAYERS = Byte.SIZE;

  /** L'indice du calque qui contient la bordure d'obstacles */
  public static final int BORDER_LAYER_INDEX = 0;

  /** L'initialisation de la classe {@code ObstacleConstants} est interdite. */
  private ObstacleConstants() {
    throw new AssertionError();
  }
}
