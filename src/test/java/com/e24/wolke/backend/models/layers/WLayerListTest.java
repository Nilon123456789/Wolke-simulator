package com.e24.wolke.backend.models.layers;

import com.e24.wolke.backend.models.editor.layers.WLayer;
import com.e24.wolke.backend.models.editor.layers.WLayerList;
import org.junit.Assert;
import org.junit.Test;

/** Classe permettant de tester les fonctionnalites de {@code WLayerList} */
public class WLayerListTest {

  /** Test pour {@code WLayerList#removeLayerAt(int)} */
  @Test
  public void testRemoveLayerAt00() {
    int max = 10;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    Assert.assertTrue(layerList.addLayer());

    WLayer expected = layerList.getLayerAt(0);
    WLayer actual = layerList.removeLayerAt(0);

    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WLayerList#addLayer()} */
  @Test
  public void testAddLayer00() {
    // Verifier que calques ne sont pas ajoutees apres max atteint

    int max = 10;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    Assert.assertFalse(layerList.addLayer());
    Assert.assertFalse(layerList.addLayer());
    Assert.assertEquals(max, layerList.size());
  }

  /** Test pour {@code WLayerList#addLayer(int)} */
  // @Test
  public void testAddLayer01() {
    int max = 10;
    int selected = 5;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max - 3; i++) Assert.assertTrue(layerList.addLayer());

    int expectedIndex = selected;
    int actualIndex = expectedIndex;

    layerList.setSelectedLayerIndex(expectedIndex);
    WLayer actualLayer = layerList.getSelectedLayer();
    WLayer expectedLayer = layerList.getLayerAt(expectedIndex);

    Assert.assertEquals(expectedLayer, actualLayer);

    // No offset if added on top

    layerList.addLayer(selected + 1);
    expectedIndex = selected;
    actualIndex = layerList.getSelectedLayerIndex();

    expectedLayer = layerList.getSelectedLayer();
    actualLayer = layerList.getLayerAt(expectedIndex);
    Assert.assertEquals(expectedIndex, actualIndex);
    Assert.assertEquals(expectedLayer, actualLayer);

    // Offset (i + 1) if added below
    layerList.addLayer(expectedIndex - 1);
    expectedIndex = expectedIndex + 1;
    actualIndex = layerList.getSelectedLayerIndex();

    expectedLayer = layerList.getSelectedLayer();
    actualLayer = layerList.getLayerAt(expectedIndex);
    Assert.assertEquals(expectedIndex, actualIndex);
    Assert.assertEquals(expectedLayer, actualLayer);

    // Offset (i + 1) if added at same pos

    layerList.addLayer(expectedIndex);
    expectedIndex = expectedIndex + 1;
    actualIndex = layerList.getSelectedLayerIndex();

    expectedLayer = layerList.getSelectedLayer();
    actualLayer = layerList.getLayerAt(expectedIndex);
    Assert.assertEquals(expectedIndex, actualIndex);
    Assert.assertEquals(expectedLayer, actualLayer);
  }

