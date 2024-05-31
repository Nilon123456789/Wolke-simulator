package com.e24.wolke.filesystem.scenes;

/**
 * L'interface {@code WSceneMember} permet de definir le composant a exporter et importer pour
 * chaque objet affecte
 *
 * @param <T> Le type de l'objet que la classe qui implemente
 * @author MeriBouisri
 */
public interface WSceneMember<T> {

  /**
   * Methode permettant de retourner le composant de scene de l'objet qui implemente {@code
   * WSceneMember}
   *
   * @return L'objet de type generique qui constitue le composant necessaire
   */
  public T getSceneComponent();

  /**
   * Methode permettant de definir comment appliquer le composant de scene a la classe qui
   * implemente {@code WSceneMember}
   *
   * @param sceneComponent L'objet de type generique qui constitue le composant de scene propre a la
   *     classe qui l'implemente
   * @return {@code true} si le composant de scene a ete applique avec succes
   */
  public boolean setSceneComponent(T sceneComponent);
}
