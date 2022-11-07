/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.EmployeeAccessRightsEnum;
import util.enumeration.TransitStatusEnum;
import util.exception.CarCategoryExistException;
import util.exception.CarModelExistException;
import util.exception.EmployeeExistException;
import util.exception.InputDataValidationException;
import util.exception.OutletExistException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;

    @EJB
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;

    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    
    

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @PostConstruct
    public void postConstruct() {
        try
        {
            outletSessionBeanLocal.retrieveOutletByOutletName("Main Outlet");
        }
        catch(OutletNotFoundException ex)
        {
            initialiseData();
        } 
    }
    
    public void initialiseData() {
        try {
            Outlet outletA = new Outlet("Outlet A", null, null);
            Outlet outletB = new Outlet("Outlet B", null, null);
            Outlet outletC = new Outlet("Outlet C", 10, 22);
            
            Employee a1 = new Employee("Employee A1", "a1", EmployeeAccessRightsEnum.SALES_MANAGER, outletA);
            Employee a2 = new Employee("Employee A2", "a2", EmployeeAccessRightsEnum.OPERATIONS_MANAGER, outletA);
            Employee a3 = new Employee("Employee A3", "a3", EmployeeAccessRightsEnum.CS_EXECUTIVE , outletA);
            Employee a4 = new Employee("Employee A4", "a4", EmployeeAccessRightsEnum.EMPLOYEE, outletA);
            Employee a5 = new Employee("Employee A5", "a5", EmployeeAccessRightsEnum.EMPLOYEE, outletA);
            Employee b1 = new Employee("Employee B1", "b1", EmployeeAccessRightsEnum.SALES_MANAGER, outletB);
            Employee b2 = new Employee("Employee B2", "b2", EmployeeAccessRightsEnum.OPERATIONS_MANAGER, outletB);
            Employee b3 = new Employee("Employee B3", "b3", EmployeeAccessRightsEnum.CS_EXECUTIVE, outletB);
            Employee c1 = new Employee("Employee C1", "c1", EmployeeAccessRightsEnum.SALES_MANAGER, outletC);
            Employee c2 = new Employee("Employee C2", "c2", EmployeeAccessRightsEnum.OPERATIONS_MANAGER, outletC);
            Employee c3 = new Employee("Employee C3", "c3", EmployeeAccessRightsEnum.CS_EXECUTIVE, outletC);
            
            CarCategory standard = new CarCategory("Standard Sedan");
            CarCategory family = new CarCategory("Family Sedan");
            CarCategory luxury = new CarCategory("Luxury Sedan");
            CarCategory suv = new CarCategory("SUV and Minivan");
            
            carCategorySessionBeanLocal.createNewCarCategory(standard);
            carCategorySessionBeanLocal.createNewCarCategory(family);
            carCategorySessionBeanLocal.createNewCarCategory(luxury);
            carCategorySessionBeanLocal.createNewCarCategory(suv);
            
            outletSessionBeanLocal.createNewOutlet(outletA);
            outletSessionBeanLocal.createNewOutlet(outletB);
            outletSessionBeanLocal.createNewOutlet(outletC);
            employeeSessionBeanLocal.createNewEmployee(a1);
            employeeSessionBeanLocal.createNewEmployee(a2);
            employeeSessionBeanLocal.createNewEmployee(a3);
            employeeSessionBeanLocal.createNewEmployee(a4);
            employeeSessionBeanLocal.createNewEmployee(a5);
            
            employeeSessionBeanLocal.createNewEmployee(b1);
            employeeSessionBeanLocal.createNewEmployee(b2);
            employeeSessionBeanLocal.createNewEmployee(b3);
            
            employeeSessionBeanLocal.createNewEmployee(c1);
            employeeSessionBeanLocal.createNewEmployee(c2);
            employeeSessionBeanLocal.createNewEmployee(c3);
            
            partnerSessionBeanLocal.createNewPartner(new Partner("Holiday.com"));
            
            outletSessionBeanLocal.associateEmployeeWithOutlet(a1.getEmployeeId(), outletA.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(a2.getEmployeeId(), outletA.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(a3.getEmployeeId(), outletA.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(a4.getEmployeeId(), outletA.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(a5.getEmployeeId(), outletA.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(b1.getEmployeeId(), outletB.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(b2.getEmployeeId(), outletB.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(b3.getEmployeeId(), outletB.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(c1.getEmployeeId(), outletC.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(c2.getEmployeeId(), outletC.getOutletId());
            outletSessionBeanLocal.associateEmployeeWithOutlet(c3.getEmployeeId(), outletC.getOutletId());
            
            CarModel corolla = new CarModel("Toyota", "Corolla", standard);
            CarModel civic = new CarModel("Honda", "Civic", standard);
            CarModel sunny = new CarModel("Nissan", "Sunny", standard);
            CarModel eClass = new CarModel("Mercedes", "E Class", luxury);
            CarModel series = new CarModel("BMW", "5 Series", luxury);
            CarModel a6 = new CarModel("Audi", "A6", luxury);
            
            carModelSessionBeanLocal.createNewCarModel(corolla);
            carModelSessionBeanLocal.createNewCarModel(civic);
            carModelSessionBeanLocal.createNewCarModel(sunny);
            carModelSessionBeanLocal.createNewCarModel(eClass);
            carModelSessionBeanLocal.createNewCarModel(series);
            carModelSessionBeanLocal.createNewCarModel(a6);
            
            List<Long> standardModels = new ArrayList<>();
            
            standardModels.add(corolla.getCarModelId());
            standardModels.add(civic.getCarModelId());
            standardModels.add(sunny.getCarModelId());
            
            List<Long> luxuryModels = new ArrayList<>();
            
            luxuryModels.add(eClass.getCarModelId());
            luxuryModels.add(series.getCarModelId());
            luxuryModels.add(a6.getCarModelId());
            
            carCategorySessionBeanLocal.associateCarModelsWithCarCategory(standard.getCarCategoryId(), standardModels);
            carCategorySessionBeanLocal.associateCarModelsWithCarCategory(luxury.getCarCategoryId(), luxuryModels);
            
            
        } catch (CarCategoryExistException | OutletExistException | PartnerExistException | EmployeeExistException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        } catch (CarModelExistException | InputDataValidationException ex) {
            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
