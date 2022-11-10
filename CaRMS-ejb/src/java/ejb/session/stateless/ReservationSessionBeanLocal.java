/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.OwnCustomer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarNotFoundException;
import util.exception.ReservationNotFoundException;

/**
 *
 * @author nevanng
 */
@Local
public interface ReservationSessionBeanLocal {
    Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException;
    BigDecimal calculateRefundPenalty(Reservation reservation);
    public List<Reservation> retrieveCurrentDayReservations();
    public void allocateCar(Long carId, Long reservationId) throws CarNotFoundException, ReservationNotFoundException;
    public List<Reservation> retrieveReservationsByDate(Date date);
    List<Reservation> retrieveReservationsByCustomerEmail(String email);
    OwnCustomer removeReservation(Long reservationId, OwnCustomer ownCustomer) throws ReservationNotFoundException;
}
