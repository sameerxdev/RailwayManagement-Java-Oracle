package com.company;

import java.sql.*;
import java.util.Scanner;

public class Station {
    int stationID;
    String stationName;
    String city;
    String contact;

    public Station(String stationName, String city, String contact){
        this.stationID = 0;
        this.stationName = stationName;
        this.city = city;
        this.contact = contact;
    }

    public Station(int arg){
        Scanner t = new Scanner(System.in);
        while (true){
            System.out.println("1. Add Station Record");
            System.out.println("2. View Station Record");
            System.out.println("3. Update Station Record");
            System.out.println("4. Delete Station Record");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1){
                add_station();
            }
            else if (input == 2){
                view_station();
            }
            else if(input == 0){
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public void add_station(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Station Name : ");
        stationName = input.next();
        System.out.print("Enter Station City : ");
        city = input.next();
        System.out.print("Enter Station Contact : ");
        contact = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            int s_ID = auto_generateStationID(con);
            if (s_ID == 0){
                System.out.println("Error Generating ID");
            }
            else {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Station VALUES (?,?,?,?,'admin')");
                preparedStatement.setInt(1, s_ID);
                preparedStatement.setString(2, stationName);
                preparedStatement.setString(3, city);
                preparedStatement.setString(4, contact);
                preparedStatement.executeQuery();
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void view_station(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * from Station");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+15+"s"+"%-"+25+"s"+"%-"+20+"s"+"%-"+20+"s","Station ID","Station Name","City","Contact"));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+15+"s"+"%-"+25+"s"+"%-"+20+"s"+"%-"+20+"s",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public int auto_generateStationID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(stationID) from Station");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                stationID = rs.getInt(1) + 1;
            }
            else{
                stationID = 1;
            }
            return stationID;
        }
        catch(Exception e) {
            System.out.println(e);
            return stationID;
        }
    }
}
