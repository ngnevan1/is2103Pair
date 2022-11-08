/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Outlet;
import entity.OwnCustomer;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
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
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;
    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private CarSessionBeanLocal carSessionBeanLocal;
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public ReservationSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Reservation createNewReservationByModel(Reservation newReservation, OwnCustomer customer, String carModelName, String pickupOutletName, String returnOutletName) 
            throws CarCategoryNotFoundException, CarModelNotFoundException, OutletNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(newReservation);
        if(constraintViolations.isEmpty()) {
            try {
                em.persist(newReservation);
                newReservation.setCustomer(customer);
                customer.getReservations().add(newReservation);
                       
                CarModel carModel = carModelSessionBeanLocal.retrieveCarModelByModelName(carModelName);
                carModel.getReservations().add(newReservation);
                newReservation.setCarModel(carModel);
                
                CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCategoryName(carModel.getCarCategory().getCategoryName());
                carCategory.getReservations().add(newReservation);
                newReservation.setCarCategory(carCategory);
            
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
    public Reservation createNewReservationByCategory(Reservation newReservation, OwnCustomer customer, String carCategoryName, String pickupOutletName, String returnOutletName) 
            throws CarCategoryNotFoundException, OutletNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(newReservation);
        if(constraintViolations.isEmpty()) {
            try {
                em.persist(newReservation);
                newReservation.setCustomer(customer);
                customer.getReservations().add(newReservation);
                
                CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCategoryName(carCategoryName);
                carCategory.getReservations().add(newReservation);
                newReservation.setCarCategory(carCategory);
            
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
    
    @Override
    public BigDecimal calculateRefundPenalty(Reservation reservation) {
        Date currentDate = new Date();
        Date paymentDate = reservation.getPaymentDate();
        
        Calendar startingDate = new GregorianCalendar();
        startingDate.setTime(reservation.getReservationStartDate());
        startingDate.add(Calendar.DAY_OF_MONTH, -14);                   // (20%) StartDate - 14 Days 
        Date penaltyFourteen = startingDate.getTime();                  
        startingDate.add(Calendar.DAY_OF_MONTH, 7);                     // (50%) StartDate - 14 Days + 7 Days 
        Date penaltySeven = startingDate.getTime();
        startingDate.add(Calendar.DAY_OF_MONTH, 4);                     // (70%) StartDate - 14 Days + 7 Days + 4 Days
        Date penaltyThree = startingDate.getTime();
        
        if (currentDate.after(penaltyThree)) {
            if (paymentDate.compareTo(reservation.getReservationStartDate()) < 0) {
                return new BigDecimal("0.3").multiply(reservation.getTotalAmount());
            }
            else {
                return new BigDecimal("0.7").multiply(reservation.getTotalAmount());
            }
        } 
        else if (currentDate.after(penaltySeven)) {
           if (paymentDate.compareTo(reservation.getReservationStartDate()) < 0) {
                return new BigDecimal("0.5").multiply(reservation.getTotalAmount());
            }
            else {
                return new BigDecimal("0.5").multiply(reservation.getTotalAmount());
            }
        }
        else if (currentDate.after(penaltyFourteen)) {
            if (paymentDate.compareTo(reservation.getReservationStartDate()) < 0) {
                return new BigDecimal("0.8").multiply(reservation.getTotalAmount());
            }
            else {
                return new BigDecimal("0.2").multiply(reservation.getTotalAmount());
            }
        }
        else {
            if (paymentDate.compareTo(reservation.getReservationStartDate()) < 0) {
                return reservation.getTotalAmount();
            }
            else {
                return new BigDecimal("0.0");
            }
        }

    }
    @Override
    public List<Reservation> retrieveCurrentDayReservations() {
        Date today = new Date();
        Query query = em.createQuery("SELECT r FROM Reservation r");

        List<Reservation> allReservations = query.getResultList();
        
        for (Reservation res:allReservations) {
            if(!res.getReservationStartDate().equals(today)) {
                allReservations.remove(res);
            }
        }
        return allReservations;
    }
    
    @Override
    public List<Reservation> retrieveReservationsByDate(Date date) {
        Query query = em.createQuery("SELECT r FROM Reservation r");

        List<Reservation> allReservations = query.getResultList();
        
        for (Reservation res:allReservations) {
            if(!res.getReservationStartDate().equals(date)) {
                allReservations.remove(res);
            }
        }
        return allReservations;
    }
    
    @Override
    public List<Reservation> retrieveReservationsByCustomerEmail(String email) {
        Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.customer.email = :inEmail");
        query.setParameter("inEmail", email);
        return query.getResultList();
    }
    
    @Override
    public OwnCustomer removeReservation(Long reservationId, OwnCustomer ownCustomer) throws ReservationNotFoundException {
        Reservation oldReservation = retrieveReservationByReservationId(reservationId);
        
        if (oldReservation.getCar().getCurrentReservation() != null) {
            if (oldReservation.getCar().getCurrentReservation().equals(oldReservation)) {
                oldReservation.getCar().setCurrentReservation(null);
            }
        }
        
        oldReservation.getCar().getReservations().remove(oldReservation);
        oldReservation.getCarModel().getReservations().remove(oldReservation);
        ownCustomer.getReservations().remove(oldReservation);
        em.remove(oldReservation);
        return ownCustomer;
    }
    
    @Override
    public void allocateCar(Long carId, Long reservationId) throws CarNotFoundException, ReservationNotFoundException {
        Car car = carSessionBeanLocal.retrieveCarByCarId(carId, false, true, false);
        Reservation res = retrieveReservationByReservationId(reservationId);
        
        car.getReservations().add(res);
        res.setCar(car);
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>>constraintViolations) {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
