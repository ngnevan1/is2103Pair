/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.TransitDispatchRecordSessionBeanRemote;
import entity.Car;
import entity.CarCategory;
import entity.CarModel;
import entity.Employee;
import entity.Outlet;
import entity.TransitDispatchRecord;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.CarStatusEnum;
import util.enumeration.EmployeeAccessRightsEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.CarExistException;
import util.exception.CarModelExistException;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.TransitDispatchRecordNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCarException;

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
    private OutletSessionBeanRemote outletSessionBeanRemote;
    private EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;
    private TransitDispatchRecordSessionBeanRemote transitDispatchRecordSessionBeanRemote;
    private EmployeeSessionBeanRemote employeeSessionBeanRemote;

    public OperationsManager() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public OperationsManager(CarCategorySessionBeanRemote carCategorySessionBeanRemote, CarModelSessionBeanRemote carModelSessionBeanRemote, CarSessionBeanRemote carSessionBeanRemote, Employee currentEmployee, OutletSessionBeanRemote outletSessionBeanRemote, EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote, TransitDispatchRecordSessionBeanRemote transitDispatchRecordSessionBeanRemote, EmployeeSessionBeanRemote employeeSessionBeanRemote) {
        this();
        this.carCategorySessionBeanRemote = carCategorySessionBeanRemote;
        this.carModelSessionBeanRemote = carModelSessionBeanRemote;
        this.carSessionBeanRemote = carSessionBeanRemote;
        this.outletSessionBeanRemote = outletSessionBeanRemote;
        this.currentEmployee = currentEmployee;
        this.ejbTimerSessionBeanRemote = ejbTimerSessionBeanRemote;
        this.transitDispatchRecordSessionBeanRemote = transitDispatchRecordSessionBeanRemote;
        this.employeeSessionBeanRemote = employeeSessionBeanRemote;
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
            System.out.println("-----------------------");
            System.out.println("8: View Transit Driver Dispatch Records for Current Day Reservations");
            System.out.println("9: Assign Transit Driver");
            System.out.println("10: Update Transit As Completed");
            System.out.println("-----------------------");
            System.out.println("11: Allocate Cars to Current Day Reservations");
            System.out.println("-----------------------");

            System.out.println("12: Back\n");
            response = 0;

            while (response < 1 || response > 12) {
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
                    doViewDispatchRecords();
                } else if (response == 9) {
                    doAssignTransitDriver();
                } else if (response == 10) {
                    doUpdateTransitAsCompleted();
                } else if (response == 11) {
                    doAllocateCars();
                } else if (response == 12) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            if (response == 12) {
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
            System.out.print("Enter Make Name> ");
            newCarModel.setMakeName(scanner.nextLine().trim());
			System.out.print("Enter Model Name> ");
            newCarModel.setModelName(scanner.nextLine().trim());
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
                System.out.println("Car Model: " + createdCarModel.getMakeName() + " " + createdCarModel.getModelName() + " created successfully!");
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
        System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", "Car Model ID", "Car Category" , "Make Name", "Model Name", "Disabled?");

        for (CarModel model : carModels) {
            System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", model.getCarModelId().toString(), model.getCarCategory().getCategoryName(), model.getMakeName(), model.getModelName(), model.getIsDisabled() ? "Yes" : "No");
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
            System.out.print("Enter Make Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                model.setMakeName(input);
            }
			System.out.print("Enter Model Name (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                model.setModelName(input);
            }
            System.out.printf("%8s%20s\n", "Car Category ID", "Car Category Name");
            for (CarCategory category : carCategories) {
                System.out.printf("%8s%20s\n", category.getCarCategoryId().toString(), category.getCategoryName());
            }
            System.out.print("Enter Car Category ID (negative number if no change)> ");
            longInput = scanner.nextLong();
            if (longInput > 0l) {
                CarCategory chosenCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(longInput, false, false);
                model.setCarCategory(chosenCategory);
            } 
			scanner.nextLine();

            Set<ConstraintViolation<CarModel>> constraintViolations = validator.validate(model);

            if (constraintViolations.isEmpty()) {
                carModelSessionBeanRemote.updateCarModel(model);
				System.out.println("Car Model: " + model.getModelName() + " updated successfully!");
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

        System.out.println("Enter Model Name of Car Model to Delete>");

        try {
            CarModel model = carModelSessionBeanRemote.retrieveCarModelByModelName(scanner.nextLine().trim());
			 System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", "Car Model ID", "Car Category" , "Make Name", "Model Name", "Disabled?");
            System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", model.getCarModelId().toString(), model.getCarCategory().getCategoryName(), model.getMakeName(), model.getModelName(), model.getIsDisabled() ? "Yes" : "No");
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
        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();
        Car newCar = new Car();

        try {
            System.out.println("*** CaRMS System :: Sales Management - Operations Manager :: Create New Car ***\n");
            System.out.print("Enter Licence Plate Number> ");
            newCar.setLicensePlate(scanner.nextLine().trim());
            System.out.print("Enter Colour> ");
            newCar.setColour(scanner.nextLine().trim());
            System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", "Car Model ID", "Car Category" , "Make Name", "Model Name", "Disabled?");

            for (CarModel model : carModels) {
            System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", model.getCarModelId().toString(), model.getCarCategory().getCategoryName(), model.getMakeName(), model.getModelName(), model.getIsDisabled() ? "Yes" : "No");
            }
            System.out.print("Enter Car Model ID> ");
            CarModel chosenModel = carModelSessionBeanRemote.retrieveCarModelByCarModelId(scanner.nextLong(), false, false, false);
            newCar.setCarModel(chosenModel);

            while (true) {
                System.out.print("Select Car Status (1: Available , 2: On Rental, 3: In Transit, 4: Servicing)> ");
                Integer carStatusInt = scanner.nextInt();

                if (carStatusInt >= 1 && carStatusInt <= 4) {
                    newCar.setCarStatus(CarStatusEnum.values()[carStatusInt - 1]);
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            System.out.printf("%-15s%-20s%-20s\n", "Outlet ID", "Outlet Name", "Outlet Address");

            for (Outlet outlet : outlets) {
                System.out.printf("%-15s%-20s%-20s\n", outlet.getOutletId().toString(), outlet.getOutletName(), outlet.getOutletAddress());
            }
            System.out.print("Enter Outlet ID> ");
            Outlet chosenOutlet = outletSessionBeanRemote.retrieveOutletByOutletId(scanner.nextLong(), false, false, false);
            newCar.setOutlet(chosenOutlet);

            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(newCar);

            if (constraintViolations.isEmpty()) {
                Car createdCar = carSessionBeanRemote.createNewCar(newCar);
                System.out.println("Car " + createdCar.getLicensePlate() + " created successfully!");
            } else {
                showInputDataValidationErrorsForCar(constraintViolations);
            }

        } catch (CarModelNotFoundException ex) {
            System.out.println("Error retrieving Car Model: " + ex.getMessage());
        } catch (OutletNotFoundException ex) {
            System.out.println("Error retrieving Outlet: " + ex.getMessage());
        } catch (CarExistException | UnknownPersistenceException ex) {
            System.out.println("Error creating Car: " + ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doViewAllCars() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** CaRMS System :: Sales Management - Operations Manager ::  View All Cars ***\n");

        List<Car> cars = carSessionBeanRemote.retrieveAllCars();
        System.out.printf("%-10s%-25s%-20s%-20s%-20s\n", "Car ID", "Licence Plate Number", "Car Category", "Make Name", "Model Name", "Disabled?");

        for (Car car : cars) {
            System.out.printf("%-10s%-25s%-20s%-20s%-20s\n", car.getCarId().toString(), car.getLicensePlate(), car.getCarModel().getCarCategory().getCategoryName(), car.getCarModel().getMakeName(), car.getCarModel().getModelName(), car.getIsDisabled() ? "Yes" : "No");
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doViewCarDetails() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("*** CaRMS System :: Sales Management - Operations Manager ::  View Car Details ***\n");

        try {
            System.out.println("Enter Car Licence Plate Number>");
            Car car = carSessionBeanRemote.retrieveCarByLicensePlate(scanner.nextLine().trim());

            System.out.printf("%-10s%-25s%-20s%-20s%-20s\n", "Car ID", "Licence Plate Number", "Car Category", "Make Name", "Model Name", "Disabled?");
            System.out.printf("%-10s%-20s%-20s%-20s%-20s\n", car.getCarId().toString(), car.getLicensePlate(), car.getCarModel().getCarCategory().getCategoryName(), car.getCarModel().getMakeName(), car.getCarModel().getModelName(), car.getIsDisabled() ? "Yes" : "No");

            System.out.println("------------------------");
            System.out.println("1: Update Car");
            System.out.println("2: Delete Car");
            System.out.println("3: Back\n");
            System.out.print("> ");

            int response = scanner.nextInt();

            if (response == 1) {
                doUpdateCar(car);
            } else if (response == 2) {
                doDeleteCar(car);
            }

        } catch (CarNotFoundException ex) {
            System.out.println("An error has occured while retrieving car " + ex.getMessage() + "\n");
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();

    }

    private void doUpdateCar(Car car) {
        Scanner scanner = new Scanner(System.in);
        String input;
        Long longInput;
        List<CarModel> carModels = carModelSessionBeanRemote.retrieveAllCarModels();
        List<Outlet> outlets = outletSessionBeanRemote.retrieveAllOutlets();

        try {
            System.out.println("*** CaRMS System :: Sales Management - Operations Manager :: View Car Details :: Update Car ***\n");
            System.out.print("Enter Colour (blank if no change)> ");
            input = scanner.nextLine().trim();
            if (input.length() > 0) {
                car.setColour(input);
            }
			System.out.println("Enter License Plate (blank if no change)> ");
			input = scanner.nextLine().trim();
			if (input.length() > 0) {
				car.setLicensePlate(input);
			}
			System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", "Car Model ID", "Car Category" , "Make Name", "Model Name", "Disabled?");
            for (CarModel model : carModels) {
			System.out.printf("%-15s%-20s%-20s%-20s%-5s\n", model.getCarModelId().toString(), model.getCarCategory().getCategoryName(), model.getMakeName(), model.getModelName(), model.getIsDisabled() ? "Yes" : "No");
            }
            System.out.print("Enter Car Model ID (negative number if no change)> ");
            longInput = scanner.nextLong();
            if (longInput > 0l) {
                CarModel chosenModel = carModelSessionBeanRemote.retrieveCarModelByCarModelId(longInput, false, false, false);
                car.setCarModel(chosenModel);
            }
            while (true) {
                System.out.print("Select Car Status (1: Available, 2: On Rental, 3: In Transit, 4: Servicing)> ");
                Integer carStatusInt = scanner.nextInt();

                if (carStatusInt >= 1 && carStatusInt <= 4) {
                    car.setCarStatus(CarStatusEnum.values()[carStatusInt - 1]);
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }

            System.out.printf("%-15s%-20s%-20s\n", "Outlet ID", "Outlet Name", "Outlet Address");

            for (Outlet outlet : outlets) {
                System.out.printf("%-15s%-20s%-20s\n", outlet.getOutletId().toString(), outlet.getOutletName(), outlet.getOutletAddress());
            }
            System.out.print("Enter Outlet ID (negative number if no change)> ");
            longInput = scanner.nextLong();
            if (longInput > 0l) {
                Outlet chosenOutlet = outletSessionBeanRemote.retrieveOutletByOutletId(longInput, false, false, false);
                car.setOutlet(chosenOutlet);
            }
            Set<ConstraintViolation<Car>> constraintViolations = validator.validate(car);

            if (constraintViolations.isEmpty()) {
                carSessionBeanRemote.updateCar(car);
                System.out.println("Car " + car.getLicensePlate() + " updated successfully!");
            } else {
                showInputDataValidationErrorsForCar(constraintViolations);
            }

        } catch (CarModelNotFoundException ex) {
            System.out.println("Error retrieving Car Model: " + ex.getMessage());
        } catch (OutletNotFoundException ex) {
            System.out.println("Error retrieving Outlet: " + ex.getMessage());
        } catch (CarNotFoundException | UpdateCarException ex) {
            System.out.println("Error updating Car: " + ex.getMessage());
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage() + "\n");
        }
    }

    private void doDeleteCar(Car car) {
        Scanner scanner = new Scanner(System.in);
        String input;

        System.out.println("*** CaRMS System :: Sales Management - Operations Manager :: View Car Details :: Delete Car ***\n");
        System.out.printf("Confirm Delete Car %s (Enter 'Y' to Delete)> ", car.getLicensePlate());
        input = scanner.nextLine().trim();

        if (input.equals("Y")) {
            try {
                carSessionBeanRemote.deleteCar(car.getCarId());
                System.out.println("Car deleted successfully!\n");
            } catch (CarNotFoundException | OutletNotFoundException | ReservationNotFoundException | CarModelNotFoundException ex) {
                System.out.println("An error has occurred while deleting Car: " + ex.getMessage() + "\n");
            }
        } else {
            System.out.println("Car NOT deleted!\n");
        }
    }

    private void doViewDispatchRecords() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        System.out.println("*** CaRMS System :: Sales Management - Operations Manager ::  View All Transit Dispatch Records ***\n");

        List<TransitDispatchRecord> tdrs = transitDispatchRecordSessionBeanRemote.retrieveCurrentDayTransitDispatchRecords();
        System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", "Transit Dispatch Record ID", "Dispatch Time", "Current Outlet", "Destination Outlet", "Employee");

        for (TransitDispatchRecord tdr : tdrs) {
            System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", tdr.getTransitDispatchRecordId().toString(), outputDateFormat.format(tdr.getDispatchTime()), tdr.getCurrentOutlet().getOutletName(), tdr.getDestinationOutlet().getOutletName(), tdr.getEmployee().getEmployeeName());
        }

        System.out.print("Press any key to continue...> ");
        scanner.nextLine();
    }

    private void doAssignTransitDriver() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        System.out.println("*** CaRMS System :: Sales Management - Operations Manager ::  Assign Transit Driver ***\n");

        List<TransitDispatchRecord> tdrs = transitDispatchRecordSessionBeanRemote.retrieveCurrentDayTransitDispatchRecords();
        System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", "Transit Dispatch Record ID", "Dispatch Time", "Current Outlet", "Destination Outlet", "Employee");
        for (TransitDispatchRecord tdr : tdrs) {
            System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", tdr.getTransitDispatchRecordId().toString(), outputDateFormat.format(tdr.getDispatchTime()), tdr.getCurrentOutlet().getOutletName(), tdr.getDestinationOutlet().getOutletName(), tdr.getEmployee().getEmployeeName());
        }

        System.out.print("Enter ID of Transit Dispatch Record to Assign Driver> ");
        Long chosenTdrId = scanner.nextLong();

        List<Employee> employees = employeeSessionBeanRemote.retrieveEmployeesAtOutlet(currentEmployee.getOutlet());

        System.out.printf("%-15s%-20s%-20s\n", "Employee ID", "Employee Name", "Transit Status");

        for (Employee e : employees) {
            System.out.printf("%-15s%-20s%-20s\n", e.getEmployeeId().toString(), e.getEmployeeName(), e.getTransitStatus());
        }
        System.out.print("Enter ID of Employee to be assigned> ");
        Long chosenEmployeeId = scanner.nextLong();

        try {
            transitDispatchRecordSessionBeanRemote.assignDriver(chosenTdrId, chosenEmployeeId);
        } catch (EmployeeNotFoundException | TransitDispatchRecordNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage());
        }

    }

    private void doUpdateTransitAsCompleted() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
        System.out.println("*** CaRMS System :: Sales Management - Operations Manager ::  Update Transit Dispatch Record as Completed ***\n");

        List<TransitDispatchRecord> tdrs = transitDispatchRecordSessionBeanRemote.retrieveCurrentDayTransitDispatchRecords();
        System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", "Transit Dispatch Record ID", "Dispatch Time", "Current Outlet", "Destination Outlet", "Employee");

        for (TransitDispatchRecord tdr : tdrs) {
            System.out.printf("%-15s%-20s%-20s%-20s%-20s\n", tdr.getTransitDispatchRecordId().toString(), outputDateFormat.format(tdr.getDispatchTime()), tdr.getCurrentOutlet().getOutletName(), tdr.getDestinationOutlet().getOutletName(), tdr.getEmployee().getEmployeeName());
        }

        System.out.print("Enter ID of Transit Dispatch Record to be updated> ");
        try {
            transitDispatchRecordSessionBeanRemote.transitCompleted(scanner.nextLong());
        } catch (TransitDispatchRecordNotFoundException ex) {
            System.out.println("An error has occurred: " + ex.getMessage());
        }

    }

    private void doAllocateCars() {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
        System.out.println("*** CaRMS System :: Sales Management - Operations Manager :: Allocate Cars to Current Day Reservations ***\n");

        try {
            System.out.print("Enter Date (DD/MM/YYYY)> ");
            Date date = inputDateFormat.parse(scanner.nextLine().trim());

            ejbTimerSessionBeanRemote.allocateCarsManually(date);

        } catch (ParseException ex) {
            System.out.println("Invalid Date Entered!");
        }
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
