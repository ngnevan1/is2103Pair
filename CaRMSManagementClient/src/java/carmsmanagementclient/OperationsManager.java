/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import entity.Employee;
import java.util.Scanner;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.InvalidAccessRightException;

/**
 *
 * @author nevanng
 */
public class OperationsManager {
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private Employee currentEmployee;

    public OperationsManager() {
    }

    public OperationsManager(CarModelSessionBeanRemote carModelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, Employee currentEmployee) {
        this();
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }
    
    public void menuOperationsManager() throws InvalidAccessRightException {
        if(currentEmployee.getEmployeeAccessRights() != EmployeeAccessRightsEnum.OPERATIONS_MANAGER)
        {
            throw new InvalidAccessRightException("You don't have OPERATIONS MANAGER rights to access the sales management module.");
        }
        
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true)
        {
            System.out.println("*** CaRMS System :: Sales Management - Operations Manager ***\n");
            System.out.println("1: Create New Model");
            System.out.println("2: View All Models");
            System.out.println("3: Update Model");
            System.out.println("4: Delete Model");
            System.out.println("-----------------------");
            System.out.println("5: Create New Car");
            System.out.println("6: View All Cars");
            System.out.println("7: View Car Details");
            System.out.println("8: Update Car");
            System.out.println("9: Delete Car");
            System.out.println("-----------------------");
            System.out.println("10: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("11: Assign Transit Driver");
            System.out.println("12: Update Transit As Completed");
            System.out.println("-----------------------");
            System.out.println("13: Back\n");
            response = 0;
            
            while(response < 1 || response > 13)
            {
                System.out.print("> ");

                response = scanner.nextInt();

                if(response == 1)
                {
                    doCreateNewModel();
                }
                else if(response == 2)
                {
                    doViewAllModels();
                }
                else if(response == 3)
                {
                    doUpdateModel();
                }
                else if(response == 4)
                {
                    doDeleteModel();
                }
                else if(response == 5)
                {
                    doCreateNewCar();
                }
                else if(response == 6)
                {
                    doViewAllCars();
                }
                else if (response == 7)
                {
                    doViewCarDetails();
                }
                else if (response == 8)
                {
                    doUpdateCar();
                }
                else if (response == 9)
                {
                    doDeleteCar();
                }
                else if (response == 10)
                {
                    doViewDispatchRecords();
                }
                else if (response == 11)
                {
                    doAssignTransitDriver();
                }
                else if (response == 12)
                {
                    doUpdateTransitAsCompleted();
                }
                else if (response == 13) 
                {
                    break;
                }
                {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 13)
            {
                break;
            }
        }
    }

    private void doCreateNewModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllModels() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteModel() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doCreateNewCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewAllCars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewCarDetails() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doDeleteCar() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doViewDispatchRecords() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doAssignTransitDriver() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void doUpdateTransitAsCompleted() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
