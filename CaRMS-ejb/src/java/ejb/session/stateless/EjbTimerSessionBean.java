/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.Reservation;
import entity.TransitDispatchRecord;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import util.enumeration.CarStatusEnum;
import util.exception.CarNotFoundException;
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

	//@Schedule(hour = "*", minute = "*", second = "*/5", info = "allocateCars")
	// For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
	// To trigger the timer at 2am instead, use the following the @Schedule annotation
	@Schedule(hour = "2", info = "allocateCars")
	public void allocateCars() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm");
		System.out.println("********** Car Allocation(): Timeout at " + timeStamp);

		List<Reservation> currentDayReservations = reservationSessionBeanLocal.retrieveCurrentDayReservations();

		for (Reservation reservation : currentDayReservations) {
			List<Car> possibleCars = reservation.getCarModel().getCars();
			Outlet pickupOutlet = reservation.getPickUpOutlet();
			boolean carAllocated = false;
			for (Car car : possibleCars) {
				if (Objects.equals(pickupOutlet.getOutletId(), car.getOutlet().getOutletId())) {
					try {
						if (carSessionBeanLocal.isAvailable(car.getCarId(), reservation.getReservationStartDate())) {
							reservationSessionBeanLocal.allocateCar(car.getCarId(), reservation.getReservationId());
							System.out.println("Car " + car.getLicensePlate() + " allocated to " + reservation.getCustomer().getName() + " for pickup on " + outputDateFormat.format(reservation.getReservationStartDate()) + " at " + pickupOutlet.getOutletName());
							carAllocated = true;
							break;
						}
					} catch (CarNotFoundException | ReservationNotFoundException ex) {
						System.out.println("An error has occured: " + ex.getMessage());
					}

				}
			}
			if (!carAllocated) {
				for (Car car : possibleCars) {
					if (car.getCarStatus().equals(CarStatusEnum.AVAILABLE)) {

						try {
							reservationSessionBeanLocal.allocateCar(car.getCarId(), reservation.getReservationId());
							System.out.println("Car " + car.getLicensePlate() + " allocated to " + reservation.getCustomer().getName() + " for pickup on " + outputDateFormat.format(reservation.getReservationStartDate()) + " at " + pickupOutlet.getOutletName());
							System.out.println("Car is currently at " + car.getOutlet().getOutletName() + " and needs transit dispatch");
							break;
						} catch (CarNotFoundException | ReservationNotFoundException ex) {
							System.out.println("An error has occured: " + ex.getMessage());
						}

					}
				}
			}

		}
	}

	@Override
	public void allocateCarsManually(Date date) {
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm");
		System.out.println("********** Manual Car Allocation(): for " + outputDateFormat.format(date));

		List<Reservation> currentDayReservations = reservationSessionBeanLocal.retrieveReservationsByDate(date);

		for (Reservation reservation : currentDayReservations) {
			List<Car> possibleCars = reservation.getCarModel().getCars();
			Outlet pickupOutlet = reservation.getPickUpOutlet();
			boolean carAllocated = false;

			// check cars at outlet
			for (Car car : possibleCars) {
				if (Objects.equals(pickupOutlet.getOutletId(), car.getOutlet().getOutletId())) {
					try {
						if (carSessionBeanLocal.isAvailableOnDate(car.getCarId(), reservation.getReservationStartDate(), reservation.getReservationEndDate())) {
							reservationSessionBeanLocal.allocateCar(car.getCarId(), reservation.getReservationId());
							System.out.println("Car " + car.getLicensePlate() + " allocated to " + reservation.getCustomer().getName() + " for pickup on " + outputDateFormat.format(reservation.getReservationStartDate()) + " at " + pickupOutlet.getOutletName());
							carAllocated = true;
							break;
						}
					} catch (CarNotFoundException | ReservationNotFoundException ex) {
						System.out.println("An error has occured: " + ex.getMessage());
					}

				}
			}

			// if no cars available at outlet, check all cars
			if (!carAllocated) {
				for (Car car : possibleCars) {
					try {
						if (carSessionBeanLocal.isAvailableOnDate(car.getCarId(), reservation.getReservationStartDate(), reservation.getReservationEndDate())) {
							reservationSessionBeanLocal.allocateCar(car.getCarId(), reservation.getReservationId());
							System.out.println("Car " + car.getLicensePlate() + " allocated to " + reservation.getCustomer().getName() + " for pickup on " + outputDateFormat.format(reservation.getReservationStartDate()) + " at " + pickupOutlet.getOutletName());
							System.out.println("Car is currently at " + car.getOutlet().getOutletName() + " and needs transit dispatch");
							break;
						}

					} catch (CarNotFoundException | ReservationNotFoundException ex) {
						System.out.println("An error has occured: " + ex.getMessage());
					}

				}
			}
		}

	}

	@Schedule(hour = "2", minute = "5", info = "generateTransitDispatchRecords")
	public void generateTransitDispatchRecordsForCurrentDay() {
		String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy hh.mm");
		System.out.println("********** Car Allocation(): Timeout at " + timeStamp);

		List<Reservation> currentDayReservations = reservationSessionBeanLocal.retrieveCurrentDayReservations();

		for (Reservation r : currentDayReservations) {
			if (!r.getCar().getOutlet().equals(r.getPickUpOutlet())) {
				try {
					TransitDispatchRecord newDispatch = new TransitDispatchRecord();
					newDispatch.setCurrentOutlet(r.getCar().getOutlet());
					newDispatch.setDestinationOutlet(r.getPickUpOutlet());

					Date dispatchTime = r.getReservationStartDate();
					dispatchTime.setHours(r.getReservationStartDate().getHours() - 2);
					newDispatch.setDispatchTime(dispatchTime);

					transitDispatchRecordSessionBeanLocal.createNewTransitDispatchRecord(newDispatch);
				} catch (TransitDispatchRecordExistException | UnknownPersistenceException ex) {
					System.out.println("An error has occured: " + ex.getMessage());
				}
			}
		}
	}
}
