package com.e24.wolke.utils.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * WMathTest.java
 *
 * <p>Cette classe est responsable de tester les méthodes de la classe NMath
 *
 * @author Nilon123456789
 * @author MeriBouisri
 */
public class WMathTest {

  /** Test de la méthode lerp */
  @Test
  public void testLerp1() {
    double a = 0;
    double b = 10;
    double k = 0.5;
    double expected = 5;
    double actual = WMath.lerp(a, b, k);
    Assert.assertEquals(expected, actual, 0.0001);
  }

  /** Test de la méthode lerp */
  @Test
  public void testLerp2() {
    double a = 0;
    double b = 10;
    double k = 0;
    double expected = 0;
    double actual = WMath.lerp(a, b, k);
    Assert.assertEquals(expected, actual, 0.0001);
  }

  /** Test de la méthode lerp */
  @Test
  public void testLerp3() {
    double a = 0;
    double b = 10;
    double k = 1;
    double expected = 10;
    double actual = WMath.lerp(a, b, k);
    Assert.assertEquals(expected, actual, 0.0001);
  }

  /** Test de la méthode bilerp */
  @Test
  public void testBilerp1() {
    double a = 0;
    double b = 10;
    double c = 20;
    double d = 30;
    double k = 0.5;
    double l = 0.5;
    double expected = 15;
    double actual = WMath.bilerp(a, b, c, d, k, l);
    Assert.assertEquals(expected, actual, 0.0001);
  }

  /** Test de la méthode bilerp */
  @Test
  public void testBilerp2() {
    double a = 0;
    double b = 10;
    double c = 20;
    double d = 30;
    double k = 0;
    double l = 0;
    double expected = 0;
    double actual = WMath.bilerp(a, b, c, d, k, l);
    Assert.assertEquals(expected, actual, 0.0001);
  }

  /** Test de la méthode bilerp */
  @Test
  public void testBilerp3() {
    double a = 0;
    double b = 10;
    double c = 20;
    double d = 30;
    double k = 1;
    double l = 1;
    double expected = 30;
    double actual = WMath.bilerp(a, b, c, d, k, l);
    Assert.assertEquals(expected, actual, 0.0001);
  }

  /** Test de la methode {@code WMath#nearlyEquals(double, double)} */
  @Test
  public void testNearlyEquals() {
    double a = 1;
    double b = 0.99999999999;

    Assert.assertTrue(WMath.nearlyEquals(a, b));
  }

  /** Test de la methode {@code WMath#nearlyZero(double)} */
  @Test
  public void testNearlyZero() {
    double zero = Math.sin(0); // Returns value close to zero, but not exactly

    Assert.assertTrue(WMath.nearlyZero(zero));
  }

  /** Test #1 la methode {code WMath#normalize(double)} */
  @Test
  public void testNormalize1() {
    double value = 0.32905;
    double expected = 0.32905;
    double actual = WMath.normalize(value);
    Assert.assertEquals(expected, actual, 0);
  }

  /** Test #2 la methode {code WMath#normalize(double)} */
  @Test
  public void testNormalize2() {
    double value = WMath.EPSILON / 2;
    double expected = 0.0d;
    double actual = WMath.normalize(value);
    Assert.assertEquals(expected, actual, 0);
  }

  /** Methode permettant de tester {@code WMath#normalizeAngle(double)} */
  @Test
  public void testNormalizeAngle() {
    double angle = 3 * Math.PI / 2;
    double actual = WMath.normalizeAngle(angle);
    double expected = -Math.PI / 2;

    Assert.assertTrue(WMath.nearlyEquals(expected, actual));
  }
}
