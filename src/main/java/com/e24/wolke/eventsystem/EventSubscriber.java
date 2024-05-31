package com.e24.wolke.eventsystem;

import java.util.function.Consumer;

/**
 * La classe {@code EventSubscriber} permet d'abonner une fonction de rappel {@code Consumer} a un
 * {@code Subject}, de sorte que cette fonction soit executée lors de la diffusion du {@code
 * Subject}. Pour que les {@code EventPublisher} puissent communiquer avec les {@code
 * EventSubscriber}, ils doivent etre liés au même {@code EventBroker}.
 *
 * <p>Les classes utilisant un {@code EventSubscriber} sont responsables de s'assurer, pour un
 * {@code Subject} donné, que le type de parametre accepté par le {@code Consumer<T>} respecte le
 * typage defini par {@code Subject#getMessageType()}. Elles sont aussi responsables d'assurer, dans
 * l'implementation du {@code Consumer}, que le {@code message} transmis par un {@code
 * EventPublisher} respecte le typage attendu.
 *
 * @see EventBroker Gestionnaire des diffusions et des abonnements
 * @see EventPublisher Diffuseur de {@code Subject}
 * @see Subject Enumeration de sujets/evenements pouvant etre diffusés
 * @see Consumer Fonction de rappel déclenchée lors de la diffusion d'un {@code Subject}
 * @author MeriBouisri
 */
public class EventSubscriber {

  /** Le {@code EventBroker} qui gere les abonnements de cette instance */
  private EventBroker broker;

  /**
   * Construction d'un {@code EventSubscriber} avec un {@code EventBroker}. Il est possible
   * d'effectuer des abonnements/desabonnements au {@code EventBroker} memorisé directement.
   *
   * @param broker Le {@code EventBroker} qui gere les abonnements de cette instance.
   * @see EventSubscriber#subscribe(Subject, Consumer)
   * @see EventSubscriber#unsubscribe(Subject, Consumer)
   */
  public EventSubscriber(EventBroker broker) {
    this.broker = broker;
  }

  /**
   * Construction d'un {@code EventSubscriber} sans {@code EventBroker} Il est possible d'effectuer
   * des abonnements/desabonnements en passant un {@code EventBroker} en parametre aux methodes.
   *
   * @see EventSubscriber#subscribe(EventBroker, Subject, Consumer)
   * @see EventSubscriber#unsubscribe(EventBroker, Subject, Consumer)
   */
  public EventSubscriber() {
    this(null);
  }

  /**
   * Setter pour le {@code EventBroker} de cette instance.
   *
   * <p>Les abonnements faits par cette instance de {@code EventSubscriber} ecoutent les diffusions
   * sur le {@code EventBroker} mémorisé à l'instant de l'appel à la méthode {@link
   * EventSubscriber#subscribe(Subject, Consumer)}. Lorsqu'un {@code EventPublisher} diffuse le
   * {@code Subject} par ce {@code EventBroker}, l'opération definie par le {@code Consumer} est
   * executée.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}, tel que {@code
   * this#getEventBroker()#equals(EventPublisher#getEventBroker())}.
   *
   * <p>Le changement du {@code EventBroker} de cette instance n'efface pas les abonnements faits
   * sur un autre {@code EventBroker} par le passé. Il est possible d'effectuer des
   * abonnements/desabonnements au {@code EventBroker} memorisé directement.
   *
   * @param broker Le {@code EventBroker} qui gere les abonnements de cette instance.
   * @see #subscribe(Subject, Consumer) Abonner un {@code Consumer} a un {@code Subject} sur le
   *     {@code EventBroker} actuel.
   * @see #unsubscribe(Subject, Consumer) Désabonner un {@code Consumer} d'un {@code Subject} sur le
   *     {@code EventBroker} actuel.
   */
  public void setEventBroker(EventBroker broker) {
    this.broker = broker;
  }

