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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.partner.CarModel;
import ws.client.partner.CarModelNotFoundException_Exception;
import ws.client.partner.InvalidLoginCredentialException;
import ws.client.partner.InvalidLoginCredentialException_Exception;
import ws.client.partner.OutletNotFoundException_Exception;
import ws.client.partner.Partner;
import ws.client.partner.PartnerNotFoundException;
import ws.client.partner.PartnerNotFoundException_Exception;
// import ws.client.partner.Partner;
import ws.client.partner.PartnerWebService_Service;
import ws.client.partner.PartnerWebService;
import ws.client.partner.RentalRate;
import ws.client.partner.RentalRateNotAvailableException_Exception;


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
                    partnerViewAllReservations();
                }
                else if(response == 3) {
                    partnerViewReservationDetails();
                }
                else if(response == 4) {
                    partnerCancelReservation();
                }
                else if (response == 5) {
                    System.out.println("Logged Out Successfully!\n");
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5) {
                System.out.println("Logged Out Successfully!\n");
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
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y H");
            GregorianCalendar calendar = new GregorianCalendar();
            
            System.out.println("*** Holiday Reservation System :: Partner Search Car ***\n");
            System.out.println("*** CaRMS Reservation System :: Search Car ***\n");
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
                List<CarModel> availableCarModels = searchAvailableCarModels(pickupDate, pickupOutlet, returnDate, returnOutlet);
                System.out.printf("%-15s%-15s%-15s%-15s\n", "Car Category", "Car Make", "Car Model", "Rental Rate");
                
                // WIP
                /* 
                for(CarModel carModel : availableCarModels) {
                    List<RentalRate> rentalRates = retrieveRentalRateByCarCategory(carModel.getCarCategory());
                    BigDecimal rentalRate = rentalRateSessionBeanRemote.calculateRentalRate(rentalRates, pickupDate, returnDate);
                    System.out.printf("%15s%15s%15s", carModel.getCarCategory().getCategoryName(), carModel.getMakeName(), carModel.getModelName(), rentalRate);
                }
            
                System.out.println("------------------------");
                System.out.println("1: Make Reservation By Car Category");
                System.out.println("2: Make Reservation By Car Model");
                System.out.println("3: Back\n");
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();
            
                if (partner != null) {
                    if (response == 1) {
                        System.out.print("Enter Car Category Name> ");
                        String carCategoryName = scanner.nextLine().trim();
                        reserveCarByCarCategory(carCategoryName, pickupDate, returnDate, pickupOutlet, returnOutlet);
                    }
                    else if (response == 2) {
                        System.out.print("Enter Car Model Name> ");
                        String carModelName = scanner.nextLine().trim();
                        reserveCarByCarModel(carModelName, pickupDate, returnDate, pickupOutlet, returnOutlet);
                    }
                }
                else {
                    System.out.println("Please login first before making a reservation!\n");
                }
                */
            }
            else {
                System.out.println("Outlet is closed during Pickup or Return Time!");
            }
        } catch (ParseException | DatatypeConfigurationException ex) {
            System.out.println("Invalid date/time input!");
        } catch (OutletNotFoundException_Exception | CarModelNotFoundException_Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    /*
    public void reserveCarByCarModel(String carModelName, Date pickupDate, Date returnDate, String pickupOutlet, String returnOutlet) throws CarModelNotFoundException {
        Scanner scanner = new Scanner(System.in);
            
        System.out.println("*** CaRMS Reservation System :: Reserve Car ***\n");
        System.out.print("Enter Credit Card Number> ");
        String ccNumber = scanner.nextLine().trim();
        System.out.print("Do you want to do immediate payment? (Enter 'Y' to Pay)> ");
        String input = scanner.nextLine().trim();
    
    }
    
    public void reserveCarByCarCategory(String carCategoryName, Date pickupDate, Date returnDate, String pickupOutlet, String returnOutlet) throws CarCategoryNotFoundException {
        Scanner scanner = new Scanner(System.in);
            
        System.out.println("*** CaRMS Reservation System :: Reserve Car ***\n");
        System.out.print("Enter Credit Card Number> ");
        String ccNumber = scanner.nextLine().trim();
        System.out.print("Do you want to do immediate payment? (Enter 'Y' to Pay)> ");
        String input = scanner.nextLine().trim();
    
    }
    */
    public void partnerViewAllReservations() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner View All Reservations ***\n");
    }
    
    public void partnerViewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner View Reservation Details ***\n");
    }
    
    public void partnerCancelReservation() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner Cancel Reservation ***\n");
    }
    
    private static Partner partnerLogin(java.lang.String username, java.lang.String password) throws InvalidLoginCredentialException_Exception, PartnerNotFoundException_Exception {
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
    
    private static java.util.List<ws.client.partner.RentalRate> calculateRentalRate(java.util.List<ws.client.partner.RentalRate> rentalRates, javax.xml.datatype.XMLGregorianCalendar pickupDate, javax.xml.datatype.XMLGregorianCalendar returnDate) throws RentalRateNotAvailableException_Exception {
        ws.client.partner.PartnerWebService_Service service = new ws.client.partner.PartnerWebService_Service();
        ws.client.partner.PartnerWebService port = service.getPartnerWebServicePort();
        return port.calculateRentalRate(rentalRates, pickupDate, returnDate);
    }

}
