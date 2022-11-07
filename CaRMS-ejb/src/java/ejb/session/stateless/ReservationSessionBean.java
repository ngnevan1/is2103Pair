/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import entity.Outlet;
import entity.OwnCustomer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
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
    private CarModelSessionBeanLocal carModelSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Reservation createNewReservation(Reservation newReservation, OwnCustomer customer, String carModelName, String pickupOutletName, String returnOutletName) 
            throws UnknownPersistenceException, CarModelNotFoundException, OutletNotFoundException, InputDataValidationException {
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(newReservation);
        if(constraintViolations.isEmpty()) {
            try {
                em.persist(newReservation);
                newReservation.setCustomer(customer);
                customer.getReservations().add(newReservation);
                       
                CarModel carModel = carModelSessionBeanLocal.retrieveCarModelByModelName(carModelName);
                carModel.getReservations().add(newReservation);
                newReservation.setCarModel(carModel);
            
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
        else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
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
    
    public BigDecimal calculateRefund(Reservation reservation) {
        // WIP
        return null;
    }
    
    public BigDecimal calculatePenalty(Reservation reservation) {
        // WIP
        return null;
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>>constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    

}