  /**
   * Getter pour le {@code EventBroker} de cette instance.
   *
   * <p>Les abonnements faits par cette instance de {@code EventSubscriber} ecoutent les diffusions
   * sur le {@code EventBroker} mémorisé à l'instant de l'appel à la méthode {@link
   * EventSubscriber#subscribe(Subject, Consumer)}. Lorsqu'un {@code EventPublisher} diffuse le
   * {@code Subject} par ce {@code EventBroker}, l'opération definie par le {@code Consumer} est
   * executée.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}.
   *
   * @return Le {@code EventBroker} qui gere les abonnements de cette instance. La valeur retournee
   *     peut etre {@code null}.
   */
  public EventBroker getEventBroker() {
    return broker;
  }

  /**
   * Methode permettant, avec le {@code EventBroker} memorisé par cette instance, d'abonner une
   * fonction de rappel {@code Consumer} a un {@code Subject} donné, de sorte que {@link
   * Consumer#accept(Object)} est appelé sur le {@code callback} passé en paramètre lorsqu'un {@code
   * EventPublisher} diffuse un {@code message} au {@code subject} sur le meme {@code EventBroker}.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}.
   *
   * <p>Le {@code Consumer} passé en paramètre est responsable de s'assurer, dans son
   * implémentation, que le type d'objet accepté en paramètre dans {@link Consumer#accept(Object)}
   * respecte le typage defini par {@link Subject#getMessageType()} du {@code Subject} auquel il
   * compte s'abonner, puisqu'il y a un risque que le {@code EventPublisher} diffuse un {@code
   * message} de type erroné ou inattendu.
   *
   * @param subject Le {@code Subject} dont la diffusion déclenchera l'execution du {@code callback}
   *     passé en paramètre
   * @param callback Le {@code Consumer} qui sera executée lors de la diffusion du {@code subject}
   *     passé en paramètre
   * @return {@code true} si le {@code callback} a été abonné au {@code subject} avec succès. {@code
   *     false} si l'instance ne possede pas de {@code EventBroker}.
   * @see Consumer Fonction de rappel qui prend un parametre et ne retourne rien
   */
  public boolean subscribe(Subject subject, Consumer<Object> callback) {
    return subscribe(broker, subject, callback);
  }

  /**
   * Methode permettant, avec un {@code EventBroker} quelconque, d'abonner une fonction de rappel
   * {@code Consumer} a un {@code Subject} donné, de sorte que {@link Consumer#accept(Object)} est
   * appelé sur le {@code callback} passé en paramètre lorsqu'un {@code EventPublisher} diffuse un
   * {@code message} au {@code subject} sur le meme {@code EventBroker}.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}.
   *
   * <p>Le {@code Consumer} passé en paramètre est responsable de s'assurer, dans son
   * implémentation, que le type d'objet accepté en paramètre dans {@link Consumer#accept(Object)}
   * respecte le typage defini par {@link Subject#getMessageType()} du {@code Subject} auquel il
   * compte s'abonner, puisqu'il y a un risque que le {@code EventPublisher} diffuse un {@code
   * message} de type erroné ou inattendu.
   *
   * @param broker Le {@code EventBroker} avec lequel l'abonnement est effectué
   * @param subject Le {@code Subject} dont la diffusion déclenchera l'execution du {@code callback}
   *     passé en paramètre
   * @param callback Le {@code Consumer} qui sera executée lors de la diffusion du {@code subject}
   *     passé en paramètre
   * @return {@code true} si le {@code callback} a été abonné au {@code subject} avec succès. {@code
   *     false} si l'instance ne possede pas de {@code EventBroker}.
   * @see Consumer Fonction de rappel qui prend un parametre et ne retourne rien
   */
  public boolean subscribe(EventBroker broker, Subject subject, Consumer<Object> callback) {
    if (broker == null) return false;

    return broker.subscribe(subject, callback);
  }

