package com.e24.wolke.backend.models.editor.exceptions;

/**
 * La classe {@code WEditorException} permet de definir une exception relative au mode editeur
 *
 * @author MeriBouisri
 */
public class WEditorException extends Exception {

  /** Sérialisation de la classe */
  private static final long serialVersionUID = 1L;

  /** Construction d'un {@code WEditorException} */
  public WEditorException() {
    super();
  }

  /**
   * Construction d'un {@code WEditorException}
   *
   * @param message Le message a afficher
   */
  public WEditorException(String message) {
    super(message);
  }
}
