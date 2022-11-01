/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarModel;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarModelExistException;
import util.exception.CarModelNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Local
public interface CarModelSessionBeanLocal {
    List<CarModel> retrieveAllCarModels();
    public CarModel retrieveCarModelByCarModelId(Long carModelId) throws CarModelNotFoundException; 
    CarModel retrieveCarModelByModelName(String modelName) throws CarModelNotFoundException;
}
