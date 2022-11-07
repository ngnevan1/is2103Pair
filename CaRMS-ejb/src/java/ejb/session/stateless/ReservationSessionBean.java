/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.OwnCustomer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CarNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public ReservationSessionBean() {
    }

    @Override
    public Reservation createNewReservation(Reservation newReservation, OwnCustomer customer, String licensePlate, String pickupOutletName, String returnOutletName) throws UnknownPersistenceException, CarNotFoundException, OutletNotFoundException {
        try {
            em.persist(newReservation);
            newReservation.setCustomer(customer);
            customer.getReservations().add(newReservation);
            
            Car car = carSessionBeanLocal.retrieveCarByLicensePlate(licensePlate);
            newReservation.setCar(car);
            car.getReservations().add(newReservation);
            
            Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(pickupOutletName);
            pickupOutlet.getReservations().add(newReservation);
            newReservation.setPickUpOutlet(pickupOutlet);
            
            Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(returnOutletName);
            newReservation.setReturnOutlet(returnOutlet);
            
            em.flush();
            em.refresh(newReservation);
            return newReservation;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                throw new UnknownPersistenceException(ex.getMessage());
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {
        Reservation reservation = em.find(Reservation.class, reservationId);
        
        if(reservation != null) {
            return reservation;
        }
        else {
            throw new ReservationNotFoundException("Reservation ID " + reservationId + "does not exist!");
        }
    }
    
    public BigDecimal calculateRentalRate(Car reserveCar, Date pickupDate, Date returnDate) {
        // stub
        return null;
    }
    
    public BigDecimal calculateRefund(Reservation reservation) {
        // stub
        return null;
    }
    
    public BigDecimal calculatePenalty(Reservation reservation) {
        // stub
        return null;
    }
}
