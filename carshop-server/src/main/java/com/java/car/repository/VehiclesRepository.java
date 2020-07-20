package com.java.car.repository;


import com.java.car.domain.Vehicles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

/**
 * Spring Data  repository for the Vehicles entity.
 */
@Repository
public interface VehiclesRepository extends JpaRepository<Vehicles, Long> {
}
