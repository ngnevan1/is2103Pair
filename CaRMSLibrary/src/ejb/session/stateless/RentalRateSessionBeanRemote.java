/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import java.util.List;
import javax.ejb.Remote;
import util.exception.InputDataValidationException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author KMwong
 */
@Remote
public interface RentalRateSessionBeanRemote {

    public RentalRate createNewRentalRate(RentalRate newRentalRate) throws RentalRateExistException, UnknownPersistenceException, InputDataValidationException;

    List<RentalRate> retrieveAllRentalRates();

    RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId, Boolean retrieveCarCategory) throws RentalRateNotFoundException;

    RentalRate retrieveRentalRateByRateName(String rateName) throws RentalRateNotFoundException;

    public void updateRentalRate(RentalRate rate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;
}
