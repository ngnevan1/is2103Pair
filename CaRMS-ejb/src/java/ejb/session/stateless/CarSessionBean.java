/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.CarExistException;
import util.exception.CarNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Stateless
public class CarSessionBean implements CarSessionBeanRemote, CarSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    public CarSessionBean() {
    }
    
    @Override
    public Car createNewCar(Car newCar) throws CarExistException, UnknownPersistenceException {
        try {
            em.persist(newCar);
            em.flush();
            em.refresh(newCar);
            return newCar;
        }
        catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CarExistException("Car already exists!");
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
    public Car retrieveCarByCarId(Long carId, Boolean retrieveCarModel, Boolean retrieveReservation, Boolean retrieveOutlet) throws CarNotFoundException {
        Car car = em.find(Car.class, carId);
        
        if(car != null) {
            if (retrieveCarModel) {
                car.getCarModel();
            }
            if (retrieveReservation) {
                car.getReservations().size();
            }
            if (retrieveOutlet) {
                car.getOutlet();
            }
            return car;
        }
        else {
            throw new CarNotFoundException("Car ID " + carId + "does not exist!");
        }
    }
    
    @Override
    public Car retrieveCarByLicensePlate(String licensePlate) throws CarNotFoundException {
        Query query = em.createQuery("SELECT c FROM Car c WHERE c.licensePlate = :inLicensePlate");
        query.setParameter("inLicensePlate", licensePlate);
        
        try {
            return (Car)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new CarNotFoundException("Car License Plate " + licensePlate + " does not exist!");
        }
    }

}
