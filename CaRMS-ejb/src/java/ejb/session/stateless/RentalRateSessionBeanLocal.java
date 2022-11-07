/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.RentalRateNotFoundException;

/**
 *
 * @author KMwong
 */
@Local
public interface RentalRateSessionBeanLocal {
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;
    BigDecimal calculateRentalRate(List<RentalRate> rates, Date pickupDate, Date returnDate);
}
