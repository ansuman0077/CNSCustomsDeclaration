package com.cns.customs.declaration.exception;

public class DuplicateFlightException extends RuntimeException {
  public DuplicateFlightException(String message) {
    super(message);
  }
}
