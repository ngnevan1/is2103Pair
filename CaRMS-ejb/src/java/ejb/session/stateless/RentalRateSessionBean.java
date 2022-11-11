/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import entity.RentalRate;
import entity.Reservation;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
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
import util.enumeration.RentalRateEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotAvailableException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author KMwong
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

	@EJB
	private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

	@PersistenceContext(unitName = "CaRMS-ejbPU")
	private EntityManager em;
	private final ValidatorFactory validatorFactory;
	private final Validator validator;

	public RentalRateSessionBean() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	// Add business logic below. (Right-click in editor and choose
	// "Insert Code > Add Business Method")
	@Override
	public RentalRate createNewRentalRate(RentalRate newRentalRate) throws RentalRateExistException, UnknownPersistenceException, InputDataValidationException {

		Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

		if (constraintViolations.isEmpty()) {
			try {
				em.persist(newRentalRate);
				em.flush();
				em.refresh(newRentalRate);
				CarCategory category = em.find(CarCategory.class, newRentalRate.getCarCategory().getCarCategoryId());
				category.getRentalRates().add(newRentalRate);
				return newRentalRate;
			} catch (PersistenceException ex) {
				if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
					if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
						throw new RentalRateExistException("Rental Rate already exists!");
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
	public RentalRate createNewRentalRate(RentalRate newRentalRate, Long carCategoryId) throws RentalRateExistException, CarCategoryNotFoundException, UnknownPersistenceException, InputDataValidationException {
		CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCarCategoryId(carCategoryId);
		newRentalRate.setCarCategory(carCategory);
		Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

		if (constraintViolations.isEmpty()) {
			try {
				em.persist(newRentalRate);
				em.flush();
				em.refresh(newRentalRate);
				CarCategory category = em.find(CarCategory.class, newRentalRate.getCarCategory().getCarCategoryId());
				category.getRentalRates().add(newRentalRate);
				return newRentalRate;
			} catch (PersistenceException ex) {
				if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
					if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
						throw new RentalRateExistException("Rental Rate already exists!");
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
	public List<RentalRate> retrieveAllRentalRates() {
		Query query = em.createQuery("SELECT rr FROM RentalRate rr ORDER BY rr.carCategory, rr.rateEndDate");
		return query.getResultList();
	}

	@Override
	public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId) throws RentalRateNotFoundException {
		RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);

		if (rentalRate != null) {
			return rentalRate;
		} else {
			throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + "does not exist!");
		}
	}

	@Override
	public RentalRate retrieveRentalRateByRateName(String rateName) throws RentalRateNotFoundException {
		Query query = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.rateName = :inRateName");
		query.setParameter("inRateName", rateName);

		try {
			return (RentalRate) query.getSingleResult();
		} catch (NoResultException | NonUniqueResultException ex) {
			throw new RentalRateNotFoundException("Rental Rate Name " + rateName + " does not exist!");
		}
	}

	@Override
	public List<RentalRate> retrieveRentalRateByCarCategory(CarCategory carCategory) {
		Query query = em.createQuery("SELECT rr FROM RentalRate rr WHERE rr.carCategory = :inCarCategory");
		query.setParameter("inCarCategory", carCategory);
		return query.getResultList();
	}

	@Override
	public void updateRentalRate(RentalRate rate) throws RentalRateNotFoundException, UpdateRentalRateException, InputDataValidationException {
		if (rate != null && rate.getRentalRateId() != null) {
			Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(rate);

			if (constraintViolations.isEmpty()) {
				RentalRate rateToUpdate = retrieveRentalRateByRentalRateId(rate.getRentalRateId());

				if (rateToUpdate.getRateStartDate().equals(rate.getRateStartDate())) {
					rateToUpdate.setRateName(rate.getRateName());
					rateToUpdate.setCarCategory(rate.getCarCategory());
					rateToUpdate.setRatePerDay(rate.getRatePerDay());
					rateToUpdate.setRateStartDate(rate.getRateStartDate());
					rateToUpdate.setRateEndDate(rate.getRateEndDate());

				} else {
					throw new UpdateRentalRateException("Rate Start Date of Rental Rate record to be updated does not match the existing record");
				}
			} else {
				throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
			}
		} else {
			throw new RentalRateNotFoundException("Rental Rate ID not provided for rental rate to be updated");
		}
	}

	@Override
	public void deleteRentalRate(Long rentalRateId) throws RentalRateNotFoundException {
		RentalRate rateToRemove = retrieveRentalRateByRentalRateId(rentalRateId);
		// check if there is existing reservation before removing
		// if there is, set disabled to true, if not just remove
		List<Reservation> reservations = rateToRemove.getCarCategory().getReservations();
		boolean inUse = false;
		Date today = new Date();
		for (Reservation r : reservations) {
			if (today.after(r.getReservationStartDate()) && today.before(r.getReservationEndDate())) {
				inUse = true;
				break;
			}
		}

		if (inUse) {
			rateToRemove.setIsDisabled(true);
		} else {
			em.remove(rateToRemove);
		}
	}

	@Override
	public List<RentalRate> calculateRentalRate(List<RentalRate> rates, Date pickupDate, Date returnDate) throws RentalRateNotAvailableException {
                Calendar startDate = new GregorianCalendar();
		startDate.setTime(pickupDate);
		Calendar endDate = new GregorianCalendar();
		endDate.setTime(returnDate);
                List<RentalRate> usedRates = new ArrayList<>();
                
                while (startDate.before(endDate)) {
                    BigDecimal cheapestRate = new BigDecimal("1000.00");
                    RentalRate rateToUse = null;
                    Date currentDate = startDate.getTime();
                    Boolean correctRate = Boolean.FALSE;
                    
                    for (RentalRate rate : rates) {
                        Date rateStartDate = rate.getRateStartDate();
                        Date rateEndDate = rate.getRateEndDate();
                        if ((!rate.getIsDisabled()) && 
                                (rateStartDate.before(currentDate) || rateStartDate.equals(currentDate)) && 
                                (rateEndDate.equals(currentDate) || rateEndDate.after(currentDate)) && 
                                ((rate.getRatePerDay().compareTo(cheapestRate) == -1) /*|| (cheapestRate.equals(new BigDecimal("0.00"))) */ )) {
                            cheapestRate = rate.getRatePerDay();
                            rateToUse = rate;
                            correctRate = Boolean.TRUE;
                        }
                    }
                    
                    if (correctRate) {
                        usedRates.add(rateToUse);
                    } 
                    else {
                        throw new RentalRateNotAvailableException("Rental Rate is not available for a particular day!");
                    }
                    
                    startDate.add(Calendar.DAY_OF_MONTH, 1);
                }
                
                return usedRates;
                		
                /*
		while (startDate.before(endDate)) {
			BigDecimal cheapestRate = new BigDecimal("1000.0");

			for (RentalRate rate : rates) {
				Calendar rentalRateStartDate = new GregorianCalendar();
				rentalRateStartDate.setTime(rate.getRateStartDate());
				Calendar rentalRateEndDate = new GregorianCalendar();
				rentalRateStartDate.setTime(rate.getRateEndDate());

				if (!rate.getIsDisabled()) {
					if (rentalRateStartDate == null
							|| (rentalRateStartDate.before(startDate) && rentalRateEndDate.after(startDate)
							|| (rentalRateStartDate.equals(startDate) || rentalRateEndDate.equals(startDate)))) {

						if (rate.getRateType().equals(RentalRateEnum.PEAK)) {
							cheapestRate = rate.getRatePerDay();
							break;
						} else {
							if (rate.getRatePerDay().compareTo(cheapestRate) == -1) {
								cheapestRate = rate.getRatePerDay();
							}
						}
					}
				}
			}

			totalAmount = totalAmount.add(cheapestRate);
			startDate.add(Calendar.DAY_OF_MONTH, 1);
		}

		return totalAmount;
                */
	}

	private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
		String msg = "Input data validation error!:";

		for (ConstraintViolation constraintViolation : constraintViolations) {
			msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
		}

		return msg;
	}
}
