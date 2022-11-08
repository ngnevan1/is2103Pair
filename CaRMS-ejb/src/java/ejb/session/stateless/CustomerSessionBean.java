/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.OwnCustomer;
import entity.Reservation;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
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
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public CustomerSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Customer createNewCustomer(Customer newCustomer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newCustomer);
        if(constraintViolations.isEmpty()) {
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
        else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
     
    }
    
    @Override
    public Customer retrieveCustomerByCustomerEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM Customer c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);
        
        try {
            Customer customer = (Customer) query.getSingleResult();
            if (customer != null) {
                customer.getReservations().size();
                customer.getPartner();
            }
            return customer;
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Email " + email + " does not exist!");
        }

    }
    
    @Override
    public OwnCustomer createNewOwnCustomer(OwnCustomer newOwnCustomer) throws OwnCustomerExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Customer>>constraintViolations = validator.validate(newOwnCustomer);
        if(constraintViolations.isEmpty()) {
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
        else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
     
    }
    
    @Override
    public OwnCustomer retrieveOwnCustomerByUsername(String username) throws OwnCustomerNotFoundException {
        Query query = em.createQuery("SELECT oc FROM OwnCustomer oc WHERE oc.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try {
            OwnCustomer ownCustomer = (OwnCustomer) query.getSingleResult();
            if (ownCustomer != null) {
                ownCustomer.getReservations().size();
            }
            return ownCustomer;
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new OwnCustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }
    
    @Override
    public OwnCustomer ownCustomerLogin(String username, String password) throws InvalidLoginCredentialException, OwnCustomerNotFoundException {
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
        catch(OwnCustomerNotFoundException ex) {
            throw new OwnCustomerNotFoundException("Customer does not exist or invalid password!");
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>>constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
