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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author KMwong
 */
@Entity
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reservationId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date reservationStartDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date reservationEndDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date paymentDate;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal totalAmount;
    @Column(nullable = false, length = 64)
    private String CreditCardNumber;
    @Column(nullable = false)
    private Boolean isPaid;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Car car;
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarModel carModel;
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private CarCategory carCategory;
    @ManyToOne(optional = false)
    private Partner partner;
    @ManyToOne(optional = false)
    private Customer customer;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet pickUpOutlet;
    @OneToOne(optional = false)
    private Outlet returnOutlet;
    

    
    public Reservation() {
    }

    public Reservation(Date reservationStartDate, Date reservationEndDate, Date paymentDate, BigDecimal totalAmount, String CreditCardNumber, Boolean isPaid) {
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.paymentDate = paymentDate;
        this.totalAmount = totalAmount;
        this.CreditCardNumber = CreditCardNumber;
        this.isPaid = isPaid;
    }
    
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reservationId != null ? reservationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the reservationId fields are not set
        if (!(object instanceof Reservation)) {
            return false;
        }
        Reservation other = (Reservation) object;
        if ((this.reservationId == null && other.reservationId != null) || (this.reservationId != null && !this.reservationId.equals(other.reservationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reservation[ id=" + reservationId + " ]";
    }

    /**
     * @return the reservationStartDate
     */
    public Date getReservationStartDate() {
        return reservationStartDate;
    }

    /**
     * @param reservationStartDate the reservationStartDate to set
     */
    public void setReservationStartDate(Date reservationStartDate) {
        this.reservationStartDate = reservationStartDate;
    }

    /**
     * @return the reservationEndDate
     */
    public Date getReservationEndDate() {
        return reservationEndDate;
    }

    /**
     * @param reservationEndDate the reservationEndDate to set
     */
    public void setReservationEndDate(Date reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    /**
     * @return the paymentDate
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the totalAmount
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount the totalAmount to set
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * @return the CreditCardNumber
     */
    public String getCreditCardNumber() {
        return CreditCardNumber;
    }

    /**
     * @param CreditCardNumber the CreditCardNumber to set
     */
    public void setCreditCardNumber(String CreditCardNumber) {
        this.CreditCardNumber = CreditCardNumber;
    }

    /**
     * @return the isPaid
     */
    public Boolean getIsPaid() {
        return isPaid;
    }

    /**
     * @param isPaid the isPaid to set
     */
    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    /**
     * @return the car
     */
    public Car getCar() {
        return car;
    }

    /**
     * @param car the car to set
     */
    public void setCar(Car car) {
        this.car = car;
    }

    /**
     * @return the carModel
     */
    public CarModel getCarModel() {
        return carModel;
    }

    /**
     * @param carModel the carModel to set
     */
    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
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
     * @return the partner
     */
    public Partner getPartner() {
        return partner;
    }

    /**
     * @param partner the partner to set
     */
    public void setPartner(Partner partner) {
        this.partner = partner;
    }

    /**
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the pickUpOutlet
     */
    public Outlet getPickUpOutlet() {
        return pickUpOutlet;
    }

    /**
     * @param pickUpOutlet the pickUpOutlet to set
     */
    public void setPickUpOutlet(Outlet pickUpOutlet) {
        this.pickUpOutlet = pickUpOutlet;
    }

    /**
     * @return the returnOutlet
     */
    public Outlet getReturnOutlet() {
        return returnOutlet;
    }

    /**
     * @param returnOutlet the returnOutlet to set
     */
    public void setReturnOutlet(Outlet returnOutlet) {
        this.returnOutlet = returnOutlet;
    }
    
}
