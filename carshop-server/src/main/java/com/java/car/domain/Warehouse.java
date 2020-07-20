package com.java.car.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Warehouse.
 */
@Entity
@Table(name = "warehouse")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(orphanRemoval = true,
            cascade = CascadeType.ALL)
    @JoinColumn(unique = true)
    @JsonProperty("location")
    private Location location;

    @OneToMany(cascade = CascadeType.ALL)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JoinColumn(name = "ware_house_id")
    private Set<Car> cars = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Warehouse name(String name) {
        this.name = name;
        return this;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Warehouse locationId(Location location) {
        this.location = location;
        return this;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public Warehouse cars(Set<Car> cars) {
        this.cars = cars;
        return this;
    }

    public Warehouse addCars(Car car) {
        this.cars.add(car);
        car.setWareHouse(this);
        return this;
    }

    public Warehouse removeCars(Car car) {
        this.cars.remove(car);
        car.setWareHouse(null);
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Warehouse)) {
            return false;
        }
        return id != null && id.equals(((Warehouse) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Warehouse{" +
                "id=" + getId() +
                ", name='" + getName() + "'" +
                "}";
    }
}
