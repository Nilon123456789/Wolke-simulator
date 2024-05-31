package com.e24.wolke.backend.simulation.physics.engine;

import com.e24.wolke.backend.models.WModel;
import com.e24.wolke.backend.models.application.LocaleManager;
import com.e24.wolke.backend.models.obstacles.WObstacleCell;
import com.e24.wolke.backend.models.obstacles.WObstacleCellMatrix;
import com.e24.wolke.backend.models.simulation.SimulationConstants;
import com.e24.wolke.backend.models.simulation.SimulationConstants.BORDER_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationConstants.OBSTACLE_TYPE;
import com.e24.wolke.backend.models.simulation.SimulationModel;
import com.e24.wolke.backend.simulation.physics.ParticleMatrix;
import com.e24.wolke.backend.simulation.physics.SimulationData;
import com.e24.wolke.eventsystem.Subject;
import com.e24.wolke.utils.Timer;
import com.e24.wolke.utils.interfaces.Reinitializable;
import com.e24.wolke.utils.math.WDoubleMatrix;
import com.e24.wolke.utils.math.WMath;
import com.e24.wolke.utils.pool.DoubleMatrixPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * PhysicsEngine.java
 *
 * <p>Cette classe est responsable d'effectuer tout les calculs physiques de la simulation de fluide
 *
 * <p>Les calculs mathématiques sont basés sur l'implémentation de l'article Real-Time Fluid
 * Simulation on the GPU (<a
 * href="https://fileadmin.cs.lth.se/cs/Education/EDAN35/projects/13SoderlingStrandberg_Fluid.pdf">...</a>)
 *
 * @author Nilon123456789
 */
public class PhysicsEngine implements Runnable, Reinitializable {

  /** Données de la simulation */
  protected SimulationData simulationData;

  /** Modèle de la simulation */
  private WModel simulationModel;

  /** Statut d thread la simulation */
  private boolean isRunning;

  /** Statut du calcule de la simulaiton */
  private boolean isCalculating;

  /** Temps actuel de la simulation */
  private double currentTime;

  /** Iteration actuel de la simulation */
  private int currentIteration;

  /** Logger de la classe */
  protected static final Logger LOGGER = LogManager.getLogger(PhysicsEngine.class.getSimpleName());

  /** Timer pour mesurer le temps d'execution */
  protected final Timer timer = new Timer("Simulation", LOGGER);

  /** Piscine de double array de la taille de la matrice */
  protected DoubleMatrixPool matriceArrayPool;

  /**
   * Constructeur de la classe PhysicsEngine
   *
   * @param simulationData Les données de la simulation
   */
  public PhysicsEngine(SimulationData simulationData) {
    this.simulationData = simulationData;
    this.isRunning = false;
    this.isCalculating = false;

    matriceArrayPool =
        new DoubleMatrixPool(
            simulationData.getCurrentParticleMatrix().getXLength(),
            simulationData.getCurrentParticleMatrix().getYLength());
  }

  /**
   * Constructeur de la classe PhysicsEngine
   *
   * @param simulationModel Le modèle de la simulation
   */
  public PhysicsEngine(SimulationModel simulationModel) {
    this(simulationModel.getSimulationData());

    this.simulationModel = simulationModel;
  }

  /**
   * Retourne les données de la simulation
   *
   * @return Les données de la simulation
   */
  public SimulationData getSimulationData() {
    return simulationData;
  }

  /**
   * Met à jour les données de la simulation
   *
   * @param newSimulationData Les nouvelles données de la simulation
   */
  public void setSimulationData(SimulationData newSimulationData) {
    simulationData = newSimulationData;

    if (matriceArrayPool.getSize() != newSimulationData.getCurrentParticleMatrix().getSize())
      matriceArrayPool =
          new DoubleMatrixPool(
              this.simulationData.getCurrentParticleMatrix().getXLength(),
              this.simulationData.getCurrentParticleMatrix().getYLength());
  }

  /**
   * Retourne le statut de la simulation
   *
   * @return Le statut du thread la simulation
   */
  public boolean isRunning() {
    return isRunning;
  }

  /**
   * Retourne le statut du calcul de la simulation
   *
   * @return Le statut du calcul de la simulation
   */
  public boolean isCalculating() {
    return isCalculating;
  }

