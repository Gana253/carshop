package com.java.car.rest;

import com.java.CarshopServerApplication;
import com.java.car.domain.Vehicles;
import com.java.car.repository.VehiclesRepository;
import com.java.car.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link VehiclesResource} REST controller.
 */
@SpringBootTest(classes = CarshopServerApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class VehiclesResourceIT {

    private static final String DEFAULT_MAKE = "AAAAAAAAAA";
    private static final String UPDATED_MAKE = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final long DEFAULT_yearModel = 0L;
    private static final long UPDATED_yearModel = 1L;

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final String DEFAULT_LICENSED = "AAAAAAAAAA";
    private static final String UPDATED_LICENSED = "BBBBBBBBBB";

    private static final Date DEFAULT_dateAdded = Date.from(Instant.now());
    private static final Date UPDATED_dateAdded = Date.from(Instant.now());

    @Autowired
    private VehiclesRepository vehiclesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVehiclesMockMvc;

    private Vehicles vehicles;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicles createEntity(EntityManager em) {
        Vehicles vehicles = new Vehicles()
                .make(DEFAULT_MAKE)
                .model(DEFAULT_MODEL)
                .yearModel(DEFAULT_yearModel)
                .price(DEFAULT_PRICE)
                .licensed(DEFAULT_LICENSED)
                .dateAdded(DEFAULT_dateAdded);
        return vehicles;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vehicles createUpdatedEntity(EntityManager em) {
        Vehicles vehicles = new Vehicles()
                .make(UPDATED_MAKE)
                .model(UPDATED_MODEL)
                .yearModel(UPDATED_yearModel)
                .price(UPDATED_PRICE)
                .licensed(UPDATED_LICENSED)
                .dateAdded(UPDATED_dateAdded);
        return vehicles;
    }

    @BeforeEach
    public void initTest() {
        vehicles = createEntity(em);
    }

    @Test
    @Transactional
    public void createVehicles() throws Exception {
        int databaseSizeBeforeCreate = vehiclesRepository.findAll().size();
        // Create the Vehicles
        restVehiclesMockMvc.perform(post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicles)))
                .andExpect(status().isCreated());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeCreate + 1);
        Vehicles testVehicles = vehiclesList.get(vehiclesList.size() - 1);
        assertThat(testVehicles.getMake()).isEqualTo(DEFAULT_MAKE);
        assertThat(testVehicles.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testVehicles.getYearModel()).isEqualTo(DEFAULT_yearModel);
        assertThat(testVehicles.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVehicles.getLicensed()).isEqualTo(DEFAULT_LICENSED);
    }

    @Test
    @Transactional
    public void createVehiclesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = vehiclesRepository.findAll().size();

        // Create the Vehicles with an existing ID
        vehicles.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVehiclesMockMvc.perform(post("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicles)))
                .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get all the vehiclesList
        restVehiclesMockMvc.perform(get("/api/vehicles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(vehicles.getId().intValue())))
                .andExpect(jsonPath("$.[*].make").value(hasItem(DEFAULT_MAKE)))
                .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
                .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
                .andExpect(jsonPath("$.[*].licensed").value(hasItem(DEFAULT_LICENSED)));
    }

    @Test
    @Transactional
    public void getVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        // Get the vehicles
        restVehiclesMockMvc.perform(get("/api/vehicles/{id}", vehicles.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(vehicles.getId().intValue()))
                .andExpect(jsonPath("$.make").value(DEFAULT_MAKE))
                .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
                .andExpect(jsonPath("$.yearModel").value(DEFAULT_yearModel))
                .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
                .andExpect(jsonPath("$.licensed").value(DEFAULT_LICENSED));
    }

    @Test
    @Transactional
    public void getNonExistingVehicles() throws Exception {
        // Get the vehicles
        restVehiclesMockMvc.perform(get("/api/vehicles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();

        // Update the vehicles
        Vehicles updatedVehicles = vehiclesRepository.findById(vehicles.getId()).get();
        // Disconnect from session so that the updates on updatedVehicles are not directly saved in db
        em.detach(updatedVehicles);
        updatedVehicles
                .make(UPDATED_MAKE)
                .model(UPDATED_MODEL)
                .yearModel(UPDATED_yearModel)
                .price(UPDATED_PRICE)
                .licensed(UPDATED_LICENSED)
                .dateAdded(UPDATED_dateAdded);

        restVehiclesMockMvc.perform(put("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedVehicles)))
                .andExpect(status().isOk());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
        Vehicles testVehicles = vehiclesList.get(vehiclesList.size() - 1);
        assertThat(testVehicles.getMake()).isEqualTo(UPDATED_MAKE);
        assertThat(testVehicles.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testVehicles.getYearModel()).isEqualTo(UPDATED_yearModel);
        assertThat(testVehicles.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVehicles.getLicensed()).isEqualTo(UPDATED_LICENSED);
    }

    @Test
    @Transactional
    public void updateNonExistingVehicles() throws Exception {
        int databaseSizeBeforeUpdate = vehiclesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVehiclesMockMvc.perform(put("/api/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(vehicles)))
                .andExpect(status().isBadRequest());

        // Validate the Vehicles in the database
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVehicles() throws Exception {
        // Initialize the database
        vehiclesRepository.saveAndFlush(vehicles);

        int databaseSizeBeforeDelete = vehiclesRepository.findAll().size();

        // Delete the vehicles
        restVehiclesMockMvc.perform(delete("/api/vehicles/{id}", vehicles.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vehicles> vehiclesList = vehiclesRepository.findAll();
        assertThat(vehiclesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
