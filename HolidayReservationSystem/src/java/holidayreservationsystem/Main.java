/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystem;

import ejb.session.stateless.CarSessionBeanRemote;
import ejb.session.stateless.CustomerSessionBeanRemote;
import ejb.session.stateless.PartnerSessionBeanRemote;
import ejb.session.stateless.ReservationSessionBeanRemote;
import javax.ejb.EJB;

/**
 *
 * @author nevanng
 */
public class Main {

    @EJB
    private static CarSessionBeanRemote carSessionBeanRemote;
    @EJB
    private static CustomerSessionBeanRemote customerSessionBeanRemote;
    @EJB
    private static PartnerSessionBeanRemote partnerSessionBeanRemote;
    @EJB
    private static ReservationSessionBeanRemote reservationSessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        MainApp mainApp = new MainApp(carSessionBeanRemote, customerSessionBeanRemote, partnerSessionBeanRemote, reservationSessionBeanRemote);
        mainApp.runApp();
    }
    
}
