package com.java.car.rest;


import com.java.car.domain.Car;
import com.java.car.repository.CarsRepository;
import com.java.car.rest.errors.BadRequestAlertException;
import com.java.car.util.HeaderUtil;
import com.java.car.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * REST controller for managing {@link Car}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CarsResource {

    private static final String ENTITY_NAME = "cars";
    private final Logger log = LoggerFactory.getLogger(CarsResource.class);
    private final CarsRepository carsRepository;
    @Value("${spring.application.name}")
    private String applicationName;

    public CarsResource(CarsRepository carsRepository) {
        this.carsRepository = carsRepository;
    }

    /**
     * {@code POST  /cars} : Create a new cars.
     *
     * @param car the cars to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cars, or with status {@code 400 (Bad Request)} if the cars has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cars")
    public ResponseEntity<Car> createCars(@RequestBody Car car) throws URISyntaxException {
        log.debug("REST request to save Cars : {}", car);
        if (car.getId() != null) {
            throw new BadRequestAlertException("A new cars cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Car result = carsRepository.save(car);
        return ResponseEntity.created(new URI("/api/cars/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /cars} : Updates an existing cars.
     *
     * @param car the cars to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cars,
     * or with status {@code 400 (Bad Request)} if the cars is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cars couldn't be updated.
     */
    @PutMapping("/cars")
    public ResponseEntity<Car> updateCars(@RequestBody Car car) {
        log.debug("REST request to update Cars : {}", car);
        if (car.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Car result = carsRepository.save(car);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, car.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /cars} : get all the cars.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cars in body.
     */
    @GetMapping("/cars")
    @CrossOrigin(origins = "http://localhost:4200")
    public Collection<Car> getAllCars() {
        log.debug("REST request to get all Cars");
        return new ArrayList<>(carsRepository.findAll());
    }

    /**
     * {@code GET  /cars/:id} : get the "id" cars.
     *
     * @param id the id of the cars to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cars, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCars(@PathVariable Long id) {
        log.debug("REST request to get Cars : {}", id);
        Optional<Car> cars = carsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cars);
    }

    /**
     * {@code DELETE  /cars/:id} : delete the "id" cars.
     *
     * @param id the id of the cars to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCars(@PathVariable Long id) {
        log.debug("REST request to delete Cars : {}", id);
        carsRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
