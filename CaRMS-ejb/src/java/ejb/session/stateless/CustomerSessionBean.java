/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.OwnCustomer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OwnCustomerExistException;
import util.exception.OwnCustomerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Stateless
public class CustomerSessionBean implements CustomerSessionBeanRemote, CustomerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public CustomerSessionBean() {
    }
    
    @Override
    public Customer createNewCustomer(Customer newCustomer) throws CustomerExistException, UnknownPersistenceException {
        try {
            em.persist(newCustomer);
            em.flush();
            em.refresh(newCustomer);
            return newCustomer;
        }
        catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CustomerExistException("Customer already exists!");
                }
                else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId, Boolean retrieveReservation, Boolean retrievePartner) throws CustomerNotFoundException {
        Customer customer = em.find(Customer.class, customerId);
        
        if(customer != null) {
            if (retrieveReservation) {
                customer.getReservations().size();
            }
            if (retrievePartner) {
                customer.getPartner();
            }
            return customer;
        }
        else {
            throw new CustomerNotFoundException("Customer ID " + customerId + "does not exist!");
        }
    }
    
    @Override
    public OwnCustomer createNewOwnCustomer(OwnCustomer newOwnCustomer) throws OwnCustomerExistException, UnknownPersistenceException {
        try {
            em.persist(newOwnCustomer);
            em.flush();
            em.refresh(newOwnCustomer);
            return newOwnCustomer;
        }
        catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new OwnCustomerExistException("Customer already exists!");
                }
                else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public OwnCustomer retrieveOwnCustomerByUsername(String username) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT oc FROM OwnCustomer oc WHERE oc.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            return (OwnCustomer)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }
    
    public OwnCustomer ownCustomerLogin(String username, String password) throws InvalidLoginCredentialException, CustomerNotFoundException {
        try {
            OwnCustomer ownCustomer = retrieveOwnCustomerByUsername(username);
            
            if(ownCustomer.getPassword().equals(password)) {
                ownCustomer.getReservations().size();                
                return ownCustomer;
            }
            else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        }
        catch(CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer does not exist or invalid password!");
        }
    }
}