  /**
   * Met à jour la simulation
   *
   * @param deltaTime Le temps écoulé depuis la dernière mise à jour
   */
  public void update(double deltaTime) {
    if (isCalculating) return;
    isCalculating = true;

    this.timer.start("Update");

    currentTime += deltaTime;
    currentIteration++;

    if (this.simulationData.getBorderType() == BORDER_TYPE.WIND_TUNNEL) addInitialVelocity();

    // Si c'est le premier pas de la simulation, on met la matrice précédente égale à la matrice
    // actuelle
    if (this.currentIteration == 1)
      simulationData.setPreviousParticleMatrix(simulationData.getCurrentParticleMatrix());

    this.timer.start("Advection");
    advect(deltaTime);
    this.timer.stop("Advection");

    this.timer.start("VorticityConfinement");
    vorticityConfinement();
    this.timer.stop("VorticityConfinement");

    this.timer.start("Diffusion");
    diffusion(deltaTime);
    this.timer.stop("Diffusion");

    this.timer.start("AddForce");
    addForce(0, 0);
    this.timer.stop("AddForce");

    // Début projection

    this.timer.start("VelocityDivergence");
    velocityDivergence();
    this.timer.stop("VelocityDivergence");

    this.timer.start("PressureSolver");
    pressureSolver();
    this.timer.stop("PressureSolver");

    this.timer.start("PressureGradient");
    pressureGradient();
    this.timer.stop("PressureGradient");

    this.timer.start("SubstractPressureGradient");
    substractPressureGradient();
    this.timer.stop("SubstractPressureGradient");

    // Fin projection

    this.timer.start("FindMinMax");
    findMinMax();
    this.timer.stop("FindMinMax");

    // Publie le rendue finit
    simulationData.setNewRenderMatrix();

    if (this.simulationModel != null) {
      simulationModel.getController().getRendererModel().newFrameReady();
      simulationModel.getPublisher().publish(Subject.ON_RENDERING_DONE, null);
    }

    double time = timer.stop("Update");

    isCalculating = false;

    if (!isRunning) {
      PhysicsEngine.LOGGER.info(
          LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.updated"), time);

      // Vérifie si la simulation est instable
      checkCFL();

      return;
    }

    if (this.currentIteration % SimulationConstants.CFL_CHECK_FREQUENCY != 0) return;

    // Affiche le temps d'execution toutes les CFL_CHECK_FREQUENCY itérations
    PhysicsEngine.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.updatedOnAvrage"),
        this.timer.getAverage("Update"));

    // Vérifie si la simulation est instable
    checkCFL();
  }

