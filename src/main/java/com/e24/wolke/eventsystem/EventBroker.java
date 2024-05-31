package com.e24.wolke.eventsystem;

import com.e24.wolke.utils.interfaces.Reinitializable;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * La classe {@code EventBroker} sert a gerer la communication entre les {@code EventSubscriber} et
 * les {@code EventPublisher}.
 *
 * @author MeriBouisri
 */
public class EventBroker implements Reinitializable {

  /** Arbre binaire contenant les {@code Subscription} */
  private TreeSet<Subscription> subscriptions;

  /**
   * Le {@code ConcurrentHashMap} permettant de memoriser les {@code Consumer} avec un identifiant
   * de type {@code Integer}
   */
  private ConcurrentHashMap<Integer, ArrayList<Consumer<Object>>> callbackIDMap;

  /** Construction d'un {@code EventBroker}. */
  public EventBroker() {
    subscriptions = new TreeSet<Subscription>();
    callbackIDMap = new ConcurrentHashMap<Integer, ArrayList<Consumer<Object>>>();
  }

  // ============================
  // SUBSCRIBER/PUBLISHER METHODS
  // ============================

  /**
   * Methode permettant d'abonner une fonction de rappel {@code Consumer} a un {@code Subject}
   * donné, de sorte que {@link Consumer#accept(Object)} est appelé sur le {@code callback} passé en
   * paramètre lorsqu'un {@code EventPublisher} diffuse un {@code message} au {@code subject} a
   * travers cette instance de {@code EventBroker}.
   *
   * @param subject Le {@code Subject} dont la diffusion déclenchera l'execution du {@code callback}
   *     passé en paramètre
   * @param callback Le {@code Consumer} qui sera executée lors de la diffusion du {@code subject}
   *     passé en paramètre
   * @return {@code true} si le {@code callback} a été abonné au {@code subject} avec succès.
   * @see Consumer Fonction de rappel qui prend un parametre et ne retourne rien
   */
  protected boolean subscribe(Subject subject, Consumer<Object> callback) {
    add(subject);
    return get(subject).subscribe(callback);
  }

  /**
   * Methode permettant de desabonner une fonction de rappel {@code Consumer} d'un {@code Subject}
   * donné, de sorte que {@link Consumer#accept(Object)} ne soit plus appelé sur le {@code callback}
   * passé en paramètre lors de la diffusion du {@code Subject}.
   *
   * <p>Pour se désabonner avec succès, la référence au {@code Consumer} a désabonner doit être la
   * même que celui passé en paramètre, tel que {@code consumer1.equals(consumer2)}.
   *
   * @param subject Le {@code Subject} duquel le {@code callback} sera désabonné
   * @param callback Le {@code Consumer} qui arretera d'etre déclenché lors de la diffusion du
   *     {@code Subject}
   * @return {@code true} si le {@code callback} a été désabonné du {@code subject} avec succès,
   *     {@code false} si la reference au {@code callback} n'etait pas presente dans la liste du
   *     {@code Subscription}.
   */
  protected boolean unsubscribe(Subject subject, Consumer<Object> callback) {
    if (!contains(subject)) return false;

    return get(subject).unsubscribe(callback);
  }

  /**
   * Methode permettant de diffuser un {@code Object} aux abonnés d'un {@code Subject} donné, de
   * sorte que la reference du {@code Object} est passé en paramètre à tous les {@code Consumer}
   * abonnés par un {@code EventSubscriber} sur le meme {@code EventBroker}.
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
   * @return {@code true} si la diffusion du message a ete effectuée avec succès
   */
  protected boolean publish(Subject subject, Object message) {
    if (!contains(subject)) return false;

    return get(subject).invokeCallbacks(message);
  }

  // =================
  // UTILITARY METHODS
  // =================

  /**
   * Methode permettant de verifier si la liste de {@code Subscription} de cette instance contient
   * un {@code Subscription} associé à un {@code Subject} donné.
   *
   * @param subject Le {@code Subject} associé au {@code Subscription} dont la presence doit etre
   *     verifiee
   * @return {@code true} si la liste de {@code Subscription} de cette instance contient un {@code
   *     Subscription} associé au {@code subject}
   */
  private boolean contains(Subject subject) {
    return subscriptions.contains(new Subscription(subject));
  }

