package com.e24.wolke.backend.simulation.physics;

import com.e24.wolke.backend.models.renderer.RendererConstants.VisualizationType;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.utils.math.WDoubleMatrix;
import com.e24.wolke.utils.math.WMath;

/**
 * ParticleMatrix.java
 *
 * <p>Cette classe est responsable de contenir les particules de la simulation
 *
 * @author Nilon123456789
 * @author adrienles
 * @author MeriBouisri
 */
public class ParticleMatrix {

  /** Velocité en x */
  private final WDoubleMatrix xVelocity;

  /** Valeur minimale et maximale de la vélocité en x */
  private double[] xVelocityMinMax;

  /** Velocité en y */
  private final WDoubleMatrix yVelocity;

  /** Valeur minimale et maximale de la vélocité en y */
  private double[] yVelocityMinMax;

  /** Module de la velocité */
  private final WDoubleMatrix velocity;

  /** Valeur minimale et maximale du module de la velocité */
  private double[] velocityMinMax;

  /** Pression */
  private final WDoubleMatrix pressure;

  /** Valeur minimale et maximale de la pression */
  private double[] pressureMinMax;

  /** Température */
  private final WDoubleMatrix temperature;

  /** Valeur minimale et maximale de la température */
  private double[] temperatureMinMax;

  /** Densité de zone */
  private final WDoubleMatrix areaDensity;

  /** Valeur minimale et maximale de la densité de zone */
  private final double[] areaDensityMinMax = {0, 1};

  /** Divergence de la vélocité */
  private final WDoubleMatrix velocityDivergence;

  /** Valeur minimale et maximale de la divergence de la vélocité */
  private double[] velocityDivergenceMinMax;

  /** Gradient de pression en x */
  private final WDoubleMatrix xPressureGradient;

  /** Valeur minimale et maximale du gradient de pression en x */
  private double[] xPressureGradientMinMax;

  /** Gradient de pression en y */
  private final WDoubleMatrix yPressureGradient;

  /** Valeur minimale et maximale du gradient de pression en y */
  private double[] yPressureGradientMinMax;

  /** Curl de la vélocité */
  private final WDoubleMatrix velocityCurl;

  /** Valeur minimale et maximale du curl */
  private double[] velocityCurlMinMax;

  /** Longueur en x de la matrice */
  private final int xLength;

  /** Longueur en y de la matrice */
  private final int yLength;

  /** Taille totale de la matrice */
  private final int size;

  /**
   * Constructeur de la classe ParticleMatrix
   *
   * @param xLength La taille en x de la matrice de particules
   * @param yLength La taille en y de la matrice de particules
   */
  public ParticleMatrix(int xLength, int yLength) {

    size = xLength * yLength;
    this.xLength = xLength;
    this.yLength = yLength;

    xVelocity = new WDoubleMatrix(xLength, yLength);
    yVelocity = new WDoubleMatrix(xLength, yLength);
    velocity = new WDoubleMatrix(xLength, yLength);
    pressure = new WDoubleMatrix(xLength, yLength);
    temperature = new WDoubleMatrix(xLength, yLength);
    areaDensity = new WDoubleMatrix(xLength, yLength);
    velocityDivergence = new WDoubleMatrix(xLength, yLength);
    xPressureGradient = new WDoubleMatrix(xLength, yLength);
    yPressureGradient = new WDoubleMatrix(xLength, yLength);
    velocityCurl = new WDoubleMatrix(xLength, yLength);

    xVelocityMinMax = new double[4];
    yVelocityMinMax = new double[4];
    velocityMinMax = new double[4];
    pressureMinMax = new double[4];
    temperatureMinMax = new double[4];
    velocityCurlMinMax = new double[4];
    velocityDivergenceMinMax = new double[4];
    xPressureGradientMinMax = new double[4];
    yPressureGradientMinMax = new double[4];

    for (int i = 0; i < size; i++) {
      temperature.getMatrix()[i] = SimulationConstants.DEFAULT_TEMPERATURE_DEG;
    }
  }

  /**
   * Retourne la position (x, y) à partir de la position pos. La position pos est équivalente à y *
   * xLength + x
   *
   * @param pos La position
   * @return La position (x, y) à partir de la position pos
   */
  public int[] getXAndY(int pos) {
    return new int[] {pos % xLength, pos / xLength};
  }

