package com.cns.customs.declaration.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record FlightRequest(
    @NotBlank(message = "Flight number is required") String flightNumber,
    @NotNull(message = "Arrival date/time is required")
        @Future(message = "Arrival date/time must be in the future")
        LocalDateTime arrivalDateTime,
    @NotBlank(message = "Airport code is required")
        @Size(min = 3, max = 4, message = "Airport code must be 3-4 characters (e.g. LGW)")
        String airportCode) {}
