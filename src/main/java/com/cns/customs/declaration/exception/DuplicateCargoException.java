package com.cns.customs.declaration.exception;

public class DuplicateCargoException extends RuntimeException {
  public DuplicateCargoException(String message) {
    super(message);
  }
}
