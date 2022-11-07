/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarExistException;
import util.exception.CarModelExistException;
import util.exception.CarModelNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
public class OperationsManager {

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
    private CarModelSessionBeanRemote carModelSessionBeanRemote;
    private CarSessionBeanRemote carSessionBeanRemote;
    private Employee currentEmployee;

    public OperationsManager() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationsManager(CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, Employee currentEmployee) {
        this();
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.currentEmployee = currentEmployee;
    }

    public void menuOperationsManager() throws InvalidAccessRightException {
        if (currentEmployee.getEmployeeAccessRights() != EmployeeAccessRightsEnum.OPERATIONS_MANAGER) {
            throw new InvalidAccessRightException("You don't have OPERATIONS MANAGER rights to access the sales management module.");
        }

        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
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

            while (response < 1 || response > 13) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    doCreateNewModel();
                } else if (response == 2) {
                    doViewAllModels();
                } else if (response == 3) {
                    doUpdateModel();
                } else if (response == 4) {
                    doDeleteModel();
                } else if (response == 5) {
                    doCreateNewCar();
                } else if (response == 6) {
                    doViewAllCars();
                } else if (response == 7) {
                    doViewCarDetails();
                } else if (response == 8) {
                    doUpdateCar();
                } else if (response == 9) {
                    doDeleteCar();
                } else if (response == 10) {
                    doViewDispatchRecords();
                } else if (response == 11) {
                    doAssignTransitDriver();
                } else if (response == 12) {
                    doUpdateTransitAsCompleted();
                } else if (response == 13) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 13) {
                break;
            }
        }
    }

    private void doCreateNewModel() {
        Scanner scanner = new Scanner(System.in);
        List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        CarModel newCarModel = new CarModel();

        try {
            System.out.println("*** CaRMS System :: Sales Management - Operations Manager :: Create New Car Model ***\n");
            System.out.print("Enter Model Name> ");
            newCarModel.setModelName(scanner.nextLine().trim());
            System.out.print("Enter Make Name> ");
            newCarModel.setMakeName(scanner.nextLine().trim());
            System.out.printf("%8s%20s\n", "Car Category ID", "Car Category Name");
            for (CarCategory category : carCategories) {
                System.out.printf("%8s%20s\n", category.getCarCategoryId().toString(), category.getCategoryName());
            }
            System.out.print("Enter Car Category ID> ");
            CarCategory chosenCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(scanner.nextLong(), false, false);
            newCarModel.setCarCategory(chosenCategory);
            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(newCarModel);

            if (constraintViolations.isEmpty()) {
                CarModel createdCarModel = carModelSessionBeanRemote.createNewCarModel(newCarModel);
                System.out.println("Car Model " + createdCarModel.getCarModelId() + " created successfully!");
            } else {
                showInputDataValidationErrorsForCarModel(constraintViolations);
            }

        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Invalid Car Category");
        } catch (CarModelExistException | UnknownPersistenceException ex) {
            System.out.println("Error creating Car Model: " + ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doViewAllModels() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS System :: Sales Management - Operations Manager ::  View All Car Models ***\n");

        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();
        System.out.printf("%-15s%-20s%-20s%-5s\n", "Car Model ID", "Model Name", "Make Name", "Disabled?");

        for (CarModel model : carModels) {
            System.out.printf("%-15s%-20s%-20s%-5s\n", model.getCarModelId().toString(), model.getModelName(), model.getMakeName(), model.getIsDisabled());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doUpdateModel() {
        Scanner scanner = new Scanner(System.in);
        String input;
        Long longInput;

        List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();
        System.out.println("*** CaRMS System :: Sales Management - Operations Manager ::  Update Car Model ***\n");

        System.out.println("Enter Model Name of Car Model to Update>");
        try {
            CarModel model = carModelSessionBeanRemote.retrieveCarModelByModelName(scanner.nextLine().trim());

            System.out.print("Enter Model Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                model.setModelName(input);
            }
            System.out.print("Enter Make Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                model.setMakeName(input);
            }
            System.out.printf("%8s%20s\n", "Car Category ID", "Car Category Name");
            for (CarCategory category : carCategories) {
                System.out.printf("%8s%20s\n", category.getCarCategoryId().toString(), category.getCategoryName());
            }
            System.out.print("Enter Car Category ID (blank if no change)> ");
            longInput = scanner.nextLong();
            if (longInput > 0l) {
                CarCategory chosenCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(longInput, false, false);
                model.setCarCategory(chosenCategory);
            }

            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(model);

            if (constraintViolations.isEmpty()) {
                carModelSessionBeanRemote.updateCarModel(model);
            } else {
                showInputDataValidationErrorsForCarModel(constraintViolations);
            }

        } catch (CarModelNotFoundException ex) {
            System.out.println("Error retrieving Car Model: " + ex.getMessage());
        } catch (CarCategoryNotFoundException ex) {
            System.out.println("Error retrieving Car Category: " + ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }

    }

    private void doDeleteModel() {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** CaRMS System :: Sales Management - Operations Manager :: Delete Car Model ***\n");

        System.out.println("Enter Model Name of Car Model to Update>");

        try {
            CarModel model = carModelSessionBeanRemote.retrieveCarModelByModelName(scanner.nextLine().trim());
            System.out.printf("Confirm Delete Car Model %s (Enter 'Y' to Delete)> ", model.getModelName());
            
            input = scanner.nextLine().trim();
            if (input.equals("Y")) {
                carModelSessionBeanRemote.deleteCarModel(model.getCarModelId());
                System.out.println("Car Model deleted successfully!\n");
            } else {
                System.out.println("Car Model NOT deleted!\n");
            }
        } catch (CarModelNotFoundException ex) {
            System.out.println("An error has occurred while deleting Car Model: " + ex.getMessage() + "\n");
        }
    }

    private void doCreateNewCar() {
        Scanner scanner = new Scanner(System.in);
        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();
        Car newCar = new Car();

        try {
            System.out.println("*** CaRMS System :: Sales Management - Operations Manager :: Create New Car ***\n");
            System.out.print("Enter Licence Plate Number> ");
            newCar.setLicensePlate(scanner.nextLine().trim());
            System.out.print("Enter Colour> ");
            newCar.setColour(scanner.nextLine().trim());
            System.out.printf("%-15s%-20s%-20s%-5s\n", "Car Model ID", "Model Name", "Make Name", "Disabled?");

        for (CarModel model : carModels) {
            System.out.printf("%-15s%-20s%-20s%-5s\n", model.getCarModelId().toString(), model.getModelName(), model.getMakeName(), model.getIsDisabled());
        }
            System.out.print("Enter Car Model ID> ");
            CarModel chosenModel = carModelSessionBeanRemote.retrieveCarModelByCarModelId(scanner.nextLong(), false, false, false);
            newCar.setCarModel(chosenModel);
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

            if (constraintViolations.isEmpty()) {
                Car createdCar = carSessionBeanRemote.createNewCar(newCar);
                System.out.println("Car " + createdCar.getCarId() + " created successfully!");
            } else {
                showInputDataValidationErrorsForCar(constraintViolations);
            }

        } catch (CarModelNotFoundException ex) {
            System.out.println("Invalid Car Model");
        } catch (CarExistException | UnknownPersistenceException ex) {
            System.out.println("Error creating Car: " + ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
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

    private void showInputDataValidationErrorsForCarModel(Set<ConstraintViolation<CarModel>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }
    
    private void showInputDataValidationErrorsForCar(Set<ConstraintViolation<Car>> constraintViolations) {
        System.out.println("\nInput data validation error!:");

        for (ConstraintViolation constraintViolation : constraintViolations) {
            System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
        }

        System.out.println("\nPlease try again......\n");
    }

}
