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
import util.enumeration.EmployeeAccessRightsEnum;
import util.enumeration.RentalRateEnum;
import util.exception.CarCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidAccessRightException;
import util.exception.RentalRateExistException;
import util.exception.RentalRateNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateRentalRateException;

/**
 *
 * @author nevanng
 */
public class SalesManager {

	private final ValidatorFactory validatorFactory;
	private final Validator validator;
	private RentalRateSessionBeanRemote rentalRateSessionBeanRemote;
	private CarCategorySessionBeanRemote carCategorySessionBeanRemote;
	private Employee currentEmployee;

	public SalesManager() {
		validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
	}

	public SalesManager(RentalRateSessionBeanRemote rentalRateSessionBeanRemote, Employee currentEmployee, CarCategorySessionBeanRemote carCategorySessionBeanRemote) {
		this();
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
			System.out.println("-----------------------");
			System.out.println("4: Back\n");
			response = 0;

			while (response < 1 || response > 4) {
				System.out.print("> ");

				response = scanner.nextInt();

				if (response == 1) {
					doCreateNewRentalRate();
				} else if (response == 2) {
					doViewAllRentalRates();
				} else if (response == 3) {
					doViewRentalRateDetails();
				} else if (response == 4) {
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

	private void doCreateNewRentalRate() {

		Scanner scanner = new Scanner(System.in);
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");
		List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();
		RentalRate newRentalRate = new RentalRate();
		String input;

		try {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			Date fillerStart = format.parse("01/01/1900 00:00");
			Date fillerEnd = format.parse("31/12/2100 23:59");
			System.out.println("*** CaRMS System :: Sales Management - Sales Manager :: Create New Rental Rate ***\n");
			System.out.print("Enter Name> ");
			newRentalRate.setRateName(scanner.nextLine().trim());
			System.out.print("Choose Car Category by entering Car Category ID \n");
			System.out.printf("%-20s%-20s\n", "Car Category ID", "Car Category Name");
			for (CarCategory category : carCategories) {
				System.out.printf("%-20s%-20s\n", category.getCarCategoryId().toString(), category.getCategoryName());
			}
			System.out.print("> ");
			CarCategory chosenCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(scanner.nextLong(), false, false);
			newRentalRate.setCarCategory(chosenCategory);
			System.out.print("Enter Rate Per Day> ");
			newRentalRate.setRatePerDay(scanner.nextBigDecimal());
			scanner.nextLine();
			System.out.print("Enter Rental Rate Start Date (DD/MM/YYYY) (Leave blank if always valid)> ");
			input = scanner.nextLine().trim();
			if (input.length() > 0) {
				newRentalRate.setRateStartDate(inputDateFormat.parse(input));
			} else {
				newRentalRate.setRateStartDate(fillerStart);
			}
			System.out.print("Enter Rental Rate End Date (DD/MM/YYYY) (Leave blank if always valid)> ");
			input = scanner.nextLine().trim();
			if (input.length() > 0) {
				newRentalRate.setRateStartDate(inputDateFormat.parse(input));
			} else {
				newRentalRate.setRateEndDate(fillerEnd);
			}
			while (true) {
				System.out.print("Select Rental Rate Type (1: Default, 2: Peak, 3: Promotion)> ");
				Integer rentalRateInt = scanner.nextInt();

				if (rentalRateInt >= 1 && rentalRateInt <= 3) {
					newRentalRate.setRateType(RentalRateEnum.values()[rentalRateInt - 1]);
					break;
				} else {
					System.out.println("Invalid option, please try again!\n");
				}
			}

			Set<ConstraintViolation<RentalRate>> constraintViolations = validator.validate(newRentalRate);

			if (constraintViolations.isEmpty()) {
				RentalRate createdRentalRate = rentalRateSessionBeanRemote.createNewRentalRate(newRentalRate);
				System.out.println("Rental Rate " + createdRentalRate.getRateName() + " created successfully!");
			} else {
				showInputDataValidationErrorsForRentalRate(constraintViolations);
			}

		} catch (ParseException ex) {
			System.out.println("Invalid Date Input!\n");
		} catch (CarCategoryNotFoundException ex) {
			System.out.println("Invalid Car Category");
		} catch (RentalRateExistException | UnknownPersistenceException ex) {
			System.out.println("Error creating Rental Rate: " + ex.getMessage());
		} catch (InputDataValidationException ex) {
			System.out.println(ex.getMessage() + "\n");
		}

	}

	private void doViewAllRentalRates() {
		Scanner scanner = new Scanner(System.in);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		System.out.println("*** CaRMS System :: Sales Management - Sales Manager ::  View All Rental Rates ***\n");

		List<RentalRate> rentalRates = rentalRateSessionBeanRemote.retrieveAllRentalRates();
		System.out.printf("%-15s%-35s%-15s%-18s%-18s%-18s%-5s\n", "Rental Rate ID", "Rate Name", "Rate Per Day", "Rate Start Date", "Rate End Date", "Car Category", "Disabled?");

		for (RentalRate rate : rentalRates) {
			String rateStartDate = outputDateFormat.format(rate.getRateStartDate());
			String rateEndDate = outputDateFormat.format(rate.getRateEndDate());
			System.out.printf("%-15s%-35s%-15s%-18s%-18s%-18s%-5s\n", rate.getRentalRateId().toString(), rate.getRateName(), rate.getRatePerDay().toString(), rateStartDate, rateEndDate, rate.getCarCategory().getCategoryName(), rate.getIsDisabled() ? "Yes" : "No" );
		}

		System.out.print("Press any key to continue...> ");
		scanner.nextLine();

	}

	private void doViewRentalRateDetails() {
		Scanner scanner = new Scanner(System.in);
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy");

		System.out.println("*** CaRMS System :: Sales Management - Sales Manager ::  View Rental Rate Details ***\n");

		try {
			System.out.println("Enter Rental Rate Name>");
			RentalRate rate = rentalRateSessionBeanRemote.retrieveRentalRateByRateName(scanner.nextLine().trim());

			System.out.printf("%-15s%-35s%-15s%-18s%-18s%-18s%-5s\n", "Rental Rate ID", "Rate Name", "Rate Per Day", "Rate Start Date", "Rate End Date", "Car Category", "Disabled?");

			String rateStartDate = outputDateFormat.format(rate.getRateStartDate());
			String rateEndDate = outputDateFormat.format(rate.getRateEndDate());
			System.out.printf("%-15s%-35s%-15s%-18s%-18s%-18s%-5s\n", rate.getRentalRateId().toString(), rate.getRateName(), rate.getRatePerDay().toString(), rateStartDate, rateEndDate, rate.getCarCategory().getCategoryName(), rate.getIsDisabled() ? "Yes" : "No");

			System.out.println("------------------------");
			System.out.println("1: Update Rental Rate");
			System.out.println("2: Delete Rental Rate");
			System.out.println("3: Back\n");
			System.out.print("> ");

			int response = scanner.nextInt();

			if (response == 1) {
				doUpdateRentalRate(rate);
			} else if (response == 2) {
				doDeleteRentalRate(rate);
			}

		} catch (RentalRateNotFoundException ex) {
			System.out.println("An error has occured while retrieving rental rate " + ex.getMessage() + "\n");
		}

		System.out.print("Press any key to continue...> ");
		scanner.nextLine();

	}

	private void doUpdateRentalRate(RentalRate rate) {
		Scanner scanner = new Scanner(System.in);
		String input;
		BigDecimal bigDecimalInput;
		Long longInput;
		List<CarCategory> carCategories = carCategorySessionBeanRemote.retrieveAllCarCategories();
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("d/M/y");

		try {
			System.out.println("*** CaRMS System :: Sales Management - Sales Manager :: View Rental Rate Details :: Update Rental Rate ***\n");
			System.out.print("Enter Name (blank if no change)> ");
			input = scanner.nextLine().trim();
			if (input.length() > 0) {
				rate.setRateName(input);
			}

			System.out.printf("%-20s%-20s\n", "Car Category ID", "Car Category Name");
			for (CarCategory category : carCategories) {
				System.out.printf("%-20s%-20s\n", category.getCarCategoryId().toString(), category.getCategoryName());
			}
			System.out.print("Enter Car Category ID (negative number if no change)> ");
			longInput = scanner.nextLong();
			if (longInput > 0l) {
				CarCategory chosenCategory = carCategorySessionBeanRemote.retrieveCarCategoryByCarCategoryId(longInput, false, false);
				rate.setCarCategory(chosenCategory);
			}

			System.out.print("Enter Rate Per Day (negative number if no change)> ");
			bigDecimalInput = scanner.nextBigDecimal();
			if (bigDecimalInput.compareTo(BigDecimal.ZERO) > 0) {
				rate.setRatePerDay(bigDecimalInput);
			}
			scanner.nextLine();

			System.out.print("Enter Rental Rate End Date (DD/MM/YYYY) (blank if no change)> ");
			input = scanner.nextLine().trim();
			if (input.length() > 0) {
				rate.setRateEndDate(inputDateFormat.parse(input));
			}
			while (true) {
				System.out.print("Select Rental Rate Type (1: Default, 2: Peak, 3: Promotion) (negative number if no change)> ");
				Integer rentalRateInt = scanner.nextInt();

				if (rentalRateInt >= 1 && rentalRateInt <= 3) {
					rate.setRateType(RentalRateEnum.values()[rentalRateInt - 1]);
					break;
				} else if (rentalRateInt > 3) {
					System.out.println("Invalid option, please try again!\n");
				}
			}
			
			rentalRateSessionBeanRemote.updateRentalRate(rate);
			System.out.println("Rental Rate " + rate.getRateName() + " updated successfully!");

		} catch (ParseException ex) {
			System.out.println("Invalid Date Input!\n");
		} catch (CarCategoryNotFoundException ex) {
			System.out.println("Invalid Car Category");
		} catch (RentalRateNotFoundException | UpdateRentalRateException ex) {
			System.out.println("An error has occured while updating rental rate: " + ex.getMessage() + "\n");
		} catch (InputDataValidationException ex) {
			System.out.println(ex.getMessage() + "\n");
		}

	}

	private void doDeleteRentalRate(RentalRate rate) {
		Scanner scanner = new Scanner(System.in);
		String input;

		System.out.println("*** CaRMS System :: Sales Management - Sales Manager :: View Rental Rate Details :: Delete Rental Rate ***\n");
		System.out.printf("Confirm Delete Rental Rate %s (Enter 'Y' to Delete)> ", rate.getRateName());
		input = scanner.nextLine().trim();

		if (input.equals("Y")) {
			try {
				rentalRateSessionBeanRemote.deleteRentalRate(rate.getRentalRateId());
				System.out.println("Rental Rate deleted successfully!\n");
			} catch (RentalRateNotFoundException ex) {
				System.out.println("An error has occurred while deleting rental rate: " + ex.getMessage() + "\n");
			}
		} else {
			System.out.println("Rental Rate NOT deleted!\n");
		}
	}

	private void showInputDataValidationErrorsForRentalRate(Set<ConstraintViolation<RentalRate>> constraintViolations) {
		System.out.println("\nInput data validation error!:");

		for (ConstraintViolation constraintViolation : constraintViolations) {
			System.out.println("\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage());
		}

		System.out.println("\nPlease try again......\n");
	}

}
