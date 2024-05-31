package com.e24.wolke.utils.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester la classe {@code WBitwise}
 *
 * @author MeriBouisri
 */
public class WBitwiseTest {

  /** Test pour {@code WBitwise#set(int, int)} */
  @Test
  public void testSet00() {
    int n0 = 0b00;
    int n1 = 0b10;

    int p0 = 1;
    int p1 = 0;

    int actual_n0 = WBitwise.set(n0, p0);
    int actual_n1 = WBitwise.set(n1, p1);

    int expected_n0 = 0b10;
    int expected_n1 = 0b11;

    Assert.assertEquals(buildBinaryString(expected_n0, actual_n0, 2), expected_n0, actual_n0);
    Assert.assertEquals(buildBinaryString(expected_n1, actual_n1, 2), expected_n1, actual_n1);
  }

  /** Test pour {@code WBitwise#set(int, int, int)} */
  @Test
  public void testSet01() {
    int n0 = 0b10;
    int n1 = 0b10;
    int n2 = 0b01;
    int n3 = 0b01;

    int p0 = 0;
    int p1 = 0;
    int p2 = 1;
    int p3 = 1;

    int b0 = 1;
    int b1 = 0;
    int b2 = 1;
    int b3 = 0;

    int actual_n0 = WBitwise.set(n0, p0, b0);
    int actual_n1 = WBitwise.set(n1, p1, b1);
    int actual_n2 = WBitwise.set(n2, p2, b2);
    int actual_n3 = WBitwise.set(n3, p3, b3);

    int expected_n0 = 0b11;
    int expected_n1 = 0b10;
    int expected_n2 = 0b11;
    int expected_n3 = 0b01;

    Assert.assertEquals(buildBinaryString(expected_n0, actual_n0, 2), expected_n0, actual_n0);
    Assert.assertEquals(buildBinaryString(expected_n1, actual_n1, 2), expected_n1, actual_n1);
    Assert.assertEquals(buildBinaryString(expected_n2, actual_n2, 2), expected_n2, actual_n2);
    Assert.assertEquals(buildBinaryString(expected_n3, actual_n3, 2), expected_n3, actual_n3);
  }

  /** Test pour {@code WBitwise#extract(int, int)} */
  @Test
  public void testExtract() {
    int n = 0b1010;

    int p0 = 0;
    int p1 = 1;
    int p2 = 2;
    int p3 = 3;

    int actual_n0 = WBitwise.extract(n, p0);
    int actual_n1 = WBitwise.extract(n, p1);
    int actual_n2 = WBitwise.extract(n, p2);
    int actual_n3 = WBitwise.extract(n, p3);

    int expected_n0 = 0b0;
    int expected_n1 = 0b1;
    int expected_n2 = 0b0;
    int expected_n3 = 0b1;

    Assert.assertEquals(buildBinaryString(expected_n0, actual_n0, 2), expected_n0, actual_n0);
    Assert.assertEquals(buildBinaryString(expected_n1, actual_n1, 2), expected_n1, actual_n1);
    Assert.assertEquals(buildBinaryString(expected_n2, actual_n2, 2), expected_n2, actual_n2);
    Assert.assertEquals(buildBinaryString(expected_n3, actual_n3, 2), expected_n3, actual_n3);
  }

  /** Test pour {@code WBitwise#generateOnBitmask(int)} */
  @Test
  public void testGenerateOnBitmask() {
    int n0 = 1;
    int n1 = 4;
    int n2 = 6;

    int actual_n0 = WBitwise.mask(n0);
    int actual_n1 = WBitwise.mask(n1);
    int actual_n2 = WBitwise.mask(n2);

    int expected_n0 = 0b1;
    int expected_n1 = 0b1111;
    int expected_n2 = 0b11_1111;

    Assert.assertEquals(buildBinaryString(expected_n0, actual_n0, 8), expected_n0, actual_n0);
    Assert.assertEquals(buildBinaryString(expected_n1, actual_n1, 8), expected_n1, actual_n1);
    Assert.assertEquals(buildBinaryString(expected_n2, actual_n2, 8), expected_n2, actual_n2);
  }

  /** Test pour {@code WBitwise#countSetBits(int)} */
  @Test
  public void testCountSetBits() {
    int n0 = 0b0011_1111_1111;
    int n1 = 0b0;
    int n2 = 0b1010_1010_1010;
    int n3 = 0b1000_0000_0000;

    int expected_n0 = 10;
    int expected_n1 = 0;
    int expected_n2 = 6;
    int expected_n3 = 1;

    int actual_n0 = WBitwise.countSetBits(n0);
    int actual_n1 = WBitwise.countSetBits(n1);
    int actual_n2 = WBitwise.countSetBits(n2);
    int actual_n3 = WBitwise.countSetBits(n3);

    Assert.assertEquals(buildBinaryString(expected_n0, actual_n0, 8), expected_n0, actual_n0);
    Assert.assertEquals(buildBinaryString(expected_n1, actual_n1, 8), expected_n1, actual_n1);
    Assert.assertEquals(buildBinaryString(expected_n2, actual_n2, 8), expected_n2, actual_n2);
    Assert.assertEquals(buildBinaryString(expected_n3, actual_n3, 8), expected_n3, actual_n3);
  }

  /** Test pour {@code WBitwise#rotate(int, int, int)} */
  public void testRotate() {
    int size = 4;

    int n0 = 0b1001;
    int n1 = 0b0011;
    int n2 = 0b1100;

    Assert.assertEquals(n1, WBitwise.rotate(n0, 1, size));
    Assert.assertEquals(n0, WBitwise.rotate(n2, 1, size));
    Assert.assertEquals(n0, WBitwise.rotate(n0, size, size));
    Assert.assertEquals(n2, WBitwise.rotate(n0, -1, size));
    Assert.assertEquals(n1, WBitwise.rotate(n0, -2, size));
  }

  /**
   * Methode utilitaire pour afficher valeurs en format binaire
   *
   * @param expected Valeur attendue
   * @param actual Valeur reelle
   * @param buf Nombre de buffer zeros
   * @return {@code String}
   */
  private static String buildBinaryString(int expected, int actual, int buf) {
    String f = "%" + buf + "s";
    return "expected="
        + String.format(f, Integer.toBinaryString(expected)).replace(' ', '0')
        + ", actual="
        + String.format(f, Integer.toBinaryString(actual)).replace(' ', '0');
  }
}
