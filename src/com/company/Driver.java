package com.company;

import java.sql.*;
import java.util.Scanner;

public class Driver {
    String driverCNIC;
    String driverName;
    String driverPhone;

    public Driver(String driverCNIC, String driverName, String driverPhone){
        this.driverCNIC = driverCNIC;
        this.driverName = driverName;
        this.driverPhone = driverPhone;
    }

    public Driver(int arg){
        Scanner t = new Scanner(System.in);
        while (true){
            System.out.println("1. Add Driver Record");
            System.out.println("2. View Driver Record");
            System.out.println("3. Update Driver Record");
            System.out.println("4. Delete Driver Record");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1){
                add_driver();
            }
            else if (input == 2){
                view_driver();
            }
            else if( input == 3){
                update_Driver();
            }
            else if( input == 4){
                delete_Driver();
            }
            else if(input == 0){
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public void add_driver() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Driver CNIC : ");
        driverCNIC = input.next();
        System.out.print("Enter Driver Name : ");
        driverName = input.next();
        System.out.print("Enter Driver Phone number : ");
        driverPhone = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Driver VALUES('"+ driverCNIC + "','" + driverName + "','" + driverPhone +"')");
            preparedStatement.executeUpdate();
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void view_driver(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * from Driver");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+20+"s"+"%-"+30+"s"+"%-"+20+"s","Driver CNIC","Driver Name","Phone No."));
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

    public void update_Driver() {
        Scanner input = new Scanner(System.in);
        String d_id;
        System.out.print("Enter Driver CNIC you want to Update : ");
        d_id = input.next();
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkDriver = con.prepareStatement("SELECT * FROM DRIVER  WHERE DRIVERCNIC = ?");
            checkDriver.setString(1,d_id);
            ResultSet rsc = checkDriver.executeQuery();
            if(rsc.next()){
                System.out.print("Enter New Driver Name : ");
                String d_name = input.next();
                System.out.print("Enter New Driver Phone number : ");
                String d_phone = input.next();
                PreparedStatement preparedStatement = con.prepareStatement("UPDATE DRIVER SET DRIVERNAME = ?, DRIVERPHONE = ? WHERE DRIVERCNIC = ?");
                preparedStatement.setString(1, d_name);
                preparedStatement.setString(2, d_phone);
                preparedStatement.setString(3, d_id);
                preparedStatement.executeQuery();
            }
            else{
                System.out.println("Driver CNIC not Found");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void delete_Driver(){
        Scanner input = new Scanner(System.in);
        String dcnic;
        System.out.print("Enter Driver CNIC you want to Delete : ");
        dcnic = input.next();
        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement checkDriver = con.prepareStatement("SELECT * FROM DRIVER  WHERE DRIVERCNIC = ?");
            checkDriver.setString(1,dcnic);
            ResultSet rsc = checkDriver.executeQuery();
            if (rsc.next()) {
                PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM DRIVER WHERE DRIVERCNIC = ?");
                preparedStatement.setString(1, dcnic);
                preparedStatement.executeQuery();
            }
            else{
                System.out.println("Driver CNIC not Found");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
