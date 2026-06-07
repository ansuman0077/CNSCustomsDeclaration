package com.cns.customs.declaration.repo;

import com.cns.customs.declaration.model.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {

  Optional<Cargo> findByCargoNumber(String cargoNumber);
}
