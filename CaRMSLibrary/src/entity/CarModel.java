/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author KMwong
 */
@Entity
public class CarModel implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carModelId;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String modelName;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String makeName;
    @Column(nullable = false)
    private Boolean isDisabled;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;
    @OneToMany(mappedBy = "carModel")
    private List<Reservation> reservations;
    @OneToMany(mappedBy = "carModel")
    private List<Car> cars;
    
    public CarModel() {
        reservations = new ArrayList<>();
        cars = new ArrayList<>();
        isDisabled = false;
    }

    public CarModel(String makeName, String modelName, CarCategory category) {
        this();
        this.makeName = makeName;
        this.modelName = modelName;
        this.carCategory = category;
    }

    public Long getCarModelId() {
        return carModelId;
    }

    public void setCarModelId(Long carModelId) {
        this.carModelId = carModelId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carModelId != null ? carModelId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carModelId fields are not set
        if (!(object instanceof CarModel)) {
            return false;
        }
        CarModel other = (CarModel) object;
        if ((this.carModelId == null && other.carModelId != null) || (this.carModelId != null && !this.carModelId.equals(other.carModelId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarModel[ id=" + carModelId + " ]";
    }

    /**
     * @return the modelName
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * @param modelName the modelName to set
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * @return the makeName
     */
    public String getMakeName() {
        return makeName;
    }

    /**
     * @param makeName the makeName to set
     */
    public void setMakeName(String makeName) {
        this.makeName = makeName;
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

    /**
     * @return the reservations
     */
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @param reservations the reservations to set
     */
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * @return the cars
     */
    public List<Car> getCars() {
        return cars;
    }

    /**
     * @param cars the cars to set
     */
    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
    
}
