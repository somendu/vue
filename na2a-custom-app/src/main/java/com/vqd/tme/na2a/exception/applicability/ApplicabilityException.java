package com.vqd.tme.na2a.exception.applicability;

/**
 * Functional exception for rejecting backend actions
 *
 * @author edgardegraaff
 *
 */
public abstract class ApplicabilityException extends Exception {
  private static final long serialVersionUID = 1L;

  public ApplicabilityException(String message, Object... args) {
    this(null, message, args);
  }

  public ApplicabilityException(Throwable cause, String message, Object... args) {
    super(String.format(message, args), cause, false, false);
  }
}

