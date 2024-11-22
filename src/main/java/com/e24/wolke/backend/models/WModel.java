package com.e24.wolke.backend.models;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.eventsystem.WEventMember;

/**
 * La classe {@code WModel} permet de definir un objet géré par un {@code Controller}.
 *
 * @see Controller
 * @author MeriBouisri
 * @author adrienles
 */
public abstract class WModel extends WEventMember {

  /** Le {@code Controller} de cette instance */
  private Controller controller;

  /** Valeur permettant de determiner si cette instance a ete initialisee */
  private boolean isInitialized;

  /**
   * Construction d'un {@code WModel} avec un {@code Controller} global. La methode {@code
   * this#initialize()} n'est pas appelee dans le constructeur.
   *
   * @param controller Le {@code Controller} qui gere cette instance
   */
  public WModel(Controller controller) {
    super();
    this.setController(controller);
  }

  /** Construction d'un {@code WModel} par defaut. */
  public WModel() {
    super();
  }

  /**
   * Setter pour le {@code Controller} de cette instance. Le {@code EventBroker} du {@code
   * Controller} est memorisé en tant que {@code EventBroker} global de cette instance
   *
   * @param controller Le {@code Controller} qui gere cette instance de {@code WModel}.
   */
  public void setController(Controller controller) {
    this.controller = controller;
  }

  /**
   * Getter pour le {@code Controller} de cette instance. Le {@code Controller} peut etre {@code
   * null}.
   *
   * @return Le {@code Controller} de cette instance
   */
  public Controller getController() {
    return controller;
  }

  /**
   * Methode permettant d'initialiser cette instance. Si elle n'est pas deja initialisee, la methode
   * {@code WModel#setup()} est appelee. Si cette instance est deja initialisee, {@code this#setup}
   * n'est pas appelee. La valeur de {@code this#isInitialized} est modifiee selon le succes de
   * l'initialisation (valeur retournee par l'appel a {@code WModel#setup()})
   *
   * @see WModel#setup()
   */
  public void initialize() {
    if (isInitialized) return;

    // isInitialized = true si le setup a ete effectue avec succes
    isInitialized = setup();
  }

  /**
   * Si cette instance de {@code WModel} a ete initialisee
   *
   * @return {@code true} si cette instance a ete initialisee
   */
  public boolean isInitialized() {
    return isInitialized;
  }

  /**
   * Setter pour {@code this#isInitialized}, l'etat d'initialisation de cette instance
   *
   * @param isInitialized Si cette instance a ete initialisee ou non
   */
  protected void setInitialized(boolean isInitialized) {
    this.isInitialized = isInitialized;
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    super.reinitialize();
    softReinitialize();
  }

  /**
   * Methode appelee lors de l'initialisation de cette instance.
   *
   * @return {@code true} si l'initialisation a ete effectuee avec succes
   */
  protected abstract boolean setup();

  /**
   * Methode permettant de reinitialiser le {@code WModel} sans reinitialiser le {@code EventBroker}
   * de l'instance.
   */
  public abstract void softReinitialize();

  /** Methode permettant de definir les abonnements aux evenements de l'instance. */
  public abstract void setupKeybindSubscriptions();
}
