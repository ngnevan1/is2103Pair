/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package holidayreservationsystemwebclient;

import java.util.Scanner;

/**
 *
 * @author KMwong
 */

public class MainApp {
    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
    
        while(true) {
            System.out.println("*** Welcome to Holiday Reservation System ***\n");
            System.out.println("1: Partner Login");
            System.out.println("2: Partner Search Car");
            System.out.println("3: Exit\n");
            response = 0;
            
            while(response < 1 || response > 3) {
                System.out.print("> ");
                response = scanner.nextInt();

                if(response == 1) {
                    partnerLogin();
                    System.out.println("Login successful!\n");
                    menuMain();
                }
                else if(response == 2) {
                    partnerSearchCar();
                }
                else if (response == 3) {
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 3)
            {
                break;
            }
        }
    }
    
    public void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;
        
        while(true) {
            System.out.println("*** Welcome to Holiday Reservation System :: Main Menu ***\n");
            System.out.println("1: Partner Search Car");
            System.out.println("2: Partner View All Reservations");
            System.out.println("3: Partner View Reservation Details");
            System.out.println("4: Partner Cancel Reservation");
            System.out.println("5: Logout\n");
            response = 0;
            
            while(response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();
                scanner.nextLine();

                if(response == 1) {
                    partnerSearchCar();
                }
                else if(response == 2) {
                    partnerViewAllReservations();
                }
                else if(response == 3) {
                    partnerViewReservationDetails();
                }
                else if(response == 4) {
                    partnerCancelReservation();
                }
                else if (response == 5) {
                    System.out.println("Logged Out Successfully!\n");
                    break;
                }
                else {
                    System.out.println("Invalid option, please try again!\n");                
                }
            }
            
            if(response == 5) {
                System.out.println("Logged Out Successfully!\n");
                break;
            }
        }
    }
    
    public void partnerLogin() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner Login ***\n");
    }
    
    public void partnerSearchCar() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner Search Car ***\n");
    }
    
    public void partnerViewAllReservations() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner View All Reservations ***\n");
    }
    
    public void partnerViewReservationDetails() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner View Reservation Details ***\n");
    }
    
    public void partnerCancelReservation() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Holiday Reservation System :: Partner Cancel Reservation ***\n");
    }
}
