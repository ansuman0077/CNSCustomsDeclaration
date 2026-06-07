package com.cns.customs.declaration.service;

import com.cns.customs.declaration.dto.CargoRequest;
import com.cns.customs.declaration.dto.CargoResponse;
import com.cns.customs.declaration.enums.CargoStatus;

public interface CargoService {
    CargoResponse createCargo(CargoRequest request);
    CargoResponse updateCargoStatus(String cargoNumber, CargoStatus newStatus);
    CargoResponse getCargo(String cargoNumber);
}
