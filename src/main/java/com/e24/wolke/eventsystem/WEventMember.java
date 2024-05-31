package com.e24.wolke.eventsystem;

import com.e24.wolke.utils.interfaces.Reinitializable;

/**
 * La classe {@code WEventMember} permet de generaliser les classes pouvant utiliser les objets
 * {@code EventBroker}, {@code EventSubscriber} et {@code EventPublisher}.
 *
 * @see EventBroker
 * @see EventSubscriber
 * @see EventPublisher
 * @author MeriBouisri
 */
public class WEventMember implements Reinitializable {

  /** Le {@code EventBroker} géré par l'instance */
  private EventBroker broker;

  /** Le {@code EventSubscriber} de cette instance */
  private EventSubscriber subscriber;

  /** Le {@code EventPublisher} de cette instance */
  private EventPublisher publisher;

  /** Consruction d'un {@code WEventMember}. */
  public WEventMember() {
    broker = new EventBroker();
    subscriber = new EventSubscriber(broker);
    publisher = new EventPublisher(broker);
  }

  /**
   * Getter pour le {@code EventBroker} de cette instance
   *
   * @return le {@code EventBroker} de cette instance
   */
  public EventBroker getBroker() {
    return broker;
  }

  /**
   * Getter pour le {@code EventSubscriber} de cette instance.
   *
   * @return Le {@code EventSubsriber de cette instance}
   */
  public EventSubscriber getSubscriber() {
    return subscriber;
  }

  /**
   * Getter pour le {@code EVentPublisher} de cette instance
   *
   * @return Le {@code EventPublisher} de cette instance
   */
  public EventPublisher getPublisher() {
    return publisher;
  }

  /**
   * {@inheritDoc} Le {@code EventBroker} de cette instance est reinitialise.
   *
   * @see EventBroker#reinitialize()
   */
  @Override
  public void reinitialize() {
    broker.reinitialize();
  }
}
