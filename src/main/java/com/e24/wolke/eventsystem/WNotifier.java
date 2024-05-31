package com.e24.wolke.eventsystem;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * La classe {@code WNotifier} permet d'avertir une liste de {@code WObserver} d'un changement.
 *
 * @author MeriBouisri
 */
public class WNotifier {

  /** La liste des {@code WObserver} a avertir */
  private ConcurrentLinkedQueue<WObserver> observers;

  /** Construction d'un {@code WObservable}. */
  public WNotifier() {
    observers = new ConcurrentLinkedQueue<WObserver>();
  }

  /**
   * Ajouter un {@code WObserver} a la liste de {@code WObserver} a avertir lors d'un changement.
   *
   * @param observer Le {@code WObserver} a ajouter a la liste
   * @return {@code true} si l'ajout a ete effectue avec succes
   */
  public boolean addObserver(WObserver observer) {
    return observers.add(observer);
  }

  /**
   * Retirer un {@code WObserver} a la liste de {@code WObserver} a avertir lors d'un changement.
   *
   * @param observer Le {@code WObserver} a ajouter a la liste
   * @return {@code true} si le retrait a ete effectue avec succes
   */
  public boolean remove(WObserver observer) {
    return observers.remove(observer);
  }

  /** Methode a invoquer afin d'avertir tous les {@code WObserver} d'un changement d'etat */
  public void notifyObservers() {
    observers.forEach(WObserver::onStateChanged);
  }
}
