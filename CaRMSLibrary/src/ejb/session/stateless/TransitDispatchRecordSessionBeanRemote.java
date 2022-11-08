/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.TransitDispatchRecord;
import java.util.List;
import javax.ejb.Remote;
import util.exception.TransitDispatchRecordNotFoundException;

/**
 *
 * @author nevanng
 */
@Remote
public interface TransitDispatchRecordSessionBeanRemote {
    public List<TransitDispatchRecord> retrieveCurrentDayTransitDispatchRecords();
    public void transitCompleted(Long dispatchId) throws TransitDispatchRecordNotFoundException;
}