  /**
   * Retourne la position dans les matrice en fonction des coordonnées x et y
   *
   * @param x Coordonnée X
   * @param y Coordonnée Y
   * @return La position dans la matrice
   */
  public int getPos(int x, int y) {
    return x + y * xLength;
  }

  /**
   * Vérifie si la position n'est pas dans l'intervalle
   *
   * @param pos Position a vérifier
   * @return vrai si la position est en dehors de l'intervalle
   */
  public boolean isPositionOutsideRange(int pos) {
    return (pos < 0) || (pos >= size);
  }

  /**
   * Retourne la longueur en x de la matrice de particules
   *
   * @return La longueur en x de la matrice de particules
   */
  public int getXLength() {
    return xLength;
  }

  /**
   * Retourne la longueur en y de la matrice de particules
   *
   * @return La longueur en y de la matrice de particules
   */
  public int getYLength() {
    return yLength;
  }

  /**
   * Retourne la taille totale de la matrice de particules
   *
   * @return La taille totale de la matrice de particules
   */
  public int getSize() {
    return size;
  }

  /**
   * Retourne la longueur en x et en y de la matrice de particules
   *
   * @return La longueur en x et en y de la matrice de particules {@code int[2] = {xLength,
   *     yLength}}
   */
  public int[] getXandYLength() {
    return new int[] {xLength, yLength};
  }

  /**
   * Retourne la vélocité en x
   *
   * @return La vélocité en x à la position pos
   */
  public WDoubleMatrix getXVelocity() {
    return xVelocity;
  }

