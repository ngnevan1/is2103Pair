/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Car;
import entity.Customer;
import entity.Employee;
import entity.Reservation;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Scanner;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.CarNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.ReservationNotFoundException;
import util.exception.UpdateCarException;

/**
 *
 * @author nevanng
 */
public class CustomerService {

    private Employee currentEmployee;
    private CarSessionBeanRemote carSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;
    private CustomerSessionBeanRemote customerSessionBeanRemote;

    public CustomerService(Employee currentEmployee, CarSessionBeanRemote carSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote, CustomerSessionBeanRemote customerSessionBeanRemote) {
        this.currentEmployee = currentEmployee;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
        this.customerSessionBeanRemote = customerSessionBeanRemote;
    }

    public void menuCustomerService() throws InvalidAccessRightException {
        if (currentEmployee.getEmployeeAccessRights() != EmployeeAccessRightsEnum.CS_EXECUTIVE) {
            throw new InvalidAccessRightException("You don't have Customer Service Executive rights to access the sales management module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS System :: Customer Service Module ***\n");
            System.out.println("1: Pickup Car");
            System.out.println("2: Return Car");
            System.out.println("-----------------------");
            System.out.println("3: Back\n");
            response = 0;

            while (response < 1 || response > 3) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doPickupCar();
                } else if (response == 2) {
                    doReturnCar();
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 4) {
                break;
            }
        }

    }

    private void doPickupCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS System :: Customer Service Module :: Pickup Car ***\n");
        try {
            System.out.print("Enter Customer Email> ");
            Customer customer = customerSessionBeanRemote.retrieveCustomerByCustomerEmail(scanner.nextLine().trim());

            Reservation reservation = reservationSessionBeanRemote.retrieveReservationsByCustomer(customer);
            if (reservation.getCreditCardNumber().isEmpty()) {
                BigDecimal paymentDue = reservation.getTotalAmount();
                System.out.println("Please pay amount outstanding: $" + paymentDue);

                System.out.print("Enter Credit Card Number> ");
                String ccNumber = scanner.nextLine().trim();
                reservation.setPaymentDate(new Date());
                System.out.print("Enter CVV> ");
                scanner.nextLine();
                reservation.setCreditCardNumber(ccNumber);

                System.out.println("Amount Paid: $" + paymentDue);
            }
            Car car = reservation.getCar();
            car.setCarStatus(CarStatusEnum.ONRENTAL);
            car.setOutlet(null);
            car.setCurrentReservation(reservation);
            carSessionBeanRemote.updateCar(car);
            reservationSessionBeanRemote.updateReservation(reservation);

            System.out.println("Customer has been allocated " + car.getColour() + " " + car.getCarModel().getMakeName() + " " + car.getCarModel().getModelName() + " with licence plate number: " + car.getLicensePlate());
            System.out.println("Car must be returned by " + reservation.getReservationEndDate());
        } catch (CustomerNotFoundException | ReservationNotFoundException | CarNotFoundException | UpdateCarException | InputDataValidationException ex) {
            System.out.println("An error has occurred: " + ex.getMessage());
        }
    }

    private void doReturnCar() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS System :: Customer Service Module :: Return Car ***\n");
        
        try {
            System.out.print("Enter Customer Email> ");
            Customer customer = customerSessionBeanRemote.retrieveCustomerByCustomerEmail(scanner.nextLine().trim());

            Reservation reservation = reservationSessionBeanRemote.retrieveReservationsByCustomer(customer);
            
            Car car = reservation.getCar();
            car.setCarStatus(CarStatusEnum.AVAILABLE);
            car.setOutlet(reservation.getReturnOutlet());
            car.setCurrentReservation(null);
            carSessionBeanRemote.updateCar(car);
            System.out.println("Car " + car.getLicensePlate() + "returned successfully!");

        } catch (CustomerNotFoundException | ReservationNotFoundException | CarNotFoundException | UpdateCarException | InputDataValidationException ex) {
            System.out.println("An error has occurred: " + ex.getMessage());
        }
    }

}
