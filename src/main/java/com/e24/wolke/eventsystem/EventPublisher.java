package com.e24.wolke.eventsystem;

/**
 * La classe {@code EventPublisher} permet de diffuser un message a tous les {@code
 * EventSubscribers} abonnés à un {@code Subject} donné. Pour que les {@code EventPublisher}
 * puissent communiquer avec les {@code EventSubscriber}, ils doivent etre liés au même {@code
 * EventBroker}.
 *
 * <p>Les classes utilisant un {@code EventPublisher} sont responsables de s'assurer, pour un {@code
 * Subject} donné, que le {@code message} transmis respecte le typage defini par {@code
 * Subject#getMessageType()}.
 *
 * @see EventBroker Gestionnaire des diffusions et des abonnements
 * @see EventSubscriber Abonné aux {@code Subject}
 * @see Subject Enumeration de sujets/evenements pouvant etre diffusés
 * @author MeriBouisri
 */
public class EventPublisher {

  /** Le {@code EventBroker} qui gere les diffusions de cette instance */
  private EventBroker broker;

  /**
   * Construction d'un {@code EventPublisher} avec un {@code EventBroker}. Il est possible
   * d'effectuer des diffusions au {@code EventBroker} memorisé directement.
   *
   * @param broker Le {@code EventBroker} qui gere les diffusions de cette instance.
   * @see EventPublisher#publish(Subject, Object)
   */
  public EventPublisher(EventBroker broker) {
    this.broker = broker;
  }

  /**
   * Construction d'un {@code EventPublisher} sans {@code EventBroker}. Il est possible d'effectuer
   * des diffusions en passant un {@code EventBroker} en parametre aux methodes.
   *
   * @see EventPublisher#publish(EventBroker, Subject, Object)
   */
  public EventPublisher() {
    this(null);
  }

  /**
   * Setter pour le {@code EventBroker} de cette instance.
   *
   * <p>Les diffusions faites par cette instance de {@code EventPublisher} sont transmises au {@code
   * EventBroker} mémorisé à l'instant de l'appel à la méthode {@link
   * EventPublisher#publish(Subject, Object)}. Lorsqu'un {@code EventPublisher} diffuse le {@code
   * Subject} par ce {@code EventBroker}, le {@code Object} est passé en paramètre aux {@code
   * Consumer} abonnés.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}.
   *
   * @param broker Le {@code EventBroker} qui gere les diffusions de cette instance.
   * @see #publish(Subject, Object) Diffusion d'un {@code Object} aux abonnés du {@code Subject} sur
   *     le {@code EventBroker} actuel
   */
  public void setEventBroker(EventBroker broker) {
    this.broker = broker;
  }

  /**
   * Getter pour le {@code EventBroker} de cette instance.
   *
   * <p>Les diffusions faites par cette instance de {@code EventPublisher} sont transmises au {@code
   * EventBroker} mémorisé à l'instant de l'appel à la méthode {@link
   * EventPublisher#publish(Subject, Object)}. Lorsqu'un {@code EventPublisher} diffuse un {@code
   * Subject} a travers ce {@code EventBroker}, le {@code Object} est passé en paramètre aux {@code
   * Consumer} abonnés.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}, tel que {@code
   * this#getEventBroker()#equals(EventSubscriber#getEventBroker())}.
   *
   * @return broker Le {@code EventBroker} qui gere les abonnements de cette instance.
   */
  public EventBroker getEventBroker() {
    return broker;
  }

  /**
   * Methode permettant, avec le {@code EventBroker} memorisé par cette instance, de diffuser un
   * {@code Object} aux abonnés d'un {@code Subject} donné, de sorte que la reference du {@code
   * Object} est passé en paramètre à tous les {@code Consumer} abonnés par un {@code
   * EventSubscriber} sur le meme {@code EventBroker}.
   *
   * <p>La methode {@code Consumer#accept(Object)} opere par des "side-effects", et risque donc de
   * modifier l'état du {@code Object} diffusé apres l'invocation des fonctions de rappel definies
   * par les {@code EventSubscribers}.
   *
   * <p>Les classes utilisant un {@code EventPublisher} sont responsables de s'assurer, pour un
   * {@code Subject} donné, que le {@code message} transmis respecte le typage defini par {@code
   * Subject#getMessageType()}.
   *
   * @param subject Le {@code Subject} auquel les receveurs du {@code message} sont abonnés
   * @param message Le {@code Object} qui sera diffusé aux abonnés du {@code subject}
   * @return {@code true} si la diffusion du message a ete effectuée avec succès, {@code false} si
   *     le {@code EventBroker} memorisé par cette instance est {@code null}
   */
  public boolean publish(Subject subject, Object message) {
    return publish(broker, subject, message);
  }

  /**
   * Methode permettant, avec un {@code EventBroker} quelconque, de diffuser un {@code Object} aux
   * abonnés d'un {@code Subject} donné, de sorte que la reference du {@code Object} est passé en
   * paramètre à tous les {@code Consumer} abonnés par un {@code EventSubscriber} sur le meme {@code
   * EventBroker}.
   *
   * <p>La methode {@code Consumer#accept(Object)} opere par des "side-effects", et risque donc de
   * modifier l'état du {@code Object} diffusé apres l'invocation des fonctions de rappel definies
   * par les {@code EventSubscribers}.
   *
   * <p>Les classes utilisant un {@code EventPublisher} sont responsables de s'assurer, pour un
   * {@code Subject} donné, que le {@code message} transmis respecte le typage defini par {@code
   * Subject#getMessageType()}.
   *
   * @param broker Le {@code EventBroker} avec lequel la diffusion est effectuee
   * @param subject Le {@code Subject} auquel les receveurs du {@code message} sont abonnés
   * @param message Le {@code Object} qui sera diffusé aux abonnés du {@code subject}
   * @return {@code true} si la diffusion du message a ete effectuée avec succès, {@code false} si
   *     le {@code EventBroker} est {@code null}
   */
  public boolean publish(EventBroker broker, Subject subject, Object message) {
    if (broker == null) return false;

    return broker.publish(subject, message);
  }
}
