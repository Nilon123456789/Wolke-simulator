package com.e24.wolke.eventsystem;

/**
 * L'interface {@code WObserver} permet de commncer et arreter une phase d'observation d'un objet.
 *
 * @author MeriBouisri
 */
public interface WObserver {
  /** Methode permettant d'avertir le {@code WObserver} d'un changement */
  void onStateChanged();
}
