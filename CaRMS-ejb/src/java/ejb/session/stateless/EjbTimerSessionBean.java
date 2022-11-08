/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Car;
import entity.Outlet;
import entity.Reservation;
import java.text.SimpleDateFormat;
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

    @Schedule(hour = "*", minute = "*", second = "*/5", info = "allocateCars")
    // For testing purpose, we are allowing the timer to trigger every 5 seconds instead of every 5 minutes
    // To trigger the timer at 2am instead, use the following the @Schedule annotation
    // @Schedule(hour = "2", info = "allocateCars")
    public void allocateCars() {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        System.out.println("********** Car Allocation(): Timeout at " + timeStamp);

        List<Reservation> currentDayReservations = reservationSessionBeanLocal.retrieveCurrentDayReservations();

        for (Reservation reservation : currentDayReservations) {
            List<Car> possibleCars = reservation.getCarModel().getCars();
            Outlet pickupOutlet = reservation.getPickUpOutlet();
            boolean carAllocated = false;
            for (Car car : possibleCars) {
                if (Objects.equals(pickupOutlet.getOutletId(), car.getOutlet().getOutletId())) {
                    try {
                        if (carSessionBeanLocal.isAvailableAtOutlet(car.getCarId(), pickupOutlet.getOutletId(), reservation.getReservationStartDate())) {
                            reservationSessionBeanLocal.allocateCar(car.getCarId(), reservation.getReservationId());
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
                            break;
                        } catch (CarNotFoundException | ReservationNotFoundException ex) {
                            System.out.println("An error has occured: " + ex.getMessage());
                        }

                    }
                }
            }

        }
    }

}
