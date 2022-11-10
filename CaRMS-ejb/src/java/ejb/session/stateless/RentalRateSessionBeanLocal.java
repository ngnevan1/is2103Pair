/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotAvailableException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Local
public interface RentalRateSessionBeanLocal {
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;
    public RentalRate createNewRentalRate(RentalRate newRentalRate) throws RentalRateExistException, UnknownPersistenceException, InputDataValidationException;
    public RentalRate retrieveRentalRateByRateName(String rateName) throws RentalRateNotFoundException;
    List<RentalRate> retrieveRentalRateByCarCategory(CarCategory carCategory);
    List<RentalRate> calculateRentalRate(List<RentalRate> rates, Date pickupDate, Date returnDate) throws RentalRateNotAvailableException;
}
