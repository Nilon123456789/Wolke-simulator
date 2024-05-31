package com.e24.wolke.backend.models.obstacles;

import com.e24.wolke.utils.math.WMath;
import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les methodes de {@code WNormalCell}
 *
 * @author MeriBouisri
 */
public class WNormalCellTest {

  /** Methode pour tester {@code WNormalCell#addNormalOrientation()} */
  @Test
  public void testAddNormalOrientation1() {
    WNormalCell normalCell = new WNormalCell();
    double angle1 = Math.PI;
    double angle2 = Math.PI / 2;

    // Add initial angle
    normalCell.addNormalOrientation(angle1);
    double expected = angle1;
    Assert.assertTrue(WMath.nearlyEquals(expected, normalCell.getNormalOrientation()));

    // Add vector of same angle. Orientation should stay the same.
    normalCell.addNormalOrientation(angle1);
    Assert.assertTrue(WMath.nearlyEquals(expected, normalCell.getNormalOrientation()));

    // Add vector of different orientation
    normalCell.addNormalOrientation(angle2);
    expected = 3 * Math.PI / 4;
    Assert.assertTrue(WMath.nearlyEquals(expected, normalCell.getNormalOrientation()));
  }

  /** Methode #2 pour tester {@code WNormalCell#addNormalOrientation()} */
  @Test
  public void testAddNormalOrientation2() {
    WNormalCell normalCell = new WNormalCell();
    double angle1 = Math.PI;
    double angle2 = 0;

    double expected = angle1;
    normalCell.addNormalOrientation(angle1);
    Assert.assertTrue(WMath.nearlyEquals(expected, normalCell.getNormalOrientation()));

    // Normal cancels out when adding opposite angles form unit circle
    normalCell.addNormalOrientation(angle2);
    Assert.assertTrue(normalCell.isNullOrientation());
  }

  /**
   * Methode #1 pour tester {@code WNormalCell#addVectorOrientation(double, double)}. Cas ou les
   * vecteurs sont perpendiculaires.
   */
  @Test
  public void testAddVectorOrientation1() {
    Assert.assertTrue(testAddVectorOrientation(Math.PI, Math.PI / 2, 3 * Math.PI / 4));
    Assert.assertTrue(testAddVectorOrientation(Math.PI, -Math.PI / 2, 5 * Math.PI / 4));
    Assert.assertTrue(testAddVectorOrientation(-Math.PI / 2, 0, 7 * Math.PI / 4));
    Assert.assertTrue(testAddVectorOrientation(0, Math.PI / 2, Math.PI / 4));
  }

  /**
   * Methode #2 pour tester {@code WNormalCell#addVectorOrientation(double, double)}. Cas ou les
   * normales s'annulent.
   */
  @Test
  public void testAddVectorOrientation2() {
    Assert.assertTrue(testAddVectorOrientation(Math.PI, 0, Double.NaN));
    Assert.assertTrue(testAddVectorOrientation(-Math.PI / 2, Math.PI / 2, Double.NaN));
    Assert.assertTrue(testAddVectorOrientation(3 * Math.PI / 2, Math.PI / 2, Double.NaN));
    Assert.assertTrue(testAddVectorOrientation(3 * Math.PI / 2, Math.PI / 2, Double.NaN));
  }

  /**
   * Methode pour tester {@code WNormalCell#addVectorOrientation(double, double)}
   *
   * @param theta1 Premier angle
   * @param theta2 deuxieme angle
   * @param expected angle resultant
   * @return True si {@code WMath#nearlyEquals(expected, actual)}
   */
  public boolean testAddVectorOrientation(double theta1, double theta2, double expected) {
    double actual = WNormalCell.addVectorOrientation(theta1, theta2);
    expected = WMath.normalizeAngle(expected);

    if (Double.isNaN(expected)) return Double.isNaN(actual);

    return WMath.nearlyEquals(expected, actual);
  }
}