  /**
   * Exécute la boucle de simulation
   *
   * @see java.lang.Runnable#run()
   */
  @Override
  public void run() {
    while (isRunning) {

      // Mise à jour de la simulation
      update(simulationData.getTimeStep());

      try {
        Thread.sleep((int) (this.simulationData.getSleepTime()));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** Démarrer une boucle de simulation */
  public void start() {
    if (isRunning) {
      PhysicsEngine.LOGGER.warn(
          LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.alreadyRunning"));
      return;
    }

    Thread proc = new Thread(this);
    proc.setName(getClass().getSimpleName() + " Thread");
    proc.setPriority(7);
    proc.start();
    isRunning = true;

    PhysicsEngine.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.started"));
  }

  /** Arrêter la boucle de simulation */
  public void stop() {
    if (!this.isRunning) {
      PhysicsEngine.LOGGER.warn(
          LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.alreadyStopped"));
      return;
    }

    isRunning = false;

    waitForCalculation();

    PhysicsEngine.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.stopped"));
  }

  /** Bloque le thread jusqu'à ce que le calcule soit fini */
  public void waitForCalculation() {
    while (isCalculating) {
      Thread.yield();
    }
  }

  /**
   * Retourne le temps actuel de la simulation
   *
   * @return Le temps actuel de la simulation
   */
  public double getCurrentTime() {
    return currentTime;
  }

  /**
   * Retourne l'itération actuelle de la simulation
   *
   * @return L'itération actuelle de la simulation
   */
  public int getCurrentIteration() {
    return currentIteration;
  }

  /**
   * Calcule le nombre de courant de la simulation
   *
   * @return Le nombre de courant de la simulation
   * @see <a href="https://www.simscale.com/blog/cfl-condition/">CFL condition</a>
   */
  public double calculateCFL() {
    double maxXVelocity = simulationData.getCurrentParticleMatrix().getXVelocityMinMax()[1];
    double maxYVelocity = simulationData.getCurrentParticleMatrix().getYVelocityMinMax()[1];

    if (WMath.nearlyZero(maxXVelocity)
        || WMath.nearlyZero(
            maxYVelocity)) { // Si la vélocité est nulle, la simulation est déjà instable
      return Double.MAX_VALUE;
    }

    double xMeterByPixel = simulationData.xMeterByPixel();
    double yMeterByPixel = simulationData.yMeterByPixel();

    double timeStep = simulationData.getTimeStep();

    double xcfl = maxXVelocity * timeStep / xMeterByPixel;
    double ycfl = maxYVelocity * timeStep / yMeterByPixel;

    return Math.max(xcfl, ycfl);
  }

  /**
   * Calcule le nombre de courant de la simulation et informe si la simulation est instable
   *
   * @see <a href="https://www.simscale.com/blog/cfl-condition/">CFL condition</a>
   */
  public void checkCFL() {
    double maxXVelocity = simulationData.getCurrentParticleMatrix().getXVelocityMinMax()[1];
    double maxYVelocity = simulationData.getCurrentParticleMatrix().getYVelocityMinMax()[1];

    // On ignore le yPixelByMeter car il est toujours égal à xPixelByMeter
    double xMeterByPixel = simulationData.xMeterByPixel();

    double cfl = calculateCFL();

    if (cfl < SimulationConstants.MIN_CFL_WARN) return;

    if (cfl == Double.MAX_VALUE) {
      PhysicsEngine.LOGGER.error(
          LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.zeroVelocity"));
      return;
    }

    double recomendedTimeStep =
        (SimulationConstants.MIN_CFL_WARN * xMeterByPixel) / Math.max(maxXVelocity, maxYVelocity);
    String recTimeStep = String.format("%.5f", recomendedTimeStep);

    if (cfl >= SimulationConstants.MIN_CFL_ERR) {
      PhysicsEngine.LOGGER.error(
          LocaleManager.getLocaleResourceBundle()
              .getString("log.physicsEngine.cflConditionTooHigh"),
          cfl,
          recTimeStep);
      return;
    }

    PhysicsEngine.LOGGER.warn(
        LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.cflCondition"),
        cfl,
        SimulationConstants.MIN_CFL_WARN,
        recTimeStep);
  }

  /** Applique une velocité initiale sur la matrice de particules pour les cellules inflow */
  public void addInitialVelocity() {
    ParticleMatrix pm = simulationData.getCurrentParticleMatrix();
    WObstacleCellMatrix obstacle = simulationData.getObstacle();

    int xLength = pm.getXLength();
    int yLength = pm.getYLength();

    double[] xVelocity = pm.getXVelocity().getMatrix();
    double[] yVelocity = pm.getYVelocity().getMatrix();
    double[] areaDensity = pm.getAreaDensity().getMatrix();

    double vel = simulationData.getInitialVelocity();

    for (int pos = 0; pos < pm.getSize(); pos++) {
      WObstacleCell cell = obstacle.getElementAt(pos);
      if (cell == null) continue;
      if (cell.getObstacleType() != OBSTACLE_TYPE.INFLOW) continue;
      double normal = cell.getAverageNormal();

      int x = (int) Math.round(Math.cos(normal));
      int y = (int) Math.round(Math.sin(normal));

      double xVel = vel * x;
      double yVel = vel * y;

      int p1 = getPosAtOffset(pos, xLength, yLength, x, y);
      int p2 = getPosAtOffset(pos, xLength, yLength, 2 * x, 2 * y);
      int p3 = getPosAtOffset(pos, xLength, yLength, 3 * x, 3 * y);
      int p4 = getPosAtOffset(pos, xLength, yLength, 4 * x, 4 * y);
      int p5 = getPosAtOffset(pos, xLength, yLength, 5 * x, 5 * y);

      xVelocity[p1] = xVelocity[p2] = xVelocity[p3] = xVelocity[p4] = xVelocity[p5] = xVel;
      yVelocity[p1] = yVelocity[p2] = yVelocity[p3] = yVelocity[p4] = yVelocity[p5] = yVel;

      if (pm.getXAndY(pos)[1] % 10 == 0) { // TODO : Switch areaDensity to a separate function
        areaDensity[p1] = areaDensity[p2] = areaDensity[p3] = areaDensity[p4] = areaDensity[p5] = 1;
      } else {
        areaDensity[p1] = areaDensity[p2] = areaDensity[p3] = areaDensity[p4] = areaDensity[p5] = 0;
      }
    }
    applyBoundaryConditions();
  }

  /**
   * Calcule l'advection des particules
   *
   * @param timeStep Le pas de la simulation (en ms)
   */
  protected void advect(double timeStep) {
    ParticleMatrix prevParticleMatrix = simulationData.getPreviousParticleMatrix();

    int xLength = prevParticleMatrix.getXLength();
    int yLength = prevParticleMatrix.getYLength();
    int size = xLength * yLength;

    int x, y, previousXWhole, previousYWhole, pos00, pos01, pos10, pos11;
    double prevX, prevY, previousXFraction, previousYFraction, p00, p10, p01, p11;

    double xMeterByPixel = simulationData.xPixelByMeter();
    double yMeterByPixel = simulationData.yPixelByMeter();

    ParticleMatrix newParticleMatrix = simulationData.borrowParticleMatrixFromPool();

    double[] xVelocity = prevParticleMatrix.getXVelocity().getMatrix();
    double[] yVelocity = prevParticleMatrix.getYVelocity().getMatrix();
    double[] temperature = prevParticleMatrix.getTemperature().getMatrix();
    double[] areaDensity = prevParticleMatrix.getAreaDensity().getMatrix();
    double[] pressure = prevParticleMatrix.getPressure().getMatrix();

    double[] newxVelocity = newParticleMatrix.getXVelocity().getMatrix();
    double[] newyVelocity = newParticleMatrix.getYVelocity().getMatrix();
    double[] newTemperature = newParticleMatrix.getTemperature().getMatrix();
    double[] newAreaDensity = newParticleMatrix.getAreaDensity().getMatrix();
    double[] newPressure = newParticleMatrix.getPressure().getMatrix();

    for (int pos = 0; pos < size; pos++) {

      // Calcul de la position précédente de la particule
      x = pos % xLength;
      y = pos / xLength;

      // Calcul de la position précédente de la particule en fonction de la vitesse
      prevX = x - timeStep * xVelocity[pos] * xMeterByPixel;
      prevY = y - timeStep * yVelocity[pos] * yMeterByPixel;

      // Décomposition de la position précédente en partie entière et fractionnaire
      previousXWhole = (int) Math.floor(prevX);
      previousXFraction = prevX - previousXWhole;
      previousYWhole = (int) Math.floor(prevY);
      previousYFraction = prevY - previousYWhole;

      pos00 = getPosAtOffset(previousXWhole + previousYWhole * xLength, xLength, yLength, 0, 0);
      pos01 = getPosAtOffset(pos00, xLength, yLength, 0, 1);
      pos10 = getPosAtOffset(pos00, xLength, yLength, 1, 0);
      pos11 = getPosAtOffset(pos00, xLength, yLength, 1, 1);

      p00 = xVelocity[pos00];
      p10 = xVelocity[pos10];
      p01 = xVelocity[pos01];
      p11 = xVelocity[pos11];

      // Mise à jour de la vélocité en X
      newxVelocity[pos] = WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction);

      // Récupération des vélocité en Y
      p00 = yVelocity[pos00];
      p10 = yVelocity[pos10];
      p01 = yVelocity[pos01];
      p11 = yVelocity[pos11];

      // Mise à jour de la vélocité en Y

      newyVelocity[pos] =
          WMath.normalize(WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction));

      // Récupération de la température précédente
      p00 = temperature[pos00];
      p10 = temperature[pos10];
      p01 = temperature[pos01];
      p11 = temperature[pos11];

      // Mise à jour de la température
      newTemperature[pos] =
          WMath.normalize(WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction));

      // Récupération de densité de zone
      p00 = areaDensity[pos00];
      p10 = areaDensity[pos10];
      p01 = areaDensity[pos01];
      p11 = areaDensity[pos11];

      // Mise à jour de la densité de zone
      newAreaDensity[pos] =
          WMath.normalize(WMath.bilerp(p00, p10, p01, p11, previousXFraction, previousYFraction));

      // Recuperation de la pression
      newPressure[pos] = pressure[pos];
    }

    simulationData.setCurrentParticleMatrix(newParticleMatrix);

    applyBoundaryConditions();
  }

  /**
   * Utilise la méthode de Jacobi itérative pour résoudre des equations de poisson Equation de
   * poisson : x_{i,j}^{(k+1)}=\frac{x_{i-1,j}^{k}+x_{i+1,j}^{k}+x_{i,j-1}^{k}+x_{i,j+1}^{k}+\alpha
   * b_{i,j}}{\beta}
   *
   * @param x La matrice a résoudre
   * @param xLength La taille en x de la matrice
   * @param yLength La taille en y de la matrice
   * @param alpha le coefficient alpha
   * @param rBeta la reciproque du coefficient beta (1/beta )
   * @param b la matrice b (matrice doit être de la même taille que x)
   */
  protected void jacobiSolver(
      WDoubleMatrix x, int xLength, int yLength, double alpha, double rBeta, WDoubleMatrix b) {

    int size = x.getSize();

    if (b.getSize() != size)
      throw new IllegalArgumentException("La taille de la matrice x et b doit être égale à size");
    // Les valeurs de x_{i-1,j}, x_{i+1,j}, x_{i,j-1}, x_{i,j+1}
    double xL, xR, xB, xT, valX, valXNew;
    int xLPos, xRPos, xBPos, xTPos; // Les positions de x_{i-1,j}, x_{i+1,j}, x_{i,j-1}, x_{i,j+1}
    double cellDiff; // Le % de différence entre deux ité pour un élément de la matrice
    double curentDiff = 1.0d; // Le % maximal obtenu entre deux ité pour un élément de la matrice
    WDoubleMatrix xNewMatrix = matriceArrayPool.borrowObject();
    double[] xNew = xNewMatrix.getMatrix();

    for (int iter = 0; iter < SimulationConstants.MAX_JACOBI_ITERATIONS; iter++) {

      applyBoundaryConditions();

      for (int pos = 0; pos < size; pos++) {
        // On récupère les valeurs de x_{i-1,j}, x_{i+1,j}, x_{i,j-1}, x_{i,j+1}

        xLPos = getPosAtOffset(pos, xLength, yLength, -1, 0); // x_{i-1,j}
        xRPos = getPosAtOffset(pos, xLength, yLength, 1, 0); // x_{i+1,j}
        xBPos = getPosAtOffset(pos, xLength, yLength, 0, -1); // x_{i,j-1}
        xTPos = getPosAtOffset(pos, xLength, yLength, 0, 1); // x_{i,j+1}

        xL = x.getMatrix()[xLPos]; // x_{i-1,j}
        xR = x.getMatrix()[xRPos]; // x_{i+1,j}
        xB = x.getMatrix()[xBPos]; // x_{i,j-1}
        xT = x.getMatrix()[xTPos]; // x_{i,j+1}

        // On calcule la nouvelle valeur de x_{i,j}
        valXNew =
            xNew[pos] = WMath.normalize((xL + xR + xB + xT + alpha * b.getMatrix()[pos]) * rBeta);

        // On calcule le pourcentage de différence entre deux itérations pour un élément de la
        // matrice

        valX = x.getMatrix()[pos];

        if (valX == valXNew) continue;
        cellDiff = (valXNew - valX) / valX;

        if (cellDiff < 0) {
          cellDiff = -cellDiff;
        }

        curentDiff = Math.min(cellDiff, curentDiff);
      }

      // On échange les matrices
      System.arraycopy(xNew, 0, x.getMatrix(), 0, size);

      // On verifie si la difference entre deux itérations est inférieur à maxDif
      if (curentDiff < SimulationConstants.MAX_JACOBI_DIFF) break;
    }

    // On retourne la matrice x_new a la piscine
    matriceArrayPool.returnObject(xNewMatrix);
  }

  /**
   * Simule la diffusion des particules dans la matrice
   *
   * @param timeStep Le pas de la simulation (en ms)
   */
  private void diffusion(double timeStep) {
    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();

    // Si l'echel de la simulation change de 1x1, alpha = echelX * echelY / (viscosite * timeStep)
    double alpha =
        (simulationData.xMeterByPixel() * simulationData.xMeterByPixel())
            / (timeStep * simulationData.getKinematicViscosity());
    double rBeta = 1.0d / (4.0d + alpha);

    // On récupère une matrice de double de la piscine
    WDoubleMatrix b = matriceArrayPool.borrowObject();

    WDoubleMatrix xVelocity = particleMatrix.getXVelocity();
    WDoubleMatrix yVelocity = particleMatrix.getYVelocity();

    // On résout l'équation de poisson pour la vélocité en X
    b.setMatrix(xVelocity.getMatrix().clone());
    jacobiSolver(xVelocity, xLength, yLength, alpha, rBeta, b);

    // On résout l'équation de poisson pour la vélocité en Y
    b.setMatrix(yVelocity.getMatrix().clone());
    jacobiSolver(yVelocity, xLength, yLength, alpha, rBeta, b);

    matriceArrayPool.returnObject(b);
  }

  /** Calcule la divergence de la matrice de vélocité */
  private void velocityDivergence() {
    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();
    int size = xLength * yLength;

    double xL, xR, yB, yT;
    int xLPos, xRPos, yBPos, yTPos;

    double[] xVelocity = particleMatrix.getXVelocity().getMatrix();
    double[] yVelocity = particleMatrix.getYVelocity().getMatrix();
    double[] velocityDivergence = particleMatrix.getVelocityDivergence().getMatrix();

    double rDenomX = 1.0d / (2.0d * simulationData.xMeterByPixel());
    double rDenomY = 1.0d / (2.0d * simulationData.yMeterByPixel());

    for (int i = 0; i < size; i++) {

      xLPos = getPosAtOffset(i, xLength, yLength, -1, 0); // x_{i-1,j}
      xRPos = getPosAtOffset(i, xLength, yLength, 1, 0); // x_{i+1,j}
      yBPos = getPosAtOffset(i, xLength, yLength, 0, -1); // y_{i,j-1}
      yTPos = getPosAtOffset(i, xLength, yLength, 0, 1); // y_{i,j+1}

      xL = xVelocity[xLPos]; // x_{i-1,j}
      xR = xVelocity[xRPos]; // x_{i+1,j}
      yB = yVelocity[yBPos]; // y_{i,j-1}
      yT = yVelocity[yTPos]; // y_{i,j+1}

      velocityDivergence[i] = WMath.normalize((xR - xL) * rDenomX + (yT - yB) * rDenomY);
    }
  }

  /**
   * Résolution de l'équation de poisson pour la pression
   *
   * <p>p_{i,j}=\frac{ p_{i+1,j} + p_{i-1,j} + p_{i,j+1} + p_{i,j-1} - 4p_{i,j}}{(\delta x)^2}
   */
  private void pressureSolver() {
    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();

    double alpha = -(simulationData.xMeterByPixel() * simulationData.yMeterByPixel());
    double rBeta = 1.0d / 4.0d;

    WDoubleMatrix pressure = particleMatrix.getPressure();
    WDoubleMatrix velocityDivergence = particleMatrix.getVelocityDivergence();

    // particleMatrix.pressure = new double[xLength * yLength]; // Plus rapide sans

    // On résout l'équation de poisson pour la pression
    jacobiSolver(pressure, xLength, yLength, alpha, rBeta, velocityDivergence);
  }

  /** Calcule le gradient de la matrice de pression */
  private void pressureGradient() {
    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();
    int size = xLength * yLength;

    double xL, xR, xB, xT;
    int xLPos, xRPos, yBPos, yTPos;

    double[] p = particleMatrix.getPressure().getMatrix();
    double[] xPressureGradient = particleMatrix.getXPressureGradient().getMatrix();
    double[] yPressureGradient = particleMatrix.getYPressureGradient().getMatrix();

    // Reciproque du denominateur
    double rDenomX = 1.0d / (2.0d * simulationData.xMeterByPixel());
    double rDenomY = 1.0d / (2.0d * simulationData.yMeterByPixel());

    for (int pos = 0; pos < size; pos++) {

      xLPos = getPosAtOffset(pos, xLength, yLength, -1, 0); // p_{i-1,j}
      xRPos = getPosAtOffset(pos, xLength, yLength, 1, 0); // p_{i+1,j}
      yBPos = getPosAtOffset(pos, xLength, yLength, 0, -1); // p_{i,j-1}
      yTPos = getPosAtOffset(pos, xLength, yLength, 0, 1); // p_{i,j+1}

      xL = p[xLPos]; // p_{i-1,j}
      xR = p[xRPos]; // p_{i+1,j}
      xB = p[yBPos]; // p_{i,j-1}
      xT = p[yTPos]; // p_{i,j+1}

      xPressureGradient[pos] = WMath.normalize((xR - xL) * rDenomX);
      yPressureGradient[pos] = WMath.normalize((xT - xB) * rDenomY);
    }
  }

  /** Soustrait le gradient de pression a la vélocité */
  private void substractPressureGradient() {
    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    double[] xVelocity = particleMatrix.getXVelocity().getMatrix();
    double[] yVelocity = particleMatrix.getYVelocity().getMatrix();
    double[] velocity = particleMatrix.getVelocity().getMatrix();
    double[] xPressureGradient = particleMatrix.getXPressureGradient().getMatrix();
    double[] yPressureGradient = particleMatrix.getYPressureGradient().getMatrix();

    double xVel, yVel;
    int size = particleMatrix.getSize();

    for (int pos = 0; pos < size; pos++) {
      if (isCellObstructed(pos)) continue;
      xVel = xVelocity[pos] -= xPressureGradient[pos];
      yVel = yVelocity[pos] -= yPressureGradient[pos];

      velocity[pos] = WMath.normalize(WMath.modulus(xVel, yVel));
    }

    applyBoundaryConditions();
  }

  /**
   * Ajoute une force sur toute la matrice de particules
   *
   * @param xForce La force en x
   * @param yForce La force en y
   */
  private void addForce(double xForce, double yForce) {
    if (xForce == 0 && yForce == 0) return;

    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();
    int size = xLength * yLength;

    double[] xVelocity = particleMatrix.getXVelocity().getMatrix();
    double[] yVelocity = particleMatrix.getYVelocity().getMatrix();

    for (int pos = 0; pos < size; pos++) {
      xVelocity[pos] += xForce;
      yVelocity[pos] += yForce;
    }

    applyBoundaryConditions();
  }

  /** Calcule le confinement de vortex avec le curl de la vélocité */
  protected void vorticityConfinement() {
    double vorticityFactor = simulationData.getVortexConfinementFactor();

    if (vorticityFactor == 0) return;

    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();

    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();
    int size = xLength * yLength;

    double xMeterByPixel = simulationData.xMeterByPixel();
    double yMeterByPixel = simulationData.yMeterByPixel();

    int xTPos, xBPos, yLPos, yRPos;
    double gradX, gradY, grad, fx, fy, fac, velCurl;

    double[] xVelocity = particleMatrix.getXVelocity().getMatrix();
    double[] yVelocity = particleMatrix.getYVelocity().getMatrix();
    double[] velocityCurl = particleMatrix.getVelocityCurl().getMatrix();

    for (int i = 0; i < size; i++) {

      if (isCellObstructed(i)) continue;

      xTPos = getPosAtOffset(i, xLength, yLength, 0, 1); // x_{i,j+1}
      xBPos = getPosAtOffset(i, xLength, yLength, 0, -1); // x_{i,j-1}
      yLPos = getPosAtOffset(i, xLength, yLength, -1, 0); // y_{i-1,j}
      yRPos = getPosAtOffset(i, xLength, yLength, 1, 0); // y_{i+1, j}

      velCurl =
          velocityCurl[i] =
              0.5 * ((yVelocity[yRPos] - yVelocity[yLPos]) - (xVelocity[xTPos] - xVelocity[xBPos]));

      gradX = 0.5 * (Math.abs(velocityCurl[yRPos]) - Math.abs(velocityCurl[yLPos]));
      gradY = 0.5 * (Math.abs(velocityCurl[xTPos]) - Math.abs(velocityCurl[xBPos]));

      grad = Math.sqrt(Math.pow(gradX, 2) + Math.pow(gradY, 2));

      if (WMath.nearlyZero(grad)) continue;

      fac = 1 / grad;
      fx = fac * gradY;
      fy = -fac * gradX;

      xVelocity[i] += vorticityFactor * fx * velCurl * xMeterByPixel;
      yVelocity[i] += vorticityFactor * fy * velCurl * yMeterByPixel;
    }

    applyBoundaryConditions();
  }

  /** Calcule le minimum et le maximum de la matrice de particules */
  public void findMinMax() {
    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();
    ParticleMatrix previousMatrix = simulationData.getPreviousParticleMatrix();

    // On récupère les valeurs min et max de la matrice précédente
    particleMatrix.setMinMax(previousMatrix);

    int size = particleMatrix.getSize();

    double velXmin,
        velXmax,
        velYmin,
        velYmax,
        velMax,
        velMin,
        tempMin,
        tempMax,
        presMin,
        presMax,
        velDivMin,
        velDivMax,
        pressureXGradMin,
        pressureXGradMax,
        pressureYGradMin,
        pressureYGradMax,
        velCurlMin,
        velCurlMax;
    velXmin =
        velYmin =
            velMin =
                tempMin =
                    velCurlMin =
                        presMin =
                            velDivMin = pressureXGradMin = pressureYGradMin = Double.MAX_VALUE;
    velXmax =
        velYmax =
            velMax =
                tempMax =
                    velCurlMax =
                        presMax =
                            velDivMax = pressureXGradMax = pressureYGradMax = Double.MIN_VALUE;

    double[] xVelocity = particleMatrix.getXVelocity().getMatrix();
    double[] yVelocity = particleMatrix.getYVelocity().getMatrix();
    double[] velocity = particleMatrix.getVelocity().getMatrix();
    double[] temperature = particleMatrix.getTemperature().getMatrix();
    double[] pressure = particleMatrix.getPressure().getMatrix();
    double[] velocityDivergence = particleMatrix.getVelocityDivergence().getMatrix();
    double[] xPressureGradient = particleMatrix.getXPressureGradient().getMatrix();
    double[] yPressureGradient = particleMatrix.getYPressureGradient().getMatrix();
    double[] velocityCurl = particleMatrix.getVelocityCurl().getMatrix();

    for (int pos = 0; pos < size; pos++) {
      if (isCellObstructed(pos)) continue;

      velXmin = Math.min(velXmin, xVelocity[pos]);
      velXmax = Math.max(velXmax, xVelocity[pos]);
      velYmin = Math.min(velYmin, yVelocity[pos]);
      velYmax = Math.max(velYmax, yVelocity[pos]);
      velMin = Math.min(velMin, velocity[pos]);
      velMax = Math.max(velMax, velocity[pos]);
      tempMin = Math.min(tempMin, temperature[pos]);
      tempMax = Math.max(tempMax, temperature[pos]);
      presMin = Math.min(presMin, pressure[pos]);
      presMax = Math.max(presMax, pressure[pos]);
      velCurlMax = Math.max(velCurlMax, velocityCurl[pos]);
      velCurlMin = Math.min(velCurlMin, velocityCurl[pos]);
      velDivMin = Math.min(velDivMin, velocityDivergence[pos]);
      velDivMax = Math.max(velDivMax, velocityDivergence[pos]);
      pressureXGradMin = Math.min(pressureXGradMin, xPressureGradient[pos]);
      pressureXGradMax = Math.max(pressureXGradMax, xPressureGradient[pos]);
      pressureYGradMin = Math.min(pressureYGradMin, yPressureGradient[pos]);
      pressureYGradMax = Math.max(pressureYGradMax, yPressureGradient[pos]);
    }

    particleMatrix.setXVelocityMinMax(velXmin, velXmax, currentIteration);
    particleMatrix.setYVelocityMinMax(velYmin, velYmax, currentIteration);
    particleMatrix.setVelocityMinMax(velMin, velMax, currentIteration);
    particleMatrix.setTemperatureMinMax(tempMin, tempMax, currentIteration);
    particleMatrix.setVelocityCurlMinMax(velCurlMin, velCurlMax, currentIteration);
    particleMatrix.setPressureMinMax(presMin, presMax, currentIteration);
    particleMatrix.setVelocityDivergenceMinMax(velDivMin, velDivMax, currentIteration);
    particleMatrix.setXPressureGradientMinMax(pressureXGradMin, pressureXGradMax, currentIteration);
    particleMatrix.setYPressureGradientMinMax(pressureYGradMin, pressureYGradMax, currentIteration);
  }

  /**
   * Retourne si la position est hors de la matrice
   *
   * @param pos La position
   * @return vrai si la position est hors de la matrice, faux sinon
   */
  protected boolean isPosOutBoundary(int pos) {
    return pos < 0 || pos >= simulationData.getCurrentParticleMatrix().getSize();
  }

  /**
   * Determine si une cellule est hors de la matrice ou s'il y a un obstacle
   *
   * @param pos La position de la cellule
   * @return vrai si la cellule est hors de la matrice, faux sinon
   */
  protected boolean isCellObstructed(int pos) {
    if (isPosOutBoundary(pos)) return true;

    if (this.simulationData.getObstacle() == null) return false;

    return this.simulationData.getObstacle().getElementAt(pos) != null;
  }

  /**
   * Retourne la position d'une cellule voisine ou -1 si la cellule est hors de la matrice
   *
   * @param pos La position de la cellule
   * @param xLength La taille en x de la matrice
   * @param yLength La taille en y de la matrice
   * @param xOffset Le décalage en x
   * @param yOffset Le décalage en y
   * @return La position de la cellule voisine ou la cellule la plus proche si la cellule est hors
   *     de la matrice
   */
  protected static int getPosAtOffset(int pos, int xLength, int yLength, int xOffset, int yOffset) {
    return WMath.clamp(pos % xLength + xOffset, 0, xLength - 1)
        + WMath.clamp(pos / xLength + yOffset, 0, yLength - 1) * xLength;
  }

  /** Applique les conditions sur les frontière (obstacles et bordures) */
  protected void applyBoundaryConditions() {
    timer.start("ApplyBoundaryConditions");

    ParticleMatrix particleMatrix = simulationData.getCurrentParticleMatrix();
    int xLength = particleMatrix.getXLength();
    int yLength = particleMatrix.getYLength();
    int size = xLength * yLength;

    WObstacleCellMatrix obstacles = simulationData.getObstacle();

    if (obstacles == null) return;

    double[] xVelocity = particleMatrix.getXVelocity().getMatrix();
    double[] yVelocity = particleMatrix.getYVelocity().getMatrix();
    double[] pressure = particleMatrix.getPressure().getMatrix();
    double[] areaDensity = particleMatrix.getAreaDensity().getMatrix();

    WObstacleCell obstacleCell;
    OBSTACLE_TYPE obstacle;

    double normalOrientation, nCos, nSin;

    int nPos;

    double angle, cos, sin, xV, yV, xRot, yRot;

    for (int pos = 0; pos < size; pos++) {
      obstacleCell = obstacles.getElementAt(pos);

      if (obstacleCell == null) continue;

      obstacle = obstacles.getElementAt(pos).getObstacleType();

      if (obstacle == OBSTACLE_TYPE.NONE) continue;

      xVelocity[pos] = 0.0d;
      yVelocity[pos] = 0.0d;
      pressure[pos] = 0.0d;

      if (obstacle == OBSTACLE_TYPE.ZERO) continue;

      normalOrientation = obstacleCell.getAverageNormal();

      if (Double.isNaN(normalOrientation)) continue;

      nCos = Math.cos(normalOrientation);
      nSin = Math.sin(normalOrientation);

      nPos = getPosAtOffset(pos, xLength, yLength, (int) Math.round(nCos), (int) Math.round(nSin));
      if (isCellObstructed(nPos)) continue;

      pressure[pos] = pressure[nPos];

      switch (obstacle) {
        case INFLOW:
          xVelocity[pos] = WMath.normalize(xVelocity[nPos] * Math.abs(nCos));
          yVelocity[pos] = WMath.normalize(yVelocity[nPos] * Math.abs(nSin));
          pressure[pos] *= -1;
          break;

        case OUTFLOW:
          xVelocity[pos] = WMath.normalize(xVelocity[nPos] * Math.abs(nCos));
          yVelocity[pos] = WMath.normalize(yVelocity[nPos] * Math.abs(nSin));
          areaDensity[pos] = areaDensity[nPos];
          break;

        case SLIP:
          angle = WMath.PI_OVER_2 - normalOrientation;
          cos = Math.cos(angle);
          sin = Math.sin(angle);
          xV = xVelocity[nPos];
          yV = yVelocity[nPos];

          // Rotation de la vélocité sur la normale
          // Inversion du sens de la vélocité parallele à la normale
          xRot = xV * cos - yV * sin;
          yRot = -1 * (xV * sin + yV * cos);

          // Dérotation de la vélocité
          xVelocity[pos] = WMath.normalize(xRot * cos + yRot * sin);
          yVelocity[pos] = WMath.normalize(-1 * (xRot * sin - yRot * cos));

          break;

        case STICK:
          xVelocity[pos] = -xVelocity[nPos];
          yVelocity[pos] = -yVelocity[nPos];
          break;

        default:
      }
    }

    timer.stop("ApplyBoundaryConditions");
  }

  /**
   * Retourne le temp d'execution moyen
   *
   * @return Le temp d'execution moyen (en ms)
   */
  public long getAverageStepExecutionTime() {
    long advectionAverage = timer.getAverage("Advection");
    long vortexConfinementAverage = timer.getAverage("VorticityConfinement");
    long diffusionAverage = timer.getAverage("Diffusion");
    long velocityDivergenceAverage = timer.getAverage("VelocityDivergence");
    long pressureSolverAverage = timer.getAverage("PressureSolver");
    long pressureGradientAverage = timer.getAverage("PressureGradient");
    long substractPressureGradientAverage = timer.getAverage("SubstractPressureGradient");
    long addForceAverage = timer.getAverage("AddForce");
    return advectionAverage
        + vortexConfinementAverage
        + diffusionAverage
        + velocityDivergenceAverage
        + pressureSolverAverage
        + pressureGradientAverage
        + substractPressureGradientAverage
        + addForceAverage;
  }

  /** Afficher la moyenne de temps d'execution de la simulation */
  public void printAverageTime() {
    long advectionAverage = timer.getAverage("Advection");
    long vortexConfinementAverage = timer.getAverage("VorticityConfinement");
    long diffusionAverage = timer.getAverage("Diffusion");
    long velocityDivergenceAverage = timer.getAverage("VelocityDivergence");
    long pressureSolverAverage = timer.getAverage("PressureSolver");
    long pressureGradientAverage = timer.getAverage("PressureGradient");
    long substractPressureGradientAverage = timer.getAverage("SubstractPressureGradient");
    long addForceAverage = timer.getAverage("AddForce");
    long applyBoundaryConditionsAverage = timer.getAverage("ApplyBoundaryConditions");
    long totalAverage =
        advectionAverage
            + vortexConfinementAverage
            + diffusionAverage
            + velocityDivergenceAverage
            + pressureSolverAverage
            + pressureGradientAverage
            + substractPressureGradientAverage
            + addForceAverage;

    System.out.println(
        "Moyenne de temps d'execution de la simulation (" + getClass().getSimpleName() + ")");

    System.out.println("Advection : " + advectionAverage + " ms");
    System.out.println("VorticityConfinement : " + vortexConfinementAverage + " ms");
    System.out.println("Diffusion : " + diffusionAverage + " ms");
    System.out.println("AddForce : " + addForceAverage + " ms");
    System.out.println("VelocityDivergence : " + velocityDivergenceAverage + " ms");
    System.out.println("PressureSolver : " + pressureSolverAverage + " ms");
    System.out.println("PressureGradient : " + pressureGradientAverage + " ms");
    System.out.println("SubstractPressureGradient : " + substractPressureGradientAverage + " ms");
    System.out.println("ApplyBoundaryConditions : " + applyBoundaryConditionsAverage + " ms");
    System.out.println("Total : " + totalAverage + " ms");
  }

  /** {@inheritDoc} */
  @Override
  public void reinitialize() {
    boolean isRunning = this.isRunning();
    if (isRunning) stop();
    else waitForCalculation();

    simulationData.resetParticleMatrix();
    currentTime = 0;
    currentIteration = 0;
    timer.reinitialize();

    if (isRunning) start();

    PhysicsEngine.LOGGER.info(
        LocaleManager.getLocaleResourceBundle().getString("log.physicsEngine.reseted"));
  }
}
