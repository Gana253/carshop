package com.java.car.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * A Vehicles.
 */
@Entity
@Table(name = "vehicles")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vehicles implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "make")
    private String make;

    @Column(name = "model")
    private String model;

    @Column(name = "yearModel")
    private long yearModel;

    @Column(name = "price")
    private Long price;

    @Column(name = "licensed")
    private String licensed;

    @Column(name = "dateAdded")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date dateAdded;
    @ManyToOne
    //@JsonIgnoreProperties(value = "vehicles", allowSetters = true)
    @JsonBackReference
    private Car car;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public Vehicles make(String make) {
        this.make = make;
        return this;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Vehicles model(String model) {
        this.model = model;
        return this;
    }

    public long getYearModel() {
        return yearModel;
    }

    public void setYearModel(long yearModel) {
        this.yearModel = yearModel;
    }

    public Vehicles yearModel(long yearModel) {
        this.yearModel = yearModel;
        return this;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Vehicles price(Long price) {
        this.price = price;
        return this;
    }

    public String getLicensed() {
        return licensed;
    }

    public void setLicensed(String licensed) {
        this.licensed = licensed;
    }

    public Vehicles licensed(String licensed) {
        this.licensed = licensed;
        return this;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Vehicles dateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
        return this;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Vehicles vehicles(Car car) {
        this.car = car;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vehicles)) {
            return false;
        }
        return id != null && id.equals(((Vehicles) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vehicles{" +
                "id=" + getId() +
                ", make='" + getMake() + "'" +
                ", model='" + getModel() + "'" +
                ", yearModel='" + getYearModel() + "'" +
                ", price=" + getPrice() +
                ", licensed='" + getLicensed() + "'" +
                ", dateAdded='" + getDateAdded() + "'" +
                "}";
    }
}
