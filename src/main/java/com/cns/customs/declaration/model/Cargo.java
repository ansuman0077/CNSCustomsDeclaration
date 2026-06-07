package com.cns.customs.declaration.model;

import com.cns.customs.declaration.enums.CargoStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cargo")
@Data
public class Cargo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String cargoNumber;

  @Column(nullable = false)
  private Double weight;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private CargoStatus status;

  @ManyToOne
  @JoinColumn(name = "flight_id")
  private Flight flight;
}
