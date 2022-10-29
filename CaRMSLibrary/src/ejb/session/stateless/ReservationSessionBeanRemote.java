/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Reservation;
import javax.ejb.Remote;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Remote
public interface ReservationSessionBeanRemote {
    Reservation createNewReservation(Reservation newReservation) throws UnknownPersistenceException;
    Reservation retrieveRentalRateByRentalRateId(Long reservationId) throws ReservationNotFoundException;
}