  /** Test pour {@code WLayerList#moveSelectedLayer(int)} */
  @Test
  public void testMoveSelectedLayer() {
    int max = 5;
    int selected = 0;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer ls = layerList.getLayerAt(0);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer l1 = layerList.getLayerAt(1);
    WLayer l2 = layerList.getLayerAt(2);
    WLayer l3 = layerList.getLayerAt(3);
    WLayer l4 = layerList.getLayerAt(4);

    layerList.setSelectedLayerIndex(selected);

    // Layer layout #1

    WLayer[] expectedLayers = {ls, l1, l2, l3, l4};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Layer layout #2

    int newIndex = 2;
    layerList.moveSelectedLayer(newIndex);
    expectedLayers = new WLayer[] {l1, l2, ls, l3, l4};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Layer layout #3

    newIndex = 4;
    layerList.moveSelectedLayer(newIndex);
    expectedLayers = new WLayer[] {l1, l2, l3, l4, ls};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Layer layout #4

    newIndex = 0;
    layerList.moveSelectedLayer(newIndex);
    expectedLayers = new WLayer[] {ls, l1, l2, l3, l4};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));
  }

  /** Test pour {@code WLayerList#moveSelectedLayerUp()} */
  @Test
  public void testMoveSelectedLayerUp() {
    int max = 3;
    int selected = 0;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer ls = layerList.getLayerAt(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer l1 = layerList.getLayerAt(1);
    WLayer l2 = layerList.getLayerAt(2);

    WLayer[] expectedLayers = {ls, l1, l2};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Move #1

    Assert.assertTrue(layerList.moveSelectedLayerUp());

    expectedLayers = new WLayer[] {l1, ls, l2};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Move #2
    Assert.assertTrue(layerList.moveSelectedLayerUp());

    expectedLayers = new WLayer[] {l1, l2, ls};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Final move

    Assert.assertFalse(layerList.moveSelectedLayerUp());
    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));
  }

  /** Test pour {@code WLayerList#moveSelectedLayerDown()} */
  // @Test
  public void testMoveSelectedLayerDown() {
    int max = 3;
    int selected = 2;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    layerList.setSelectedLayerIndex(selected);
    WLayer ls = layerList.getLayerAt(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer l1 = layerList.getLayerAt(0);
    WLayer l2 = layerList.getLayerAt(1);

    WLayer[] expectedLayers = {l1, l2, ls};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Move #1

    Assert.assertTrue(layerList.moveSelectedLayerDown());

    expectedLayers = new WLayer[] {l1, ls, l2};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Move #2
    Assert.assertTrue(layerList.moveSelectedLayerDown());

    expectedLayers = new WLayer[] {ls, l1, l2};

    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));

    // Final move

    Assert.assertFalse(layerList.moveSelectedLayerDown());
    for (int i = 0; i < max; i++) Assert.assertEquals(expectedLayers[i], layerList.getLayerAt(i));
  }

  /** Methode pour tester {@code WLayerList#addLayerAboveSelection()} */
  @Test
  public void testAddLayerAboveSelection() {
    int max = 4;
    int initSize = 2;
    int selected = 0;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < initSize; i++) Assert.assertTrue(layerList.addLayer());

    layerList.setSelectedLayerIndex(selected);

    WLayer lf = layerList.getLayerAt(initSize - 1); // last
    WLayer l0 = layerList.getSelectedLayer(); // first

    Assert.assertEquals(l0, layerList.getLayerAt(selected));

    // Add #1
    int newSize = initSize;
    Assert.assertTrue(layerList.addLayerAboveSelection());
    newSize++;
    Assert.assertEquals(layerList.size(), newSize);
    Assert.assertEquals(lf, layerList.getLayerAt(newSize - 1));
    Assert.assertEquals(l0, layerList.getLayerAt(selected));

    // Add #2
    selected = 1;
    WLayer l1 = layerList.getLayerAt(selected);
    Assert.assertNotEquals(l0, layerList.getSelectedLayer());
    Assert.assertEquals(l1, layerList.getSelectedLayer());
    Assert.assertTrue(layerList.addLayerAboveSelection());

    newSize++;
    Assert.assertEquals(layerList.size(), newSize);
    Assert.assertEquals(l1, layerList.getLayerAt(selected));
    Assert.assertEquals(lf, layerList.getLayerAt(newSize - 1));
    Assert.assertEquals(l0, layerList.getLayerAt(0));

    // Final add

    Assert.assertFalse(layerList.addLayerAboveSelection());
    Assert.assertEquals(layerList.size(), newSize);
  }

  /** Test pour selection du calque */
  @Test
  public void testSelectedLayer00() {

    int max = 10;
    int selected = 5;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max - 3; i++) Assert.assertTrue(layerList.addLayer());

    layerList.setSelectedLayerIndex(selected);

    WLayer expected = layerList.getLayerAt(selected);
    WLayer actual = layerList.getSelectedLayer();

    Assert.assertEquals(expected, actual);
  }

  /** Test pour {@code WLayerList#fill(int)} */
  @Test
  public void testFill() {
    int max = 10;
    int numFill = 5;

    WLayerList expected = new WLayerList(1, 1, 1, max);
    WLayerList actual = new WLayerList(1, 1, 1, max);

    // Expected behavior
    for (int i = 0; i < numFill; i++) expected.addLayer();

    // Actual behavior
    actual.fill(numFill);

    Assert.assertEquals(expected.size(), actual.size());
  }

  /** Methode pour tester {@code WLayerList#getLayersBelowSelection()} */
  @Test
  public void testGetLayersBelowSelection00() {
    int max = 5;
    int selected = 0;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer ls = layerList.getLayerAt(selected);
    layerList.setSelectedLayerIndex(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer[] expected = {};

    WLayer[] actual = layerList.getLayersBelowSelection();

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode pour tester {@code WLayerList#getLayersBelowSelection()} */
  @Test
  public void testGetLayersBelowSelection02() {
    int max = 5;
    int selected = 4;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer l1 = layerList.getLayerAt(0);
    WLayer l2 = layerList.getLayerAt(1);
    WLayer l3 = layerList.getLayerAt(2);
    WLayer l4 = layerList.getLayerAt(3);

    WLayer ls = layerList.getLayerAt(selected);
    layerList.setSelectedLayerIndex(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer[] expected = {l1, l2, l3, l4};

    WLayer[] actual = layerList.getLayersBelowSelection();

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode pour tester {@code WLayerList#getLayersBelowSelection()} */
  @Test
  public void testGetLayersBelowSelection03() {
    int max = 5;
    int selected = 2;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer l1 = layerList.getLayerAt(0);
    WLayer l2 = layerList.getLayerAt(1);

    WLayer ls = layerList.getLayerAt(selected);
    layerList.setSelectedLayerIndex(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer[] expected = {l1, l2};

    WLayer[] actual = layerList.getLayersBelowSelection();

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode pour tester {@code WLayerList#getLayersAboveSelection()} */
  @Test
  public void testGetLayersAboveSelection00() {
    int max = 5;
    int selected = max - 1;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer ls = layerList.getLayerAt(selected);
    layerList.setSelectedLayerIndex(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer[] expected = {};

    WLayer[] actual = layerList.getLayersAboveSelection();

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode pour tester {@code WLayerList#getLayersAboveSelection()} */
  @Test
  public void testGetLayersAboveSelection01() {
    int max = 5;
    int selected = 2;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer ls = layerList.getLayerAt(selected);
    layerList.setSelectedLayerIndex(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer l3 = layerList.getLayerAt(3);
    WLayer l4 = layerList.getLayerAt(4);

    WLayer[] expected = {l3, l4};

    WLayer[] actual = layerList.getLayersAboveSelection();

    Assert.assertArrayEquals(expected, actual);
  }

  /** Methode pour tester {@code WLayerList#getLayersAboveSelection()} */
  @Test
  public void testGetLayersAboveSelection03() {
    int max = 5;
    int selected = 0;
    WLayerList layerList = new WLayerList(1, 1, 1, max);

    for (int i = 0; i < max; i++) Assert.assertTrue(layerList.addLayer());

    WLayer ls = layerList.getLayerAt(selected);
    layerList.setSelectedLayerIndex(selected);
    Assert.assertEquals(ls, layerList.getSelectedLayer());

    WLayer l1 = layerList.getLayerAt(1);
    WLayer l2 = layerList.getLayerAt(2);
    WLayer l3 = layerList.getLayerAt(3);
    WLayer l4 = layerList.getLayerAt(4);

    WLayer[] expected = {l1, l2, l3, l4};

    WLayer[] actual = layerList.getLayersAboveSelection();

    Assert.assertArrayEquals(expected, actual);
  }
}
