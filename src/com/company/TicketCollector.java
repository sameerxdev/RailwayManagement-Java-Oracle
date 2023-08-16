package com.company;

import java.sql.*;
import java.util.Scanner;

public class TicketCollector {
    String tc_CNIC;
    String tcName;
    String tcPhone;

    public TicketCollector(String tc_CNIC, String tcName, String tcPhone){
        this.tc_CNIC = tc_CNIC;
        this.tcName = tcName;
        this.tcPhone = tcPhone;
    }

    public TicketCollector(int arg){
        Scanner t = new Scanner(System.in);
        while (true){
            System.out.println("1. Add Ticket Collector Record");
            System.out.println("2. View Ticket Collector Record");
            System.out.println("3. Update Ticket Collector Record");
            System.out.println("4. Delete Ticket Collector Record");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1){
                add_TicketCollector();
            }
            else if (input == 2){
                view_ticketCollector();
            }
            else if (input == 3){
                update_TicketCollector();
            }
            else if (input == 4){
                delete_TicketCollector();
            }
            else if(input == 0){
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public void add_TicketCollector() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Ticket Collector CNIC : ");
        tc_CNIC = input.next();
        System.out.print("Enter Ticket Collector Name : ");
        tcName = input.next();
        System.out.print("Enter Ticket Collector Phone number : ");
        tcPhone = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO TICKET_COLLECTOR VALUES('"+ tc_CNIC + "','" + tcName + "','" + tcPhone +"')");
            preparedStatement.executeUpdate();
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void view_ticketCollector(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * from TICKET_COLLECTOR");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+20+"s"+"%-"+30+"s"+"%-"+20+"s","TC CNIC","TC Name","Phone No."));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+20+"s"+"%-"+30+"s"+"%-"+30+"s",rs.getString(1),rs.getString(2),rs.getString(3)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void update_TicketCollector() {
        Scanner input = new Scanner(System.in);
        String tcnic;
        System.out.print("Enter Ticket Collector CNIC you want to Update : ");
        tcnic = input.next();
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkticketCollector = con.prepareStatement("SELECT * FROM TICKET_COLLECTOR  WHERE TC_CNIC = ?");
            checkticketCollector.setString(1,tcnic);
            ResultSet rsc = checkticketCollector.executeQuery();
            if(rsc.next()){
                System.out.print("Enter New Ticket Collector Name : ");
                String tname = input.next();
                System.out.print("Enter New Ticket Collector Phone No. : ");
                String tphone = input.next();
                PreparedStatement preparedStatement = con.prepareStatement("UPDATE TICKET_COLLECTOR SET TCNAME = ?, TCPHONE = ? WHERE TC_CNIC = ?");
                preparedStatement.setString(1, tname);
                preparedStatement.setString(2, tphone);
                preparedStatement.setString(3, tcnic);
                preparedStatement.executeQuery();
            }
            else{
                System.out.println("Ticket Collector CNIC not Found");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void delete_TicketCollector(){
        Scanner input = new Scanner(System.in);
        String tcnic;
        System.out.print("Enter Ticket Collector CNIC you want to Delete : ");
        tcnic = input.next();
        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement checkDriver = con.prepareStatement("SELECT * FROM TICKET_COLLECTOR WHERE TC_CNIC = ?");
            checkDriver.setString(1,tcnic);
            ResultSet rsc = checkDriver.executeQuery();
            if (rsc.next()) {
                PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM TICKET_COLLECTOR WHERE TC_CNIC = ?");
                preparedStatement.setString(1, tcnic);
                preparedStatement.executeQuery();
            }
            else{
                System.out.println("Ticket Collector CNIC not Found");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
