package com.company;

import java.sql.*;
import java.util.Scanner;

public class Passenger {
    String passengerCNIC;
    String passengerPassword;
    String passengerName;
    String passengerPhone;

    public Passenger( String passengerCNIC, String passengerPassword, String passengerName, String passengerPhone) {
        this.passengerCNIC = passengerCNIC;
        this.passengerPassword = passengerPassword;
        this.passengerName = passengerName;
        this.passengerPhone = passengerPhone;
    }

    public Passenger(String b){
        Scanner t = new Scanner(System.in);
        while (true){
            System.out.println("1. Passenger Signup");
            System.out.println("2. Passenger Login");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1){
                signup_passenger();
            }
            else if (input == 2){
                login_passenger();
                break;
            }
            else if (input == 0){
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public void signup_passenger() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your CNIC : ");
        passengerCNIC = input.next();
        System.out.print("Enter your Password : ");
        passengerPassword = input.next();
        System.out.print("Enter your Name : ");
        passengerName = input.next();
        System.out.print("Enter your Phone number : ");
        passengerPhone = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO DBPROJECT.PASSENGER (PASSENGERCNIC, PASSENGERPASSWORD, PASSENGERNAME, PASSENGERPHONE) VALUES('" + passengerCNIC + "','" + passengerPassword + "','" + passengerName + "','" + passengerPhone +"')");
            preparedStatement.executeQuery();
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void login_passenger(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter your CNIC : ");
        passengerCNIC = input.next();
        System.out.print("Enter your Password : ");
        passengerPassword = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT PASSENGERCNIC FROM Passenger WHERE PASSENGERCNIC = '"+passengerCNIC+"'AND PASSENGERPASSWORD = '" + passengerPassword+"'");
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                Scanner t = new Scanner(System.in);
                while (true){
                    Scanner in = new Scanner(System.in);
                    System.out.println("1. Add/View Feedback");
                    System.out.println("2. Select/View Routes");
                    System.out.println("3. Proceed/View Payments");
                    System.out.println("4. View Ticket");
                    System.out.println("Enter 0 to Exit");
                    System.out.print("Choose your Option: ");
                    int x = in.nextInt();
                    if (x == 1) {
                        Feedback f = new Feedback(passengerCNIC);
                    }
                    else if (x == 2) {
                        Rates rt = new Rates(passengerCNIC);
                    }
                    else if (x == 2) {
                        Rates rt = new Rates(passengerCNIC);
                    }
                    else if (x == 3) {
                        Payment py = new Payment(passengerCNIC);
                    }
                    else if (x == 4) {
                        Ticket tk = new Ticket(passengerCNIC);
                        tk.view_Ticket();
                    }
                    else if (x == 0){
                        break;
                    }
                    else{
                        System.out.println("Invalid Input");
                    }
                }
            }
            else{
                System.out.println("Invalid CNIC or Password");
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
