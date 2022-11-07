/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import java.util.List;
import javax.ejb.Remote;
import util.exception.OutletExistException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Remote
public interface OutletSessionBeanRemote {
    public Outlet createNewOutlet(Outlet newOutlet) throws OutletExistException, UnknownPersistenceException;
    public Outlet retrieveOutletByOutletId(Long outletId, Boolean retrieveCars, Boolean retrieveEmployees, Boolean retrieveTransitDispatchRecords) throws OutletNotFoundException;
    public List<Outlet> retrieveAllOutlets();
    
}
