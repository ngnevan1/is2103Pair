/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.OwnCustomer;
import javax.ejb.Local;
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
@Local
public interface CustomerSessionBeanLocal {
    Customer createNewCustomer(Customer newCustomer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException;
    Customer retrieveCustomerByCustomerEmail(String email) throws CustomerNotFoundException;
    OwnCustomer createNewOwnCustomer(OwnCustomer newOwnCustomer) throws OwnCustomerExistException, UnknownPersistenceException, InputDataValidationException;
    OwnCustomer retrieveOwnCustomerByUsername(String username) throws OwnCustomerNotFoundException;
    OwnCustomer ownCustomerLogin(String username, String password) throws InvalidLoginCredentialException, OwnCustomerNotFoundException;
}
