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
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Remote
public interface CustomerSessionBeanRemote {
    Customer createNewCustomer(Customer newCustomer) throws CustomerExistException, UnknownPersistenceException;
    Customer retrieveCustomerByCustomerId(Long customerId, Boolean retrieveReservation, Boolean retrievePartner) throws CustomerNotFoundException;
    OwnCustomer retrieveOwnCustomerByUsername(String username) throws CustomerNotFoundException;
}
