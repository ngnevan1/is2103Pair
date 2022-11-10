/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.OwnCustomer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryNotFoundException;
// import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.OwnCustomerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Remote
public interface ReservationSessionBeanRemote {
    public Reservation createNewReservationByCategory(Reservation newReservation, OwnCustomer customer, String carCategoryName, String pickupOutletName, String returnOutletName)
            throws CarCategoryNotFoundException, OutletNotFoundException, OwnCustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    // public Reservation createNewReservationByModel(Reservation newReservation, OwnCustomer customer, String carModelName, String pickupOutletName, String returnOutletName)
            // throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, OwnCustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    BigDecimal calculateRefundPenalty(Reservation reservation);
    List<Reservation> retrieveReservationsByCustomerEmail(String email);
    OwnCustomer removeReservationByOwnCustomer(Long reservationId, OwnCustomer ownCustomer) throws ReservationNotFoundException;
    public Reservation retrieveReservationsByCustomer(Customer customer) throws ReservationNotFoundException;
    public void updateReservation(Reservation reservation) throws ReservationNotFoundException, InputDataValidationException;
}
