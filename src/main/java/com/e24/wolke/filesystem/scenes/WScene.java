package com.e24.wolke.filesystem.scenes;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * La classe {@code WScene} permet de definir une scene formee d'une image d'obstacles et des
 * proprietes de simulation et de visualisation.
 *
 * @author MeriBouisri
 */
public class WScene {

  /** Nom de la scene */
  private String name;

  /** l'image de la scene */
  private BufferedImage editorImage;

  /** Les proprietes de la simulation */
  private Properties simulationProperties;

  /** Les proprietes de la simulation */
  private Properties rendererProperties;

  /**
   * Construction d'une scene avec une image et les donnees de simulation et de visualisation.
   *
   * @param editorImage L'image destinee a l'editeur
   * @param simulationProperties Les proprietes de la simulation
   * @param rendererProperties Les proprietes de visualisation
   */
  public WScene(
      BufferedImage editorImage, Properties simulationProperties, Properties rendererProperties) {
    this.name = String.valueOf(this.hashCode());
    this.editorImage = editorImage;
    this.simulationProperties = simulationProperties;
    this.rendererProperties = rendererProperties;
  }

  /**
   * Construction d'une scene avec une image et les donnees de simulation et de visualisation.
   *
   * @param name Nom de la scene
   * @param editorImage L'image destinee a l'editeur
   * @param simulationProperties Les proprietes de la simulation
   * @param rendererProperties Les proprietes de visualisation
   */
  public WScene(
      String name,
      BufferedImage editorImage,
      Properties simulationProperties,
      Properties rendererProperties) {
    this.name = name;
    this.editorImage = editorImage;
    this.simulationProperties = simulationProperties;
    this.rendererProperties = rendererProperties;
  }

  /**
   * Construction d'une scene avec un nom
   *
   * @param name Nom de la scene
   */
  public WScene(String name) {
    this(name, null, null, null);
  }

  /** Construction d'une scene */
  public WScene() {
    this(null, null, null);
  }

  /**
   * Getter pour {@code this#editorImage}, le {@code BufferedImage} destine a l'editeur
   *
   * @return Le {@code BufferedImage} destine a l'editeur
   */
  public BufferedImage getEditorImage() {
    return editorImage;
  }

  /**
   * Setter pour {@code this#editorImage}, le {@code BufferedImage} destine a l'editeur
   *
   * @param editorImage Le {@code BufferedImage} destine a l'editeur
   */
  protected void setEditorImage(BufferedImage editorImage) {
    this.editorImage = editorImage;
  }

  /**
   * Getter pour {@code this#simulationProperties}, le {@code Properties} destine au {@code
   * SimulationModel}
   *
   * @return Le {@code Properties} destine au {@code SimulationModel}
   */
  public Properties getSimulationProperties() {
    return simulationProperties;
  }

  /**
   * Setter pour {@code this#simulationProperties}, le {@code Properties} destine au {@code
   * SimulationModel}
   *
   * @param simulationProperties Le {@code Properties} destine au {@code SimulationModel}
   */
  protected void setSimulationProperties(Properties simulationProperties) {
    this.simulationProperties = simulationProperties;
  }

  /**
   * Getter pour {@code this#rendererProperties}, le {@code Properties} destine au {@code
   * RendererModel}
   *
   * @return Le {@code Properties} destine au {@code RendererModel}
   */
  public Properties getRendererProperties() {
    return rendererProperties;
  }

  /**
   * Setter pour {@code this#rendererProperties}, le {@code Properties} destine au {@code
   * RendererModel}
   *
   * @param rendererProperties Le {@code Properties} destine au {@code RendererModel}
   */
  protected void setRendererProperties(Properties rendererProperties) {
    this.rendererProperties = rendererProperties;
  }

  /**
   * Getter pour {@code this#name}
   *
   * @return Le nom de la scene
   */
  public String getName() {
    return this.name;
  }

  /**
   * Setter pour {@code this#name}
   *
   * @param name Le nom de la scene
   */
  public void setName(String name) {
    this.name = name;
  }
}
