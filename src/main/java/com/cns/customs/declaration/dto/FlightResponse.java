package com.cns.customs.declaration.dto;

import java.time.LocalDateTime;
import java.util.List;

public record FlightResponse(
    String flightNumber,
    LocalDateTime arrivalDateTime,
    String airportCode,
    List<CargoResponse> cargoList) {}
