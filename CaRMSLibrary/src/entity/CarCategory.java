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
import javax.persistence.OneToMany;

/**
 *
 * @author KMwong
 */
@Entity
public class CarCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carCategoryId;
    @Column(nullable = false, length = 64, unique = true)
    private String categoryName;
    
    @OneToMany(mappedBy = "carCategory")
    private List<CarModel> carModels;
    @OneToMany(mappedBy = "carCategory")
    private List<RentalRate> rentalRates;
    @OneToMany(mappedBy = "carCategory")
    private List<Reservation> reservations;

    
    public CarCategory() {
        carModels = new ArrayList<>();
        rentalRates = new ArrayList<>();
        reservations = new ArrayList<>();
    }

    public CarCategory(String categoryName) {
        this();
        this.categoryName = categoryName;
    }
    
    public Long getCarCategoryId() {
        return carCategoryId;
    }

    public void setCarCategoryId(Long carCategoryId) {
        this.carCategoryId = carCategoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (carCategoryId != null ? carCategoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the carCategoryId fields are not set
        if (!(object instanceof CarCategory)) {
            return false;
        }
        CarCategory other = (CarCategory) object;
        if ((this.carCategoryId == null && other.carCategoryId != null) || (this.carCategoryId != null && !this.carCategoryId.equals(other.carCategoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CarCategory[ id=" + carCategoryId + " ]";
    }

    /**
     * @return the categoryName
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName the categoryName to set
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return the carModels
     */
    public List<CarModel> getCarModels() {
        return carModels;
    }

    /**
     * @param carModels the carModels to set
     */
    public void setCarModels(List<CarModel> carModels) {
        this.carModels = carModels;
    }

    /**
     * @return the rentalRates
     */
    public List<RentalRate> getRentalRates() {
        return rentalRates;
    }

    /**
     * @param rentalRates the rentalRates to set
     */
    public void setRentalRates(List<RentalRate> rentalRates) {
        this.rentalRates = rentalRates;
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
    
}
