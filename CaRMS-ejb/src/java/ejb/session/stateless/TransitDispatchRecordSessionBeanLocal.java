/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDispatchRecord;
import javax.ejb.Local;
import util.exception.OutletNotFoundException;
import util.exception.TransitDispatchRecordExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Local
public interface TransitDispatchRecordSessionBeanLocal {
    public TransitDispatchRecord createNewTransitDispatchRecord(TransitDispatchRecord newDispatch) throws TransitDispatchRecordExistException, OutletNotFoundException, UnknownPersistenceException;
}
