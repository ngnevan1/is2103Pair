/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import javax.ejb.Remote;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Remote
public interface RentalRateSessionBeanRemote {
    RentalRate createNewRentalRate(RentalRate newRentalRate) throws RentalRateExistException, UnknownPersistenceException;
    RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId, Boolean retrieveCarCategory) throws RentalRateNotFoundException;
}
