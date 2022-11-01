/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarModelExistException;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Remote
public interface CarModelSessionBeanRemote {
    public CarModel createNewCarModel(CarModel newCarModel) throws CarModelExistException, UnknownPersistenceException, InputDataValidationException;
    List<CarModel> retrieveAllCarModels();
    CarModel retrieveCarModelByCarModelId(Long carModelId, Boolean retrieveCar, Boolean retrieveReservation, Boolean retrieveCarCategory) throws CarModelNotFoundException;
    CarModel retrieveCarModelByModelName(String modelName) throws CarModelNotFoundException;
    public void updateCarModel(CarModel model) throws CarModelNotFoundException, InputDataValidationException;
    public void deleteCarModel(Long carModelId) throws CarModelNotFoundException;
}