  /**
   * Methode permettant, avec le {@code EventBroker} memorisé par cette instance, d'abonner une
   * fonction de rappel {@code Consumer} a un {@code Subject} donné, de sorte que {@link
   * Consumer#accept(Object)} est appelé sur le {@code callback} passé en paramètre lorsqu'un {@code
   * EventPublisher} diffuse un {@code message} au {@code subject} sur le meme {@code EventBroker}.
   *
   * <p>Cette fonction de rappel sera associee a un identifiant specifique choisi par celui qui
   * appelle la methode, de maniere a pouvoir retirer tous les {@code Consumer} associes a
   * l'identifiant facilement.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}.
   *
   * <p>Le {@code Consumer} passé en paramètre est responsable de s'assurer, dans son
   * implémentation, que le type d'objet accepté en paramètre dans {@link Consumer#accept(Object)}
   * respecte le typage defini par {@link Subject#getMessageType()} du {@code Subject} auquel il
   * compte s'abonner, puisqu'il y a un risque que le {@code EventPublisher} diffuse un {@code
   * message} de type erroné ou inattendu.
   *
   * @param subject Le {@code Subject} dont la diffusion déclenchera l'execution du {@code callback}
   *     passé en paramètre
   * @param callback Le {@code Consumer} qui sera executée lors de la diffusion du {@code subject}
   *     passé en paramètre
   * @param id L'identifiant associe a ce {@code callback}
   * @return {@code true} si le {@code callback} a été abonné au {@code subject} avec succès. {@code
   *     false} si l'instance ne possede pas de {@code EventBroker}.
   * @see Consumer Fonction de rappel qui prend un parametre et ne retourne rien
   */
  public boolean subscribeWithID(Subject subject, Consumer<Object> callback, int id) {
    return subscribeWithID(broker, subject, callback, id);
  }

  /**
   * Methode permettant, avec un {@code EventBroker} quelconque, d'abonner une fonction de rappel
   * {@code Consumer} a un {@code Subject} donné, de sorte que {@link Consumer#accept(Object)} est
   * appelé sur le {@code callback} passé en paramètre lorsqu'un {@code EventPublisher} diffuse un
   * {@code message} au {@code subject} sur le meme {@code EventBroker}.
   *
   * <p>Cette fonction de rappel sera associee a un identifiant specifique choisi par celui qui
   * appelle la methode, de maniere a pouvoir retirer tous les {@code Consumer} associes a
   * l'identifiant facilement.
   *
   * <p>Pour que les {@code EventPublisher} puissent communiquer avec les {@code EventSubscriber},
   * ils doivent etre liés au même {@code EventBroker}.
   *
   * <p>Le {@code Consumer} passé en paramètre est responsable de s'assurer, dans son
   * implémentation, que le type d'objet accepté en paramètre dans {@link Consumer#accept(Object)}
   * respecte le typage defini par {@link Subject#getMessageType()} du {@code Subject} auquel il
   * compte s'abonner, puisqu'il y a un risque que le {@code EventPublisher} diffuse un {@code
   * message} de type erroné ou inattendu.
   *
   * @param broker Le {@code EventBroker} avec lequel effectuer l'abonnement
   * @param subject Le {@code Subject} dont la diffusion déclenchera l'execution du {@code callback}
   *     passé en paramètre
   * @param callback Le {@code Consumer} qui sera executée lors de la diffusion du {@code subject}
   *     passé en paramètre
   * @param id L'identifiant associe a ce {@code callback}
   * @return {@code true} si le {@code callback} a été abonné au {@code subject} avec succès. {@code
   *     false} si l'instance ne possede pas de {@code EventBroker}.
   * @see Consumer Fonction de rappel qui prend un parametre et ne retourne rien
   */
  public boolean subscribeWithID(
      EventBroker broker, Subject subject, Consumer<Object> callback, int id) {
    if (broker == null) return false;

    return broker.subscribeWithID(subject, callback, id);
  }

  /**
   * Methode permettant, avec le {@code EventBroker} mémoriser par cette instance, de desabonner une
   * fonction de rappel {@code Consumer} d'un {@code Subject} donné, de sorte que {@link
   * Consumer#accept(Object)} ne soit plus appelé sur le {@code callback} passé en paramètre lors de
   * la diffusion du {@code Subject}.
   *
   * <p>En d'autres termes, le {@link EventSubscriber#broker} mémorisé à l'instant de l'appel a
   * cette methode va effacer la reference au {@code Consumer} dans la liste de {@code Consumer} du
   * {@code Subscription} associé au {@code Subject}.
   *
   * <p>Pour se désabonner avec succès, la référence au {@code Consumer} a désabonner doit être la
   * même que celui passé en paramètre, tel que {@code consumer1.equals(consumer2)}.
   *
   * @param subject Le {@code Subject} duquel le {@code callback} sera désabonné
   * @param callback Le {@code Consumer} qui arretera d'etre déclenché lors de la diffusion du
   *     {@code Subject}
   * @return {@code true} si le {@code callback} a été désabonné du {@code subject} avec succès,
   *     {@code false} si la reference au {@code callback} n'etait pas presente dans la liste du
   *     {@code Subscription}, ou si le {@code EventBroker} de cette instance est {@code null}
   */
  public boolean unsubscribe(Subject subject, Consumer<Object> callback) {
    return unsubscribe(broker, subject, callback);
  }

