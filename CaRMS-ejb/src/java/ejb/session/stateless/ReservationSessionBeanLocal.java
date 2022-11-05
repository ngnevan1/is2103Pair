/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.OwnCustomer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Local
public interface ReservationSessionBeanLocal {
    Reservation createNewReservation(Reservation newReservation, OwnCustomer customer, String licensePlate, String pickupOutletName, String returnOutletName) throws UnknownPersistenceException, CarNotFoundException, OutletNotFoundException;
    Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    BigDecimal calculateRentalRate(Car reserveCar, Date pickupDate, Date returnDate);
    BigDecimal calculateRefund(Reservation reservation);
    BigDecimal calculatePenalty(Reservation reservation);
}
