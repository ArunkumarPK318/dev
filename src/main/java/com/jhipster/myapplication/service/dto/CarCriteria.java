package com.jhipster.myapplication.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.jhipster.myapplication.domain.Car} entity. This class is used
 * in {@link com.jhipster.myapplication.web.rest.CarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter carName;

    private StringFilter carModal;

    private StringFilter brandName;

    private StringFilter enginType;

    private IntegerFilter engineNo;

    private ZonedDateTimeFilter manufactureDate;

    private LongFilter employeeId;

    public CarCriteria(){
    }

    public CarCriteria(CarCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.carName = other.carName == null ? null : other.carName.copy();
        this.carModal = other.carModal == null ? null : other.carModal.copy();
        this.brandName = other.brandName == null ? null : other.brandName.copy();
        this.enginType = other.enginType == null ? null : other.enginType.copy();
        this.engineNo = other.engineNo == null ? null : other.engineNo.copy();
        this.manufactureDate = other.manufactureDate == null ? null : other.manufactureDate.copy();
        this.employeeId = other.employeeId == null ? null : other.employeeId.copy();
    }

    @Override
    public CarCriteria copy() {
        return new CarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCarName() {
        return carName;
    }

    public void setCarName(StringFilter carName) {
        this.carName = carName;
    }

    public StringFilter getCarModal() {
        return carModal;
    }

    public void setCarModal(StringFilter carModal) {
        this.carModal = carModal;
    }

    public StringFilter getBrandName() {
        return brandName;
    }

    public void setBrandName(StringFilter brandName) {
        this.brandName = brandName;
    }

    public StringFilter getEnginType() {
        return enginType;
    }

    public void setEnginType(StringFilter enginType) {
        this.enginType = enginType;
    }

    public IntegerFilter getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(IntegerFilter engineNo) {
        this.engineNo = engineNo;
    }

    public ZonedDateTimeFilter getManufactureDate() {
        return manufactureDate;
    }

    public void setManufactureDate(ZonedDateTimeFilter manufactureDate) {
        this.manufactureDate = manufactureDate;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CarCriteria that = (CarCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(carName, that.carName) &&
            Objects.equals(carModal, that.carModal) &&
            Objects.equals(brandName, that.brandName) &&
            Objects.equals(enginType, that.enginType) &&
            Objects.equals(engineNo, that.engineNo) &&
            Objects.equals(manufactureDate, that.manufactureDate) &&
            Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        carName,
        carModal,
        brandName,
        enginType,
        engineNo,
        manufactureDate,
        employeeId
        );
    }

    @Override
    public String toString() {
        return "CarCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (carName != null ? "carName=" + carName + ", " : "") +
                (carModal != null ? "carModal=" + carModal + ", " : "") +
                (brandName != null ? "brandName=" + brandName + ", " : "") +
                (enginType != null ? "enginType=" + enginType + ", " : "") +
                (engineNo != null ? "engineNo=" + engineNo + ", " : "") +
                (manufactureDate != null ? "manufactureDate=" + manufactureDate + ", " : "") +
                (employeeId != null ? "employeeId=" + employeeId + ", " : "") +
            "}";
    }

}
