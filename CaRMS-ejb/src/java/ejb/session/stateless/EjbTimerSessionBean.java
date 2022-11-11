/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.CarCategory;
import entity.Reservation;
import entity.TransitDispatchRecord;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.exception.CarModelNotFoundException;
import util.exception.CarNotFoundException;
import util.exception.OutletNotFoundException;
import util.exception.ReservationNotFoundException;
import util.exception.TransitDispatchRecordExistException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author nevanng
 */
@Stateless
public class EjbTimerSessionBean implements EjbTimerSessionBeanRemote, EjbTimerSessionBeanLocal {

	@EJB
	private TransitDispatchRecordSessionBeanLocal transitDispatchRecordSessionBeanLocal;

	@EJB
	private CarSessionBeanLocal carSessionBeanLocal;

	@EJB
	private ReservationSessionBeanLocal reservationSessionBeanLocal;
	
	@Schedule(hour = "2", info = "allocateCars")
	public void allocateCars() {
		Date date = new Date();
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm");
		System.out.println("********** Car Allocation(): Timeout at " + timeStamp);

		// Get list of reservations that need a car to be allocated to them
		List<Reservation> currentDayReservations = reservationSessionBeanLocal.retrieveReservationsByDate(date);

		// Get list of cars that can be allocated
		try {
			for (Reservation r : currentDayReservations) {
				CarCategory category = r.getCarCategory();
				List<Car> possibleCars = carSessionBeanLocal.retrieveCarsByCarCategory(category);
				boolean carAllocated = false;
				// Iterate through list of possible cars at pickup outlet and assign first one that is available
				for (Car c : possibleCars) {
					if (c.getOutlet().equals(r.getPickUpOutlet())) {
						if (carSessionBeanLocal.isAvailable(c.getCarId(), r.getReservationStartDate(), r.getReservationEndDate())) {
							reservationSessionBeanLocal.allocateCar(c.getCarId(), r.getReservationId());
							carAllocated = true;
							System.out.println("Car " + c.getLicensePlate() + " allocated to " + r.getCustomer().getName() + " for pickup on " + outputDateFormat.format(r.getReservationStartDate()) + " at " + r.getPickUpOutlet().getOutletName());
							break;
						}
					}
				}
				// if no cars are available at pickup outlet, assign first car available from any outlet
				if (!carAllocated) {
					for (Car c : possibleCars) {
						int minTransitWindow = r.getReservationStartDate().getHours() - 2;
						Date adjustedTime = new Date();
						adjustedTime.setTime(r.getReservationStartDate().getTime());
						adjustedTime.setHours(minTransitWindow);
						if (carSessionBeanLocal.isAvailable(c.getCarId(), adjustedTime, r.getReservationEndDate())) {
							reservationSessionBeanLocal.allocateCar(c.getCarId(), r.getReservationId());
							System.out.println("Car " + c.getLicensePlate() + " allocated to " + r.getCustomer().getName() + " for pickup on " + outputDateFormat.format(r.getReservationStartDate()) + " at " + r.getPickUpOutlet().getOutletName());
							System.out.println("Car is currently at " + c.getOutlet().getOutletName() + " and needs transit dispatch");
							// generate transit dispatch record
							TransitDispatchRecord newDispatch = new TransitDispatchRecord();
							newDispatch.setCurrentOutlet(r.getCar().getOutlet());
							newDispatch.setDestinationOutlet(r.getPickUpOutlet());

							Date dispatchTime = r.getReservationStartDate();
							dispatchTime.setHours(r.getReservationStartDate().getHours() - 2);
							newDispatch.setDispatchTime(dispatchTime);

							transitDispatchRecordSessionBeanLocal.createNewTransitDispatchRecord(newDispatch);
							System.out.println("New Transit Dispatch Record Created for " + r.getCar().getLicensePlate());
							break;
						}
					}
				}

			}
		} catch (CarNotFoundException | CarModelNotFoundException | ReservationNotFoundException | OutletNotFoundException | TransitDispatchRecordExistException | UnknownPersistenceException ex) {
			System.out.println("An error has occurred: " + ex.getMessage());
		}
	}

