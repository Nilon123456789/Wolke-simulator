package com.e24.wolke.utils.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * Vector2Test.java
 *
 * <p>Cette classe est responsable de tester la classe Vector2
 *
 * @author Nilon123456789
 * @author MeriBouisri
 * @author n-o-o-d-l-e
 */
public class WVector2DTest {

  /** Test de l'obtention de la composante x du vecteur */
  @Test
  public void testGetX() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    Assert.assertEquals(1.0, vector.getX(), 0.0);
  }

  /** Test de l'obtention de la composante y du vecteur */
  @Test
  public void testGetY() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    Assert.assertEquals(2.0, vector.getY(), 0.0);
  }

  /** Test de la modification de la composante x du vecteur */
  @Test
  public void testSetX() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    vector.setX(3.0);
    Assert.assertEquals(3.0, vector.getX(), 0.0);
  }

  /** Test de la modification de la composante y du vecteur */
  @Test
  public void testSetY() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    vector.setY(3.0);
    Assert.assertEquals(3.0, vector.getY(), 0.0);
  }

  /** Test du constructeur vide de la classe Vector2 */
  @Test
  public void testEmptyConstructor() {
    WVector2D vector = new WVector2D();
    Assert.assertEquals(0.0, vector.getX(), 0.0);
    Assert.assertEquals(0.0, vector.getY(), 0.0);
  }

  /** Test de l'optention des composantes x et y du vecteur en même temps */
  @Test
  public void testGetXYasArray() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    Assert.assertArrayEquals(new double[] {1.0, 2.0}, vector.getComponents(), 0.0);
  }

  /** Test de la modification des composantes x et y du vecteur en même temps */
  @Test
  public void testSetXY() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    vector.setComponents(3.0, 4.0);
    Assert.assertArrayEquals(new double[] {3.0, 4.0}, vector.getComponents(), 0.0);
  }

  /** Test de l'onbention de la norme du vecteur */
  @Test
  public void testGetNorm1() {
    WVector2D vector = new WVector2D(3.0, 4.0);
    Assert.assertEquals(5.0, vector.getNorm(), 0.0);
  }

  /** Test de l'obtention de la norme du vecteur */
  @Test
  public void testGetNorm2() {
    WVector2D vector = new WVector2D(1.0, 1.0);
    Assert.assertEquals(Math.sqrt(2), vector.getNorm(), 0.0);
  }

  /** Test de l'obtention d'un vecteur normalisé */
  @Test
  public void testNormalize1() {
    WVector2D vector = new WVector2D(3.0, 4.0);
    WVector2D result = vector.normalize();
    Assert.assertArrayEquals(new double[] {0.6, 0.8}, result.getComponents(), 0.0);
  }

  /** Test de l'obtention d'un vecteur normalisé */
  @Test
  public void testNormalize2() {
    WVector2D vector = new WVector2D(1.0, 1.0);
    WVector2D result = vector.normalize();
    Assert.assertArrayEquals(
        new double[] {1 / Math.sqrt(2), 1 / Math.sqrt(2)}, result.getComponents(), 0.0);
  }

  /** Test de l'obtention d'un vecteur normalisé */
  @Test
  public void testNormalize3() {
    WVector2D vector = new WVector2D(0.0, 0.0);
    WVector2D result = vector.normalize();
    Assert.assertArrayEquals(new double[] {0.0, 0.0}, result.getComponents(), 0.0);
  }

  /** Test de l'obtention d'un vecteur normalisé */
  @Test
  public void testNormalize4() {
    WVector2D vector = new WVector2D(-2.0, 3.0);
    WVector2D result = vector.normalize();
    Assert.assertArrayEquals(
        new double[] {-(2 * Math.sqrt(13)) / 13, (3 * Math.sqrt(13)) / 13},
        result.getComponents(),
        WMath.EPSILON);
  }

  /** Test de l'obtention de modules de vecteurs */
  @Test
  public void testModulus1() {
    WVector2D vector = new WVector2D(3.0, 4.0);
    Assert.assertEquals(5.0, vector.modulus(), 0.0);
  }

  /** Test de l'obtention de modules de vecteurs */
  @Test
  public void testModulus2() {
    WVector2D vector = new WVector2D(1.0, 1.0);
    Assert.assertEquals(Math.sqrt(2), vector.modulus(), 0.0);
  }

  /** Test de l'obtention de modules de vecteurs */
  @Test
  public void testModulus3() {
    WVector2D vector = new WVector2D(0.0, 0.0);
    Assert.assertEquals(0.0, vector.modulus(), 0.0);
  }

  /** Test de l'addition de deux vecteurs */
  @Test
  public void testAdd() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(3.0, 4.0);
    WVector2D result = vector1.add(vector2);
    Assert.assertArrayEquals(new double[] {4.0, 6.0}, result.getComponents(), 0.0);
  }

  /** Test de la soustraction de deux vecteurs */
  @Test
  public void testSubtract() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(3.0, 4.0);
    WVector2D result = vector1.substract(vector2);
    Assert.assertArrayEquals(new double[] {-2.0, -2.0}, result.getComponents(), 0.0);
  }

  /** Test de la multiplication d'un vecteur par un scalaire */
  @Test
  public void testMultiply() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    WVector2D result = vector.multiply(2.0);
    Assert.assertArrayEquals(new double[] {2.0, 4.0}, result.getComponents(), 0.0);
  }

  /** Test de la division d'un vecteur par un scalaire */
  @Test
  public void testDivide() {
    WVector2D vector = new WVector2D(2.0, 4.0);
    WVector2D result = vector.div(2.0);
    Assert.assertArrayEquals(new double[] {1.0, 2.0}, result.getComponents(), 0.0);
  }

  /** Test de la division d'un vecteur par un scalaire nul */
  @Test(expected = IllegalArgumentException.class)
  public void testDivideByZero() {
    WVector2D vector = new WVector2D(2.0, 4.0);
    vector.div(0.0);
  }

  /** Test du produit scalaire de deux vecteurs */
  @Test
  public void testDotProduct() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(3.0, 4.0);
    Assert.assertEquals(11.0, vector1.dot(vector2), 0.0);
  }

  /** Test l'obtention de l'angle entre deux vecteurs */
  @Test
  public void testGetAngle1() {
    WVector2D vector1 = new WVector2D(1.0, 0.0);
    WVector2D vector2 = new WVector2D(0.0, 1.0);
    Assert.assertEquals(Math.PI / 2, vector1.getAngle(vector2), 0.0);
  }

  /** Test l'obtention de l'angle entre deux vecteurs */
  @Test
  public void testGetAngle2() {
    WVector2D vector1 = new WVector2D(1.0, 0.0);
    WVector2D vector2 = new WVector2D(5.0, 0.0);
    Assert.assertEquals(0.0, vector1.getAngle(vector2), 0.0);
  }

  /** Test l'obtention de l'angle entre deux vecteurs */
  @Test
  public void testGetAngle3() {
    WVector2D vector1 = new WVector2D(1.0, 0.0);
    WVector2D vector2 = new WVector2D(-1.0, 0.0);
    Assert.assertEquals(Math.PI, vector1.getAngle(vector2), 0.0);
  }

  /** Test la projection d'un vecteur sur un autre */
  @Test
  public void testProject() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(3.0, 4.0);
    WVector2D result = vector1.project(vector2);

    Assert.assertArrayEquals(new double[] {1.32, 1.76}, result.getComponents(), 0.0);
  }

  /** Test equals */
  @Test
  public void testEquals() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(1.0, 2.0);
    Assert.assertEquals(vector1, vector2);
  }

  /** Test clone */
  @Test
  public void testClone1() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    WVector2D clone = vector.clone();
    Assert.assertEquals(vector, clone);
  }

  /** Test clone */
  @Test
  public void testClone2() {
    WVector2D vector = new WVector2D(1.0, 2.0);
    WVector2D clone = vector.clone();
    clone.setX(3.0);
    Assert.assertNotEquals(vector, clone);
  }

  /** Test interpolation linéaire */
  @Test
  public void testLerp1() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(3.0, 4.0);
    WVector2D result = WVector2D.lerp(vector1, vector2, 0);
    Assert.assertEquals(vector1, result);
  }

  /** Test interpolation linéaire */
  @Test
  public void testLerp2() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(3.0, 4.0);
    WVector2D result = WVector2D.lerp(vector1, vector2, 1);
    Assert.assertEquals(vector2, result);
  }

  /** Test interpolation linéaire */
  @Test
  public void testLerp3() {
    WVector2D vector1 = new WVector2D(1.0, 2.0);
    WVector2D vector2 = new WVector2D(3.0, 4.0);
    WVector2D result = WVector2D.lerp(vector1, vector2, 0.5);
    Assert.assertEquals(new WVector2D(2.0, 3.0), result);
  }

  /** Test le constructeur avec angle comme parametre. */
  @Test
  public void testAngleConstructor() {
    WVector2D v1 = new WVector2D(Math.PI);
    WVector2D v2 = new WVector2D(-1, 0);

    Assert.assertTrue(WMath.nearlyEquals(v1.getX(), v2.getX()));
    Assert.assertTrue(WMath.nearlyEquals(v1.getY(), v2.getY()));

    v1 = new WVector2D(0);
    v2 = new WVector2D(1, 0);

    Assert.assertTrue(WMath.nearlyEquals(v1.getX(), v2.getX()));
    Assert.assertTrue(WMath.nearlyEquals(v1.getY(), v2.getY()));

    v1 = new WVector2D(Math.PI / 2);
    v2 = new WVector2D(0, 1);

    Assert.assertTrue(WMath.nearlyEquals(v1.getX(), v2.getX()));
    Assert.assertTrue(WMath.nearlyEquals(v1.getY(), v2.getY()));

    v1 = new WVector2D(-Math.PI / 2);
    v2 = new WVector2D(0, -1);

    Assert.assertTrue(WMath.nearlyEquals(v1.getX(), v2.getX()));
    Assert.assertTrue(WMath.nearlyEquals(v1.getY(), v2.getY()));

    v1 = new WVector2D(Math.toRadians(45));
    v2 = new WVector2D(Math.sqrt(2) / 2, Math.sqrt(2) / 2);

    Assert.assertTrue(WMath.nearlyEquals(v1.getX(), v2.getX()));
    Assert.assertTrue(WMath.nearlyEquals(v1.getY(), v2.getY()));
  }

  /** Methdoe permettant de tester {@code Vector2#getAngle()} */
  @Test
  public void testGetAngle4() {
    int MAX_ANGLES = 5;
    double unitAngle = (2 * Math.PI) / MAX_ANGLES;

    for (int i = 0; i < MAX_ANGLES * 2; i++) {
      double angle = unitAngle * i;
      WVector2D v1 = new WVector2D(angle);

      angle = WMath.normalizeAngle(angle);
      Assert.assertTrue(WMath.nearlyEquals(angle, v1.getAngle()));
    }
  }

  /** Methode pour tester {@code Vector2#isZeroVector()} et {@code Vector2#isNearlyZeroVector()} */
  @Test
  public void testIsZeroVector() {
    WVector2D v1 = new WVector2D(WMath.EPSILON, WMath.EPSILON);
    WVector2D v2 = new WVector2D(WMath.EPSILON * 2, 0);

    Assert.assertTrue(WVector2D.ZERO_VECTOR.isZeroVector());
    Assert.assertTrue(v1.isNearlyZeroVector());
    Assert.assertFalse(v2.isNearlyZeroVector());
  }
}
