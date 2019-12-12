package com.vqd.tme.na2a.exception.applicability;

public class CouldNotSaveException extends ApplicabilityException {
  private static final long serialVersionUID = 1L;

  public CouldNotSaveException(String message, Object... args) {
    super(message, args);
  }
}
