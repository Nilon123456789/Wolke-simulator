package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE;
import com.e24.wolke.utils.math.WBitwiseCompass;

/**
 * La classe {@code WObstacleEncoder} est une classe utilitaire permettant d'encoder des donnees
 * d'un obstacle dans la representation binaire d'un entier.
 *
 * @author MeriBouisri
 */
public final class WObstacleEncoder {

  /** Classe non instanciable */
  private WObstacleEncoder() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Methode utilitaire permettant d'encoder les donnees d'un obstacle dans un {@code byte}, tel que
   * les bits aux positions 0-3 contiennent l'angle de la normale, et les bits aux positions 4-6
   * contiennent les donnees du type de la bordure.
   *
   * @param borderType Le type de bordure de l'obstacle
   * @param normalAngle L'angle de la normale appliquee par l'obstacle
   * @return Un {@code byte} contenant les donnees passees en parametre
   */
  public static byte encode(BORDER_TYPE borderType, WBitwiseCompass normalAngle) {
    byte obstacle = 0b0;

    // obstacle |= borderType.getBinaryValue();
    return obstacle;
  }
}
