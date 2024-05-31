package com.e24.wolke.backend.models.obstacles;

import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester les methodes de {@code WCell}
 *
 * @author MeriBouisri
 */
public class WCellTest {

  /** Methode pour tester {@code WCell#isNeighbor(WCell)} */
  @Test
  public void testIsNeighbor00() {
    WCell cell1 = new WCell(0, 0);
    WCell cell2 = new WCell(1, 0);
    WCell cell3 = new WCell(1, 1);
    WCell cell4 = new WCell(2, 0);

    Assert.assertTrue(cell1.isNeighbor(cell2));
    Assert.assertTrue(cell1.isNeighbor(cell3));
    Assert.assertFalse(cell1.isNeighbor(cell4));
  }

  /** Methode pour tester {@code WCell#isNeighbor(int, int, int, int)} statique */
  @Test
  public void testIsNeighbor01() {
    int[] cell1 = {0, 0};
    int[] cell2 = {1, 0};
    int[] cell3 = {1, 1};
    int[] cell4 = {2, 0};

    Assert.assertTrue(WCell.isNeighbor(cell1[0], cell1[1], cell2[0], cell2[1]));
    Assert.assertTrue(WCell.isNeighbor(cell1[0], cell1[1], cell3[0], cell3[1]));
    Assert.assertFalse(WCell.isNeighbor(cell1[0], cell1[1], cell4[0], cell4[1]));
  }

  /** Methode pour tester {@code WCell#isOrthogonalNeighbor(WCell)} */
  @Test
  public void testIsOrthogonalNeighbor00() {
    WCell cell1 = new WCell(0, 0);
    WCell cell2 = new WCell(1, 0);
    WCell cell3 = new WCell(1, 1);

    Assert.assertTrue(cell1.isOrthogonalNeighbor(cell2));
    Assert.assertFalse(cell1.isOrthogonalNeighbor(cell3));
  }

  /** Methode pour tester {@code WCell#isOrthogonalNeighbor(int, int, int, int)} statique */
  @Test
  public void testIsOrthogonalNeighbor01() {
    int[] cell1 = {0, 0};
    int[] cell2 = {1, 0};
    int[] cell3 = {1, 1};

    Assert.assertTrue(WCell.isOrthogonalNeighbor(cell1[0], cell1[1], cell2[0], cell2[1]));
    Assert.assertFalse(WCell.isOrthogonalNeighbor(cell1[0], cell1[1], cell3[0], cell3[1]));
  }

  /** Methode pour tester {@code WCell#isHorizontalNeighbor(WCell)} */
  @Test
  public void testIsHorizontalNeighbor00() {
    WCell cell1 = new WCell(0, 0);
    WCell cell2 = new WCell(0, 1);
    WCell cell3 = new WCell(1, 1);

    Assert.assertTrue(cell1.isHorizontalNeighbor(cell2));
    Assert.assertFalse(cell1.isHorizontalNeighbor(cell3));
  }

  /** Methode pour tester {@code WCell#isHorizontalNeighbor(int, int, int, int)} */
  @Test
  public void testIsHorizontalNeighbor01() {
    int[] cell1 = {0, 0};
    int[] cell2 = {0, 1};
    int[] cell3 = {1, 1};

    Assert.assertTrue(WCell.isHorizontalNeighbor(cell1[0], cell1[1], cell2[0], cell2[1]));
    Assert.assertFalse(WCell.isHorizontalNeighbor(cell1[0], cell1[1], cell3[0], cell3[1]));
  }

  /** Methode pour tester {@code WCell#isHorizontalNeighbor(WCell)} */
  @Test
  public void testIsVerticalNeighbor00() {
    WCell cell1 = new WCell(0, 0);
    WCell cell2 = new WCell(1, 0);
    WCell cell3 = new WCell(1, 1);

    Assert.assertTrue(cell1.isVerticalNeighbor(cell2));
    Assert.assertFalse(cell1.isVerticalNeighbor(cell3));
  }

  /** Methode pour tester {@code WCell#isHorizontalNeighbor(int, int, int, int)} statique */
  @Test
  public void testIsVerticalNeighbor01() {
    int[] cell1 = {0, 0};
    int[] cell2 = {1, 0};
    int[] cell3 = {1, 1};

    Assert.assertTrue(WCell.isVerticalNeighbor(cell1[0], cell1[1], cell2[0], cell2[1]));
    Assert.assertFalse(WCell.isVerticalNeighbor(cell1[0], cell1[1], cell3[0], cell3[1]));
  }

  /** Methode pour tester {@code WCell#isDiagonalNeighbor(WCell)} */
  @Test
  public void testIsDiagonalNeighbor00() {
    WCell cell1 = new WCell(0, 0);
    WCell cell2 = new WCell(1, 0);
    WCell cell3 = new WCell(1, 1);

    Assert.assertFalse(cell1.isDiagonalNeighbor(cell2));
    Assert.assertTrue(cell1.isDiagonalNeighbor(cell3));
  }

  /** Methode pour tester {@code WCell#isDiagonalNeighbor(int, int, int, int)} */
  @Test
  public void testIsDiagonalNeighbor01() {
    int[] cell1 = {0, 0};
    int[] cell2 = {1, 0};
    int[] cell3 = {1, 1};

    Assert.assertFalse(WCell.isDiagonalNeighbor(cell1[0], cell1[1], cell2[0], cell2[1]));
    Assert.assertTrue(WCell.isDiagonalNeighbor(cell1[0], cell1[1], cell3[0], cell3[1]));
  }
}
