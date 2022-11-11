/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Partner;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import util.exception.InvalidLoginCredentialException;
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
    
    @Override
    public Partner retrievePartnerByPartnerUsername(String username) throws PartnerNotFoundException {
        Query query = em.createQuery("SELECT p FROM Partner p WHERE p.username = :inUsername");
        query.setParameter("inUsername", username);
        
         try {
            Partner partner = (Partner) query.getSingleResult();
            if (partner != null) {
                partner.getReservations().size();
                partner.getCustomers().size();
            }
            return partner;
        }
        catch(NoResultException | NonUniqueResultException ex) {
            throw new PartnerNotFoundException("Partner Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Partner partnerLogin(String username, String password) throws InvalidLoginCredentialException, PartnerNotFoundException {
        Partner partner = retrievePartnerByPartnerUsername(username);
        if (partner.getPassword().equals(password)) {
            return partner;
        }
        else {
            throw new InvalidLoginCredentialException("Incorrect login credentials!");
        }
    }
    
}