	@Override
	public List<String> allocateCarsManually(Date date) {
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		List<String> output = new ArrayList<>();
		System.out.println("********** Manual Car Allocation " + outputDateFormat.format(date));

		// Get list of reservations that need a car to be allocated to them
		List<Reservation> currentDayReservations = reservationSessionBeanLocal.retrieveReservationsByDate(date);

		// Get list of cars that can be allocated
		try {
			for (Reservation r : currentDayReservations) {
				CarCategory category = r.getCarCategory();
				List<Car> possibleCars = carSessionBeanLocal.retrieveCarsByCarCategory(category);
				boolean carAllocated = false;
				// Iterate through list of possible cars at pickup outlet and assign first one that is available
				for (Car c : possibleCars) {
					if (c.getOutlet().equals(r.getPickUpOutlet())) {
						if (carSessionBeanLocal.isAvailable(c.getCarId(), r.getReservationStartDate(), r.getReservationEndDate())) {
							reservationSessionBeanLocal.allocateCar(c.getCarId(), r.getReservationId());
							carAllocated = true;
							output.add("Car " + c.getLicensePlate() + " allocated to " + r.getCustomer().getName() + " for pickup on " + outputDateFormat.format(r.getReservationStartDate()) + " at " + r.getPickUpOutlet().getOutletName());
							System.out.println("Car " + c.getLicensePlate() + " allocated to " + r.getCustomer().getName() + " for pickup on " + outputDateFormat.format(r.getReservationStartDate()) + " at " + r.getPickUpOutlet().getOutletName());
							break;
						}
					}
				}
				// if no cars are available at pickup outlet, assign first car available from any outlet
				if (!carAllocated) {
					for (Car c : possibleCars) {
						int minTransitWindow = r.getReservationStartDate().getHours() - 2;
						Date adjustedTime = new Date();
						adjustedTime.setTime(r.getReservationStartDate().getTime());
						adjustedTime.setHours(minTransitWindow);
						if (carSessionBeanLocal.isAvailable(c.getCarId(), adjustedTime, r.getReservationEndDate())) {
							reservationSessionBeanLocal.allocateCar(c.getCarId(), r.getReservationId());
							output.add("Car " + c.getLicensePlate() + " allocated to " + r.getCustomer().getName() + " for pickup on " + outputDateFormat.format(r.getReservationStartDate()) + " at " + r.getPickUpOutlet().getOutletName());
							output.add("Car is currently at " + c.getOutlet().getOutletName() + " and needs transit dispatch");
							System.out.println("Car " + c.getLicensePlate() + " allocated to " + r.getCustomer().getName() + " for pickup on " + outputDateFormat.format(r.getReservationStartDate()) + " at " + r.getPickUpOutlet().getOutletName());
							System.out.println("Car is currently at " + c.getOutlet().getOutletName() + " and needs transit dispatch");
							// generate transit dispatch record
							TransitDispatchRecord newDispatch = new TransitDispatchRecord();
							newDispatch.setCurrentOutlet(r.getCar().getOutlet());
							newDispatch.setDestinationOutlet(r.getPickUpOutlet());

							Date dispatchTime = r.getReservationStartDate();
							dispatchTime.setHours(r.getReservationStartDate().getHours() - 2);
							newDispatch.setDispatchTime(dispatchTime);

							transitDispatchRecordSessionBeanLocal.createNewTransitDispatchRecord(newDispatch);
							output.add("New Transit Dispatch Record Created for " + r.getCar().getLicensePlate());
							System.out.println("New Transit Dispatch Record Created for " + r.getCar().getLicensePlate());
							break;
						}
					}
				}

			}
		} catch (CarNotFoundException | CarModelNotFoundException | ReservationNotFoundException | OutletNotFoundException | TransitDispatchRecordExistException | UnknownPersistenceException ex) {
			System.out.println("An error has occurred: " + ex.getMessage());
		}
		return output;
	}

}
