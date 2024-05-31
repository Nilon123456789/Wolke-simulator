package com.e24.wolke.utils.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * WDoubleMatrixTest.java
 *
 * <p>Cette classe est responsable de tester la classe WDoubleMatrix
 *
 * @author Nilon123456789
 */
public class WDoubleMatrixTest {

  /** Test la méthode getMatrix de la classe WDoubleMatrix */
  @Test
  public void testGetMatrix() {
    WDoubleMatrix matrix = new WDoubleMatrix(3, 3);
    double[] actual = matrix.getMatrix();
    double[] expected = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    Assert.assertArrayEquals(expected, actual, 0.0);
  }

  /** Test #1 la méthode setMatrix de la classe WDoubleMatrix */
  @Test
  public void testSetMatrix1() {
    WDoubleMatrix matrix = new WDoubleMatrix(3, 3);
    double[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    matrix.setMatrix(expected);
    double[] actual = matrix.getMatrix();
    Assert.assertArrayEquals(expected, actual, 0.0);
  }

  /** Test #2 la méthode setMatrix de la classe WDoubleMatrix */
  @Test(expected = IllegalArgumentException.class)
  public void testSetMatrix2() {
    WDoubleMatrix matrix = new WDoubleMatrix(3, 3);
    double[] expected = {1, 2, 3, 4, 5, 6, 7, 8};
    matrix.setMatrix(expected);
  }

  /** Test la méthode getXLenght de la classe WDoubleMatrix */
  @Test
  public void testGetXLenght() {
    WDoubleMatrix matrix = new WDoubleMatrix(2, 3);
    int actual = matrix.getXLength();
    int expected = 2;
    Assert.assertEquals(expected, actual);
  }

  /** Test la méthode getYLenght de la classe WDoubleMatrix */
  @Test
  public void testGetYLenght() {
    WDoubleMatrix matrix = new WDoubleMatrix(3, 2);
    int actual = matrix.getYLength();
    int expected = 2;
    Assert.assertEquals(expected, actual);
  }

  /** Test la méthode get size de la classe WDoubleMatrix */
  @Test
  public void testGetXYLenght() {
    WDoubleMatrix matrix = new WDoubleMatrix(3, 2);
    int[] actual = matrix.getXYLength();
    int[] expected = {3, 2};
    Assert.assertArrayEquals(expected, actual);
  }

  /** Test la méthode getSize de la classe WDoubleMatrix */
  @Test
  public void testGetSize() {
    WDoubleMatrix matrix = new WDoubleMatrix(3, 2);
    int actual = matrix.getSize();
    int expected = 6;
    Assert.assertEquals(expected, actual);
  }
}