  /**
   * Methode permettant d'abonner un {@code Consumer} a un {@code Subject} et d'associer un
   * identifiant au {@code Consumer}
   *
   * @param subject Le {@code Subject} auquel abonner le {@code Consumer}
   * @param callback Le {@code Consumer} a abonner au {@code Subject}
   * @param id L'identifiant a associer au {@code callback}
   * @return {@code true} si le {@code Consumer} a ete abonne avec succes, et que le {@code
   *     Consumer} est identifie
   */
  protected boolean subscribeWithID(Subject subject, Consumer<Object> callback, int id) {
    if (!subscribe(subject, callback)) return false;

    if (!callbackIDMap.containsKey(id)) callbackIDMap.put(id, new ArrayList<Consumer<Object>>());

    return callbackIDMap.get(id).add(callback);
  }

  /**
   * Methode permettant de desabonner les {@code Consumer} associes a un identifiant dans un {@code
   * Subject} donne
   *
   * @param subject Le {@code subject} duquel desabonner les {@code Consumer}
   * @param id L'identifiant des {@code Consumer} a retirer
   * @return {@code true} si les {@code Consumer} ont ete retires avec succes, {@code false}, si le
   *     {@code Subject} ou l'identifiant ne sont pas presents
   */
  protected boolean unsubscribeWithID(Subject subject, int id) {
    if (!contains(subject)) return false;

    if (!callbackIDMap.containsKey(id)) return false;

    return get(subject).unsubscribeAll(callbackIDMap.get(id));
  }

  /**
   * Methode permettant de desabonner les {@code Consumer} associes a un identifiant dans tous les
   * {@code Subject} de cette instance. La liste des {@code Consumer} identifies sont retires de la
   * liste {@code this#callbackIDMap}
   *
   * @param id L'identifiant des {@code Consumer} a retirer
   * @return {@code true} si au moins un {@code Subscription} a ete modifie par cette operation
   */
  protected boolean unsubscribeAllWithID(int id) {
    boolean success = false;

    if (!callbackIDMap.containsKey(id)) return false;

    for (Subscription subscription : subscriptions) {
      if (subscription.unsubscribeAll(callbackIDMap.get(id))) success = true;
    }

    clearID(id);

    return success;
  }

  /**
   * Methode permettant de retirer l'identifiant de la liste qui memorise les identifiants.
   *
   * @param id L'identifiant a retirer
   * @return La liste des {@code Consumer} associes a l'identifiant, {@code null} si l'identifiant
   *     n'est pas present
   */
  protected ArrayList<Consumer<Object>> clearID(int id) {
    if (!callbackIDMap.containsKey(id)) return null;

    return callbackIDMap.remove(id);
  }

  /**
   * Methode permettant d'obtenir la reference a un {@code Subcription} associé à un {@code Subject}
   * donné, s'il est present dans la liste de {@code Subscription} de cette instance
   *
   * @param subject Le {@code Subject} associé au {@code Subscription} a obtenir
   * @return Le {@code Subscription} associé au {@code subject}, {@code null} si la liste de {@code
   *     Subscription} ne contient pas un {@code Subscription} associé au {@code Subject}
   */
  private Subscription get(Subject subject) {
    if (!contains(subject)) return null;

    return subscriptions.stream().filter(sub -> sub.equals(subject)).findAny().get();
  }

  /**
   * Methode permettant d'ajouter, si non-present, un {@code Subscription} associé au {@code
   * Subject} passé passé en paramètre dans la liste de {@code EventBroker#subscriptions} de cette
   * instance,
   *
   * @param subject Le {@code Subject} associé au {@code Subscription} a ajouter
   * @return {@code true} si l'ajout a ete effectué avec succès, {@code false} si la liste contient
   *     deja le {@code Subscription}
   */
  private boolean add(Subject subject) {
    return subscriptions.add(new Subscription(subject));
  }

  /** {@inheritDoc} La liste de {@code Subscription} de cette instance est videe. */
  @Override
  public void reinitialize() {
    subscriptions.removeIf(o -> true);
    callbackIDMap.clear();
  }
}
