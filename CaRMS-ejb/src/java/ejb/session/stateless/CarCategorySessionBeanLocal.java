/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CarCategory;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.CarCategoryExistException;
import util.exception.CarCategoryNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Local
public interface CarCategorySessionBeanLocal {
    public CarCategory createNewCarCategory(CarCategory newCarCategory) throws CarCategoryExistException, UnknownPersistenceException;
	public CarCategory retrieveCarCategoryByCarCategoryId(Long carCategoryId) throws CarCategoryNotFoundException; 
    CarCategory retrieveCarCategoryByCategoryName(String categoryName) throws CarCategoryNotFoundException;
    public void associateCarModelsWithCarCategory(Long categoryId, List<Long> modelIds);
	public List<CarCategory> searchAvailableCarCategory(Date rNewStart, Date rNewEnd);
}
