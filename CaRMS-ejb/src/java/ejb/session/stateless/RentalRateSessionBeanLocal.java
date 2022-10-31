/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import javax.ejb.Local;
import util.exception.RentalRateNotFoundException;

/**
 *
 * @author KMwong
 */
@Local
public interface RentalRateSessionBeanLocal {

    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException;
}
