package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Admin {
    String username;
    String password;
    private Object Payment;

    public Admin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Admin(){
    }

    public void login_admin(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Username : ");
        username = input.next();
        System.out.print("Enter Password : ");
        password = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT USERNAME FROM Admin WHERE USERNAME = '"+username+"'AND PASSWORD = '" + password+"'");
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                while (true) {
                    Scanner in = new Scanner(System.in);
                    System.out.println("1. Train");
                    System.out.println("2. Routes");
                    System.out.println("3. Categories");
                    System.out.println("4. Stations");
                    System.out.println("5. Drivers");
                    System.out.println("6. Ticket Collectors");
                    System.out.println("7. View Payments");
                    System.out.println("Press 0 to Exit");
                    System.out.print("Choose your Option : ");
                    int a_menu = in.nextInt();
                    if (a_menu == 1) {
                        Train t1 = new Train(1);
                    }
                    else if (a_menu == 2) {
                        Route r1 = new Route(1);
                    }
                    else if (a_menu == 3) {
                        Category c1 = new Category(1);
                    }
                    else if (a_menu == 4) {
                        Station s1 = new Station(1);
                    }
                    else if (a_menu == 5) {
                        Driver d1 = new Driver(1);
                    }
                    else if (a_menu == 6) {
                        TicketCollector tc1 = new TicketCollector(1);
                    }
                    else if (a_menu == 7) {
                        Payment = new Payment(1);
                    }
                    else if (a_menu == 0) {
                        break;
                    }
                    else {
                        System.out.println("Enter Valid Input");
                    }
                }
            }
            else{
                System.out.println("Invalid Username or Password");
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }
}
