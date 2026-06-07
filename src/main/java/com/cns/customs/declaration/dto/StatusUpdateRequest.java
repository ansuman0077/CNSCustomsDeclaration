package com.cns.customs.declaration.dto;

import com.cns.customs.declaration.enums.CargoStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(
        @NotNull(message = "Status is required")
        CargoStatus status
) {}
