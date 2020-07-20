package com.java.car.rest;


import com.java.car.domain.Vehicles;
import com.java.car.repository.VehiclesRepository;
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
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Vehicles}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VehiclesResource {

    private static final String ENTITY_NAME = "vehicles";
    private final Logger log = LoggerFactory.getLogger(VehiclesResource.class);
    private final VehiclesRepository vehiclesRepository;
    @Value("${spring.application.name}")
    private String applicationName;

    public VehiclesResource(VehiclesRepository vehiclesRepository) {
        this.vehiclesRepository = vehiclesRepository;
    }

    /**
     * {@code POST  /vehicles} : Create a new vehicles.
     *
     * @param vehicles the vehicles to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vehicles, or with status {@code 400 (Bad Request)} if the vehicles has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vehicles")
    public ResponseEntity<Vehicles> createVehicles(@RequestBody Vehicles vehicles) throws URISyntaxException {
        log.debug("REST request to save Vehicles : {}", vehicles);
        if (vehicles.getId() != null) {
            throw new BadRequestAlertException("A new vehicles cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vehicles result = vehiclesRepository.save(vehicles);
        return ResponseEntity.created(new URI("/api/vehicles/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /vehicles} : Updates an existing vehicles.
     *
     * @param vehicles the vehicles to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vehicles,
     * or with status {@code 400 (Bad Request)} if the vehicles is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vehicles couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vehicles")
    public ResponseEntity<Vehicles> updateVehicles(@RequestBody Vehicles vehicles) throws URISyntaxException {
        log.debug("REST request to update Vehicles : {}", vehicles);
        if (vehicles.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Vehicles result = vehiclesRepository.save(vehicles);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, vehicles.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /vehicles} : get all the vehicles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vehicles in body.
     */
    @GetMapping("/vehicles")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Vehicles> getAllVehicles() {
        log.debug("REST request to get all Vehicles");
        return vehiclesRepository.findAll();
    }

    /**
     * {@code GET  /vehicles/:id} : get the "id" vehicles.
     *
     * @param id the id of the vehicles to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vehicles, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<Vehicles> getVehicles(@PathVariable Long id) {
        log.debug("REST request to get Vehicles : {}", id);
        Optional<Vehicles> vehicles = vehiclesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vehicles);
    }

    /**
     * {@code DELETE  /vehicles/:id} : delete the "id" vehicles.
     *
     * @param id the id of the vehicles to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<Void> deleteVehicles(@PathVariable Long id) {
        log.debug("REST request to delete Vehicles : {}", id);
        vehiclesRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
