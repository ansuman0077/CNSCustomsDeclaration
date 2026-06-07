package com.cns.customs.declaration.exception;

public class CargoNotFoundException extends RuntimeException {
  public CargoNotFoundException(String message) {
    super(message);
  }
}
