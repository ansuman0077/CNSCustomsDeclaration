package com.cns.customs.declaration.service;

import com.cns.customs.declaration.dto.CargoRequest;
import com.cns.customs.declaration.dto.CargoResponse;
import com.cns.customs.declaration.enums.CargoStatus;
import com.cns.customs.declaration.exception.CargoNotFoundException;
import com.cns.customs.declaration.exception.DuplicateCargoException;
import com.cns.customs.declaration.exception.FlightNotFoundException;
import com.cns.customs.declaration.exception.InvalidStatusTransitionException;
import com.cns.customs.declaration.model.Cargo;
import com.cns.customs.declaration.model.Flight;
import com.cns.customs.declaration.repo.CargoRepository;
import com.cns.customs.declaration.repo.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CargoServiceImpl implements CargoService {

  private final CargoRepository cargoRepository;
  private final FlightRepository flightRepository;

  @Override
  public CargoResponse createCargo(CargoRequest request) {
    if (cargoRepository.findByCargoNumber(request.cargoNumber()).isPresent()) {
      throw new DuplicateCargoException("Cargo number already exists: " + request.cargoNumber());
    }
    Flight flight =
        flightRepository
            .findByFlightNumber(request.flightNumber())
            .orElseThrow(
                () -> new FlightNotFoundException("Flight not found: " + request.flightNumber()));

    Cargo cargo = new Cargo();
    cargo.setCargoNumber(request.cargoNumber());
    cargo.setWeight(request.weight());
    cargo.setStatus(CargoStatus.NOT_ARRIVED);
    cargo.setFlight(flight);
    return toResponse(cargoRepository.save(cargo));
  }

  @Override
  public CargoResponse updateCargoStatus(String cargoNumber, CargoStatus newStatus) {
    Cargo cargo =
        cargoRepository
            .findByCargoNumber(cargoNumber)
            .orElseThrow(() -> new CargoNotFoundException("Cargo not found: " + cargoNumber));

    validateTransition(cargo.getStatus(), newStatus);
    cargo.setStatus(newStatus);
    return toResponse(cargoRepository.save(cargo));
  }

  @Override
  public CargoResponse getCargo(String cargoNumber) {
    return cargoRepository
        .findByCargoNumber(cargoNumber)
        .map(this::toResponse)
        .orElseThrow(() -> new CargoNotFoundException("Cargo not found: " + cargoNumber));
  }

  private void validateTransition(CargoStatus current, CargoStatus next) {
    boolean valid =
        switch (current) {
          case NOT_ARRIVED -> next == CargoStatus.ARRIVED;
          case ARRIVED -> next == CargoStatus.CUSTOMS_CLEARED;
          case CUSTOMS_CLEARED -> false;
        };
    if (!valid) {
      throw new InvalidStatusTransitionException(
          "Cannot transition cargo status from "
              + current
              + " to "
              + next
              + ". Valid transitions: NOT_ARRIVED -> ARRIVED -> CUSTOMS_CLEARED");
    }
  }

  private CargoResponse toResponse(Cargo cargo) {
    return new CargoResponse(cargo.getCargoNumber(), cargo.getWeight(), cargo.getStatus());
  }
}
