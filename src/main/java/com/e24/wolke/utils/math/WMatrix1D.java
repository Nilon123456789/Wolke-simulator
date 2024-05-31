package com.e24.wolke.utils.math;

/**
 * La classe {@code WMatrix1D} permet d'utiliser des coordonnees 2d avec un tableau d'objets
 * utilisant une coordonnee 1d.
 *
 * @param <T> Le type d'objets dans la matrice
 * @author MeriBouisri
 */
public abstract class WMatrix1D<T> {

  /** La matrice d'objets */
  private final T[] matrix;

  /** La longueur en x de la matrice, immuable */
  private final int xLength;

  /** La longueur en y de la matrice, immuable */
  private final int yLength;

  /** La taille de la matrice, immuable */
  private final int size;

  /**
   * Construction d'un {@code WMatrix1D} avec une longueur en x et en y
   *
   * @param xLength La longueur en x de la matrice
   * @param yLength La longueur en y de la matrice
   */
  public WMatrix1D(int xLength, int yLength) {
    this.xLength = xLength;
    this.yLength = yLength;
    this.size = WMatrix1D.calculateMatrixSize(xLength, yLength);
    matrix = createGenericArray();
  }

  /**
   * Getter pour {@code this#matrix}
   *
   * @return La matrice d'elements de cette instance
   */
  public T[] getMatrix() {
    return this.matrix;
  }

  /**
   * Retourne la position des 8 cellules voisines de la position donnee. S'il n'y a aucune cellule
   * voisine a une certaine position, la position a une valeur de -1.
   *
   * <p>Le tableau retourné contient les positions des cellules vosines gauche a droite, de haut en
   * bas commencant par la cellule en haut a gauche. Le tableau ne contient pas la position passee
   * en parametre.
   *
   * <p>Par exemple, si les cellules voisines de la cellule [4] sont calculees, voici l'entree et la
   * sortie :
   *
   * <p>Input :
   *
   * <p>{ [0] [1] [2] [3] [4] [5] [6] [7] [8] }
   *
   * <p>Output :
   *
   * <p>{ [0] [1] [2] [3] [5] [6] [7] [8] }
   *
   * @param position La position autour de laquelle la position des voisins sera calculee
   * @return Tableau contenant la position des voisins de la cellule a la position donnee.
   */
  public int[] getNeighborPositions(int position) {
    int[] coords = this.getPos(position);
    int[] neighbors = new int[8];

    // Remplir le tableau de neighbors avec la position des 6 cellules entourant celle a la position

    // Begin counter at -1, bc it is updated before being used
    int n = -1;

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {

        // Ne compte pas la soi-meme
        if (i == 0 && j == 0) continue;

        // Increment current neighbor
        n++;

        // Initialize to invalid position value
        neighbors[n] = -1;

        // Calculate neighbor positions
        int posX = coords[0] + j;
        int posY = coords[1] + i;

        // Check if positions are within matrix
        if (posX < 0 || posX > this.xLength || posY < 0 || posY > this.yLength) continue;

        // Calculate position in 1d array
        int pos = this.getPos(posX, posY);

        // Check that it is within range of 1D array
        if (!this.isPositionInRange(pos)) continue;

        neighbors[n] = pos;
      }
    }

