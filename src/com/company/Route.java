package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Route {
    int routeID;
    String startingPoint;
    String endingPoint;
    String departureTime;
    String arrivalTime;
    int fair;
    int distanceInKms;
    int train_id;

    public Route(int routeID, String startingPoint, String endingPoint, String departureTime, String arrivalTime, int fair, int distanceInKms){
    this.routeID = routeID;
    this.startingPoint = startingPoint;
    this.endingPoint = endingPoint;
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
    this.fair = fair;
    this.distanceInKms = distanceInKms;
    }

    public Route(int arg){
        Scanner t = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Route Record");
            System.out.println("2. View Route Record");
            System.out.println("3. Update Route Record");
            System.out.println("4. Delete Route Record");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1) {
                add_Route();
            }
            else if (input == 2){
                view_route();
            }
            else if (input == 3){
                update_Route();
            }
            else if (input == 4){
                delete_Route();
            }
            else if (input == 0) {
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public Route(String arg){
    }

    public void add_Route() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Starting Point : ");
        startingPoint = input.next();
        System.out.print("Enter Ending Point : ");
        endingPoint = input.next();
        System.out.print("Enter Departure Time : ");
        departureTime = input.next();
        System.out.print("Enter Arrival Time : ");
        arrivalTime = input.next();
        System.out.print("Enter Fair : ");
        fair = input.nextInt();
        System.out.print("Enter Distance (KMs) : ");
        distanceInKms = input.nextInt();
        Train t = new Train();
        t.show_availableTRAINS();
        System.out.print("Enter Train ID : ");
        train_id = input.nextInt();
        if (t.exist_train(train_id)) {

            try {
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
                int r_ID = auto_generateRouteID(con);
                if (r_ID == 0) {
                    System.out.println("Error Generating ID");
                }
                else {
                    PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO ROUTE VALUES (?,?,?,?,?,?,?,'admin',?)");
                    PreparedStatement preparedStatement1 = con.prepareStatement("UPDATE Train SET TRAINSTATUS = 'Y' Where TRAINID=?");
                    preparedStatement.setInt(1, r_ID);
                    preparedStatement.setString(2, startingPoint);
                    preparedStatement.setString(3, endingPoint);
                    preparedStatement.setString(4, departureTime);
                    preparedStatement.setString(5, arrivalTime);
                    preparedStatement.setInt(6, fair);
                    preparedStatement.setInt(7, distanceInKms);
                    preparedStatement.setInt(8, train_id);
                    preparedStatement1.setInt(1,train_id);
                    preparedStatement.executeQuery();
                    preparedStatement1.executeQuery();
                    System.out.println("Route added Successfully");
                }
                con.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        else {
            System.out.println("Train Id not found");
        }
    }

    public void view_route(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT ROUTEID, STARTINGPOINT, ENDINGPOINT, DEPARTURETIME, ARRIVALTIME, FAIR, DISTANCEINKMS, TRAIN_TRAINID, TRAINNO\n" +
                    "FROM Route\n" +
                    "INNER JOIN Train ON TRAIN_TRAINID=TRAINID");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+25+"s"+"%-"+25+"s"+"%-"+12+"s"+"%-"+15+"s"+"%-"+10+"s"+"%-"+10+"s","Route ID","From","To","Departure","Arrival","Fair","Distance(KMs)","Train ID","Train No."));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+25+"s"+"%-"+25+"s"+"%-"+12+"s"+"%-"+15+"s"+"%-"+10+"s"+"%-"+10+"s",rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getInt(7),rs.getInt(8),rs.getString(9)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void update_Route() {
        Scanner input = new Scanner(System.in);
        int r_id;
        System.out.print("Enter Route ID you want to Update : ");
        r_id = input.nextInt();
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkRoute = con.prepareStatement("SELECT * FROM ROUTE  WHERE ROUTEID = ?");
            checkRoute.setInt(1,r_id);
            ResultSet rsc = checkRoute.executeQuery();
            if(rsc.next()){
                System.out.print("Enter New Route Starting Point : ");
                startingPoint = input.next();
                System.out.print("Enter New Route Ending Point : ");
                endingPoint = input.next();
                System.out.print("Enter New Departure Time : ");
                departureTime = input.next();
                System.out.print("Enter New Arrival Time : ");
                arrivalTime = input.next();
                System.out.print("Enter New Fair : ");
                fair = input.nextInt();
                System.out.print("Enter New Distance (KMs) : ");
                distanceInKms = input.nextInt();
                PreparedStatement preparedStatement = con.prepareStatement("UPDATE ROUTE SET STARTINGPOINT=?, ENDINGPOINT=?, DEPARTURETIME=?, ARRIVALTIME=?, FAIR=?, DISTANCEINKMS=? WHERE ROUTEID=?");
                preparedStatement.setString(1, startingPoint);
                preparedStatement.setString(2, endingPoint);
                preparedStatement.setString(3, departureTime);
                preparedStatement.setString(4, arrivalTime);
                preparedStatement.setInt(5, fair);
                preparedStatement.setInt(6, distanceInKms);
                preparedStatement.setInt(7, r_id);
                preparedStatement.executeQuery();
                System.out.println("Route Updated Successfully");
                con.close();
            }
            else {
                System.out.println("Route Id not found");
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }


    public void delete_Route(){
        Scanner input = new Scanner(System.in);
        int r_id;
        System.out.print("Enter Route ID you want to Delete : ");
        r_id = input.nextInt();
        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement checkRoute = con.prepareStatement("SELECT TRAIN_TRAINID FROM ROUTE  WHERE ROUTEID = ?");
            checkRoute.setInt(1,r_id);
            ResultSet rsc = checkRoute.executeQuery();
            if (rsc.next()) {
                PreparedStatement preparedStatement1 = con.prepareStatement("UPDATE TRAIN SET TRAINSTATUS = 'N' WHERE TRAINID=?");
                PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM ROUTE WHERE ROUTEID = ?");
                preparedStatement1.setInt(1, rsc.getInt(1));
                preparedStatement.setInt(1, r_id);
                preparedStatement.executeQuery();
                preparedStatement1.executeQuery();
                System.out.println("Route Deleted Successfully");
            }
            else{
                System.out.println("Route ID not Found");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public int select_Route(){
        view_route();
        int c;
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Select Route ID : ");
            c = input.nextInt();
            try {
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
                PreparedStatement checkRoute = con.prepareStatement("SELECT * FROM ROUTE  WHERE ROUTEID = ?");
                checkRoute.setInt(1, c);
                ResultSet rsc = checkRoute.executeQuery();
                if (rsc.next()){
                    break;
                }
                else {
                    System.out.println("Route ID not Found");
                }
            }
            catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        return c;
    }

    public boolean exist_route(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkRoute = con.prepareStatement("SELECT * FROM ROUTE");
            ResultSet rsc = checkRoute.executeQuery();
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

    public int auto_generateRouteID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(routeID) from Route");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                routeID = rs.getInt(1) + 1;
            }
            else{
                routeID = 1;
            }
            return routeID;
        }
        catch(Exception e) {
            System.out.println(e);
            return routeID;
        }
    }
}
