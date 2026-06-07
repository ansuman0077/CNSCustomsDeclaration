package com.cns.customs.declaration.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "flights")
@Data
public class Flight {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String flightNumber;

  @Column(nullable = false)
  private LocalDateTime arrivalDateTime;

  @Column(nullable = false)
  private String airportCode;

  @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Cargo> cargos;
}
