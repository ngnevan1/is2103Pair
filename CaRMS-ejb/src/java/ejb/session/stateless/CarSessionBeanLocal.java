/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarExistException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Local
public interface CarSessionBeanLocal {
    public Car createNewCar(Car newCar) throws CarExistException, UnknownPersistenceException, InputDataValidationException;
    Car retrieveCarByCarId(Long carId, Boolean retrieveCarModel, Boolean retrieveReservation, Boolean retrieveOutlet) throws CarNotFoundException;
    Car retrieveCarByLicensePlate(String licensePlate) throws CarNotFoundException;
    List<Car> searchAvailableCars(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet);
}
