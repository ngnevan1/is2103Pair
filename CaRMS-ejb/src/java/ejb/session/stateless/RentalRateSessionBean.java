/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import entity.RentalRate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
import util.exception.InputDataValidationException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author KMwong
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public RentalRateSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public RentalRate createNewRentalRate(RentalRate newRentalRate) throws RentalRateExistException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newRentalRate);
                em.flush();
                em.refresh(newRentalRate);
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
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId, Boolean retrieveCarCategory) throws RentalRateNotFoundException {
        RentalRate rentalRate = retrieveRentalRateByRentalRateId(rentalRateId);

        if (rentalRate != null) {
            if (retrieveCarCategory) {
                rentalRate.getCarCategory();
            }
            return rentalRate;
        } else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + "does not exist!");
        }
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
        
        // TODO 
        // check if there is existing reservation before removing
        // if there is, set disabled to true, if not just remove
        em.remove(rateToRemove);
    }
    
    @Override
    public BigDecimal calculateRentalRate(List<RentalRate> rates, Date pickupDate, Date returnDate) {
        // WIP
        return null;
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<RentalRate>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
}
