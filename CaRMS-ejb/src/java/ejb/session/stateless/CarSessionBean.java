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
import entity.Reservation;
import java.util.List;
import java.util.Set;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.exception.CarExistException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author KMwong
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

	@PersistenceContext(unitName = "CaRMS-ejbPU")
	private EntityManager em;

	private final ValidatorFactory validatorFactory;
	private final Validator validator;

	public CarSessionBean() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")
	@Override
	public Car createNewCar(Car newCar) throws CarExistException, UnknownPersistenceException, InputDataValidationException {
		Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

		if (constraintViolations.isEmpty()) {
			try {
				em.persist(newCar);
				em.flush();
				em.refresh(newCar);

				// associate outlet with car
				Outlet outlet = em.find(Outlet.class, newCar.getOutlet().getOutletId());
				outlet.getCars().add(newCar);

				CarModel model = em.find(CarModel.class, newCar.getCarModel().getCarModelId());
				model.getCars().add(newCar);
				return newCar;
			} catch (PersistenceException ex) {
				if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
					if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
						throw new CarExistException("Car already exists!");
					} else {
						throw new UnknownPersistenceException(ex.getMessage());
					}
				} else {
					throw new UnknownPersistenceException(ex.getMessage());
				}
			}
		} else {
			throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
		}
	}

	@Override
	public List<Car> retrieveAllCars() {
		Query query = em.createQuery("SELECT c FROM Car c ORDER BY c.carModel.carCategory.categoryName, c.carModel.makeName, c.carModel.modelName, c.licensePlate");

		return query.getResultList();
	}

	@Override
	public Car retrieveCarByCarId(Long carId, Boolean retrieveReservations) throws CarNotFoundException {
		Car car = em.find(Car.class, carId);

		if (car != null) {
			if (retrieveReservations) {
				car.getReservations().size();
			}
			return car;
		} else {
			throw new CarNotFoundException("Car ID " + carId + "does not exist!");
		}
	}

	public Car retrieveCarByCarId(Long carId) throws CarNotFoundException {
		Car car = em.find(Car.class, carId);

		if (car != null) {
			return car;
		} else {
			throw new CarNotFoundException("Car ID " + carId + "does not exist!");
		}
	}

	@Override
	public Car retrieveCarByLicensePlate(String licensePlate) throws CarNotFoundException {
		Query query = em.createQuery("SELECT c FROM Car c WHERE c.licensePlate = :inLicensePlate");
		query.setParameter("inLicensePlate", licensePlate);

		try {
			return (Car) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException ex) {
			throw new CarNotFoundException("Car License Plate " + licensePlate + " does not exist!");
		}
	}

	@Override
	public List<Car> retrieveCarsByCarCategory(CarCategory category) {
		Query query = em.createQuery("SELECT c FROM Car c WHERE c.carModel.carCategory = :inCarCategory");
		query.setParameter("inCarCategory", category);

		return query.getResultList();
	}

	@Override
	public List<Car> searchAvailableCars(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet) {
		// Stub
		return null;
	}

	@Override
	public void updateCar(Car car) throws CarNotFoundException, UpdateCarException, InputDataValidationException {
		if (car != null && car.getCarId() != null) {
			Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

			if (constraintViolations.isEmpty()) {
				Car carToUpdate = retrieveCarByCarId(car.getCarId());

				if (carToUpdate.getLicensePlate().equals(car.getLicensePlate())) {
					em.merge(car);
				} else {
					throw new UpdateCarException("Licence Plate Number of Car record to be updated does not match the existing record");
				}
			} else {
				throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
			}
		} else {
			throw new CarNotFoundException("Car ID not provided for rental car to be updated");
		}
	}

	@Override
	public void deleteCar(Long carId) throws CarNotFoundException {
		Car carToRemove = retrieveCarByCarId(carId);

		if (carToRemove.getCarStatus().equals(CarStatusEnum.AVAILABLE)) {
			em.remove(carToRemove);
		} else {
			carToRemove.setIsDisabled(true);
		}
	}

	@Override
	public boolean isAvailable(Long carId, Date rNewStart, Date rNewEnd) throws CarNotFoundException {
		Car car = retrieveCarByCarId(carId, true);

		List<Reservation> reservations = car.getReservations();

		// check if disabled
		if (!car.getIsDisabled()) {
			// check if available during reservation time
			for (Reservation r : reservations) {
				Date rExistingStart = r.getReservationStartDate();
				Date rExistingEnd = r.getReservationEndDate();

				if ((rNewStart.before(rExistingEnd) && rNewStart.after(rExistingStart))
						|| (rNewEnd.after(rExistingStart) && rNewEnd.before(rExistingEnd))
						|| (rNewStart.before(rExistingStart) && rNewEnd.after(rExistingEnd))
						|| (rNewStart.equals(rExistingStart) && rNewEnd.equals(rExistingEnd))) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;

	}

//	@Override
//	public boolean isAvailableOnDate(Long carId, Date startDate, Date endDate) throws CarNotFoundException {
//		Car car = retrieveCarByCarId(carId);
//		List<Reservation> reservations = car.getReservations();
//		// check if car is free on particular day
//
//		for (Reservation r : reservations) {
//			Date rStart = r.getReservationStartDate();
//			Date rEnd = r.getReservationEndDate();
//			if (rEnd.after(startDate) || rStart.before(rEnd)) {
//				return false;
//			}
//		}
//		return true;
//	}
	private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Car>> constraintViolations) {
		String msg = "Input data validation error!:";

		for (ConstraintViolation constraintViolation : constraintViolations) {
			msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
		}

		return msg;
	}
}
