/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.CarCategoryExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Stateless
public class CarCategorySessionBean implements CarCategorySessionBeanRemote, CarCategorySessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public CarCategorySessionBean() {
    }
    
    @Override
    public CarCategory createNewCarCategory(CarCategory newCarCategory) throws CarCategoryExistException, UnknownPersistenceException {
        try {
            em.persist(newCarCategory);
            em.flush();
            em.refresh(newCarCategory);
            return newCarCategory;
        }
        catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CarCategoryExistException("Car Category already exists!");
                }
                else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId, Boolean retrieveCarModel, Boolean retrieveReservation, Boolean retrieveRentalRate) throws CarCategoryNotFoundException {
        CarCategory carCategory = em.find(CarCategory.class, carCategoryId);
        
        if(carCategory != null) {
            if (retrieveCarModel) {
                carCategory.getCarModels().size();
            }
            if (retrieveReservation) {
                carCategory.getReservations().size();
            }
            if (retrieveRentalRate) {
                carCategory.getRentalRates().size();
            }
            return carCategory;
        }
        else {
            throw new CarCategoryNotFoundException("Car Category ID " + carCategoryId + "does not exist!");
        }
    }
}
