/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Local;
import util.exception.InvalidLoginCredentialException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Local
public interface PartnerSessionBeanLocal {
    public Partner createNewPartner(Partner newPartner) throws PartnerExistException, UnknownPersistenceException;
    public Partner retrievePartnerByPartnerId(Long partnerId, Boolean retrieveCustomers, Boolean retreiveReservations) throws PartnerNotFoundException;
    public Partner retrievePartnerByPartnerUsername(String username) throws PartnerNotFoundException;
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException, PartnerNotFoundException;
}
