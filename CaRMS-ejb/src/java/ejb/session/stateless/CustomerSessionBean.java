/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
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
}
