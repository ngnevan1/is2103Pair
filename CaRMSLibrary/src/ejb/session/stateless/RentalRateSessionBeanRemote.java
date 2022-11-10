/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.CarModel;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.Date;
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

    public List<RentalRate> retrieveAllRentalRates();

    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId, Boolean retrieveCarCategory) throws RentalRateNotFoundException;

    public RentalRate retrieveRentalRateByRateName(String rateName) throws RentalRateNotFoundException;

    public void updateRentalRate(RentalRate rate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException;
    
    public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException;
    BigDecimal calculateRentalRate(List<RentalRate> rates, Date pickupDate, Date returnDate);
	
		public List<RentalRate> retrieveRentalRateByCarCategory(CarCategory carCategory);
}
