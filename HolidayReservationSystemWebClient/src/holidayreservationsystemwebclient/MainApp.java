/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemwebclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.partner.CarCategory;
import ws.client.partner.CarCategoryNotFoundException_Exception;
import ws.client.partner.CarModel;
import ws.client.partner.CarModelNotFoundException_Exception;
import ws.client.partner.Customer;
import ws.client.partner.CustomerExistException_Exception;
import ws.client.partner.CustomerNotFoundException_Exception;
import ws.client.partner.InputDataValidationException_Exception;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.OutletNotFoundException_Exception;
import ws.client.partner.PartnerNotFoundException_Exception;
import ws.client.partner.Partner;
import ws.client.partner.RentalRate;
import ws.client.partner.RentalRateNotAvailableException_Exception;
import ws.client.partner.Reservation;
import ws.client.partner.ReservationNotFoundException_Exception;
import ws.client.partner.UnknownPersistenceException_Exception;


/**
 *
 * @author KMwong
 */

public class MainApp {
    
    Partner partner;
        
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
    
        while(true) {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Partner Search Car");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    try {
                        partnerLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                    } catch (InvalidLoginCredentialException_Exception | PartnerNotFoundException_Exception ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage());
                    }
                }
                else if(response == 2) {
                    partnerSearchCar();
                }
                else if (response == 3) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3) {
                break;
            }
        }
    }
    
    public void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Holiday Reservation System :: Main Menu ***\n");
            System.out.println("1: Partner Search Car");
            System.out.println("2: Partner View All Reservations");
            System.out.println("3: Partner View Reservation Details");
            System.out.println("4: Partner Cancel Reservation");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();

                if(response == 1) {
                    partnerSearchCar();
                }
                else if(response == 2) {
                    partnerViewAllReservations(true);
                }
                else if(response == 3) {
                    partnerViewReservationDetails();
                }
                else if(response == 4) {
                    partnerCancelReservation();
                }
                else if (response == 5) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5) {
                System.out.println("Logged Out Successfully!\n");
                partner = null;
                break;
            }
        }
    }
    
    public void partnerLogin() throws InvalidLoginCredentialException_Exception, PartnerNotFoundException_Exception {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner Login ***\n");
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        partner = partnerLogin(username, password);       
    }
    
    public void partnerSearchCar() {
        try {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y H");
            GregorianCalendar calendar = new GregorianCalendar();
            
            System.out.println("*** Holiday Reservation System :: Partner Search Car ***\n");
            System.out.print("Enter Pickup Date/Time (dd/MM/yyyy HH> ");
            Date pDate = inputDateFormat.parse(scanner.nextLine().trim());
            calendar.setTime(pDate);
            XMLGregorianCalendar pickupDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            System.out.print("Enter Pickup Outlet> ");
            String pickupOutlet = scanner.nextLine().trim();
            System.out.print("Enter Return Date/Time> ");
            Date rDate = inputDateFormat.parse(scanner.nextLine().trim());
            calendar.setTime(rDate);
            XMLGregorianCalendar returnDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            System.out.print("Enter Return Outlet> ");
            String returnOutlet = scanner.nextLine().trim();
            
            if (checkOutletIsOpen(pickupDate, pickupOutlet, returnDate, returnOutlet)) {
					List<CarCategory> availableCarCategories = searchAvailableCarCategories(pickupDate, returnDate);
                System.out.printf("%-15s%-15s\n", "Car Category", "Rental Rate");
            
                for(CarCategory category : availableCarCategories) {
                    List<RentalRate> rentalRates = retrieveRentalRateByCarCategory(category);
                    
                    BigDecimal totalAmount = new BigDecimal("0.00");
                    List<RentalRate> usedRates = calculateRentalRate(rentalRates, pickupDate, returnDate);
                    for (RentalRate rate : usedRates) {
                        totalAmount = totalAmount.add(rate.getRatePerDay());
                    }
                    System.out.printf("%-15s%-15s\n", category.getCategoryName(), totalAmount);
                }
				
//                List<CarModel> availableCarModels = searchAvailableCarModels(pickupDate, pickupOutlet, returnDate, returnOutlet);
//                System.out.printf("%-15s%-15s%-15s%-15s\n", "Car Category", "Car Make", "Car Model", "Rental Rate");
//                
//                for(CarModel carModel : availableCarModels) {
//                    List<RentalRate> rentalRates = retrieveRentalRateByCarCategory(carModel.getCarCategory());
//                    BigDecimal totalAmount = new BigDecimal("0.00");
//                    List<RentalRate> usedRates = calculateRentalRate(rentalRates, pickupDate, returnDate);
//                    for (RentalRate rate : usedRates) {
//                        totalAmount.add(rate.getRatePerDay());
//                    }
//                    System.out.printf("%-15s%-15s%-15s%-15s\n", carModel.getCarCategory().getCategoryName(), carModel.getMakeName(), carModel.getModelName(), totalAmount);
                
            
                System.out.println("------------------------");
                System.out.println("1: Make Reservation");
                System.out.println("2: Back\n");
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();
            
                if (partner != null) {
                    if (response == 1) {
                        System.out.print("Enter Car Category Name> ");
                        String carCategoryName = scanner.nextLine().trim();
			CarCategory reserveCarCategory = retrieveCarCategoryByCategoryName(carCategoryName);
                        reserveCarByCarCategory(carCategoryName, reserveCarCategory, pickupDate, returnDate, pickupOutlet, returnOutlet);
                    }
                }
                else {
                    System.out.println("Please login first before making a reservation!\n");
                }
            }
            else {
                System.out.println("Outlet is closed during Pickup or Return Time!");
            }
        } catch (ParseException | DatatypeConfigurationException ex) {
            System.out.println("Invalid date/time input!");
        } catch (OutletNotFoundException_Exception | CarModelNotFoundException_Exception | RentalRateNotAvailableException_Exception | CarCategoryNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void reserveCarByCarCategory(String carCategoryName, CarCategory reserveCarCategory, XMLGregorianCalendar pickupDate, XMLGregorianCalendar returnDate, String pickupOutlet, String returnOutlet) throws RentalRateNotAvailableException_Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            Date currentDate = new Date();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(currentDate);
            XMLGregorianCalendar today = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            Customer newCustomer = new Customer();
            
            System.out.println("*** Holiday Reservation System :: Partner Reserve Car ***\n");
            System.out.println("Enter Customer Details Below:\n");
            System.out.print("Enter Name> ");
            newCustomer.setName(scanner.nextLine().trim());
            System.out.print("Enter Passport Number> ");
            newCustomer.setPassportNumber(scanner.nextLine().trim());
            System.out.print("Enter Email> ");
            String email = scanner.nextLine().trim();
            newCustomer.setEmail(email);
            System.out.print("Enter Phone Number> ");
            newCustomer.setPhoneNumber(scanner.nextLine().trim());
            newCustomer.setPartner(partner);
            
			try {
				newCustomer = retrieveCustomerByCustomerEmail(email);
			} catch (CustomerNotFoundException_Exception ex) {
				newCustomer = createNewCustomer(newCustomer);
			}
//            if (checkCustomerExist(email)) {
//                newCustomer = retrieveCustomerByCustomerEmail(email);
//            }
//            else {
//                newCustomer = createNewCustomer(newCustomer);
//            }
            
            System.out.print("Enter Credit Card Number> ");
            String ccNumber = scanner.nextLine().trim();
            System.out.print("Do you want to do immediate payment? (Enter 'Y' to Pay)> ");
            String input = scanner.nextLine().trim();
            
            List<RentalRate> rentalRates = retrieveRentalRateByCarCategory(reserveCarCategory);
            
            BigDecimal totalAmount = new BigDecimal("0.00");
            List<RentalRate> usedRates = calculateRentalRate(rentalRates, pickupDate, returnDate);
            for (RentalRate rate : usedRates) {
                totalAmount = totalAmount.add(rate.getRatePerDay());
            }
            
            Reservation newReservation = new Reservation();
//            newReservation.setCarCategory(reserveCarCategory);
//            newReservation.setCustomer(newCustomer);
            newReservation.setReservationStartDate(pickupDate);
            newReservation.setReservationEndDate(returnDate);
            newReservation.setTotalAmount(totalAmount);
            newReservation.setCreditCardNumber(ccNumber);
            
            if(input.equals("Y")) {
                newReservation.setPaymentDate(today);
                System.out.print("Enter CVV> ");
                scanner.nextLine();
                System.out.println("Amount Paid: $" + totalAmount);
            }
            else {
                newReservation.setPaymentDate(pickupDate);
                System.out.println("Amount Due: $" + totalAmount + " During Pickup!\n");
            }
            
            newReservation = createNewReservationByCategory(newReservation, newCustomer, partner, carCategoryName, pickupOutlet, returnOutlet);
            System.out.println("Car reserved successfully!: " + newReservation.getReservationId() + "\n");
        
        } catch (CustomerExistException_Exception ex) {
            System.out.println("An error has occurred while registering the new customer: " + ex.getMessage() + "!\n");
        } catch (CarCategoryNotFoundException_Exception | CustomerNotFoundException_Exception | OutletNotFoundException_Exception | UnknownPersistenceException_Exception ex) {
            System.out.println(ex.getMessage() + "!\n");
        } catch (InputDataValidationException_Exception ex) {
            System.out.println(ex.getMessage() + "\n");
        } catch (DatatypeConfigurationException ex) {
            System.out.println("An unknown error has occurred while creating the reservation!");
        }
    }

    public void partnerViewAllReservations(Boolean viewOnly) {       
        if(viewOnly){
            System.out.println("*** Holiday Reservation System :: Partner View All Reservations ***\n");
        }
        
        List<Reservation> reservations = retrieveReservationsByPartnerUsername(partner.getUsername());
        for(Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getReservationId());
        }
		System.out.println();
    }
    
    public void partnerViewReservationDetails() {
        try {
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            System.out.println("*** Holiday Reservation System :: Partner View Reservation Details ***\n");
            partnerViewAllReservations(false);
            System.out.print("Enter Reservation ID> ");
            Long reservationId = scanner.nextLong();
            scanner.nextLine();
            
            Reservation reservation = retrieveReservationByReservationId(reservationId);
            System.out.printf("%-5s%-25s%-25s%-25s%-10s\n", "ID", "Start Date", "End Date", "Payment Date", "Total Amount");
            System.out.printf("%-5s%-25s%-25s%-25s%-10s\n", reservation.getReservationId(), outputDateFormat.format(reservation.getReservationStartDate().toGregorianCalendar().getTime()),
                outputDateFormat.format(reservation.getReservationEndDate().toGregorianCalendar().getTime()), outputDateFormat.format(reservation.getPaymentDate().toGregorianCalendar().getTime()), reservation.getTotalAmount());
        } catch (ReservationNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void partnerCancelReservation() {
        try {
            Scanner scanner = new Scanner(System.in);
            Date currentDate = new Date();
            GregorianCalendar calendar = new GregorianCalendar();
            calendar.setTime(currentDate);
            XMLGregorianCalendar today = DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar);
            
            System.out.println("*** Holiday Reservation System :: Partner Cancel Reservation ***\n");
            partnerViewAllReservations(false);
            System.out.print("Enter Reservation ID> ");
            Long reservationId = scanner.nextLong();
            scanner.nextLine();
            
            Reservation reservation = retrieveReservationByReservationId(reservationId);
            GregorianCalendar calendarPayment = reservation.getPaymentDate().toGregorianCalendar();
                        
            if(calendarPayment.compareTo(calendar) == -1) {
                BigDecimal refundAmount = calculateRefundPenalty(reservation);
                System.out.println("Refunded Amount After Penalty Fee (if any): $ " + refundAmount);
                System.out.println("Refund Successful!\n");
            }
            else {
                BigDecimal penaltyAmount = calculateRefundPenalty(reservation);
                System.out.println("Penalty Fee: $ " + penaltyAmount + " charged to " + reservation.getCreditCardNumber());
                System.out.print("Enter CVV (For Verification)> ");
                scanner.nextLine();
                System.out.println("Payment Successful!\n");
            }
            
            partner = removeReservationByPartner(reservationId, partner);   // Ensures local copy is synchronous
        } catch (ReservationNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        } catch (DatatypeConfigurationException ex) {
            System.out.println("An unknown error has occurred while cancelling the reservation!");
        }
    }
    
    private static ws.client.partner.Partner partnerLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception, PartnerNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.partnerLogin(username, password);
    }

    private static Boolean checkOutletIsOpen(javax.xml.datatype.XMLGregorianCalendar pickupDate, java.lang.String pickupOutlet, javax.xml.datatype.XMLGregorianCalendar returnDate, java.lang.String returnOutlet) throws OutletNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        // XMLGregorianCalendar calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(pickupDate.getYear(), pickupDate.getMonth(), pickupDate.getDay(), pickupDate.getHours(), 0, 0);
        return port.checkOutletIsOpen(pickupDate, pickupOutlet, returnDate, returnOutlet);
    }

    private static java.util.List<ws.client.partner.CarModel> searchAvailableCarModels(javax.xml.datatype.XMLGregorianCalendar pickupDate, java.lang.String pickupOutlet, javax.xml.datatype.XMLGregorianCalendar returnDate, java.lang.String returnOutlet) throws CarModelNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.searchAvailableCarModels(pickupDate, pickupOutlet, returnDate, returnOutlet);
    }
	
	private static java.util.List<ws.client.partner.CarCategory> searchAvailableCarCategories(javax.xml.datatype.XMLGregorianCalendar pickupDate, javax.xml.datatype.XMLGregorianCalendar returnDate) throws CarModelNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.searchAvailableCarCategories(pickupDate, returnDate);
    }
	
	
    
    private static java.util.List<ws.client.partner.RentalRate> retrieveRentalRateByCarCategory(ws.client.partner.CarCategory carCategory) {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveRentalRateByCarCategory(carCategory);
    }
    
    private static java.util.List<ws.client.partner.RentalRate> calculateRentalRate(java.util.List<ws.client.partner.RentalRate> rentalRates, javax.xml.datatype.XMLGregorianCalendar pickupDate, javax.xml.datatype.XMLGregorianCalendar returnDate) throws RentalRateNotAvailableException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.calculateRentalRate(rentalRates, pickupDate, returnDate);
    }
    
    private static ws.client.partner.CarCategory retrieveCarCategoryByCategoryName(java.lang.String carCategoryName) throws CarCategoryNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveCarCategoryByCategoryName(carCategoryName);
    }
    
    private static ws.client.partner.Customer createNewCustomer(ws.client.partner.Customer newCustomer) throws CustomerExistException_Exception, InputDataValidationException_Exception, UnknownPersistenceException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.createNewCustomer(newCustomer);
    }
    
    private static ws.client.partner.Reservation createNewReservationByCategory(ws.client.partner.Reservation newReservation, ws.client.partner.Customer newCustomer, ws.client.partner.Partner partner, java.lang.String carCategoryName, java.lang.String pickupOutlet, java.lang.String returnOutlet) 
            throws CarCategoryNotFoundException_Exception, CustomerNotFoundException_Exception, InputDataValidationException_Exception, OutletNotFoundException_Exception, UnknownPersistenceException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.createNewReservationByCategory(newReservation, newCustomer, partner, carCategoryName, pickupOutlet, returnOutlet);
    }
    
    private static java.util.List<ws.client.partner.Reservation> retrieveReservationsByPartnerUsername(java.lang.String username) {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveReservationsByPartnerUsername(username);
    }
    
    private static ws.client.partner.Reservation retrieveReservationByReservationId(Long reservationId) throws ReservationNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveReservationByReservationId(reservationId);
    }
    
    private static BigDecimal calculateRefundPenalty(ws.client.partner.Reservation reservation) throws ReservationNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.calculateRefundPenalty(reservation);
    }
    
    private static ws.client.partner.Partner removeReservationByPartner(Long reservationId, ws.client.partner.Partner partner) throws ReservationNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.removeReservationByPartner(reservationId, partner);
    }
    
    private static Boolean checkCustomerExist(java.lang.String email) throws CustomerNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.checkCustomerExist(email);
    }
	
    private static ws.client.partner.Customer retrieveCustomerByCustomerEmail(java.lang.String email) throws CustomerNotFoundException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.retrieveCustomerByCustomerEmail(email);
    }

}
