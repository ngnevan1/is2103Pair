/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsreservationclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.CarCategory;
import entity.CarModel;
import entity.OwnCustomer;
import entity.RentalRate;
import entity.Reservation;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CarCategoryNotFoundException;
// import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.OutletNotFoundException;
import util.exception.OwnCustomerExistException;
import util.exception.OwnCustomerNotFoundException;
import util.exception.RentalRateNotAvailableException;
import util.exception.ReservationNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author KMwong
 */
public class MainApp {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    
    private OwnCustomer ownCustomer;

    
    public MainApp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public MainApp(CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, 
            CustomerSessionBeanRemote customerSessionBeanRemote, OutletSessionBeanRemote outletSessionBeanRemote, 
            ReservationSessionBeanRemote reservationSessionBeanRemote, RentalRateSessionBeanRemote rentalRateSessionBeanRemote) {
        this();
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
    }
    
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to CaRMS Reservation System ***\n");
            System.out.println("1: Register As Customer");
            System.out.println("2: Customer Login");
            System.out.println("3: Search Car");
            System.out.println("4: Exit\n");
            response = 0;
            
            while(response < 1 || response > 4) {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    registerCustomer();
                }
                else if(response == 2) {
                    try {
                        customerLogin();
                        System.out.println("Login successful!\n");
                        menuMain();
                    } catch (InvalidLoginCredentialException | OwnCustomerNotFoundException ex) {
                        System.out.println("Invalid login credential: " + ex.getMessage());
                    }
                }
                else if(response == 3) {
                    searchCar();
                }
                else if (response == 4) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 4) {
                break;
            }
        }
    }
    
    public void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to CaRMS Reservation System :: Main Menu ***\n");
            System.out.println("1: Search Car");
            System.out.println("2: View All Reservations");
            System.out.println("3: View Reservation Details");
            System.out.println("4: Cancel Reservation");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();

                if(response == 1) {
                    searchCar();
                }
                else if(response == 2) {
                    viewAllReservations(true);
                }
                else if(response == 3) {
                    viewReservationDetails();
                }
                else if(response == 4) {
                    cancelReservation();
                }
                else if (response == 5) {
                    System.out.println("Logged Out Successfully!\n");
                    ownCustomer = null;
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5) {
                System.out.println("Logged Out Successfully!\n");
                ownCustomer = null;
                break;
            }
        }
    }
    
    public void registerCustomer() {       
        Scanner scanner = new Scanner(System.in);
        OwnCustomer newOwnCustomer = new OwnCustomer();

        System.out.println("*** CaRMS Reservation System :: Register Customer ***\n");
        System.out.print("Enter Name> ");
        newOwnCustomer.setName(scanner.nextLine().trim());
        System.out.print("Enter Passport Number> ");
        newOwnCustomer.setPassportNumber(scanner.nextLine().trim());
        System.out.print("Enter Email> ");
        newOwnCustomer.setEmail(scanner.nextLine().trim());
        System.out.print("Enter Phone Number> ");
        newOwnCustomer.setPhoneNumber(scanner.nextLine().trim());
        System.out.print("Enter Username> ");
        newOwnCustomer.setUsername(scanner.nextLine().trim());
        System.out.print("Enter Password> ");
        newOwnCustomer.setPassword(scanner.nextLine().trim());
            
        Set<ConstraintViolation<OwnCustomer>>constraintViolations = validator.validate(newOwnCustomer);
        if(constraintViolations.isEmpty()) {
            try {
                newOwnCustomer = customerSessionBeanRemote.createNewOwnCustomer(newOwnCustomer);
                System.out.println("New Customer registered successfully!: " + newOwnCustomer.getUsername()+ "\n");
            }
            catch (OwnCustomerExistException ex) {
                System.out.println("An error has occurred while registering the new customer: " + ex.getMessage() + "!\n");
            }
            catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error has occurred while registering the new customer: " + ex.getMessage() + "!\n");
            } 
            catch(InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else {
            showInputDataValidationErrorsForOwnCustomer(constraintViolations);          
        }
    }
    
    public void customerLogin() throws InvalidLoginCredentialException, OwnCustomerNotFoundException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** CaRMS Reservation System :: Customer Login ***\n");
        System.out.print("Enter username> ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        String password = scanner.nextLine().trim();
        
        if(username.length() > 0 && password.length() > 0) {
            ownCustomer = customerSessionBeanRemote.ownCustomerLogin(username, password);      
        }
        else {
            throw new InvalidLoginCredentialException("Missing login credential!");
        }
    }
    
    public void searchCar() {
        try {
            Scanner scanner = new Scanner(System.in);
            Integer response = 0;
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y H");
            
            System.out.println("*** CaRMS Reservation System :: Search Car ***\n");
            System.out.print("Enter Pickup Date/Time (dd/MM/yyyy HH)> ");
            Date pickupDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Pickup Outlet> ");
            String pickupOutlet = scanner.nextLine().trim();
            System.out.print("Enter Return Date/Time (dd/MM/yyyy HH)> ");
            Date returnDate = inputDateFormat.parse(scanner.nextLine().trim());
            System.out.print("Enter Return Outlet (leave blank if same as pickup outlet> ");
            String input = scanner.nextLine().trim();
			String returnOutlet;
			if (input.length() > 0) {
				returnOutlet = input;	
			} else {
				returnOutlet = pickupOutlet;
			}
            
            if (outletSessionBeanRemote.checkOutletIsOpen(pickupDate, pickupOutlet, returnDate, returnOutlet)) {
                List<CarModel> availableCarModels = carModelSessionBeanRemote.searchAvailableCarModels(pickupDate, pickupOutlet, returnDate, returnOutlet);
                System.out.printf("%-15s%-15s%-15s%-15s\n", "Car Category", "Car Make", "Car Model", "Rental Rate");
            
                for(CarModel carModel : availableCarModels) {
                    List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveRentalRateByCarCategory(carModel.getCarCategory());
                    BigDecimal totalAmount = new BigDecimal("0.0");
                    List<RentalRate> usedRates = rentalRateSessionBeanRemote.calculateRentalRate(rentalRates, pickupDate, returnDate);
                    for (RentalRate rate : usedRates) {
                        totalAmount.add(rate.getRatePerDay());
                    }
                    System.out.printf("%-15s%-15s%-15s%-15s\n", carModel.getCarCategory().getCategoryName(), carModel.getMakeName(), carModel.getModelName(), totalAmount);
                }
                
                System.out.println("------------------------");
                System.out.println("1: Make Reservation");
                // System.out.println("2: Make Reservation By Car Model");
                System.out.println("2: Back\n");
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();
                
                if (ownCustomer != null) {
                    if (response == 1) {
                        System.out.print("Enter Car Category Name> ");
                        String carCategoryName = scanner.nextLine().trim();
			CarCategory reserveCarCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCategoryName(carCategoryName);
                        reserveCarByCarCategory(carCategoryName, reserveCarCategory, pickupDate, returnDate, pickupOutlet, returnOutlet);
                    }
                    /*
                    else if (response == 2) {
                        System.out.print("Enter Car Model Name> ");
                        String carModelName = scanner.nextLine().trim();
                        reserveCarByCarModel(carModelName, pickupDate, returnDate, pickupOutlet, returnOutlet);
                    }
                    */
                }
                else {
                    System.out.println("Please login first before making a reservation!\n");
                }
            }
            else {
                System.out.println("Outlet is closed during Pickup or Return Time!\n");
            }
        } catch (ParseException ex) {
            System.out.println("Invalid date/time input!\n");
        } catch (OutletNotFoundException /* | CarModelNotFoundException */ | CarCategoryNotFoundException | RentalRateNotAvailableException ex) {
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
            
        CarModel reserveCarModel = carModelSessionBeanRemote.retrieveCarModelByModelName(carModelName);
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveRentalRateByCarCategory(reserveCarModel.getCarCategory());
		
        BigDecimal rentalRate = rentalRateSessionBeanRemote.calculateRentalRate(rentalRates, pickupDate, returnDate);
            
        Reservation newReservation = new Reservation();
        newReservation.setCarModel(reserveCarModel);
        newReservation.setCarCategory(reserveCarModel.getCarCategory());
        newReservation.setReservationStartDate(pickupDate);
        newReservation.setReservationEndDate(returnDate);
        newReservation.setTotalAmount(rentalRate);
            
        if(input.equals("Y")) {
            newReservation.setPaymentDate(new Date());
            System.out.print("Enter CVV> ");
            String ccCVV = scanner.nextLine().trim();
                
            newReservation.setCreditCardNumber(ccNumber);
                
            System.out.println("Amount Paid: $" + rentalRate);
        }
        else {
			newReservation.setCreditCardNumber(ccNumber);
            newReservation.setPaymentDate(pickupDate);
            System.out.println("Amount Due: $" + rentalRate + " During Pickup!");
        }
            
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(newReservation);
        if(constraintViolations.isEmpty()) {
            try {
                newReservation = reservationSessionBeanRemote.createNewReservationByModel(newReservation, ownCustomer, carModelName, pickupOutlet, returnOutlet);
                System.out.println("Car reserved successfully!: " + newReservation.getReservationId() + "\n");
            } catch (CarCategoryNotFoundException | CarModelNotFoundException | OutletNotFoundException | OwnCustomerNotFoundException ex) {
                System.out.println("An error had occurred while making a reservation: " + ex.getMessage() + "!\n");
            } catch (UnknownPersistenceException ex) {
                System.out.println("An unknown error had occurred while making a reservation: " + ex.getMessage() + "!\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else {
            showInputDataValidationErrorsForReservation(constraintViolations);          
        }
    }


	//TODO optimise    
    public void reserveCarByCarCategory(String carCategoryName, CarCategory reserveCarCategory, Date pickupDate, Date returnDate, String pickupOutlet, String returnOutlet) throws CarCategoryNotFoundException, RentalRateNotAvailableException {
        Scanner scanner = new Scanner(System.in);
            
        System.out.println("*** CaRMS Reservation System :: Reserve Car ***\n");
        System.out.print("Enter Credit Card Number> ");
        String ccNumber = scanner.nextLine().trim();
        System.out.print("Do you want to do immediate payment? (Enter 'Y' to Pay)> ");
        String input = scanner.nextLine().trim();
        
        List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveRentalRateByCarCategory(reserveCarCategory);	
        // For Testing to check if rates returned are correct
        for (RentalRate rate : rentalRates) {
            System.out.println(rate);
        }
        // Testing Ends
        
        BigDecimal totalAmount = new BigDecimal("0.00");
        List<RentalRate> usedRates = rentalRateSessionBeanRemote.calculateRentalRate(rentalRates, pickupDate, returnDate);
        for (RentalRate rate : usedRates) {
            totalAmount.add(rate.getRatePerDay());
        }
        
        Reservation newReservation = new Reservation();
        newReservation.setCarCategory(reserveCarCategory);
        newReservation.setReservationStartDate(pickupDate);
        newReservation.setReservationEndDate(returnDate);
        newReservation.setTotalAmount(totalAmount);
        newReservation.setCreditCardNumber(ccNumber);
            
        if(input.equals("Y")) {
            newReservation.setPaymentDate(new Date());
            System.out.print("Enter CVV> ");
            scanner.nextLine();
            System.out.println("Amount Paid: $" + totalAmount);
        }
        else {
			newReservation.setCreditCardNumber(ccNumber);
            newReservation.setPaymentDate(pickupDate);
            System.out.println("Amount Due: $" + totalAmount + " During Pickup!\n");
        }
            
        Set<ConstraintViolation<Reservation>>constraintViolations = validator.validate(newReservation);
        if(constraintViolations.isEmpty()) {
            try {
                newReservation = reservationSessionBeanRemote.createNewReservationByCategory(newReservation, ownCustomer, carCategoryName, pickupOutlet, returnOutlet);
                System.out.println("Car reserved successfully!: " + newReservation.getReservationId() + "\n");
            } catch (CarCategoryNotFoundException | OutletNotFoundException | OwnCustomerNotFoundException | UnknownPersistenceException ex) {
                System.out.println("An error had occurred while making a reservation: " + ex.getMessage() + "!\n");
            } catch (InputDataValidationException ex) {
                System.out.println(ex.getMessage() + "\n");
            }
        }
        else {
            showInputDataValidationErrorsForReservation(constraintViolations);          
        }   
    }
    
    public void viewAllReservations(Boolean viewOnly) {       
        if(viewOnly){
            System.out.println("*** CaRMS Reservation System :: View All Reservations ***\n");
        }
        
        List<Reservation> reservations = reservationSessionBeanRemote.retrieveReservationsByCustomerEmail(ownCustomer.getEmail());
        for(Reservation reservation : reservations) {
            System.out.println("Reservation ID: " + reservation.getReservationId());
        }
    }
    
    public void viewReservationDetails() {
        try {
            Scanner scanner = new Scanner(System.in);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            
            System.out.println("*** CaRMS Reservation System :: View Reservation Details ***\n");
            viewAllReservations(false);
            System.out.print("Enter Reservation ID> ");
            Long reservationId = scanner.nextLong();
            scanner.nextLine();
            
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationByReservationId(reservationId);
            System.out.printf("%-5s%-25s%-25s%-25s\n", "ID", "Start Date", "End Date", "Payment Date", "Total Amount");
            System.out.printf("%-5s%-25s%-25s%-25s\n", reservation.getReservationId(), outputDateFormat.format(reservation.getReservationStartDate()),
                outputDateFormat.format(reservation.getReservationEndDate()), outputDateFormat.format(reservation.getPaymentDate()), reservation.getTotalAmount());
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public void cancelReservation() {
        try {
            Scanner scanner = new Scanner(System.in);
            
            System.out.println("*** CaRMS Reservation System :: Cancel Reservation ***\n");
            viewAllReservations(false);
            System.out.print("Enter Reservation ID> ");
            Long reservationId = scanner.nextLong();
            scanner.nextLine();
            
            Reservation reservation = reservationSessionBeanRemote.retrieveReservationByReservationId(reservationId);
            if(reservation.getPaymentDate().before(new Date())) {
                BigDecimal refundAmount = reservationSessionBeanRemote.calculateRefundPenalty(reservation);
                System.out.println("Refunded Amount After Penalty Fee (if any): $ " + refundAmount);
                System.out.println("Refund Successful!\n");
            }
            else {
                BigDecimal penaltyAmount = reservationSessionBeanRemote.calculateRefundPenalty(reservation);
                System.out.println("Penalty Fee: $ " + penaltyAmount + " charged to " + reservation.getCreditCardNumber());
                System.out.print("Enter CVV (For Verification)> ");
                scanner.nextLine();
                System.out.println("Payment Successful!\n");
            }
            
            ownCustomer = reservationSessionBeanRemote.removeReservationByOwnCustomer(reservationId, ownCustomer);   // Ensures local copy is synchronous
        } catch (ReservationNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    private void showInputDataValidationErrorsForOwnCustomer(Set<ConstraintViolation<OwnCustomer>>constraintViolations) {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForReservation(Set<ConstraintViolation<Reservation>>constraintViolations) {
        System.out.println("\nInput data validation error!:");
            
        for(ConstraintViolation constraintViolation:constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
