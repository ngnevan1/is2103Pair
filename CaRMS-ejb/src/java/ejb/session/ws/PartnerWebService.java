/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import entity.CarModel;
import entity.Customer;
import entity.Partner;
import entity.RentalRate;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CarModelNotFoundException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;

/**
 *
 * @author KMwong
 */
@WebService(serviceName = "PartnerWebService")
@Stateless()
public class PartnerWebService {

    @PersistenceContext(unitName = "CaRMS-ejbPU")
    private EntityManager em;
    
    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException, PartnerNotFoundException {
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
        /*
        em.detach(partner);
        
        for (Reservation reservation : partner.getReservations()) {
            em.detach(reservation);
            reservation.setPartner(null);
        }
        
        for (Customer customer : partner.getCustomers()) {
            em.detach(customer);
            customer.setPartner(null);
        }
        */
        return partner;
    }
    
    @WebMethod(operationName = "checkOutletIsOpen")
    public Boolean checkOutletIsOpen(@WebParam(name = "pickupDate") Date pickupDate, @WebParam(name = "pickupOutlet") String pickupOutlet, @WebParam(name = "returnDate") Date returnDate,  @WebParam(name = "returnOutlet") String returnOutlet) throws OutletNotFoundException {
        return outletSessionBeanLocal.checkOutletIsOpen(pickupDate, pickupOutlet, returnDate, returnOutlet);
    }
    
    @WebMethod(operationName = "searchAvailableCarModels")
    public List<CarModel> searchAvailableCarModels(@WebParam(name = "pickupDate") Date pickupDate, @WebParam(name = "pickupOutlet") String pickupOutlet, @WebParam(name = "returnDate") Date returnDate,  @WebParam(name = "returnOutlet") String returnOutlet) throws CarModelNotFoundException {
        return carModelSessionBeanLocal.searchAvailableCarModels(pickupDate, pickupOutlet, returnDate, returnOutlet);
    }
    
    @WebMethod(operationName = "calculateRentalRate")
    public BigDecimal calculateRentalRate(@WebParam(name = "rentalRates") List<RentalRate> rentalRates, @WebParam(name = "pickupDate") Date pickupDate, @WebParam(name = "returnDate") Date returnDate) {
        return rentalRateSessionBeanLocal.calculateRentalRate(rentalRates, pickupDate, returnDate);
    }

}
