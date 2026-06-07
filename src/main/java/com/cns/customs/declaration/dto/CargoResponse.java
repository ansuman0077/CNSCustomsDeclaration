package com.cns.customs.declaration.dto;

import com.cns.customs.declaration.enums.CargoStatus;

public record CargoResponse(String cargoNumber, Double weight, CargoStatus status) {}
