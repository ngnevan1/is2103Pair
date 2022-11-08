/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OwnCustomer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Remote
public interface ReservationSessionBeanRemote {
    Reservation createNewReservationByModel(Reservation newReservation, OwnCustomer customer, String carModelName, String pickupOutletName, String returnOutletName) 
            throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, UnknownPersistenceException, InputDataValidationException;
    Reservation createNewReservationByCategory(Reservation newReservation, OwnCustomer customer, String carCategoryName, String pickupOutletName, String returnOutletName) 
            throws CarCategoryNotFoundException, OutletNotFoundException, UnknownPersistenceException, InputDataValidationException;
    Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    BigDecimal calculateRefundPenalty(Reservation reservation);
    List<Reservation> retrieveReservationsByCustomerEmail(String email);
    OwnCustomer removeReservation(Long reservationId, OwnCustomer ownCustomer) throws ReservationNotFoundException;
}
