/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.OwnCustomer;
import entity.Partner;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.OwnCustomerNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Local
public interface ReservationSessionBeanLocal {
    Reservation createNewReservationByCategory(Reservation newReservation, OwnCustomer customer, String carCategoryName, String pickupOutletName, String returnOutletName) 
            throws CarCategoryNotFoundException, OutletNotFoundException, OwnCustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    Reservation createNewReservationByCategory(Reservation newReservation, Customer customer, Partner partner, String carCategoryName, String pickupOutletName, String returnOutletName)
            throws CarCategoryNotFoundException, OutletNotFoundException, CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException;
    Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    public List<Reservation> retrieveCurrentDayReservations();
    public void allocateCar(Long carId, Long reservationId) throws CarNotFoundException, CarModelNotFoundException, ReservationNotFoundException;
    public List<Reservation> retrieveReservationsByDate(Date date);
    List<Reservation> retrieveReservationsByCustomerEmail(String email);
    List<Reservation> retrieveReservationsByPartnerUsername(String username);
    OwnCustomer removeReservationByOwnCustomer(Long reservationId, OwnCustomer ownCustomer) throws ReservationNotFoundException;
    Partner removeReservationByPartner(Long reservationId, Partner partner) throws ReservationNotFoundException, PartnerNotFoundException;
    BigDecimal calculateRefundPenalty(Reservation reservation);
}
