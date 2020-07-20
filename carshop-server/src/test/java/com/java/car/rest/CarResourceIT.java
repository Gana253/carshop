package com.java.car.rest;


import com.java.CarshopServerApplication;
import com.java.car.domain.Car;
import com.java.car.repository.CarsRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarsResource} REST controller.
 */
@SpringBootTest(classes = CarshopServerApplication.class)
@AutoConfigureMockMvc
@WithMockUser
public class CarResourceIT {

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private CarsRepository carsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarsMockMvc;

    private Car car;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createEntity(EntityManager em) {
        Car car = new Car()
                .location(DEFAULT_LOCATION);
        return car;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Car createUpdatedEntity(EntityManager em) {
        Car car = new Car()
                .location(UPDATED_LOCATION);
        return car;
    }

    @BeforeEach
    public void initTest() {
        car = createEntity(em);
    }

    @Test
    @Transactional
    public void createCars() throws Exception {
        int databaseSizeBeforeCreate = carsRepository.findAll().size();
        // Create the Cars
        restCarsMockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(car)))
                .andExpect(status().isCreated());

        // Validate the Cars in the database
        List<Car> carList = carsRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate + 1);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createCarsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = carsRepository.findAll().size();

        // Create the Cars with an existing ID
        car.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarsMockMvc.perform(post("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(car)))
                .andExpect(status().isBadRequest());

        // Validate the Cars in the database
        List<Car> carList = carsRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(car);

        // Get all the carsList
        restCarsMockMvc.perform(get("/api/cars?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(car.getId().intValue())))
                .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    public void getCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(car);

        // Get the cars
        restCarsMockMvc.perform(get("/api/cars/{id}", car.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(car.getId().intValue()))
                .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    public void getNonExistingCars() throws Exception {
        // Get the cars
        restCarsMockMvc.perform(get("/api/cars/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(car);

        int databaseSizeBeforeUpdate = carsRepository.findAll().size();

        // Update the cars
        Car updatedCar = carsRepository.findById(car.getId()).get();
        // Disconnect from session so that the updates on updatedCars are not directly saved in db
        em.detach(updatedCar);
        updatedCar
                .location(UPDATED_LOCATION);

        restCarsMockMvc.perform(put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(updatedCar)))
                .andExpect(status().isOk());

        // Validate the Cars in the database
        List<Car> carList = carsRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
        Car testCar = carList.get(carList.size() - 1);
        assertThat(testCar.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingCars() throws Exception {
        int databaseSizeBeforeUpdate = carsRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarsMockMvc.perform(put("/api/cars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(car)))
                .andExpect(status().isBadRequest());

        // Validate the Cars in the database
        List<Car> carList = carsRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCars() throws Exception {
        // Initialize the database
        carsRepository.saveAndFlush(car);

        int databaseSizeBeforeDelete = carsRepository.findAll().size();

        // Delete the cars
        restCarsMockMvc.perform(delete("/api/cars/{id}", car.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Car> carList = carsRepository.findAll();
        assertThat(carList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
