package com.e24.wolke.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Classe permettant de stocker des couples de clés/valeurs de manière bi-directionnelle.
 *
 * @param <K1> Type de la première clé.
 * @param <K2> Type de la deuxième clé.
 * @see HashMap
 * @author Nilon123456789
 */
public class BiHashMap<K1, K2> {

  /** HashMap contenant les couples de clés (K1) / valeurs (K2). */
  private HashMap<K1, K2> map1;

  /** HashMap contenant les couples de clés (K2) / valeurs (K1). */
  private HashMap<K2, K1> map2;

  /** Constructeur de la classe BiHashMap. */
  public BiHashMap() {
    this.map1 = new HashMap<K1, K2>();
    this.map2 = new HashMap<K2, K1>();
  }

  /**
   * Ajoute un couple de clés/valeurs dans la BiHashMap.
   *
   * @param key1 Clé de type K1.
   * @param key2 Clé de type K2.
   * @throws IllegalArgumentException Si la clé key1 ou key2 existe déjà.
   */
  public void put(K1 key1, K2 key2) {
    if (map1.containsKey(key1)) throw new IllegalArgumentException("The key1 already exists");
    if (map2.containsKey(key2)) throw new IllegalArgumentException("The key2 already exists");

    map1.put(key1, key2);
    map2.put(key2, key1);
  }

  /**
   * Retourne la valeur associée à la clé key1.
   *
   * @param key1 Clé de type K1.
   * @return Valeur de type K2 associée à la clé key1 ou null si la clé n'existe pas.
   */
  public K2 get1(K1 key1) {
    return map1.get(key1);
  }

  /**
   * Retourne la valeur associée à la clé key2.
   *
   * @param key2 Clé de type K2.
   * @return Valeur de type K1 associée à la clé key2 ou null si la clé n'existe pas.
   */
  public K1 get2(K2 key2) {
    return map2.get(key2);
  }

  /**
   * Retourne la taille de la BiHashMap.
   *
   * @return Taille de la BiHashMap.
   */
  public int size() {
    return map1.size();
  }

  /**
   * Retourne si la BiHashMap est vide ou non.
   *
   * @return True si la BiHashMap est vide, false sinon.
   */
  public boolean isEmpty() {
    return map1.isEmpty();
  }

  /**
   * Supprime un couple de clés/valeurs de la BiHashMap.
   *
   * @param key1 Clé de type K1.
   */
  public void remove1(K1 key1) {
    K2 key2 = map1.get(key1);
    map1.remove(key1);
    map2.remove(key2);
  }

  /**
   * Supprime un couple de clés/valeurs de la BiHashMap.
   *
   * @param key2 Clé de type K2.
   */
  public void remove2(K2 key2) {
    K1 key1 = map2.get(key2);
    map1.remove(key1);
    map2.remove(key2);
  }

  /** Supprime tous les couples de clés/valeurs de la BiHashMap. */
  public void clear() {
    map1.clear();
    map2.clear();
  }

  /**
   * Retourne si la BiHashMap contient la clé key1.
   *
   * @param key1 Clé de type K1.
   * @return True si la BiHashMap contient la clé key1, false sinon.
   */
  public boolean containsKey1(K1 key1) {
    return map1.containsKey(key1);
  }

  /**
   * Retourne si la BiHashMap contient la clé key2.
   *
   * @param key2 Clé de type K2.
   * @return True si la BiHashMap contient la clé key2, false sinon.
   */
  public boolean containsKey2(K2 key2) {
    return map2.containsKey(key2);
  }

  /**
   * Retourne si la BiHashMap contient la valeur value.
   *
   * @param value Valeur de type K2.
   * @return True si la BiHashMap contient la valeur value, false sinon.
   */
  public boolean containsValue1(K2 value) {
    return map1.containsValue(value);
  }

  /**
   * Retourne si la BiHashMap contient la valeur value.
   *
   * @param value Valeur de type K1.
   * @return True si la BiHashMap contient la valeur value, false sinon.
   */
  public boolean containsValue2(K1 value) {
    return map2.containsValue(value);
  }

  /**
   * Retourne la clé de la valeur value.
   *
   * @param value Valeur de type K2.
   * @return Clé de type K1 associée à la valeur value ou null si la valeur n'existe pas.
   */
  public K1 getKey1(K2 value) {
    return map2.get(value);
  }

  /**
   * Retourne la clé de la valeur value.
   *
   * @param value Valeur de type K1.
   * @return Clé de type K2 associée à la valeur value ou null si la valeur n'existe pas.
   */
  public K2 getKey2(K1 value) {
    return map1.get(value);
  }

  /**
   * Retourne un set contenant les clés K1 de la BiHashMap.
   *
   * @return Set contenant les clés K2 de la BiHashMap.
   */
  public Set<Map.Entry<K1, K2>> entrySet1() {
    return map1.entrySet();
  }

  /**
   * Retourne un set contenant les clés K2 de la BiHashMap.
   *
   * @return Set contenant les clés K2 de la BiHashMap.
   */
  public Set<Map.Entry<K2, K1>> entrySet2() {
    return map2.entrySet();
  }
}
