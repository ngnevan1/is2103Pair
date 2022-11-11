/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author KMwong
 */
@Entity
public class TransitDispatchRecord implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transitDispatchRecordId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dispatchTime;
    @Column(nullable = false)
    private Boolean isCompleted;
   
    @ManyToOne
    private Employee employee;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet destinationOutlet;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet currentOutlet;


    public TransitDispatchRecord() {
        this.isCompleted = false;
    }
    
    public TransitDispatchRecord(Date dispatchTime, Boolean isCompleted) {
        this();
        this.dispatchTime = dispatchTime;
        this.isCompleted = isCompleted;
    }

    public Long getTransitDispatchRecordId() {
        return transitDispatchRecordId;
    }

    public void setTransitDispatchRecordId(Long transitDispatchRecordId) {
        this.transitDispatchRecordId = transitDispatchRecordId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transitDispatchRecordId != null ? transitDispatchRecordId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transitDispatchRecordId fields are not set
        if (!(object instanceof TransitDispatchRecord)) {
            return false;
        }
        TransitDispatchRecord other = (TransitDispatchRecord) object;
        if ((this.transitDispatchRecordId == null && other.transitDispatchRecordId != null) || (this.transitDispatchRecordId != null && !this.transitDispatchRecordId.equals(other.transitDispatchRecordId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransitDispatchRecord[ id=" + transitDispatchRecordId + " ]";
    }

    /**
     * @return the dispatchTime
     */
    public Date getDispatchTime() {
        return dispatchTime;
    }

    /**
     * @param dispatchTime the dispatchTime to set
     */
    public void setDispatchTime(Date dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    /**
     * @return the isCompleted
     */
    public Boolean getIsCompleted() {
        return isCompleted;
    }

    /**
     * @param isCompleted the isCompleted to set
     */
    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    /**
     * @return the destinationOutlet
     */
    public Outlet getDestinationOutlet() {
        return destinationOutlet;
    }

    /**
     * @param destinationOutlet the destinationOutlet to set
     */
    public void setDestinationOutlet(Outlet destinationOutlet) {
        this.destinationOutlet = destinationOutlet;
    }

    /**
     * @return the currentOutlet
     */
    public Outlet getCurrentOutlet() {
        return currentOutlet;
    }

    /**
     * @param currentOutlet the currentOutlet to set
     */
    public void setCurrentOutlet(Outlet currentOutlet) {
        this.currentOutlet = currentOutlet;
    }
    
}
