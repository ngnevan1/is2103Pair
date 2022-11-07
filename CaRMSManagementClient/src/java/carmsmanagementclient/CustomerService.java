/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author nevanng
 */
public class CustomerService {
    private Employee currentEmployee;
    private CarSessionBeanRemote carSessionBeanRemote;
    private ReservationSessionBeanRemote reservationSessionBeanRemote;

    public CustomerService(Employee currentEmployee, CarSessionBeanRemote carSessionBeanRemote, ReservationSessionBeanRemote reservationSessionBeanRemote) {
        this.currentEmployee = currentEmployee;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.reservationSessionBeanRemote = reservationSessionBeanRemote;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doReturnCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
