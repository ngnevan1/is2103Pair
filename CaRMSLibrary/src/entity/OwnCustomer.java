/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author KMwong
 */
@Entity
public class OwnCustomer extends Customer implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(nullable = false, length = 64, unique = true)
    @NotNull
    @Size(min = 1, max = 64)
    private String username;
    @Column(nullable = false, length = 64)
    @NotNull
    @Size(min = 1, max = 64)
    private String password;

    
    public OwnCustomer() {
        super();
    }

    public OwnCustomer(String name, String passportNumber, String email, String phoneNumber, String username, String password) {
        super(name, passportNumber, email, phoneNumber);
        this.username = username;
        this.password = password;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (customerId != null ? customerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the customerId fields are not set
        if (!(object instanceof OwnCustomer)) {
            return false;
        }
        OwnCustomer other = (OwnCustomer) object;
        if ((this.customerId == null && other.customerId != null) || (this.customerId != null && !this.customerId.equals(other.customerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.OwnCustomer[ id=" + customerId + " ]";
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
    
}
