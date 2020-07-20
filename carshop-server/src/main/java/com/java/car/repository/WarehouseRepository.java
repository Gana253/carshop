package com.java.car.repository;


import com.java.car.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Warehouse entity.
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