  /**
   * Methode permettant, avec un {@code EventBroker} quelconque, de desabonner une fonction de
   * rappel {@code Consumer} d'un {@code Subject} donné, de sorte que {@link
   * Consumer#accept(Object)} ne soit plus appelé sur le {@code callback} passé en paramètre lors de
   * la diffusion du {@code Subject}.
   *
   * <p>En d'autres termes, le {@code EventBroker} passé en parametre a cette methode va effacer la
   * reference au {@code Consumer} dans la liste de {@code Consumer} du {@code Subscription} associé
   * au {@code Subject}.
   *
   * <p>Pour se désabonner avec succès, la référence au {@code Consumer} a désabonner doit être la
   * même que celui passé en paramètre, tel que {@code consumer1.equals(consumer2)}.
   *
   * @param broker Le {@code EventBroker} avec lequel le desabonnement est effectué
   * @param subject Le {@code Subject} duquel le {@code callback} sera désabonné
   * @param callback Le {@code Consumer} qui arretera d'etre déclenché lors de la diffusion du
   *     {@code Subject}
   * @return {@code true} si le {@code callback} a été désabonné du {@code subject} avec succès,
   *     {@code false} si la reference au {@code callback} n'etait pas presente dans la liste du
   *     {@code Subscription}, ou si le {@code EventBroker} passé en paramètre est {@code null}
   */
  public boolean unsubscribe(EventBroker broker, Subject subject, Consumer<Object> callback) {
    if (broker == null) return false;

    return broker.unsubscribe(subject, callback);
  }

  /**
   * Methode permettant de desabonner tous les {@code Consumer} abonnes au {@code EventBroker}, peu
   * importe le {@code Subject}
   *
   * @param subject Le {@code Subject} duquel desabonner
   * @param id L'identifiant associe aux {@code Consumer}
   * @return {@code true} si les abonnements ont ete retires avec succes
   */
  public boolean unsubscribeWithID(Subject subject, Integer id) {
    return unsubscribeWithID(broker, subject, id);
  }

  /**
   * Methode permettant de desabonner tous les {@code Consumer} abonnes au {@code EventBroker}, peu
   * importe le {@code Subject}
   *
   * @param id L'identifiant associe aux {@code Consumer}
   * @return {@code true} si les abonnements ont ete retires avec succes
   */
  public boolean unsubscribeAllWithID(Integer id) {
    return unsubscribeAllWithID(broker, id);
  }

  /**
   * Methode permettant de desabonner tous les {@code Consumer} abonnes au {@code EventBroker}, peu
   * importe le {@code Subject}
   *
   * @param broker Le {@code EventBroker} duquel desabonner
   * @param subject Le {@code Subject} duquel desabonner
   * @param id L'identifiant associe aux {@code Consumer}
   * @return {@code true} si les abonnements ont ete retires avec succes
   */
  public boolean unsubscribeWithID(EventBroker broker, Subject subject, Integer id) {
    if (broker == null) return false;

    return broker.unsubscribeWithID(subject, id);
  }

  /**
   * Methode permettant de desabonner tous les {@code Consumer} abonnes au {@code EventBroker}, peu
   * importe le {@code Subject}
   *
   * @param broker Le {@code EventBroker} duquel desabonner
   * @param id L'identifiant associe aux {@code Consumer}
   * @return {@code true} si les abonnements ont ete retires avec succes
   */
  public boolean unsubscribeAllWithID(EventBroker broker, Integer id) {
    if (broker == null) return false;

    return broker.unsubscribeAllWithID(id);
  }
}
