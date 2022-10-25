/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Outlet;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.OutletExistException;
import util.exception.OutletNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Stateless
public class OutletSessionBean implements OutletSessionBeanRemote, OutletSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Outlet createNewOutlet(Outlet newOutlet) throws OutletExistException, UnknownPersistenceException {
        try {
            em.persist(newOutlet);
            em.flush();
            em.refresh(newOutlet);
            return newOutlet;
        }
        catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new OutletExistException("Outlet already exists!");
                }
                else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    @Override
    public Outlet retrieveOutletByOutletId(Long outletId, Boolean retrieveCars, Boolean retrieveEmployees, Boolean retrieveTransitDispatchRecords) throws OutletNotFoundException {
        Outlet outlet = em.find(Outlet.class, outletId);
        
        if(outlet != null) {
            if (retrieveCars) {
                outlet.getCars().size();
            }
            if (retrieveEmployees) {
                outlet.getEmployees().size();
            }
            if (retrieveTransitDispatchRecords) {
                outlet.getTransitDispatchRecords().size();
            }
            return outlet;
        }
        else {
            throw new OutletNotFoundException("Outlet ID " + outletId + "does not exist!");
        }
    }
}