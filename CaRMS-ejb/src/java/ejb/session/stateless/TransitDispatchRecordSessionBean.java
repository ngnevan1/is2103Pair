/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.TransitDispatchRecord;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.EmployeeNotFoundException;
import util.exception.TransitDispatchRecordExistException;
import util.exception.TransitDispatchRecordNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Stateless
public class TransitDispatchRecordSessionBean implements TransitDispatchRecordSessionBeanRemote, TransitDispatchRecordSessionBeanLocal {

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @Override
    public TransitDispatchRecord createNewTransitDispatchRecord(TransitDispatchRecord newDispatch) throws TransitDispatchRecordExistException, UnknownPersistenceException {

        try {
            em.persist(newDispatch);
            em.flush();
            em.refresh(newDispatch);
            return newDispatch;
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new TransitDispatchRecordExistException("Transit Dispatch Record already exists!");
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    public TransitDispatchRecord retrieveTransitDispatchRecordByTransitDispatchRecordId(Long transitDispatchRecordId) throws TransitDispatchRecordNotFoundException {
        TransitDispatchRecord dispatchRecord = em.find(TransitDispatchRecord.class, transitDispatchRecordId);

        if (dispatchRecord != null) {
            return dispatchRecord;
        } else {
            throw new TransitDispatchRecordNotFoundException("Transit Dispatch Record ID " + transitDispatchRecordId + "does not exist!");
        }
    }

    @Override
    public List<TransitDispatchRecord> retrieveCurrentDayTransitDispatchRecords() {
        Date today = new Date();
        Query query = em.createQuery("SELECT tdr FROM TransitDispatchRecord tdr");
        List<TransitDispatchRecord> dispatchRecords = query.getResultList();

        for (TransitDispatchRecord tdr : dispatchRecords) {
            if (!tdr.getDispatchTime().equals(today)) {
                dispatchRecords.remove(tdr);
            }
        }
        return dispatchRecords;
    }

    @Override
    public void assignDriver(Long tdrId, Long employeeId) throws EmployeeNotFoundException, TransitDispatchRecordNotFoundException {
        Employee employee = employeeSessionBeanLocal.retrieveEmployeeByEmployeeId(employeeId);
        TransitDispatchRecord tdr = retrieveTransitDispatchRecordByTransitDispatchRecordId(tdrId);

    }

    @Override
    public void transitCompleted(Long dispatchId) throws TransitDispatchRecordNotFoundException {

        TransitDispatchRecord dispatchRecord = retrieveTransitDispatchRecordByTransitDispatchRecordId(dispatchId);
        dispatchRecord.setIsCompleted(true);
    }
}
