/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.OwnCustomer;
import javax.ejb.Remote;
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
@Remote
public interface CustomerSessionBeanRemote {
    public Customer createNewCustomer(Customer newCustomer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException;
    public Customer retrieveCustomerByCustomerEmail(String email) throws CustomerNotFoundException;
    public OwnCustomer createNewOwnCustomer(OwnCustomer newOwnCustomer) throws OwnCustomerExistException, UnknownPersistenceException, InputDataValidationException;
    public OwnCustomer retrieveOwnCustomerByUsername(String username) throws OwnCustomerNotFoundException;
    public OwnCustomer ownCustomerLogin(String username, String password) throws InvalidLoginCredentialException, OwnCustomerNotFoundException;
}