  /**
   * Retourne la vélocité en x à la position pos
   *
   * @param pos La position
   * @return La vélocité en x à la position pos
   */
  public double getXVelocityAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return xVelocity.getMatrix()[pos];
  }

  /**
   * Modifie la vélocité en x à la position pos
   *
   * @param pos La position
   * @param xVelocity La vélocité en x
   */
  public void setXVelocityAt(int pos, double xVelocity) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    this.xVelocity.getMatrix()[pos] = xVelocity;
  }

  /**
   * Retourne la vélocité en y
   *
   * @return La vélocité en y à la position pos
   */
  public WDoubleMatrix getYVelocity() {
    return yVelocity;
  }

  /**
   * Retourne la vélocité en y à la position pos
   *
   * @param pos La position
   * @return La vélocité en y à la position pos
   */
  public double getYVelocityAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return yVelocity.getMatrix()[pos];
  }

  /**
   * Modifie la vélocité en y à la position pos
   *
   * @param pos La position
   * @param yVelocity La vélocité en y
   */
  public void setYVelocityAt(int pos, double yVelocity) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    this.yVelocity.getMatrix()[pos] = yVelocity;
  }

  /**
   * Retourne le module de la vélocité
   *
   * @return le module de la vélocité à la position pos
   */
  public WDoubleMatrix getVelocity() {
    return velocity;
  }

  /**
   * Retourne le module de la vélocité à la position pos
   *
   * @param pos La position
   * @return le module de la vélocité à la position pos
   */
  public double getVelocityAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return velocity.getMatrix()[pos];
  }

  /**
   * Modifie le module de la vélocité à la position pos
   *
   * @param pos La position
   * @param velocity le module de la vélocité
   */
  public void setVelocityAt(int pos, double velocity) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    this.velocity.getMatrix()[pos] = velocity;
  }

  /**
   * Retourne la pression
   *
   * @return La pression à la position pos
   */
  public WDoubleMatrix getPressure() {
    return pressure;
  }

  /**
   * Retourne la pression à la position pos
   *
   * @param pos La position
   * @return La pression à la position pos
   */
  public double getPressureAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return pressure.getMatrix()[pos];
  }

  /**
   * Modifie la pression à la position pos
   *
   * @param pos La position
   * @param pressure La pression
   */
  public void setPressureAt(int pos, double pressure) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    this.pressure.getMatrix()[pos] = pressure;
  }

  /**
   * Retourne la divergence de la vélocité
   *
   * @return La divergence de la vélocité à la position pos
   */
  public WDoubleMatrix getVelocityDivergence() {
    return velocityDivergence;
  }

  /**
   * Retourne la matrice du gradient de pression en x
   *
   * @return La matrice du gradient de pression en x
   */
  public WDoubleMatrix getXPressureGradient() {
    return xPressureGradient;
  }

  /**
   * Retourne le gradient de pression en x à la position pos
   *
   * @param pos La position
   * @return Le gradient de pression en x à la position pos
   */
  public double getXPressureGradientAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return xPressureGradient.getMatrix()[pos];
  }

  /**
   * Retourne la matrice du gradient de pression en y
   *
   * @return La matrice du gradient de pression en y
   */
  public WDoubleMatrix getYPressureGradient() {
    return yPressureGradient;
  }

  /**
   * Retourne le gradient de pression en y à la position pos
   *
   * @param pos La position
   * @return Le gradient de pression en y à la position pos
   */
  public double getYPressureGradientAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return yPressureGradient.getMatrix()[pos];
  }

  /**
   * Retourne la température
   *
   * @return La température à la position pos
   */
  public WDoubleMatrix getTemperature() {
    return temperature;
  }

  /**
   * Retourne la température à la position pos
   *
   * @param pos La position
   * @return La température à la position pos
   */
  public double getTemperatureAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return temperature.getMatrix()[pos];
  }

  /**
   * Modifie la température à la position pos
   *
   * @param pos La position
   * @param temperature La température
   */
  public void setTemperatureAt(int pos, double temperature) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    this.temperature.getMatrix()[pos] = temperature;
  }

  /**
   * Retourne le curl de la vélocité
   *
   * @return Le curl de la vélocité à la position pos
   */
  public WDoubleMatrix getVelocityCurl() {
    return this.velocityCurl;
  }

  /**
   * Retourne le min et max du curl
   *
   * @return Le min et max du curl [min, max, sumMin, sumMax]
   */
  public double[] getVelocityCurlMinMax() {
    return this.velocityCurlMinMax;
  }

  /**
   * Modifie le min et max du curl
   *
   * @param min Le min du curl
   * @param max Le max du curl
   * @param i L'itération actuelle
   */
  public void setVelocityCurlMinMax(double min, double max, int i) {
    ParticleMatrix.setMinMax(this.velocityCurlMinMax, min, max, i);
  }

  /**
   * Retourne la densité de zone
   *
   * @return La densité de zone à la position pos
   */
  public WDoubleMatrix getAreaDensity() {
    return areaDensity;
  }

  /**
   * Retourne la densité de zone à la position pos
   *
   * @param pos La position
   * @return La densité de zone à la position pos
   */
  public double getAreaDensityAt(int pos) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    return areaDensity.getMatrix()[pos];
  }

  /**
   * Modifie la densité de zone à la position pos
   *
   * @param pos La position
   * @param areaDensity La densité de zone
   */
  public void setAreaDensityAt(int pos, double areaDensity) {
    if (isPositionOutsideRange(pos)) throw new IndexOutOfBoundsException(pos);
    this.areaDensity.getMatrix()[pos] = areaDensity;
  }

  /**
   * Retourne la valeur minimale et maximale de la vélocité en x
   *
   * @return La valeur minimale et maximale de la vélocité en x [min, max, sumMin, sumMax]
   */
  public double[] getXVelocityMinMax() {
    return xVelocityMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale de la vélocité en x
   *
   * @param min La valeur minimale de la vélocité en x
   * @param max La valeur maximale de la vélocité en x
   * @param i Iteration actuelle
   */
  public void setXVelocityMinMax(double min, double max, int i) {
    ParticleMatrix.setMinMax(this.xVelocityMinMax, min, max, i);
  }

  /**
   * Retourne la valeur minimale et maximale de la vélocité en y
   *
   * @return La valeur minimale et maximale de la vélocité en y [min, max, sumMin, sumMax]
   */
  public double[] getYVelocityMinMax() {
    return yVelocityMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale de la vélocité en y
   *
   * @param min La valeur minimale de la vélocité en y
   * @param max La valeur maximale de la vélocité en y
   * @param i Iteration actuelle
   */
  public void setYVelocityMinMax(double min, double max, int i) {
    ParticleMatrix.setMinMax(this.yVelocityMinMax, min, max, i);
  }

  /**
   * Retourne la valeur minimale et maximale du module de la vélocité
   *
   * @return La valeur minimale et maximale du module de la vélocité [min, max, sumMin, sumMax]
   */
  public double[] getVelocityMinMax() {
    return velocityMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale du module de la vélocité
   *
   * @param min La valeur minimale du module de la vélocité
   * @param max La valeur maximale du module de la vélocité
   * @param i Iteration actuelle
   */
  public void setVelocityMinMax(double min, double max, int i) {
    ParticleMatrix.setMinMax(this.velocityMinMax, min, max, i);
  }

  /**
   * Retourne la valeur minimale et maximale de la pression
   *
   * @return La valeur minimale et maximale de la pression [min, max, sumMin, sumMax]
   */
  public double[] getPressureMinMax() {
    return pressureMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale de la pression
   *
   * @param min La valeur minimale de la pression
   * @param max La valeur maximale de la pression
   * @param i Iteration actuelle
   */
  public void setPressureMinMax(double min, double max, int i) {
    ParticleMatrix.setMinMax(this.pressureMinMax, min, max, i);
  }

  /**
   * Retourne la valeur minimale et maximale de la température
   *
   * @return La valeur minimale et maximale de la température [min, max, sumMin, sumMax]
   */
  public double[] getTemperatureMinMax() {
    return temperatureMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale de la température
   *
   * @param min La valeur minimale de la température
   * @param max La valeur maximale de la température
   * @param i Iteration actuelle
   */
  public void setTemperatureMinMax(double min, double max, int i) {
    ParticleMatrix.setMinMax(this.temperatureMinMax, min, max, i);
  }

  /**
   * Retourne la valeur minimale et maximale de la densité de zone
   *
   * @return La valeur minimale et maximale de la densité de zone [min, max, sumMin, sumMax]
   */
  public double[] getAreaDensityMinMax() {
    return areaDensityMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale de la densité de zone
   *
   * @param min La valeur minimale de la densité de zone
   * @param max La valeur maximale de la densité de zone
   * @param i Iteration actuelle
   */
  public void setAreaDensityMinMax(double min, double max, int i) {
    ParticleMatrix.setMinMax(this.areaDensityMinMax, min, max, i);
  }

  /**
   * Retourne la valeur minimale et maximale de la divergence de la vélocité
   *
   * @return La valeur minimale et maximale de la divergence de la vélocité [min, max, sumMin,
   *     sumMax]
   */
  public double[] getVelocityDivergenceMinMax() {
    return velocityDivergenceMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale de la divergence de la vélocité
   *
   * @param velDivMin La valeur minimale de la divergence de la vélocité
   * @param velDivMax La valeur maximale de la divergence de la vélocité
   * @param i Iteration actuelle
   */
  public void setVelocityDivergenceMinMax(double velDivMin, double velDivMax, int i) {
    ParticleMatrix.setMinMax(velocityDivergenceMinMax, velDivMin, velDivMax, i);
  }

  /**
   * Retourne la valeur minimale et maximale du gradient de pression en x
   *
   * @return La valeur minimale et maximale du gradient de pression en x [min, max, sumMin, sumMax]
   */
  public double[] getXPressureGradientMinMax() {
    return xPressureGradientMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale du gradient de pression en x
   *
   * @param pressureXGradMin La valeur minimale du gradient de pression en x
   * @param pressureXGradMax La valeur maximale du gradient de pression en x
   * @param i Iteration actuelle
   */
  public void setXPressureGradientMinMax(double pressureXGradMin, double pressureXGradMax, int i) {
    ParticleMatrix.setMinMax(xPressureGradientMinMax, pressureXGradMin, pressureXGradMax, i);
  }

  /**
   * Retourne la valeur minimale et maximale du gradient de pression en y
   *
   * @return La valeur minimale et maximale du gradient de pression en y [min, max, sumMin, sumMax]
   */
  public double[] getYPressureGradientMinMax() {
    return yPressureGradientMinMax;
  }

  /**
   * Modifier la valeur minimale et maximale du gradient de pression en y
   *
   * @param pressureYGradMin La valeur minimale du gradient de pression en y
   * @param pressureYGradMax La valeur maximale du gradient de pression en y
   * @param i Iteration actuelle
   */
  public void setYPressureGradientMinMax(double pressureYGradMin, double pressureYGradMax, int i) {
    ParticleMatrix.setMinMax(yPressureGradientMinMax, pressureYGradMin, pressureYGradMax, i);
  }

  /**
   * Modifie les valeurs min et max de la matrice de particules selon les valeurs min et max de la
   * matrice de particules pm
   *
   * @param pm La matrice de particules
   */
  public void setMinMax(ParticleMatrix pm) {
    xVelocityMinMax = pm.getXVelocityMinMax().clone();
    yVelocityMinMax = pm.getYVelocityMinMax().clone();
    velocityMinMax = pm.getVelocityMinMax().clone();
    pressureMinMax = pm.getPressureMinMax().clone();
    temperatureMinMax = pm.getTemperatureMinMax().clone();
    velocityCurlMinMax = pm.getVelocityCurlMinMax().clone();
    velocityDivergenceMinMax = pm.getVelocityDivergenceMinMax().clone();
    xPressureGradientMinMax = pm.getXPressureGradientMinMax().clone();
    yPressureGradientMinMax = pm.getYPressureGradientMinMax().clone();
  }

  /**
   * Modifie les valeurs min et max en utilisant la moyenne mobile
   *
   * @param minMax Tableau de min et max [min, max, sumMin, sumMax]
   * @param min Valeur minimale
   * @param max Valeur maximale
   * @param i Indice de la moyenne mobile
   */
  private static void setMinMax(double[] minMax, double min, double max, int i) {
    minMax[0] = WMath.normalize(WMath.movingAverage(min, minMax[2], i));
    minMax[1] = WMath.normalize(WMath.movingAverage(max, minMax[3], i));
    minMax[2] += min;
    minMax[3] += max;
  }

  /**
   * Methode utilitaire permettant de retourner une matrice de particule selon un parametre de
   * visualisation.
   *
   * @param visualization Le {@code VisualizationType} associé a la matrice de particule voulue
   * @return La matrice de particule selon le {@code VisualizationType}. Retourne {@code
   *     this#areaDensity} par defaut.
   */
  public double[] getParticlesByVisualization(VisualizationType visualization) {
    switch (visualization) {
      case DENSITY:
        return areaDensity.getMatrix();

      case PRESSURE:
        return pressure.getMatrix();

      case PRESSURE_X:
        return xPressureGradient.getMatrix();

      case PRESSURE_Y:
        return yPressureGradient.getMatrix();

      case VELOCITY:
        return velocity.getMatrix();

      case VELOCITY_X:
        return xVelocity.getMatrix();

      case VELOCITY_Y:
        return yVelocity.getMatrix();

      default:
        return areaDensity.getMatrix();
    }
  }

  /**
   * Methode utilitaire permettant de retourner une particule selon un parametre de visualisation.
   *
   * @param index L'index de la particule
   * @param visualization Le {@code VisualizationType} associé a la matrice de particule voulue
   * @return La particule selon le {@code VisualizationType}. Retourne {@code this#areaDensity} par
   *     defaut.
   */
  public double getParticleByVisualization(int index, VisualizationType visualization) {
    switch (visualization) {
      case DENSITY:
        return areaDensity.getMatrix()[index];

      case PRESSURE:
        return pressure.getMatrix()[index];

      case PRESSURE_X:
        return xPressureGradient.getMatrix()[index];

      case PRESSURE_Y:
        return yPressureGradient.getMatrix()[index];

      case VELOCITY:
        return velocity.getMatrix()[index];

      case VELOCITY_X:
        return xVelocity.getMatrix()[index];

      case VELOCITY_Y:
        return yVelocity.getMatrix()[index];

      default:
        return areaDensity.getMatrix()[index];
    }
  }

  /**
   * Methode utilitaire permettant de retourner le tableau de min/max selon le type de visualization
   * associé a la matrice de particule voulue.
   *
   * @param visualization Le {@code VisualizationType} associé au tableau de min/max voulu
   * @return Tableau de min/max selon le {@code VisualizationType}. Retourne {@code
   *     this#areaDensityMinMax} par defaut.
   */
  public double[] getMinMaxByVisualization(VisualizationType visualization) {
    switch (visualization) {
      case DENSITY:
        return areaDensityMinMax;

      case PRESSURE:
        return pressureMinMax;

      case PRESSURE_X:
        return xPressureGradientMinMax;

      case PRESSURE_Y:
        return yPressureGradientMinMax;

      case VELOCITY:
        return velocityMinMax;

      case VELOCITY_X:
        return xVelocityMinMax;

      case VELOCITY_Y:
        return yVelocityMinMax;

      default:
        return areaDensityMinMax;
    }
  }
}
