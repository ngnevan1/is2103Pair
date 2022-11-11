/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.Date;
import javax.ejb.Local;
import util.exception.OutletExistException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Local
public interface OutletSessionBeanLocal {
    public Outlet createNewOutlet(Outlet newOutlet) throws OutletExistException, UnknownPersistenceException;
    public Outlet retrieveOutletByOutletId(Long outletId, Boolean retrieveCars, Boolean retrieveEmployees, Boolean retrieveTransitDispatchRecords) throws OutletNotFoundException;
    public Outlet retrieveOutletByOutletName(String outletName) throws OutletNotFoundException;
    public void associateEmployeeWithOutlet(Long employeeId, Long outletId);
    public Boolean checkOutletIsOpen(Date pickupDate, String pickupOutlet, Date returnDate, String returnOutlet) throws OutletNotFoundException;
    
}
