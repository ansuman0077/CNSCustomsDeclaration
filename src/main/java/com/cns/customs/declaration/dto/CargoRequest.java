package com.cns.customs.declaration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CargoRequest(
        @NotBlank(message = "Cargo number is required")
        String cargoNumber,

        @NotNull(message = "Weight is required")
        @Positive(message = "Weight must be a positive number in kilograms")
        Double weight,

        @NotBlank(message = "Flight number is required")
        String flightNumber
) {}
