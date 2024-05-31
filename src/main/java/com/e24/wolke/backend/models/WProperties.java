package com.e24.wolke.backend.models;

import com.e24.wolke.filesystem.properties.PropertiesManager;
import com.e24.wolke.filesystem.properties.SettingsPropertiesManager;
import com.e24.wolke.filesystem.properties.WPropertyProcessor;
import java.io.Serializable;

/**
 * La classe {@code WProperties} permet de definir les proprietes d'un {@code WModel}
 *
 * @author MeriBouisri
 */
public abstract class WProperties implements WPropertyProcessor, Serializable {
  /** SerialVersionUID de la classe */
  private static final long serialVersionUID = 1L;

  /** Le {@code PropertiesManager} par defaut */
  public static final PropertiesManager DEFAULT_MANAGER = SettingsPropertiesManager.INSTANCE;

  /** L'etat de sauvegarde par defaut */
  public static final boolean DEFAULT_SAVE = true;

  /** Le {@code PropertiesManager} de la classe */
  private PropertiesManager manager;

  /**
   * Valeur permettant de determiner si le fichier est enregistré apres l'ecriture avec cette
   * instance
   */
  private boolean save;

  /**
   * Construction d'un {@code WProperties} avec un {@code PropertiesManager}
   *
   * @param manager Le {@code PropertiesManager} de cette instance
   * @param save Si le fichier doit etre enregistré apres l'ecriture
   */
  public WProperties(PropertiesManager manager, boolean save) {
    this.manager = manager;
    this.save = save;
  }

  /**
   * Construction d'un {@code WProperties} avec un {@code PropertiesManager}. L'ecriture n'est pas
   * sauvegardee.
   *
   * @param manager Le {@code PropertiesManager} de cette instance
   */
  public WProperties(PropertiesManager manager) {
    this(manager, WProperties.DEFAULT_SAVE);
  }

  /**
   * Construction d'un {@code WProperties} avec l'etat d'enregistrement
   *
   * @param save Si le fichier doit etre enregistré apres l'ecriture
   */
  public WProperties(boolean save) {
    this(WProperties.DEFAULT_MANAGER, save);
  }

  /** Construction d'un {@code WProperties} */
  public WProperties() {
    this(WProperties.DEFAULT_MANAGER, WProperties.DEFAULT_SAVE);
  }

  /**
   * Getter pour le {@code PropertiesManager} de cette instance
   *
   * @return le {@code PropertiesManager} de cette instance
   */
  public PropertiesManager getPropertiesManager() {
    return manager;
  }

  /**
   * Setter pour le {@code PropertiesManager} de cette instance
   *
   * @param manager le {@code PropertiesManager} de cette instance
   */
  protected void setPropertiesManager(PropertiesManager manager) {
    this.manager = manager;
  }

  /**
   * Getter pour {@code this#save}, qui permet de determiner si le fichier doit etre enregistré
   * apres l'ecriture
   *
   * @return {@code true} si le fichier doit etre enregistré apres l'ecriture
   */
  public boolean getSaveState() {
    return save;
  }

  /**
   * Setter pour {@code this#save}, qui permet de determiner si le fichier doit etre enregistré
   * apres l'ecriture
   *
   * @param save Si le fichier doit etre enregistré apres l'ecriture
   */
  protected void setSaveState(boolean save) {
    this.save = save;
  }
}
