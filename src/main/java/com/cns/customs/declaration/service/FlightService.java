package com.cns.customs.declaration.service;

import com.cns.customs.declaration.dto.FlightRequest;
import com.cns.customs.declaration.dto.FlightResponse;

import java.util.List;

public interface FlightService {
    FlightResponse createFlight(FlightRequest request);
    FlightResponse getFlight(String flightNumber);
    List<FlightResponse> getAllFlights();
}
