/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CarCategoryExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Remote
public interface CarCategorySessionBeanRemote {
    public CarCategory createNewCarCategory(CarCategory newCarCategory) throws CarCategoryExistException, UnknownPersistenceException;
    public List<CarCategory> retrieveAllCarCategories();
    public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId, Boolean retrieveCarModel, Boolean retrieveRentalRate) throws CarCategoryNotFoundException;
    public CarCategory retrieveCarCategoryByCategoryName(String categoryName) throws CarCategoryNotFoundException;
}
