package com.e24.wolke.utils.math;

import org.junit.Assert;
import org.junit.Test;

/**
 * Classe permettant de tester la classe {@code BitwiseCompass}
 *
 * @author MeriBouisri
 */
public class WBitwiseCompassTest {

  /** Test pour {@code WBitwiseCompass#getVectorComponents(byte)} */
  @Test
  public void testGetVector() {
    int[] vN = {0, 1};
    int[] vE = {1, 0};
    int[] vS = {0, -1};
    int[] vW = {-1, 0};

    int[] vNE = {vE[0], vN[1]};
    int[] vSE = {vE[0], vS[1]};
    int[] vSW = {vW[0], vS[1]};
    int[] vNW = {vW[0], vN[1]};

    Assert.assertArrayEquals(vN, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_N));
    Assert.assertArrayEquals(vS, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_S));
    Assert.assertArrayEquals(vE, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_E));
    Assert.assertArrayEquals(vW, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_W));

    Assert.assertArrayEquals(vNE, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_NE));
    Assert.assertArrayEquals(vSE, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_SE));
    Assert.assertArrayEquals(vSW, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_SW));
    Assert.assertArrayEquals(vNW, WBitwiseCompass.getVectorComponents(WBitwiseCompass.MASK_NW));
  }

  /** Test pour {@code WBitwiseCompass#isOpposite(byte, byte)} */
  @Test
  public void testIsOpposite() {
    int n_s = 0;
    int e_w = 0;

    int ne_sw = 0;
    int nw_se = 0;

    int n_w = 1;
    int n_e = 1;
    int s_e = 1;
    int s_w = 1;

    Assert.assertEquals(
        n_s, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_N, WBitwiseCompass.MASK_S));
    Assert.assertEquals(
        e_w, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_W, WBitwiseCompass.MASK_E));

    Assert.assertEquals(
        ne_sw, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_NE, WBitwiseCompass.MASK_SW));
    Assert.assertEquals(
        nw_se, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_NW, WBitwiseCompass.MASK_SE));

    Assert.assertEquals(
        n_w, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_N, WBitwiseCompass.MASK_W));
    Assert.assertEquals(
        n_e, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_N, WBitwiseCompass.MASK_E));

    Assert.assertEquals(
        s_e, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_S, WBitwiseCompass.MASK_E));
    Assert.assertEquals(
        s_w, WBitwiseCompass.isOpposite(WBitwiseCompass.MASK_S, WBitwiseCompass.MASK_W));
  }
}
