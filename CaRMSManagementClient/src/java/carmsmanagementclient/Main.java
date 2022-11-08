/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carmsmanagementclient;

import ejb.session.stateless.CarCategorySessionBeanRemote;
import ejb.session.stateless.CarModelSessionBeanRemote;
import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.EjbTimerSessionBeanRemote;
import ejb.session.stateless.EmployeeSessionBeanRemote;
import ejb.session.stateless.OutletSessionBeanRemote;
import ejb.session.stateless.RentalRateSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import ejb.session.stateless.TransitDispatchRecordSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author nevanng
 */
public class Main {

    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;

    @EJB
    private static TransitDispatchRecordSessionBeanRemote transitDispatchRecordSessionBeanRemote;

    @EJB
    private static EjbTimerSessionBeanRemote ejbTimerSessionBeanRemote;

    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    @EJB
    private static OutletSessionBeanRemote outletSessionBeanRemote;

    @EJB
    private static CarCategorySessionBeanRemote carCategorySessionBeanRemote;

    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;

    @EJB
    private static RentalRateSessionBeanRemote rentalRateSessionBeanRemote;

    @EJB
    private static CarModelSessionBeanRemote carModelSessionBeanRemote;

    @EJB
    private static EmployeeSessionBeanRemote employeeSessionBeanRemote;

    
    
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        MainApp mainApp = new MainApp(employeeSessionBeanRemote, carModelSessionBeanRemote, carSessionBeanRemote, rentalRateSessionBeanRemote, carCategorySessionBeanRemote, outletSessionBeanRemote, reservationSessionBeanRemote, ejbTimerSessionBeanRemote, transitDispatchRecordSessionBeanRemote, customerSessionBeanRemote);
        mainApp.runApp();
        
    }
    
}
