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
//        try
//        {
//          //  carCategorySessionBeanLocal.retrieveStaffByUsername("manager");
//        }
//        catch(StaffNotFoundException ex)
//        {
//            initializeData();
//        }
    }
    
    public void initialiseData() {
        try {
            carCategorySessionBeanLocal.createNewCarCategory(new CarCategory("SUV"));
            outletSessionBeanLocal.createNewOutlet(new Outlet("Main Outlet", "123 Kent Ridge Park", new Date(), new Date()));
            partnerSessionBeanLocal.createNewPartner(new Partner("Partner", "partner", "password"));
            employeeSessionBeanLocal.createNewEmployee(new Employee("Manager", "manager", "password", EmployeeAccessRightsEnum.OPERATIONS_MANAGER, TransitStatusEnum.AVAILABLE));
        } catch (CarCategoryExistException | OutletExistException | PartnerExistException | EmployeeExistException | UnknownPersistenceException ex) {
            ex.printStackTrace();
        }
    }

}
