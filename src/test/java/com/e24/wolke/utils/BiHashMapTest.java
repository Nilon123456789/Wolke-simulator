package com.e24.wolke.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Classe de test de la classe BiHashMap.
 *
 * @see BiHashMap
 * @author Nilon123456789
 */
public class BiHashMapTest {

  /** BiHashMap utilisée pour les tests. */
  private BiHashMap<String, Integer> biHashMap;

  /** Méthode exécutée avant chaque test. */
  @Before
  public void setUp() {
    this.biHashMap = new BiHashMap<>();
  }

  /** Test de la méthode put */
  @Test
  public void test1Put() {
    this.biHashMap.put("Key1", 1);
    Assert.assertThrows(IllegalArgumentException.class, () -> this.biHashMap.put("Key1", 2));
    Assert.assertThrows(IllegalArgumentException.class, () -> this.biHashMap.put("Key2", 1));
  }

  /** Test de la méthode put et get1. */
  @Test
  public void test1PutAndGet1() {
    this.biHashMap.put("Key1", 1);
    Assert.assertEquals(Integer.valueOf(1), this.biHashMap.get1("Key1"));
  }

  /** Test de la méthode put et get1. */
  @Test
  public void test2PutAndGet1() {
    this.biHashMap.put("Key1", 1);
    Assert.assertEquals(null, this.biHashMap.get1("Key2"));
  }

  /** Test de la méthode put et get2. */
  @Test
  public void test1PutAndGet2() {
    this.biHashMap.put("Key1", 1);
    Assert.assertEquals("Key1", this.biHashMap.get2(1));
  }

  /** Test de la méthode put et get2. */
  @Test
  public void test2PutAndGet2() {
    this.biHashMap.put("Key1", 1);
    Assert.assertEquals(null, this.biHashMap.get2(2));
  }

  /** Test de la méthode size. */
  @Test
  public void testSize() {
    this.biHashMap.put("Key1", 1);
    this.biHashMap.put("Key2", 2);
    Assert.assertEquals(2, this.biHashMap.size());
  }

  /** Test de la méthode isEmpty. */
  @Test
  public void testIsEmpty() {
    Assert.assertTrue(this.biHashMap.isEmpty());
    this.biHashMap.put("Key1", 1);
    Assert.assertFalse(this.biHashMap.isEmpty());
  }

  /** Test de la méthode remove1. */
  @Test
  public void testRemove1() {
    this.biHashMap.put("Key1", 1);
    this.biHashMap.remove1("Key1");
    Assert.assertNull(this.biHashMap.get1("Key1"));
    Assert.assertNull(this.biHashMap.get2(1));
  }

  /** Test de la méthode remove2. */
  @Test
  public void testRemove2() {
    this.biHashMap.put("Key1", 1);
    this.biHashMap.remove2(1);
    Assert.assertNull(this.biHashMap.get1("Key1"));
    Assert.assertNull(this.biHashMap.get2(1));
  }

  /** Test de la méthode clear. */
  @Test
  public void testClear() {
    this.biHashMap.put("Key1", 1);
    this.biHashMap.put("Key2", 2);
    this.biHashMap.clear();
    Assert.assertEquals(0, this.biHashMap.size());
    Assert.assertTrue(this.biHashMap.isEmpty());
  }

  /** Test de la méthode containsKey1. */
  @Test
  public void testContainsKey1() {
    this.biHashMap.put("Key1", 1);
    Assert.assertTrue(this.biHashMap.containsKey1("Key1"));
    Assert.assertFalse(this.biHashMap.containsKey1("Key2"));
  }

  /** Test de la méthode containsKey2. */
  @Test
  public void testContainsKey2() {
    this.biHashMap.put("Key1", 1);
    Assert.assertTrue(this.biHashMap.containsKey2(1));
    Assert.assertFalse(this.biHashMap.containsKey2(2));
  }

  /** Test de la méthode containsValue1. */
  @Test
  public void testContainsValue1() {
    this.biHashMap.put("Key1", 1);
    Assert.assertTrue(this.biHashMap.containsValue1(1));
    Assert.assertFalse(this.biHashMap.containsValue1(2));
  }

  /** Test de la méthode containsValue2. */
  @Test
  public void testContainsValue2() {
    this.biHashMap.put("Key1", 1);
    Assert.assertTrue(this.biHashMap.containsValue2("Key1"));
    Assert.assertFalse(this.biHashMap.containsValue2("Key2"));
  }

  /** Test de la méthode getKey1. */
  @Test
  public void testGetKey1() {
    this.biHashMap.put("Key1", 1);
    Assert.assertEquals("Key1", this.biHashMap.getKey1(1));
  }

  /** Test de la méthode getKey2. */
  @Test
  public void testGetKey2() {
    this.biHashMap.put("Key1", 1);
    Assert.assertEquals(Integer.valueOf(1), this.biHashMap.getKey2("Key1"));
  }
}
