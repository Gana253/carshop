package com.java.car.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cars.
 */
@Entity
@Table(name = "cars")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location")
    private String location;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cars_id")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)

    @JsonManagedReference

    private Set<Vehicles> vehicles = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "cars", allowSetters = true)
    private Warehouse wareHouse;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Car location(String location) {
        this.location = location;
        return this;
    }

    public Set<Vehicles> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Set<Vehicles> vehicles) {
        this.vehicles = vehicles;
    }

    public Car vehicles(Set<Vehicles> vehicles) {
        this.vehicles = vehicles;
        return this;
    }

    public Car addVehicles(Vehicles vehicles) {
        this.vehicles.add(vehicles);
        vehicles.setCar(this);
        return this;
    }

    public Car removeVehicles(Vehicles vehicles) {
        this.vehicles.remove(vehicles);
        vehicles.setCar(null);
        return this;
    }

    public Warehouse getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(Warehouse warehouse) {
        this.wareHouse = warehouse;
    }

    public Car cars(Warehouse warehouse) {
        this.wareHouse = warehouse;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Car)) {
            return false;
        }
        return id != null && id.equals(((Car) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cars{" +
                "id=" + getId() +
                ", location='" + getLocation() + "'" +
                "}";
    }
}
