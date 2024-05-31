package com.e24.wolke.utils.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester la classe {@code WMatrix1D}.
 *
 * @author MeriBouisri
 */
public class WMatrix1DTest {

  /** Methode #1 pour tester {@code WMatrix1D#getNeighborPositions(int)} */
  @Test
  public void testGetNeighborPositions1() {
    int xLength = 3;
    int yLength = 3;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    int[] actual = matrix.getNeighborPositions(4);
    int[] expected = {0, 1, 2, 3, 5, 6, 7, 8};

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode #2 pour tester {@code WMatrix1D#getNeighborPositions(int)} */
  @Test
  public void testGetNeighborPositions2() {
    int xLength = 3;
    int yLength = 3;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    int[] actual = matrix.getNeighborPositions(0);
    int[] expected = {-1, -1, -1, -1, 1, -1, 3, 4};

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode #3 pour tester {@code WMatrix1D#getNeighborPositions(int)} */
  @Test
  public void testGetNeighborPositions3() {
    int xLength = 3;
    int yLength = 3;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    int[] actual = matrix.getNeighborPositions(7);
    int[] expected = {3, 4, 5, 6, 8, -1, -1, -1};

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode permettant de tester {@code WMatrix1D#getMatrix()} */
  @Test
  public void testGetMatrix() {
    int xLength = 5;
    int yLength = 3;

    Integer[] expectedObject = new Integer[15];
    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    Assert.assertArrayEquals(expectedObject, matrix.getMatrix());
  }

  /** Methode permettant de tester {@code WMatrix1D#getSize} */
  @Test
  public void testGetSize() {
    int xLength = 5;
    int yLength = 3;

    int expected = xLength * yLength;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    Assert.assertEquals(expected, matrix.getSize());
    Assert.assertEquals(matrix.getMatrix().length, matrix.getSize());
  }

  /** Methode permettant de tester {@code WMatrix1D#getXLength} */
  @Test
  public void testGetXLength() {
    int xLength = 5;
    int yLength = 3;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    Assert.assertEquals(xLength, matrix.getXLength());
  }

  /** Methode permettant de tester {@code WMatrix1D#getXLength} */
  @Test
  public void testGetYLength() {
    int xLength = 5;
    int yLength = 3;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    Assert.assertEquals(yLength, matrix.getYLength());
  }

  /** Methode pour tester {@code WMatrix1D#getPos(int, int)} */
  public void testGetPos1() {
    int xLength = 5;
    int yLength = 3;

    int x = 3;
    int y = 3;
    int expected = 9;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    Assert.assertEquals(expected, matrix.getPos(x, y));
  }

  /** Methode pour tester {@code WMatrix1D#getPos(int)} */
  @Test
  public void testGetPos2() {
    int xLength = 5;
    int yLength = 3;

    int[] expected1 = {3, 1};
    int[] expected2 = {xLength - 1, yLength - 1};
    int pos1 = 8;
    int pos2 = (xLength * yLength) - 1;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    Assert.assertArrayEquals(expected1, matrix.getPos(pos1));
    Assert.assertArrayEquals(expected2, matrix.getPos(pos2));
  }

  /** Methode permettant de tester {@code WMatrix1D#getElementAt(int, int)} */
  @Test
  public void testGetElementAt() {
    int xLength = 5;
    int yLength = 3;

    Integer[] expectedArray = {
      0, 0, 0, 0, 0,
      0, 0, 0, 0, 0,
      0, 0, 0, 0, 0
    };

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    int expected = 1;

    int posX = 3;
    int posY = 2;
    int pos = matrix.getPos(posX, posY);

    expectedArray[pos] = expected;
    matrix.getMatrix()[pos] = expected;

    Assert.assertEquals(matrix.getElementAt(posX, posY), expectedArray[pos]);
  }

  /** Methode permettant de tester {@code WMatrix1D#setElementAt(int, int)} */
  @Test
  public void testSetElementAt() {
    int xLength = 5;
    int yLength = 3;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    int expected = 1;

    int posX = 3;
    int posY = 2;
    int pos = matrix.getPos(posX, posY);

    matrix.setElementAt(expected, posX, posY);

    Assert.assertEquals((int) expected, matrix.getMatrix()[pos].intValue());
  }

  /** Test pour la methode {@code WMatrix1D#isPositionInRange(int)} */
  @Test
  public void testIsPositionInRange() {
    int xLength = 5;
    int yLength = 3;
    int size = xLength * yLength;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    Assert.assertFalse(matrix.isPositionInRange(size));
    Assert.assertFalse(matrix.isPositionInRange(-1));
    Assert.assertTrue(matrix.isPositionInRange(size - 1));
  }

  /** Test pour la methode {@code WMatrix1D#getXYLength()} */
  @Test
  public void testGetXYLength() {
    int xLength = 5;
    int yLength = 3;

    WMatrix1D<Integer> matrix = WMatrix1D.createIntegerMatrix(xLength, yLength);

    int[] xyLength = matrix.getXYLength();

    Assert.assertEquals(xLength, xyLength[0]);
    Assert.assertEquals(xyLength[0], matrix.getXLength());

    Assert.assertEquals(yLength, xyLength[1]);
    Assert.assertEquals(xyLength[1], matrix.getYLength());
  }

  /** Test pour {@code WMatrix1D#isBorderPosition(int, int, int, int)} */
  @Test
  public void isBorderPosition00() {
    int w0 = 5;
    int h0 = 5;

    int h1 = 7;
    int w1 = 9;

    int[] matrix0 = {
      1, 1, 1, 1, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 1, 1, 1, 1
    };

    int[] matrix1 = {
      1, 1, 1, 1, 1, 1, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 1, 1, 1, 1, 1, 1,
    };

    boolean flag0 = true;
    boolean flag1 = true;

    for (int i = 0; i < matrix0.length; i++) {
      flag0 = !(WMatrix1D.isBorderPosition(i, w0, h0) ^ matrix0[i] == 1);
    }
    for (int i = 0; i < matrix1.length; i++) {
      flag0 = !(WMatrix1D.isBorderPosition(i, w1, h1) ^ matrix1[i] == 1);
    }

    Assert.assertTrue(flag0);
    Assert.assertTrue(flag1);
  }

  /** Test pour {@code WMatrix1D#getBorderIndices(int, int)} */
  @Test
  public void testGetBorderIndices00() {
    int w0 = 5;
    int h0 = 5;

    int w1 = 7;
    int h1 = 9;

    int expected0 = 16;
    int expected1 = 28;

    int actual0 = WMatrix1D.getBorderIndices(w0, h0).length;
    int actual1 = WMatrix1D.getBorderIndices(w1, h1).length;

    Assert.assertEquals(expected0, actual0);
    Assert.assertEquals(expected1, actual1);
  }

  /** Test pour {@code WMatrix1D#getBorderIndices(int, int)} */
  @Test
  public void testgetBorderIndices01() {
    int w0 = 5;
    int h0 = 5;

    int w1 = 7;
    int h1 = 9;

    int size0 = 16;
    int size1 = 28;

    int[] matrix0 = {
      1, 1, 1, 1, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 0, 0, 0, 1,
      1, 1, 1, 1, 1
    };

    int[] matrix1 = {
      1, 1, 1, 1, 1, 1, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 0, 0, 0, 0, 0, 1,
      1, 1, 1, 1, 1, 1, 1,
    };

    int[] expected0 = new int[size0];
    int[] expected1 = new int[size1];

    int cnt = 0;

    for (int i = 0; i < matrix0.length; i++) {
      if (!WMatrix1D.isBorderPosition(i, w0, h0)) continue;
      expected0[cnt] = i;
      cnt++;
    }

    cnt = 0;
    for (int i = 0; i < matrix1.length; i++) {
      if (!WMatrix1D.isBorderPosition(i, w1, h1)) continue;
      expected1[cnt] = i;
      cnt++;
    }

    int[] actual0 = WMatrix1D.getBorderIndices(w0, h0);
    int[] actual1 = WMatrix1D.getBorderIndices(w1, h1);

    Assert.assertArrayEquals(expected0, actual0);
    Assert.assertArrayEquals(expected1, actual1);
  }
}
