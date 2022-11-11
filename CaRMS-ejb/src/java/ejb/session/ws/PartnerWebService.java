/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.CarCategorySessionBeanLocal;
import ejb.session.stateless.CarModelSessionBeanLocal;
import ejb.session.stateless.CustomerSessionBeanLocal;
import ejb.session.stateless.OutletSessionBeanLocal;
import ejb.session.stateless.PartnerSessionBeanLocal;
import ejb.session.stateless.RentalRateSessionBeanLocal;
import ejb.session.stateless.ReservationSessionBeanLocal;
import entity.Car;
import entity.CarCategory;
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
import util.exception.CarCategoryNotFoundException;
import util.exception.CarModelNotFoundException;
import util.exception.CustomerExistException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.PartnerNotFoundException;
import util.exception.RentalRateNotAvailableException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

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
    private CarCategorySessionBeanLocal carCategorySessionBeanLocal;
    @EJB
    private CarModelSessionBeanLocal carModelSessionBeanLocal;
    @EJB
    private CustomerSessionBeanLocal customerSessionBeanLocal;
    @EJB
    private OutletSessionBeanLocal outletSessionBeanLocal;
    @EJB
    private PartnerSessionBeanLocal partnerSessionBeanLocal;
    @EJB
    private ReservationSessionBeanLocal reservationSessionBeanLocal;
    @EJB
    private RentalRateSessionBeanLocal rentalRateSessionBeanLocal;
    
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "partnerLogin")
    public Partner partnerLogin(@WebParam(name = "username") String username, @WebParam(name = "password") String password) throws InvalidLoginCredentialException, PartnerNotFoundException {
        Partner partner = partnerSessionBeanLocal.partnerLogin(username, password);
        em.detach(partner);
        partner.setCustomers(null);
        partner.setReservations(null);
        return partner;
    }
    
    @WebMethod(operationName = "checkOutletIsOpen")
    public Boolean checkOutletIsOpen(@WebParam(name = "pickupDate") Date pickupDate, @WebParam(name = "pickupOutlet") String pickupOutlet, @WebParam(name = "returnDate") Date returnDate,  @WebParam(name = "returnOutlet") String returnOutlet) throws OutletNotFoundException {
        return outletSessionBeanLocal.checkOutletIsOpen(pickupDate, pickupOutlet, returnDate, returnOutlet);
    }
    
    @WebMethod(operationName = "searchAvailableCarModels")
    public List<CarModel> searchAvailableCarModels(@WebParam(name = "pickupDate") Date pickupDate, @WebParam(name = "pickupOutlet") String pickupOutlet, @WebParam(name = "returnDate") Date returnDate,  @WebParam(name = "returnOutlet") String returnOutlet) throws CarModelNotFoundException {
        List<CarModel> carModels = carModelSessionBeanLocal.searchAvailableCarModels(pickupDate, pickupOutlet, returnDate, returnOutlet);
        
        for (CarModel carModel : carModels) {
            em.detach(carModel);
            carModel.setCarCategory(null);
            
            for (Reservation reservation : carModel.getReservations()) {
                reservation.setCarModel(null);
            }
            
            for (Car car : carModel.getCars()) {
                car.setCarModel(null);
            }
        }
        return carModels;
    }

	@WebMethod(operationName = "searchAvailableCarCategories")
    public List<CarCategory> searchAvailableCarCategories(@WebParam(name = "pickupDate") Date pickupDate, @WebParam(name = "returnDate") Date returnDate) throws CarModelNotFoundException {
		
		List<CarCategory> categories = carCategorySessionBeanLocal.searchAvailableCarCategory(pickupDate, returnDate);
        
		for (CarCategory cc:categories) {
			// detach before nullifying relationships to prevent loop
			em.detach(cc);
			cc.setCarModels(null);
			cc.setRentalRates(null);
			cc.setReservations(null);
		}
		
        return categories;
    }
    
    @WebMethod(operationName = "calculateRentalRate")
    public List<RentalRate> calculateRentalRate(@WebParam(name = "rentalRates") List<RentalRate> rentalRates, @WebParam(name = "pickupDate") Date pickupDate, @WebParam(name = "returnDate") Date returnDate) throws RentalRateNotAvailableException {
        List<RentalRate> rates = rentalRateSessionBeanLocal.calculateRentalRate(rentalRates, pickupDate, returnDate);
        
        for (RentalRate rate : rates) {
            em.detach(rate);
            rate.setCarCategory(null);
        }
            
        return rates;
    }
    
    @WebMethod(operationName = "retrieveRentalRateByCarCategory")
    public List<RentalRate> retrieveRentalRateByCarCategory(@WebParam(name = "carCategory") CarCategory carCategory) {
        List<RentalRate> rates = rentalRateSessionBeanLocal.retrieveRentalRateByCarCategory(carCategory);
        
        for (RentalRate rate : rates) {
            em.detach(rate);
            rate.setCarCategory(null);
        }
        
        return rates;
    }
    
    @WebMethod(operationName = "retrieveCarCategoryByCategoryName")
    public CarCategory retrieveCarCategoryByCategoryName(@WebParam(name = "carCategoryName") String carCategoryName) throws CarCategoryNotFoundException {
        CarCategory carCategory = carCategorySessionBeanLocal.retrieveCarCategoryByCategoryName(carCategoryName);
        em.detach(carCategory);
		
        carCategory.setCarModels(null);
		carCategory.setRentalRates(null);
		carCategory.setReservations(null);
        
        return carCategory;
    }
    
    @WebMethod(operationName = "createNewCustomer")
    public Customer createNewCustomer(@WebParam(name = "newCustomer") Customer newCustomer) throws CustomerExistException, UnknownPersistenceException, InputDataValidationException {
        Customer customer = customerSessionBeanLocal.createNewCustomer(newCustomer);
        em.detach(customer);
        customer.setPartner(null);
		customer.setReservations(null);
//        
//        for (Reservation reservation : customer.getReservations()) {
//            reservation.setCustomer(null);
//        }
        
        return customer;
    }
    
    @WebMethod(operationName = "createNewReservationByCategory")
    public Reservation createNewReservationByCategory(@WebParam(name = "newReservation") Reservation newReservation, @WebParam(name = "newCustomer") Customer newCustomer, @WebParam(name = "partner") Partner partner,
            @WebParam(name = "carCategoryName") String carCategoryName, @WebParam(name = "pickupOutlet") String pickupOutlet, @WebParam(name = "returnOutlet") String returnOutlet) throws CarCategoryNotFoundException, OutletNotFoundException, CustomerNotFoundException, UnknownPersistenceException, InputDataValidationException {
        Reservation reservation = reservationSessionBeanLocal.createNewReservationByCategory(newReservation, newCustomer, partner, carCategoryName, pickupOutlet, returnOutlet);
        em.detach(reservation);
        reservation.setCar(null);
        reservation.setCarModel(null);
        reservation.setCarCategory(null);
        reservation.setPartner(null);
        reservation.setCustomer(null);
        reservation.setPickUpOutlet(null);
        reservation.setReturnOutlet(null);
        return reservation;
    }
    
    @WebMethod(operationName = "retrieveReservationsByPartnerUsername")
    public List<Reservation> retrieveReservationsByPartnerUsername(@WebParam(name = "username") String username){
        List<Reservation> reservations = reservationSessionBeanLocal.retrieveReservationsByPartnerUsername(username);
        for (Reservation reservation : reservations) {
            em.detach(reservation);
            reservation.setCar(null);
            reservation.setCarModel(null);
            reservation.setCarCategory(null);
            reservation.setPartner(null);
            reservation.setCustomer(null);
            reservation.setPickUpOutlet(null);
            reservation.setReturnOutlet(null);
        }
        return reservations;
    }
    
    @WebMethod(operationName = "retrieveReservationByReservationId")
    public Reservation retrieveReservationByReservationId(@WebParam(name = "reservationId") Long reservationId) throws ReservationNotFoundException{
        Reservation reservation = reservationSessionBeanLocal.retrieveReservationByReservationId(reservationId);
        em.detach(reservation);
        reservation.setCar(null);
        reservation.setCarModel(null);
        reservation.setCarCategory(null);
        reservation.setPartner(null);
        reservation.setCustomer(null);
        reservation.setPickUpOutlet(null);
        reservation.setReturnOutlet(null);
        return reservation;
    }
    
    @WebMethod(operationName = "calculateRefundPenalty")
    public BigDecimal calculateRefundPenalty(@WebParam(name = "reservation") Reservation reservation) throws ReservationNotFoundException{
        return reservationSessionBeanLocal.calculateRefundPenalty(reservation);
    }
    
    @WebMethod(operationName = "removeReservationByPartner")
    public Partner removeReservationByPartner(@WebParam(name = "reservationId") Long reservationId, @WebParam(name = "partner") Partner partner) throws ReservationNotFoundException{
        Partner currentPartner = reservationSessionBeanLocal.removeReservationByPartner(reservationId, partner);
        em.detach(currentPartner);
		currentPartner.setCustomers(null);
		currentPartner.setReservations(null);
        
//        for (Reservation reservation : currentPartner.getReservations()) {
//            reservation.setPartner(null);
//        }
//        
//        for (Customer customer : currentPartner.getCustomers()) {
//            customer.setPartner(null);
//        }

        return currentPartner;
    }
    
    @WebMethod(operationName = "checkCustomerExist")
    public Boolean checkCustomerExist(@WebParam(name = "email") String email) throws CustomerNotFoundException {
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerEmail(email);
        if (customer != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
    
    @WebMethod(operationName = "retrieveCustomerByCustomerEmail")
    public Customer retrieveCustomerByCustomerEmail(@WebParam(name = "email") String email) throws CustomerNotFoundException {
        Customer customer = customerSessionBeanLocal.retrieveCustomerByCustomerEmail(email);
        em.detach(customer);
        customer.setPartner(null);
        
        for (Reservation reservation : customer.getReservations()) {
            reservation.setCustomer(null);
        }
        
        return customer;
    }
    
}
