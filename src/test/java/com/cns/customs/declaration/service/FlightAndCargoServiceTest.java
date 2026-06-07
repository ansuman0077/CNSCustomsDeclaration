package com.cns.customs.declaration.service;

import com.cns.customs.declaration.dto.CargoRequest;
import com.cns.customs.declaration.dto.CargoResponse;
import com.cns.customs.declaration.dto.FlightRequest;
import com.cns.customs.declaration.dto.FlightResponse;
import com.cns.customs.declaration.enums.CargoStatus;
import com.cns.customs.declaration.exception.DuplicateFlightException;
import com.cns.customs.declaration.exception.InvalidStatusTransitionException;
import com.cns.customs.declaration.model.Cargo;
import com.cns.customs.declaration.model.Flight;
import com.cns.customs.declaration.repo.CargoRepository;
import com.cns.customs.declaration.repo.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightAndCargoServiceTest {

  @Mock private FlightRepository flightRepository;

  @Mock private CargoRepository cargoRepository;

  @InjectMocks private FlightServiceImpl flightService;

  @InjectMocks private CargoServiceImpl cargoService;

  @Test
  void createFlight_shouldReturnFlightResponse_whenFlightNumberIsUnique() {
    LocalDateTime futureDate = LocalDateTime.now().plusDays(1);
    FlightRequest request = new FlightRequest("BA123", futureDate, "LGW");

    Flight savedFlight = new Flight();
    savedFlight.setFlightNumber("BA123");
    savedFlight.setArrivalDateTime(futureDate);
    savedFlight.setAirportCode("LGW");

    when(flightRepository.findByFlightNumber("BA123")).thenReturn(Optional.empty());
    when(flightRepository.save(any(Flight.class))).thenReturn(savedFlight);

    FlightResponse response = flightService.createFlight(request);

    assertThat(response.flightNumber()).isEqualTo("BA123");
    assertThat(response.airportCode()).isEqualTo("LGW");
    assertThat(response.arrivalDateTime()).isEqualTo(futureDate);
  }

  @Test
  void createFlight_shouldThrowDuplicateFlightException_whenFlightNumberAlreadyExists() {
    FlightRequest request = new FlightRequest("BA123", LocalDateTime.now().plusDays(1), "LGW");

    Flight existing = new Flight();
    existing.setFlightNumber("BA123");
    when(flightRepository.findByFlightNumber("BA123")).thenReturn(Optional.of(existing));

    assertThatThrownBy(() -> flightService.createFlight(request))
        .isInstanceOf(DuplicateFlightException.class)
        .hasMessageContaining("BA123");
  }

  @Test
  void createCargo_shouldSetStatusToNotArrived_always() {
    CargoRequest request = new CargoRequest("CGO001", 150.0, "BA123");

    Flight flight = new Flight();
    flight.setFlightNumber("BA123");

    Cargo savedCargo = new Cargo();
    savedCargo.setCargoNumber("CGO001");
    savedCargo.setWeight(150.0);
    savedCargo.setStatus(CargoStatus.NOT_ARRIVED);
    savedCargo.setFlight(flight);

    when(cargoRepository.findByCargoNumber("CGO001")).thenReturn(Optional.empty());
    when(flightRepository.findByFlightNumber("BA123")).thenReturn(Optional.of(flight));
    when(cargoRepository.save(any(Cargo.class))).thenReturn(savedCargo);

    CargoResponse response = cargoService.createCargo(request);

    assertThat(response.status()).isEqualTo(CargoStatus.NOT_ARRIVED);
    assertThat(response.cargoNumber()).isEqualTo("CGO001");
    assertThat(response.weight()).isEqualTo(150.0);
  }

  @Test
  void updateCargoStatus_shouldThrowInvalidStatusTransitionException_whenTransitionIsIllegal() {
    Cargo cargo = new Cargo();
    cargo.setCargoNumber("CGO001");
    cargo.setStatus(CargoStatus.ARRIVED);

    when(cargoRepository.findByCargoNumber("CGO001")).thenReturn(Optional.of(cargo));

    assertThatThrownBy(() -> cargoService.updateCargoStatus("CGO001", CargoStatus.NOT_ARRIVED))
        .isInstanceOf(InvalidStatusTransitionException.class)
        .hasMessageContaining("ARRIVED")
        .hasMessageContaining("NOT_ARRIVED");
  }
}
