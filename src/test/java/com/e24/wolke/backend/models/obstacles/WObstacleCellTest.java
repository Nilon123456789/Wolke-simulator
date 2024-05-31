package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.utils.math.WMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester la classe {@code WObstacleCell}
 *
 * @author MeriBouisri
 */
public class WObstacleCellTest {

  /**
   * Methode #1 permettant de tester {@code WObstacleCell#applyNormalTo(WNormalCell)} Deux obstacles
   * entourant une cellule :
   *
   * <p>. O . . n . . . .
   */
  @Test
  public void testApplyNormalTo1() {
    WNormalCell normalCell = new WNormalCell(1, 1);

    // Obstacle en haut, normale vers le bas
    WObstacleCell obst1 = new WObstacleCell(1, 0);
    double expected1 = -Math.PI / 2;

    // Obstacle en bas, normale vers en haut
    WObstacleCell obst2 = new WObstacleCell(1, 2);
    double expected2 = Math.PI / 2;

    // Obstacle a gauche, normale vers la droite
    WObstacleCell obst3 = new WObstacleCell(0, 1);
    double expected3 = 0;

    // Obstacle a droite, normale vers la gauche
    WObstacleCell obst4 = new WObstacleCell(2, 1);
    double expected4 = Math.PI;

    obst1.applyNormalTo(normalCell);
    Assert.assertEquals(expected1, normalCell.getNormalOrientation(), WMath.EPSILON);

    normalCell.reset();
    obst2.applyNormalTo(normalCell);
    Assert.assertEquals(expected2, normalCell.getNormalOrientation(), WMath.EPSILON);

    normalCell.reset();
    obst3.applyNormalTo(normalCell);
    Assert.assertEquals(expected3, normalCell.getNormalOrientation(), WMath.EPSILON);

    normalCell.reset();
    obst4.applyNormalTo(normalCell);
    Assert.assertEquals(expected4, normalCell.getNormalOrientation(), WMath.EPSILON);
  }

  /**
   * Methode #2 permettant de tester {@code WObstacleCell#applyNormalTo(WNormalCell)}. Les obstacles
   * se situent en diagonale a la cellule de la normale.
   */
  @Test
  public void testApplyNormalTo2() {

    WNormalCell normalCell = new WNormalCell(0, 0);

    WObstacleCell otl = new WObstacleCell(-1, -1, true); // Top Left
    double otlExpected = WMath.normalizeAngle(7 * Math.PI / 4);

    WObstacleCell obl = new WObstacleCell(-1, 1, true); // Bottom left
    double oblExpected = Math.PI / 4;

    WObstacleCell otr = new WObstacleCell(1, -1, true); // Top right
    double otrExpected = WMath.normalizeAngle(5 * Math.PI / 4);

    WObstacleCell obr = new WObstacleCell(1, 1, true); // Bottom right
    double obrExpected = WMath.normalizeAngle(3 * Math.PI / 4);

    otl.applyNormalTo(normalCell);
    Assert.assertEquals(otlExpected, normalCell.getNormalOrientation(), WMath.EPSILON);

    normalCell.reset();
    obl.applyNormalTo(normalCell);
    Assert.assertEquals(oblExpected, normalCell.getNormalOrientation(), WMath.EPSILON);

    normalCell.reset();
    otr.applyNormalTo(normalCell);
    Assert.assertEquals(otrExpected, normalCell.getNormalOrientation(), WMath.EPSILON);

    normalCell.reset();
    obr.applyNormalTo(normalCell);
    Assert.assertEquals(obrExpected, normalCell.getNormalOrientation(), WMath.EPSILON);
  }

  /**
   * Methode #3 permettant de tester {@code WObstacleCell#applyNormalTo(WNormalCell)} Deux obstacles
   * entourant une cellule :
   *
   * <p>. . . . n O . O .
   */
  @Test
  public void testApplyNormalTo3() {
    WNormalCell normalCell = new WNormalCell(0, 0);

    // Obstacle en bas, normale vers le haut
    WObstacleCell obstacleCell1 = new WObstacleCell(0, 1);

    // Obstacle a droite, normale vers la gauche
    WObstacleCell obstacleCell2 = new WObstacleCell(1, 0);

    // Obstacle en bas, normale vers le haut
    double expected = Math.PI / 2;
    obstacleCell1.applyNormalTo(normalCell);
    Assert.assertEquals(expected, normalCell.getNormalOrientation(), WMath.EPSILON);

    // Obstacle
    obstacleCell2.applyNormalTo(normalCell);
    expected = WMath.normalizeAngle(3 * Math.PI / 4);
    Assert.assertEquals(expected, normalCell.getNormalOrientation(), WMath.EPSILON);
  }

  /**
   * Methode #3 permettant de tester {@code WObstacleCell#applyNormalTo(WNormalCell)} Deux obstacles
   * entourant une cellule :
   *
   * <p>. . . . n O . O .
   */
  @Test
  public void testApplyNormalTo5() {

    WNormalCell normalCell = new WNormalCell(0, 0);

    // Obstacle en haut, normale vers le bas
    WObstacleCell otm = new WObstacleCell(0, -1); // Top middle

    // Obstacle a gauche, normale vers la droite
    WObstacleCell olm = new WObstacleCell(-1, 0); // Left middle

    // Obstacle en haut, normale vers le bas
    double otmExpected = -Math.PI / 2;
    otm.applyNormalTo(normalCell);
    Assert.assertEquals(otmExpected, normalCell.getNormalOrientation(), WMath.EPSILON);

    // Obstacle a gauche, normale vers la droite
    olm.applyNormalTo(normalCell);

    // Normale resultante orientee vers le bas, a droite
    double olmExpected = WMath.normalizeAngle(7 * Math.PI / 4);
    Assert.assertEquals(olmExpected, normalCell.getNormalOrientation(), WMath.EPSILON);
  }

  /**
   * Methode #4 permettant de tester {@code WObstacleCell#applyNormalTo(WNormalCell)} 4 obstacles
   * autour d'une cellule devrait generer une normale nulle.
   *
   * <p>. O . O n O . O .
   */
  @Test
  public void testApplyNormalTo4() {
    WNormalCell normalCell = new WNormalCell(1, 1);
    WObstacleCell obst1 = new WObstacleCell(1, 0);
    WObstacleCell obst2 = new WObstacleCell(1, 2);
    WObstacleCell obst3 = new WObstacleCell(0, 1);
    WObstacleCell obst4 = new WObstacleCell(2, 1);

    obst1.applyNormalTo(normalCell);
    obst2.applyNormalTo(normalCell);

    Assert.assertTrue(normalCell.isNullOrientation());

    obst3.applyNormalTo(normalCell);
    obst4.applyNormalTo(normalCell);

    Assert.assertTrue(normalCell.isNullOrientation());
  }
}
