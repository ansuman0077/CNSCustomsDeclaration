package com.cns.customs.declaration.service;

import com.cns.customs.declaration.dto.CargoResponse;
import com.cns.customs.declaration.dto.FlightRequest;
import com.cns.customs.declaration.dto.FlightResponse;
import com.cns.customs.declaration.exception.DuplicateFlightException;
import com.cns.customs.declaration.exception.FlightNotFoundException;
import com.cns.customs.declaration.model.Cargo;
import com.cns.customs.declaration.model.Flight;
import com.cns.customs.declaration.repo.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightServiceImpl implements FlightService {

  private final FlightRepository flightRepository;

  @Override
  public FlightResponse createFlight(FlightRequest request) {
    if (flightRepository.findByFlightNumber(request.flightNumber()).isPresent()) {
      throw new DuplicateFlightException("Flight number already exists: " + request.flightNumber());
    }
    Flight flight = new Flight();
    flight.setFlightNumber(request.flightNumber());
    flight.setArrivalDateTime(request.arrivalDateTime());
    flight.setAirportCode(request.airportCode());
    return toResponse(flightRepository.save(flight));
  }

  @Override
  public FlightResponse getFlight(String flightNumber) {
    return flightRepository
        .findByFlightNumber(flightNumber)
        .map(this::toResponse)
        .orElseThrow(() -> new FlightNotFoundException("Flight not found: " + flightNumber));
  }

  @Override
  public List<FlightResponse> getAllFlights() {
    return flightRepository.findAll().stream().map(this::toResponse).toList();
  }

  private FlightResponse toResponse(Flight flight) {
    List<CargoResponse> cargoList =
        flight.getCargos() == null
            ? List.of()
            : flight.getCargos().stream().map(this::toCargoResponse).toList();
    return new FlightResponse(
        flight.getFlightNumber(), flight.getArrivalDateTime(), flight.getAirportCode(), cargoList);
  }

  private CargoResponse toCargoResponse(Cargo cargo) {
    return new CargoResponse(cargo.getCargoNumber(), cargo.getWeight(), cargo.getStatus());
  }
}
