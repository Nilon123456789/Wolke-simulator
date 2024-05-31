package com.e24.wolke.eventsystem;

/**
 * L'interface {@code WEventComponent} permet de definir le comportement d'un composant d'interface
 * graphique par rapport au systeme d'evenement
 *
 * @author MeriBouisri
 */
public interface WEventComponent {
  /** Methode permettant d'ajouter les abonnements. */
  void setupSubscribers();

  /** Methode permettant de retirer tous les abonnements. */
  void removeSubscribers();
}
