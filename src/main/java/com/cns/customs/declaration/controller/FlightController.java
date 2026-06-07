package com.cns.customs.declaration.controller;

import com.cns.customs.declaration.dto.FlightRequest;
import com.cns.customs.declaration.dto.FlightResponse;
import com.cns.customs.declaration.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

  private final FlightService flightService;

  @PostMapping
  public ResponseEntity<FlightResponse> createFlight(@Valid @RequestBody FlightRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(flightService.createFlight(request));
  }

  @GetMapping("/{flightNumber}")
  public ResponseEntity<FlightResponse> getFlight(@PathVariable String flightNumber) {
    return ResponseEntity.ok(flightService.getFlight(flightNumber));
  }

  @GetMapping
  public ResponseEntity<List<FlightResponse>> getAllFlights() {
    return ResponseEntity.ok(flightService.getAllFlights());
  }
}
