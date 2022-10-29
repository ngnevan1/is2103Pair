/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import entity.CarCategory;
import entity.Employee;
import entity.RentalRate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.InvalidAccessRightException;
import util.exception.RentalRateExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
public class SalesManager {

    private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private Employee currentEmployee;

    public SalesManager() {
    }

    public SalesManager(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, Employee currentEmployee, CarCategorySessionBeanRemote carCategorySessionBeanRemote) {
        this.rentalRateSessionBeanRemote = rentalRateSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
    }
    
    

    public void menuSalesManager() throws InvalidAccessRightException {
        if (currentEmployee.getEmployeeAccessRights() != EmployeeAccessRightsEnum.SALES_MANAGER) {
            throw new InvalidAccessRightException("You don't have Sales MANAGER rights to access the sales management module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** CaRMS System :: Sales Management - Sales Manager ***\n");
            System.out.println("1: Create Rental Rate");
            System.out.println("2: View All Rental Rates");
            System.out.println("3: View Rental Rate Details");
            System.out.println("4: Update Rental Rate");
            System.out.println("5: Delete Rental Rate");
            System.out.println("-----------------------");
            System.out.println("6: Back\n");
            response = 0;

            while (response < 1 || response > 6) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewRentalRate();
                } else if (response == 2) {
                    doViewAllRentalRates();
                } else if (response == 3) {
                    doViewRentalRateDetails();
                } else if (response == 4) {
                    doUpdateRentalRate();
                } else if (response == 5) {
                    doDeleteRentalRate();
                } else if (response == 6) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 6) {
                break;
            }
        }

    }

    private void doCreateNewRentalRate() {
        
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        RentalRate newRentalRate = new RentalRate();
        
        try {
        System.out.println("*** CaRMS System :: Sales Management - Sales Manager :: Create New Rental Rate ***\n");
        System.out.print("Enter Name> ");
        newRentalRate.setRateName(scanner.nextLine().trim());
        System.out.print("Choose Car Category by entering Car Category ID> \n");
        System.out.printf("%8s%20s\n", "Car Category ID", "Car Category Name");
        for(CarCategory category:carCategories) {
            System.out.printf("%8s%20s\n", category.getCarCategoryId().toString(), category.getCategoryName());
        }
        CarCategory chosenCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(scanner.nextLong(), false, false, false);
        newRentalRate.setCarCategory(chosenCategory);
        System.out.print("Enter Rate Per Day> ");
        newRentalRate.setRatePerDay(scanner.nextBigDecimal());
        scanner.nextLine();
        System.out.print("Enter Rental Rate Start Date (DD/MM/YYYY)> ");
        newRentalRate.setRateStartDate(inputDateFormat.parse(scanner.nextLine().trim()));
        System.out.print("Enter Rental Rate End Date (DD/MM/YYYY)> ");
        newRentalRate.setRateEndDate(inputDateFormat.parse(scanner.nextLine().trim()));
        
       RentalRate createdRentalRate = rentalRateSessionBeanRemote.createNewRentalRate(newRentalRate);
        
            System.out.println("Rental Rate " + createdRentalRate.getRentalRateId() + " created successfully!");

        } catch (ParseException ex) {
            System.out.println("Invalid Date Input!\n");
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Invalid Car Category");
        } catch (RentalRateExistException | UnknownPersistenceException ex) {
            System.out.println("Unable to create Rental Rate!");
        }
        
    }

    private void doViewAllRentalRates() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewRentalRateDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateRentalRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteRentalRate() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
