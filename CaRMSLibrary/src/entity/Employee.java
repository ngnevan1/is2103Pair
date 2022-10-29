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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import util.enumeration.EmployeeAccessRightsEnum;
import util.enumeration.TransitStatusEnum;

/**
 *
 * @author KMwong
 */
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(nullable = false, length = 64)
    private String employeeName;
    @Column(nullable = false, length = 64, unique = true)
    private String username;
    @Column(nullable = false, length = 64)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeAccessRightsEnum employeeAccessRights;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransitStatusEnum transitStatus;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Outlet outlet;
    @OneToMany(mappedBy = "employee")
    private List<TransitDispatchRecord> transitDispatchRecords;
    @OneToOne
    private TransitDispatchRecord transitDispatchRecord;

    public Employee() {
        transitDispatchRecords = new ArrayList<>();
    }

    public Employee(String employeeName, String username, String password, EmployeeAccessRightsEnum employeeAccessRights, TransitStatusEnum transitStatus, Outlet outlet) {
        this();
        this.employeeName = employeeName;
        this.username = username;
        this.password = password;
        this.employeeAccessRights = employeeAccessRights;
        this.transitStatus = transitStatus;
        this.outlet = outlet;
        this.transitDispatchRecords = new ArrayList<>();
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeId != null ? employeeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the employeeId fields are not set
        if (!(object instanceof Employee)) {
            return false;
        }
        Employee other = (Employee) object;
        if ((this.employeeId == null && other.employeeId != null) || (this.employeeId != null && !this.employeeId.equals(other.employeeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Employee[ id=" + employeeId + " ]";
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the employeeAccessRights
     */
    public EmployeeAccessRightsEnum getEmployeeAccessRights() {
        return employeeAccessRights;
    }

    /**
     * @param employeeAccessRights the employeeAccessRights to set
     */
    public void setEmployeeAccessRights(EmployeeAccessRightsEnum employeeAccessRights) {
        this.employeeAccessRights = employeeAccessRights;
    }

    /**
     * @return the transitStatus
     */
    public TransitStatusEnum getTransitStatus() {
        return transitStatus;
    }

    /**
     * @param transitStatus the transitStatus to set
     */
    public void setTransitStatus(TransitStatusEnum transitStatus) {
        this.transitStatus = transitStatus;
    }

    /**
     * @return the outlet
     */
    public Outlet getOutlet() {
        return outlet;
    }

    /**
     * @param outlet the outlet to set
     */
    public void setOutlet(Outlet outlet) {
        this.outlet = outlet;
    }

    /**
     * @return the transitDispatchRecords
     */
    public List<TransitDispatchRecord> getTransitDispatchRecords() {
        return transitDispatchRecords;
    }

    /**
     * @param transitDispatchRecords the transitDispatchRecords to set
     */
    public void setTransitDispatchRecords(List<TransitDispatchRecord> transitDispatchRecords) {
        this.transitDispatchRecords = transitDispatchRecords;
    }

    /**
     * @return the transitDispatchRecord
     */
    public TransitDispatchRecord getTransitDispatchRecord() {
        return transitDispatchRecord;
    }

    /**
     * @param transitDispatchRecord the transitDispatchRecord to set
     */
    public void setTransitDispatchRecord(TransitDispatchRecord transitDispatchRecord) {
        this.transitDispatchRecord = transitDispatchRecord;
    }
    
}
