package com.java.car.repository;


import com.java.car.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * Spring Data  repository for the Cars entity.
 */
@Repository
@CrossOrigin(origins = "http://localhost:4200")
public interface CarsRepository extends JpaRepository<Car, Long> {
}
