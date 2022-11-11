/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarExistException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

/**
 *
 * @author KMwong
 */
@Remote
public interface CarSessionBeanRemote {
    public Car createNewCar(Car newCar) throws CarExistException, UnknownPersistenceException, InputDataValidationException;
    public List<Car> retrieveAllCars();
    public Car retrieveCarByLicensePlate(String licensePlate) throws CarNotFoundException;
    public List<Car> searchAvailableCars(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet);
    public void updateCar(Car car) throws CarNotFoundException, UpdateCarException, InputDataValidationException;
	public void deleteCar(Long carId) throws CarNotFoundException, OutletNotFoundException, ReservationNotFoundException, CarModelNotFoundException;
}
