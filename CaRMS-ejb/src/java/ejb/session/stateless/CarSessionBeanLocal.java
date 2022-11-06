/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import javax.ejb.Local;
import util.exception.CarNotFoundException;

/**
 *
 * @author KMwong
 */
@Local
public interface CarSessionBeanLocal {
    Car retrieveCarByCarId(Long carId, Boolean retrieveCarModel, Boolean retrieveReservation, Boolean retrieveOutlet) throws CarNotFoundException;
    Car retrieveCarByLicensePlate(String licensePlate) throws CarNotFoundException;
}
