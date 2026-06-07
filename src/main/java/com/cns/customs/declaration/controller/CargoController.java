package com.cns.customs.declaration.controller;

import com.cns.customs.declaration.dto.CargoRequest;
import com.cns.customs.declaration.dto.CargoResponse;
import com.cns.customs.declaration.dto.StatusUpdateRequest;
import com.cns.customs.declaration.service.CargoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
public class CargoController {

  private final CargoService cargoService;

  @PostMapping
  public ResponseEntity<CargoResponse> createCargo(@Valid @RequestBody CargoRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(cargoService.createCargo(request));
  }

  @PatchMapping("/{cargoNumber}/status")
  public ResponseEntity<CargoResponse> updateStatus(
      @PathVariable String cargoNumber, @Valid @RequestBody StatusUpdateRequest request) {
    return ResponseEntity.ok(cargoService.updateCargoStatus(cargoNumber, request.status()));
  }

  @GetMapping("/{cargoNumber}")
  public ResponseEntity<CargoResponse> getCargo(@PathVariable String cargoNumber) {
    return ResponseEntity.ok(cargoService.getCargo(cargoNumber));
  }
}
