/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Customer;
import entity.Outlet;
import entity.OwnCustomer;
import entity.Partner;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import util.enumeration.CarStatusEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
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
@Stateless
public class ReservationSessionBean implements ReservationSessionBeanRemote, ReservationSessionBeanLocal {

	@PersistenceContext(unitName = "CaRMS-ejbPU")
	private EntityManager em;

	@EJB
	private CustomerSessionBeanLocal customerSessionBeanLocal;
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
	public Reservation createNewReservationByCategory(Reservation newReservation, OwnCustomer customer, String carCategoryName, String pickupOutletName, String returnOutletName)
			throws CarCategoryNotFoundException, OutletNotFoundException, OwnCustomerNotFoundException, UnknownPersistenceException, InputDataValidationException {
		Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(newReservation);
		if (constraintViolations.isEmpty()) {
			try {
				// retrieve attributes
				Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(pickupOutletName);
				Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(returnOutletName);
				CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCategoryName(carCategoryName);
				OwnCustomer managedCustomer = customerSessionBeanLocal.retrieveOwnCustomerByUsername(customer.getUsername());

				// Set Reservation Relationships
				newReservation.setPickUpOutlet(pickupOutlet);
				newReservation.setReturnOutlet(returnOutlet);
				newReservation.setCarCategory(carCategory);
				newReservation.setCustomer(customer);

				// Persist before setting other entity relationships
				em.persist(newReservation);
				pickupOutlet.getReservations().add(newReservation);
				managedCustomer.getReservations().add(newReservation);

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
		} else {
			throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
		}
	}

	@Override
	public Reservation createNewReservationByCategory(Reservation newReservation, Customer customer, Partner partner, String carCategoryName, String pickupOutletName, String returnOutletName)
			throws CarCategoryNotFoundException, OutletNotFoundException, CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException {
		Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(newReservation);
		if (constraintViolations.isEmpty()) {
			try {
				// retrieve attributes
				Outlet pickupOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(pickupOutletName);
				Outlet returnOutlet = outletSessionBeanLocal.retrieveOutletByOutletName(returnOutletName);
				CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCategoryName(carCategoryName);
				Customer managedCustomer = customerSessionBeanLocal.retrieveCustomerByCustomerEmail(customer.getEmail());

				// Set Reservation Relationships
				newReservation.setPickUpOutlet(pickupOutlet);
				newReservation.setReturnOutlet(returnOutlet);
				newReservation.setCarCategory(carCategory);
				newReservation.setCustomer(customer);
				newReservation.setPartner(partner);

				// Persist before setting other entity relationships
				em.persist(newReservation);
				pickupOutlet.getReservations().add(newReservation);
				managedCustomer.getReservations().add(newReservation);

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
		} else {
			throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
		}
	}

	@Override
	public Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException {
		Reservation reservation = em.find(Reservation.class, reservationId);

		if (reservation != null) {
			return reservation;
		} else {
			throw new ReservationNotFoundException("Reservation ID " + reservationId + "does not exist!");
		}
	}

	@Override
	public List<Reservation> retrieveCurrentDayReservations() {
		int today = new Date().getDate();
		Query query = em.createQuery("SELECT r FROM Reservation r");

		List<Reservation> allReservations = query.getResultList();
		List<Reservation> todayReservations = new ArrayList<>();
		allReservations.forEach(res -> {
			int resDate = res.getReservationStartDate().getDate();
			if (resDate == today) {
				todayReservations.add(res);
			}
		});
		return todayReservations;
	}

	@Override
	public List<Reservation> retrieveReservationsByDate(Date date) {
		Query query = em.createQuery("SELECT r FROM Reservation r");
		int dateInt = date.getDate();
		List<Reservation> allReservations = query.getResultList();
		List<Reservation> dateReservations = new ArrayList<>();
		allReservations.forEach(res -> {
			int resDate = res.getReservationStartDate().getDate();
			if (resDate == dateInt) {
				dateReservations.add(res);
			}
		});
		return dateReservations;
	}

	@Override
	public Reservation retrieveReservationsByCustomer(String email) throws CustomerNotFoundException, ReservationNotFoundException {
		Date today = new Date();
		Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerEmail(email);
		Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.customer = :inCustomer");
		query.setParameter("inCustomer", customer);

		List<Reservation> allReservations = query.getResultList();

		for (Reservation res : allReservations) {
			Date rStart = res.getReservationStartDate();
			if ((rStart.getDate() == today.getDate())
					&& (rStart.getMonth() == today.getMonth()
					&& (rStart.getYear() == today.getYear()))) {
				return res;
			}
		}
		throw new ReservationNotFoundException("Customer " + customer.getName() + " does not have a reservation today!");
	}

	@Override
	public void startReservation(Reservation reservation, Long carId) throws CarNotFoundException {
		Car car = carSessionBeanLocal.retrieveCarByCarId(carId, Boolean.TRUE);

		car.setCarStatus(CarStatusEnum.ONRENTAL);
		car.setOutlet(null);
		car.setCurrentReservation(reservation);

		em.merge(reservation);

	}

	@Override
	public void updateReservation(Reservation reservation) throws ReservationNotFoundException, InputDataValidationException {
		if (reservation != null && reservation.getReservationId() != null) {
			Set<ConstraintViolation<Reservation>> constraintViolations = validator.validate(reservation);

			if (constraintViolations.isEmpty()) {
				em.merge(reservation);

			} else {
				throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
			}
		} else {
			throw new ReservationNotFoundException("Reservation not found for reservation to be updated");
		}
	}

	@Override
	public List<Reservation> retrieveReservationsByCustomerEmail(String email) {
		Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.customer.email = :inEmail");
		query.setParameter("inEmail", email);
		return query.getResultList();
	}

	@Override
	public List<Reservation> retrieveReservationsByPartnerUsername(String username) {
		Query query = em.createQuery("SELECT r FROM Reservation r WHERE r.partner.username = :inUsername");
		query.setParameter("inUsername", username);
		return query.getResultList();
	}

	@Override
	public OwnCustomer removeReservationByOwnCustomer(Long reservationId, OwnCustomer ownCustomer) throws ReservationNotFoundException {
		Reservation oldReservation = retrieveReservationByReservationId(reservationId);

		if (oldReservation.getCar() != null) {
			oldReservation.getCar().getReservations().remove(oldReservation);
		}

		if (oldReservation.getCarModel() != null) {
			oldReservation.getCarModel().getReservations().remove(oldReservation);
		}

		oldReservation.getCarCategory().getReservations().remove(oldReservation);

		ownCustomer.getReservations().remove(oldReservation);

		oldReservation.getPickUpOutlet().getReservations().remove(oldReservation);

		em.remove(oldReservation);
		return ownCustomer;
	}

	@Override
	public Partner removeReservationByPartner(Long reservationId, Partner partner) throws ReservationNotFoundException {
		Reservation oldReservation = retrieveReservationByReservationId(reservationId);

		if (oldReservation.getCar() != null) {
			oldReservation.getCar().getReservations().remove(oldReservation);
		}

		if (oldReservation.getCarModel() != null) {
			oldReservation.getCarModel().getReservations().remove(oldReservation);
		}

		oldReservation.getCarCategory().getReservations().remove(oldReservation);

		oldReservation.getCustomer().getReservations().remove(oldReservation);

		partner.getReservations().remove(oldReservation);

		oldReservation.getPickUpOutlet().getReservations().remove(oldReservation);

		em.remove(oldReservation);
		return partner;
	}

	@Override
	public void allocateCar(Long carId, Long reservationId) throws CarNotFoundException, CarModelNotFoundException, ReservationNotFoundException {
		Car car = carSessionBeanLocal.retrieveCarByCarId(carId, true);
		CarModel model = carModelSessionBeanLocal.retrieveCarModelByCarModelId(car.getCarModel().getCarModelId());
		Reservation res = retrieveReservationByReservationId(reservationId);

		car.getReservations().add(res);
		model.getReservations().add(res);
		res.setCar(car);
		res.setCarModel(model);

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
			} else {
				return new BigDecimal("0.7").multiply(reservation.getTotalAmount());
			}
		} else if (currentDate.after(penaltySeven)) {
			if (paymentDate.compareTo(reservation.getReservationStartDate()) < 0) {
				return new BigDecimal("0.5").multiply(reservation.getTotalAmount());
			} else {
				return new BigDecimal("0.5").multiply(reservation.getTotalAmount());
			}
		} else if (currentDate.after(penaltyFourteen)) {
			if (paymentDate.compareTo(reservation.getReservationStartDate()) < 0) {
				return new BigDecimal("0.8").multiply(reservation.getTotalAmount());
			} else {
				return new BigDecimal("0.2").multiply(reservation.getTotalAmount());
			}
		} else {
			if (paymentDate.compareTo(reservation.getReservationStartDate()) < 0) {
				return reservation.getTotalAmount();
			} else {
				return new BigDecimal("0.0");
			}
		}

	}

	private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Reservation>> constraintViolations) {
		String msg = "Input data validation error!:";

		for (ConstraintViolation constraintViolation : constraintViolations) {
			msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
		}

		return msg;
	}

}
