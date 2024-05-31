package com.e24.wolke.backend.models;

import com.e24.wolke.backend.controller.Controller;
import com.e24.wolke.filesystem.Loadable;
import com.e24.wolke.filesystem.Saveable;

/**
 * La classe {@code WModelSaveable} permet d'abstraire les modeles qui peuvent etre sauvegardés ou
 * chargés
 *
 * @author MeriBouisri
 */
public abstract class WModelSaveable extends WModel implements Saveable, Loadable {

  /** Les {@code WProperties} du modele, s'il y a lieu */
  private WProperties properties;

  /**
   * Construction d'un {@code WModelSaveable} avec un {@code Controller}
   *
   * @param controller Le {@code Controller} de cette instance
   */
  public WModelSaveable(Controller controller) {
    super(controller);
  }

  /**
   * Getter pour {@code this#properties}
   *
   * @return Le {@code WProperties} de cette instance
   */
  protected WProperties getProperties() {
    return properties;
  }

  /**
   * Setter pour {@code this#properties}
   *
   * @param properties Le {@code WProperties} de cette instance
   */
  protected void setProperties(WProperties properties) {
    this.properties = properties;
  }

  /**
   * Methode permettant d'actualiser le modele apres le changement de une ou plusieurs proprietes
   */
  protected abstract void onPropertiesChanged();

  /** {@inheritDoc} */
  @Override
  public boolean save() {
    try {
      getProperties().writeProperties();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public boolean load() {
    try {
      this.getProperties().readProperties();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    onPropertiesChanged();
    return true;
  }
}
