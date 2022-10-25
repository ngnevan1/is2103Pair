/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.RentalRate;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
@Stateless
public class RentalRateSessionBean implements RentalRateSessionBeanRemote, RentalRateSessionBeanLocal {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public RentalRateSessionBean() {
    }
    
    @Override
    public RentalRate createNewRentalRate(RentalRate newRentalRate) throws RentalRateExistException, UnknownPersistenceException {
        try {
            em.persist(newRentalRate);
            em.flush();
            em.refresh(newRentalRate);
            return newRentalRate;
        }
        catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new RentalRateExistException("Rental Rate already exists!");
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
    public RentalRate retrieveRentalRateByRentalRateId(Long rentalRateId, Boolean retrieveCarCategory) throws RentalRateNotFoundException {
        RentalRate rentalRate = em.find(RentalRate.class, rentalRateId);
        
        if(rentalRate != null) {
            if (retrieveCarCategory) {
                rentalRate.getCarCategory();
            }
            return rentalRate;
        }
        else {
            throw new RentalRateNotFoundException("Rental Rate ID " + rentalRateId + "does not exist!");
        }
    }
}
