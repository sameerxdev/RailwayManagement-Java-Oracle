package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Rates {
    int rateID;
    int no_of_seats;
    int totalCost;
    int routeID;
    int categoryID;
    String passengerCNIC;

    public Rates(int no_of_seats, int totalCost){
        this.rateID = 0;
        this.no_of_seats = no_of_seats;
        this.totalCost = totalCost;
    }

    public Rates(String passengerCNIC){
        this.passengerCNIC = passengerCNIC;
        Scanner t = new Scanner(System.in);
        while (true) {
            System.out.println("1. View Routes");
            System.out.println("2. View Category");
            System.out.println("3. Make a Package");
            System.out.println("4. View Packages");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1) {
                Route r =new Route("1");
                r.view_route();
            } else if (input == 2) {
                Category c = new Category("1");
                c.view_category();
            } else if (input == 3) {
                insert_Rate();
            } else if (input == 4) {
                view_Rates();
            } else if (input == 0) {
                break;
            } else{
                System.out.println("Invalid Input");
            }
        }
    }

    public Rates(String passengerCNIC, String arg){
        this.passengerCNIC = passengerCNIC;
    }

    public void insert_Rate(){
        Route r =new Route("1");
        Category c = new Category("1");
        routeID = r.select_Route();
        categoryID = c.select_Category();
        Scanner input = new Scanner(System.in);
        System.out.print("Enter No. of Seats : ");
        no_of_seats = input.nextInt();
        Calculate_Cost();
        System.out.println("Total Cost : " + totalCost);
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            rateID = auto_generateRateID(con);
            if (rateID == 0) {
                System.out.println("Error Generating ID");
            }
            else {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO RATES (RATEID,NO_OF_SEATS,ROUTE_ROUTEID,CATEGORY_CATEGORYID,TOTALCOST,PASSENGER_PASSENGERCNIC) VALUES (?,?,?,?,?,?)");
                preparedStatement.setInt(1,rateID );
                preparedStatement.setInt(2, no_of_seats);
                preparedStatement.setInt(3, routeID);
                preparedStatement.setInt(4, categoryID);
                preparedStatement.setInt(5, totalCost);
                preparedStatement.setString(6, passengerCNIC);
                preparedStatement.executeQuery();
            }
            con.close();
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void view_Rates(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT RATEID, STARTINGPOINT, ENDINGPOINT, NO_OF_SEATS, TOTALCOST, CATEGORYNAME, DEPARTURETIME, ARRIVALTIME, TRAINNO\n" +
                    "FROM RATES\n" +
                    "INNER JOIN ROUTE ON  ROUTE_ROUTEID=ROUTEID\n" +
                    "INNER JOIN CATEGORY ON CATEGORY_CATEGORYID=CATEGORYID\n" +
                    "INNER JOIN Train ON  TRAIN_TRAINID=TRAINID\n" +
                    "WHERE PASSENGER_PASSENGERCNIC = ? AND RATEID NOT IN(SELECT RATES_RATEID FROM PAYMENT)");
            preparedStatement.setString(1, passengerCNIC);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s","Rate ID","From","To","No of Seats","Total Cost","Category Name","Departure Time","Arrival Time","Train No."));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5),rs.getString(6),rs.getString(7),rs.getString(8),rs.getString(9)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public int Calculate_Cost(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT RATEPERCENT from CATEGORY where CATEGORYID=?");
            PreparedStatement preparedStatement2 = con.prepareStatement("SELECT FAIR from ROUTE where ROUTEID=?");
            preparedStatement1.setInt(1,categoryID);
            preparedStatement2.setInt(1, routeID);
            ResultSet rs1 = preparedStatement1.executeQuery();
            ResultSet rs2 = preparedStatement2.executeQuery();
            if (rs1.next() && rs2.next()) {
                float ratePer = rs1.getFloat(1);
                int rate = rs2.getInt(1);
                totalCost = (int) (rate*ratePer*no_of_seats);
            }
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return totalCost;
    }

    public boolean exist_rate(int rateID){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkCategory = con.prepareStatement("SELECT * from RATES Where RATEID=? AND PASSENGER_PASSENGERCNIC=?");
            checkCategory.setInt(1,rateID);
            checkCategory.setString(2,passengerCNIC);
            ResultSet rsc = checkCategory.executeQuery();
            if (rsc.next()){
                return true;
            }
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            e.printStackTrace();
        }
        return false;
    }

    public boolean rate_notInPayment(int rateID){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkCategory = con.prepareStatement("SELECT * from RATES WHERE ? NOT IN(SELECT RATES_RATEID FROM PAYMENT)");
            checkCategory.setInt(1,rateID);
            ResultSet rsc = checkCategory.executeQuery();
            if (rsc.next()){
                return true;
            }
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return false;
    }

    public int auto_generateRateID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(RATEID) from RATES");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                rateID = rs.getInt(1) + 1;
            }
            else{
                rateID = 1;
            }
            return rateID;
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return rateID;
        }
    }
}
