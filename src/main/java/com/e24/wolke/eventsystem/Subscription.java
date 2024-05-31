package com.e24.wolke.eventsystem;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * La classe {@code Subscription} gere les fonctions de rappel {@code Consumer} pour un {@code
 * Subject} specifique.
 *
 * @author MeriBouisri
 */
public class Subscription implements Comparable<Subscription> {

  /** Le {@code Subject} associé à cette instance */
  private final Subject subject;

  /**
   * Liste contenant la reference des {@code Consumer} a invoquer lors de la diffusion du {@code
   * Subject} de cette instance
   */
  private ConcurrentLinkedQueue<Consumer<Object>> callbacks;

  /**
   * Construction d'un {@code Subscription} associé au {@code Subject} passé en paramètre.
   *
   * @param subject Le {@code Subject} associé à cette instance
   */
  protected Subscription(Subject subject) {
    this.subject = subject;
    callbacks = new ConcurrentLinkedQueue<Consumer<Object>>();
  }

  /**
   * Methode permettant de transmettre un {@code message} a tous les {@code callback} abonnés au
   * {@code Subject} associé à cette instance, de sorte que {@code Consumer#accept(Object)} est
   * invoqué avec le {@code message} passé en paramètre.
   *
   * @param message {@code Object} a transmettre aux {@code Consumer} dans la liste de {@code
   *     callbacks} pour le {@code Subject} associé à cette instance.
   * @return {@code true} si le {@code message} a ete transmis aux {@code callbacks}, {@code false}
   *     si le type du message ne respecte par le type attendu pour le {@code Subject} de cette
   *     instance.
   * @see Subject#isValidMessage(Object) Conditions a respecter pour transmettre un message aux
   *     abonnés d'un {@code Subject}
   */
  protected boolean invokeCallbacks(Object message) {
    if (!subject.isValidMessage(message)) return false;

    callbacks.forEach(callback -> callback.accept(message));
    return true;
  }

  /**
   * Methode permettant d'abonner un {@code Consumer} a ce {@code Subscription}, de sorte que le
   * {@code Consumer} sera invoqué lors de la diffusion du {@code Subject} par le {@code
   * EventBroker} responsable de ce {@code Subscription}.
   *
   * @param callback le {@code Consumer} a invoquer lors de la diffusion du {@code Subject} associé
   *     à cette instance
   * @return {@code true} si le {@code callback} a ete ajouté à la liste de {@code callbacks} avec
   *     succès
   * @see #invokeCallbacks(Object)
   */
  protected boolean subscribe(Consumer<Object> callback) {
    return callbacks.add(callback);
  }

  /**
   * Methode permettant de desabonner un {@code Consumer} de ce {@code Subscription}, de sorte que
   * le {@code Consumer} ne soit plus invoqué.
   *
   * @param callback le {@code Consumer} qui doit etre retiré, si present, de la liste de {@code
   *     callbacks} invoqués lors de la diffusion du {@code Subject} associé à cette instance
   * @return {@code true} si le {@code callback} a ete retiré de la liste de {@code callbacks} avec
   *     succès
   * @see #invokeCallbacks(Object)
   */
  protected boolean unsubscribe(Consumer<Object> callback) {
    return callbacks.remove(callback);
  }

  /**
   * Methode permettant de desabonner une collection de {@code Consumer} de ce {@code Subscription},
   * de sorte que les {@code Consumer} ne soientt plus invoqués.
   *
   * @param callbacks la collection de {@code Consumer} qui doit etre retirés, si present, de la
   *     liste de {@code callbacks} invoqués lors de la diffusion du {@code Subject} associé à cette
   *     instance
   * @return {@code true} si les {@code callback} communs ont ete retiré de la liste de {@code
   *     callbacks} avec succès
   * @see #invokeCallbacks(Object)
   */
  protected boolean unsubscribeAll(Collection<Consumer<Object>> callbacks) {
    return this.callbacks.removeAll(callbacks);
  }

  /**
   * Getter pour le {@code Subject} associé à cette instance de {@code SUbscription}
   *
   * @return Le {@code Subject} associé à cette instance
   */
  protected Subject getSubject() {
    return subject;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Un objet de type {@code Subcription} possede le meme {@code hashCode} que le {@code Subject}
   * qui lui est associé.
   */
  @Override
  public int hashCode() {
    return subject.hashCode();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Un objet de type {@code Subscription} est egal a un autre s'ils sont associé au meme {@code
   * Subject}.
   *
   * @see Subscription#hashCode()
   */
  @Override
  public boolean equals(Object obj) {
    return hashCode() == obj.hashCode();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Les objets de type {@code Subscription} sont comparés selon le {@code Subject} qui leur est
   * associé
   */
  @Override
  public int compareTo(Subscription subscription) {
    return subject.compareTo(subscription.getSubject());
  }
}
