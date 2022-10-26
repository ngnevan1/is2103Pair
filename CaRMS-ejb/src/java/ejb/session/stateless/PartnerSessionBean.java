/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.PartnerExistException;
import util.exception.PartnerNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Stateless
public class PartnerSessionBean implements PartnerSessionBeanRemote, PartnerSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    @Override
    public Partner createNewPartner(Partner newPartner) throws PartnerExistException, UnknownPersistenceException {
        try {
            em.persist(newPartner);
            em.flush();
            em.refresh(newPartner);
            return newPartner;
        }
        catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new PartnerExistException("Partner already exists!");
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
    public Partner retrievePartnerByPartnerId(Long partnerId, Boolean retrieveCustomers, Boolean retreiveReservations) throws PartnerNotFoundException {
        Partner partner = em.find(Partner.class, partnerId);
        
        if(partner != null) {
            if (retrieveCustomers) {
                partner.getCustomers().size();
            }
            if (retreiveReservations) {
                partner.getReservations().size();
            }
            
            return partner;
        }
        else {
            throw new PartnerNotFoundException("Partner ID " + partnerId + "does not exist!");
        }
    }
}
