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
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
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
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    public BigDecimal calculateRefundPenalty(Reservation reservation);
	public Reservation retrieveReservationsByCustomer(String email) throws CustomerNotFoundException, ReservationNotFoundException;
	public List<Reservation> retrieveReservationsByCustomerEmail(String email);
    public OwnCustomer removeReservationByOwnCustomer(Long reservationId, OwnCustomer ownCustomer) throws ReservationNotFoundException;
    public void updateReservation(Reservation reservation) throws ReservationNotFoundException, InputDataValidationException;
	public void startReservation(Reservation reservation, Long carId) throws CarNotFoundException;
}
