/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import entity.CarCategory;
import entity.Employee;
import entity.Outlet;
import entity.Partner;
import java.util.Date;
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
import util.exception.EmployeeExistException;
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
            CarCategory newCategory = new CarCategory("SUV");
            Outlet newOutlet = new Outlet("Main Outlet", "123 Kent Ridge Park", new Date(), new Date());
            Employee newEmployee = new Employee("Manager", "manager", "password", EmployeeAccessRightsEnum.OPERATIONS_MANAGER, TransitStatusEnum.AVAILABLE, newOutlet);
            
            carCategorySessionBeanLocal.createNewCarCategory(newCategory);
            outletSessionBeanLocal.createNewOutlet(newOutlet);
            employeeSessionBeanLocal.createNewEmployee(newEmployee);
            partnerSessionBeanLocal.createNewPartner(new Partner("Partner", "partner", "password"));
            outletSessionBeanLocal.associateEmployeeWithOutlet(newEmployee.getEmployeeId(), newOutlet.getOutletId());
            
        } catch (CarCategoryExistException | OutletExistException | PartnerExistException | EmployeeExistException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }

}