    return neighbors;
  }

  /**
   * Retourne l'objet aux coordonnées (x, y) de la matrice de cette instance
   *
   * @param x La coordonnée en x de l'element voulu
   * @param y La coordonnée en y de l'element voulu
   * @return L'objet aux coordonnées (x, y), {@code null} si aucun element se retrouve a ces
   *     coordonnées
   * @throws IndexOutOfBoundsException si la position est invalide selon l'implementation de {@code
   *     this#isPositionInRange(int)}
   */
  public T getElementAt(int x, int y) {
    if (!this.isPositionInRange(x, y)) throw new IndexOutOfBoundsException(x + ", " + y);

    return this.getElementAt(this.getPos(x, y));
  }

  /**
   * Retourne l'objet a la position donnee de la matrice de cette instance
   *
   * @param index L'index dans la matrice 1d
   * @return L'objet a l'index donné, {@code null} si aucun element se retrouve a ces coordonnées
   * @throws IndexOutOfBoundsException si la position est invalide selon l'implementation de {@code
   *     this#isPositionInRange(int)}
   */
  public T getElementAt(int index) {

    if (!this.isPositionInRange(index)) throw new IndexOutOfBoundsException(index);

    return this.matrix[index];
  }

  /**
   * Methode permettant de placer un element dans la matrice de cette instance
   *
   * @param element L'element a ajouter a la matrice
   * @param x La position en x de l'element
   * @param y La position en y de l'element
   * @throws IndexOutOfBoundsException si la position est invalide selon l'implementation de {@code
   *     this#isPositionInRange(int)}
   */
  public void setElementAt(T element, int x, int y) {
    if (!this.isPositionInRange(x, y)) throw new IndexOutOfBoundsException(x + ", " + y);

    this.setElementAt(element, this.getPos(x, y));
  }

  /**
   * Methode permettant de placer un element dans la matrice de cette instance
   *
   * @param element L'element a ajouter a la matrice
   * @param pos La position de l'element dans la matrice 1d
   * @throws IndexOutOfBoundsException si la position est invalide selon l'implementation de {@code
   *     this#isPositionInRange(int)}
   */
  public void setElementAt(T element, int pos) {
    if (!this.isPositionInRange(pos)) throw new IndexOutOfBoundsException(pos);

    this.matrix[pos] = element;
  }

  /**
   * Methode utilitaire permettant de convertir des coordonnées 2D en coordonnée 1D indiquant la
   * position dans {@code this#matrix}.
   *
   * @param x Coordonnee en x de la position
   * @param y Coordonnee en y de la position
   * @return Index de la position dans {@code this#matrix} a partir des coordonnees (x, y)
   */
  public int getPos(int x, int y) {
    return x + y * this.xLength;
  }

  /**
   * Methode utilitaire permettant de convertir des coordonnées 1D en coordonnée 2D indiquant la
   * position dans {@code this#matrix}.
   *
   * @param pos La position dans le tableau a 1 dimension
   * @return Coordonnes (x, y) representant les index (a partir de 0) de la position dans {@code
   *     this#matrix} a partir de la position passee en parametres
   */
  public int[] getPos(int pos) {
    if (!this.isPositionInRange(pos)) throw new IndexOutOfBoundsException(pos);

    int x = pos % xLength;
    int y = pos / xLength;

    return new int[] {x, y};
  }

  /**
   * Methode utilitaire permettant de determiner si la position passee en parametre est valide comme
   * index pour {@code this#matrix}.
   *
   * @param pos Position a verifier
   * @return {@code true} si la position est valide comme index pour {@code this#matrix}
   */
  public boolean isPositionInRange(int pos) {
    return (pos >= 0) && (pos < this.size);
  }

  /**
   * Methode utilitaire permettant de determine si la position 2d passee en parametre est valide
   * comme indices a la amtrice
   *
   * @param x L'indice en x
   * @param y L'indice en y
   * @return {@code true} si x et y sont valides comme index a {@code this#matrix}
   */
  public boolean isPositionInRange(int x, int y) {
    return !(x < 0 || x > this.xLength || y < 0 || y > this.yLength);
  }

  /**
   * Getter pour {@code this#xLength}, la longueur en x de la matrice
   *
   * @return La longueur en x de la matrice
   */
  public int getXLength() {
    return this.xLength;
  }

  /**
   * Getter pour {@code this#yLength}, la longueur en y de la matrice
   *
   * @return La longueur en y de la matrice
   */
  public int getYLength() {
    return this.yLength;
  }

  /**
   * Getter pour {@code this#xLength} et {@code this#yLength}, la longueur en x et en y de la
   * matrice, dans la forme d'un tableau
   *
   * @return La longueur en x et en y de la matrice, dans la forme d'un tableau
   */
  public int[] getXYLength() {
    return new int[] {this.xLength, this.yLength};
  }

  /**
   * Getter pour {@code this#size}, la taille de la matrice
   *
   * @return La taille de la matrice
   */
  public int getSize() {
    return this.size;
  }

  /**
   * Methode permettant de determiner si la position (pos) est situee sur la bordure
   *
   * @param pos La position a verifier
   * @return {@code true} si la position donnee est sur la bordure
   */
  public boolean isBorderPosition(int pos) {
    return WMatrix1D.isBorderPosition(pos, this.xLength, this.yLength);
  }

  /**
   * Methode permettant de determiner si la position (x, y) est situee sur la bordure
   *
   * @param x La coordonnee en x a verifier
   * @param y La coordonnee en y a verifier
   * @return {@code true} si la position donnee est sur la bordure
   */
  public boolean isBorderPosition(int x, int y) {
    return WMatrix1D.isBorderPosition(x, y, this.xLength, this.yLength);
  }

  /**
   * Methode utilitaire permettant de determiner si une position (x, y) est situee sur la bordure de
   * la matrice
   *
   * @param x La coordonnee en x de la position
   * @param y La coordonnee en y de la position
   * @param xLength La longueur horizontale de la matrice
   * @param yLength La longueur verticale de la matrice
   * @return {@code true} si la position est situee sur la bordure de la matrice de dimensions
   *     (xLength * yLength)
   */
  public static boolean isBorderPosition(int x, int y, int xLength, int yLength) {
    return x == 0 || y == 0 || x == xLength - 1 || y == yLength - 1;
  }

  /**
   * Methode utilitaire permettant de determiner si une position (x, y) est situee sur la bordure de
   * la matrice
   *
   * @param pos La position a verifier
   * @param xLength La longueur horizontale de la matrice
   * @param yLength La longueur verticale de la matrice
   * @return {@code true} si la position est situee sur la bordure de la matrice de dimensions
   *     (xLength * yLength)
   */
  public static boolean isBorderPosition(int pos, int xLength, int yLength) {
    int[] coords = WMatrix1D.getPosition2D(pos, xLength);
    return WMatrix1D.isBorderPosition(coords[0], coords[1], xLength, yLength);
  }

  /**
   * Methode utilitaire pour la creation d'un {@code WMatrix1D<Integer>}
   *
   * @param xLength Longueur en x de la matrice
   * @param yLength Longueur en y de la matrice
   * @return un nouveau {@code WMatrix1D<Integer>}
   */
  public static WMatrix1D<Integer> createIntegerMatrix(int xLength, int yLength) {
    return new WMatrix1D<Integer>(xLength, yLength) {
      @Override
      public Integer[] createGenericArray() {
        return new Integer[this.getSize()];
      }
    };
  }

  /**
   * Methode utilitaire pour la creation d'un {@code WMatrix1D<Integer>}
   *
   * @param xLength Longueur en x de la matrice
   * @param yLength Longueur en y de la matrice
   * @return un nouveau {@code WMatrix1D<Integer>}
   */
  public static WMatrix1D<Double> createDoubleMatrix(int xLength, int yLength) {
    return new WMatrix1D<Double>(xLength, yLength) {
      @Override
      public Double[] createGenericArray() {
        return new Double[this.getSize()];
      }
    };
  }

  /**
   * Methode utilitaire pour le calcul de la longueur d'un tableau 1D selon les dimensions d'une
   * matrice 2D
   *
   * @param xLength Longueur en x de la matrice
   * @param yLength Longueur en y de la matrice
   * @return La longueur du tableau 1D
   */
  public static int calculateMatrixSize(int xLength, int yLength) {
    return xLength * yLength;
  }

  /**
   * Methode utilitaire permettant de calculer la position 1D a partir de coordonnees 2D
   *
   * @param x La coordonnee en x de l'element de la matrice
   * @param y La coordonnee en y de l'element de la matrice
   * @param xLength La longueur horizontale de la matrice
   * @return La position correspondant aux coordonnees (x, y) dans un tableau 1D
   */
  public static int getPosition1D(int x, int y, int xLength) {
    return x + y * xLength;
  }

  /**
   * Methode utilitaire permettant de calculer les coordonnees 2D a partir d'une position 1D
   *
   * @param pos La position 1D de l'element du tableau
   * @param xLength La longueur horizontale de la matrice dans laquelle les coordonnees 2D seraient
   *     inscrites
   * @return Un tableau {@code int[2]} contenant les coordonnees (x, y) correspondant a la position
   */
  public static int[] getPosition2D(int pos, int xLength) {
    int x = pos % xLength;
    int y = pos / xLength;

    return new int[] {x, y};
  }

  /**
   * Methode permettant d'obtenir les indices de toutes les positions situees sur la bordure de la
   * matrice
   *
   * @return Un tableau {@code int[]} contenant les indices de toutes les positions situees sur la
   *     bordure de la matrice
   */
  public int[] getBorderIndices() {
    return WMatrix1D.getBorderIndices(this.xLength, this.yLength);
  }

  /**
   * Methode permettant d'obtenir les indices de toutes les positions situees sur la bordure de la
   * matrice
   *
   * @param xLength La longueur horizontale de la matrice
   * @param yLength La longueur verticale de la matrice
   * @return Le tableau contenant les indices des positions situees sur la bordure de la matrice
   */
  public static int[] getBorderIndices(int xLength, int yLength) {
    int numPositions = (2 * (xLength + yLength)) - 4;
    int[] borderIndices = new int[numPositions];

    int cnt = 0;

    for (int i = 0; i < WMatrix1D.calculateMatrixSize(xLength, yLength); i++) {
      if (!WMatrix1D.isBorderPosition(i, xLength, yLength)) continue;

      borderIndices[cnt] = i;
      cnt++;
    }
    return borderIndices;
  }

  /**
   * Methode permettant de convertir le {@code this#matrix} en {@code array}. Utilisee pour eviter
   * les avertissements de 'unchecked'.
   *
   * <p>Les classes qui generalisent {@code WMatrix1D} connaissent leur propre type, et peuvent donc
   * creer le tableau de type generique. Il n'est pas possible de creer un {@code array} de type
   * {@code T[]} sans avertissement.
   *
   * <p>L'implementation de cette methode doit toujours correspondre au code suivant. Par exemple,
   * pour {@code WMatrix1D<Integer>} :
   *
   * <p>{@code return new Integer[this.getSize()]}
   *
   * @return Le {@code array} qui contient les elements
   */
  protected abstract T[] createGenericArray();
}
