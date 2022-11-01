/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
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
import util.exception.CarModelExistException;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Stateless
public class CarModelSessionBean implements CarModelSessionBeanRemote, CarModelSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public CarModelSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public CarModel createNewCarModel(CarModel newCarModel) throws CarModelExistException, UnknownPersistenceException, InputDataValidationException {
        Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(newCarModel);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newCarModel);
                em.flush();
                em.refresh(newCarModel);
                return newCarModel;
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new CarModelExistException("Car Model already exists!");
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
    public CarModel retrieveCarModelByCarModelId(Long carModelId) throws CarModelNotFoundException {
        CarModel carModel = em.find(CarModel.class, carModelId);
        
        if (carModel != null) {
            return carModel;
        } else {
            throw new CarModelNotFoundException("Car Model ID " + carModelId + "does not exist!");
        }
    }

    @Override
    public CarModel retrieveCarModelByCarModelId(Long carModelId, Boolean retrieveCar, Boolean retrieveReservation, Boolean retrieveCarCategory) throws CarModelNotFoundException {
        CarModel carModel = em.find(CarModel.class, carModelId);

        if (carModel != null) {
            if (retrieveCar) {
                carModel.getCars().size();
            }
            if (retrieveReservation) {
                carModel.getReservations().size();
            }
            if (retrieveCarCategory) {
                carModel.getCarCategory();
            }
            return carModel;
        } else {
            throw new CarModelNotFoundException("Car Model ID " + carModelId + "does not exist!");
        }
    }

    @Override
    public CarModel retrieveCarModelByModelName(String modelName) throws CarModelNotFoundException {
        Query query = em.createQuery("SELECT cm FROM CarModel cm WHERE cm.modelName = :inModelName");
        query.setParameter("inModelName", modelName);

        try {
            return (CarModel) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CarModelNotFoundException("Car Model Model Name " + modelName + " does not exist!");
        }
    }

    @Override
    public List<CarModel> retrieveAllCarModels() {
        Query query = em.createQuery("SELECT cm FROM CarModel cm");

        return query.getResultList();
    }

    @Override
    public void updateCarModel(CarModel model) throws CarModelNotFoundException, InputDataValidationException {
        if (model != null && model.getCarModelId() != null) {
            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(model);

            if (constraintViolations.isEmpty()) {
                CarModel modelToUpdate = retrieveCarModelByCarModelId(model.getCarModelId());
                
                    modelToUpdate.setModelName(model.getModelName());
                    modelToUpdate.setMakeName(model.getMakeName());
                    modelToUpdate.setCarCategory(model.getCarCategory());
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CarModelNotFoundException("Car Model ID not provided for car model to be updated");
        }
    } 
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CarModel>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
