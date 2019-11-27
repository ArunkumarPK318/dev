package com.jhipster.myapplication.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "car_name")
    private String carName;

    @Column(name = "car_modal")
    private String carModal;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "engin_type")
    private String enginType;

    @Column(name = "engine_no")
    private Integer engineNo;

    @Column(name = "manufacture_date")
    private ZonedDateTime manufactureDate;

    @ManyToOne
    @JsonIgnoreProperties("cars")
    private Employee employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCarName() {
        return carName;
    }

    public Car carName(String carName) {
        this.carName = carName;
        return this;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarModal() {
        return carModal;
    }

    public Car carModal(String carModal) {
        this.carModal = carModal;
        return this;
    }

    public void setCarModal(String carModal) {
        this.carModal = carModal;
    }

    public String getBrandName() {
        return brandName;
    }

    public Car brandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getEnginType() {
        return enginType;
    }

    public Car enginType(String enginType) {
        this.enginType = enginType;
        return this;
    }

    public void setEnginType(String enginType) {
        this.enginType = enginType;
    }

    public Integer getEngineNo() {
        return engineNo;
    }

    public Car engineNo(Integer engineNo) {
        this.engineNo = engineNo;
        return this;
    }

    public void setEngineNo(Integer engineNo) {
        this.engineNo = engineNo;
    }

    public ZonedDateTime getManufactureDate() {
        return manufactureDate;
    }

    public Car manufactureDate(ZonedDateTime manufactureDate) {
        this.manufactureDate = manufactureDate;
        return this;
    }

    public void setManufactureDate(ZonedDateTime manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Car employee(Employee employee) {
        this.employee = employee;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

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

    @Override
    public String toString() {
        return "Car{" +
            "id=" + getId() +
            ", carName='" + getCarName() + "'" +
            ", carModal='" + getCarModal() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", enginType='" + getEnginType() + "'" +
            ", engineNo=" + getEngineNo() +
            ", manufactureDate='" + getManufactureDate() + "'" +
            "}";
    }
}
