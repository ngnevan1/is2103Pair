/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import util.enumeration.RentalRateEnum;

/**
 *
 * @author KMwong
 */
@Entity
public class RentalRate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentalRateId;
    @Column(nullable = false, length = 32, unique = true)
    @NotNull
    @Size(min = 1, max = 32)
    private String rateName;
    @Column(nullable = false, precision = 8, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 6, fraction = 2)
    private BigDecimal ratePerDay;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private RentalRateEnum rateType;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date rateStartDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Future
    private Date rateEndDate;
    private Boolean isDisabled;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;

    public RentalRate() {
        this.isDisabled = false;
    }

    public RentalRate(String rateName, RentalRateEnum rateType, CarCategory category, BigDecimal ratePerDay, Date rateStartDate, Date rateEndDate) {
        this();
        this.rateName = rateName;
        this.rateType = rateType;
        this.carCategory = category;
        this.ratePerDay = ratePerDay;
        this.rateStartDate = rateStartDate;
        this.rateEndDate = rateEndDate;
    }

    public RentalRateEnum getRateType() {
        return rateType;
    }

    public void setRateType(RentalRateEnum rateType) {
        this.rateType = rateType;
    }
    
    public Long getRentalRateId() {
        return rentalRateId;
    }

    public void setRentalRateId(Long rentalRateId) {
        this.rentalRateId = rentalRateId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rentalRateId != null ? rentalRateId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the rentalRateId fields are not set
        if (!(object instanceof RentalRate)) {
            return false;
        }
        RentalRate other = (RentalRate) object;
        if ((this.rentalRateId == null && other.rentalRateId != null) || (this.rentalRateId != null && !this.rentalRateId.equals(other.rentalRateId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RentalRate[ id=" + rentalRateId + " ]";
    }

    /**
     * @return the rateName
     */
    public String getRateName() {
        return rateName;
    }

    /**
     * @param rateName the rateName to set
     */
    public void setRateName(String rateName) {
        this.rateName = rateName;
    }

    /**
     * @return the ratePerDay
     */
    public BigDecimal getRatePerDay() {
        return ratePerDay;
    }

    /**
     * @param ratePerDay the ratePerDay to set
     */
    public void setRatePerDay(BigDecimal ratePerDay) {
        this.ratePerDay = ratePerDay;
    }

    /**
     * @return the rateStartDate
     */
    public Date getRateStartDate() {
        return rateStartDate;
    }

    /**
     * @param rateStartDate the rateStartDate to set
     */
    public void setRateStartDate(Date rateStartDate) {
        this.rateStartDate = rateStartDate;
    }

    /**
     * @return the rateEndDate
     */
    public Date getRateEndDate() {
        return rateEndDate;
    }

    /**
     * @param rateEndDate the rateEndDate to set
     */
    public void setRateEndDate(Date rateEndDate) {
        this.rateEndDate = rateEndDate;
    }

    /**
     * @return the isDisabled
     */
    public Boolean getIsDisabled() {
        return isDisabled;
    }

    /**
     * @param isDisabled the isDisabled to set
     */
    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    /**
     * @return the carCategory
     */
    public CarCategory getCarCategory() {
        return carCategory;
    }

    /**
     * @param carCategory the carCategory to set
     */
    public void setCarCategory(CarCategory carCategory) {
        this.carCategory = carCategory;
    }

}