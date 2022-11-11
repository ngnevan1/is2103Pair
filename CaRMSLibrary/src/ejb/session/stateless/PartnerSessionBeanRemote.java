/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Remote;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Remote
public interface PartnerSessionBeanRemote {
    public Partner createNewPartner(Partner newPartner) throws PartnerExistException, UnknownPersistenceException;
    public Partner retrievePartnerByPartnerId(Long partnerId, Boolean retrieveCustomers, Boolean retreiveReservations) throws PartnerNotFoundException;
    public Partner retrievePartnerByPartnerUsername(String username) throws PartnerNotFoundException;
}
