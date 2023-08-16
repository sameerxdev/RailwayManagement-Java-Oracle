package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Train {
    int trainID;
    String trainNo;
    int capacity;

    public Train(int trainID, String trainNo, int capacity) {
        this.trainID = 0;
        this.trainNo = trainNo;
        this.capacity = capacity;
    }

    public Train(int arg) {
        Scanner t = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Train Record");
            System.out.println("2. View Train Record");
            System.out.println("3. Update Train Record");
            System.out.println("4. Delete Train Record");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1) {
                add_Train();
            } else if (input == 2) {
                view_Train();
            } else if (input == 3) {
                update_Train();
            } else if (input == 4) {
                delete_Train();
            } else if (input == 0) {
                break;
            } else{
                System.out.println("Invalid Input");
            }
        }
    }

    public Train(){
    }

    public void add_Train(){
        Scanner input = new Scanner(System.in);
        String d_cnic, tc_cnic;
        System.out.print("Enter Train Number : ");
        trainNo = input.next();
        System.out.print("Enter Train Capacity : ");
        capacity = input.nextInt();
        System.out.print("Enter Driver CNIC : ");
        d_cnic = input.next();
        System.out.print("Enter TC CNIC : ");
        tc_cnic = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            int t_ID = auto_generateTrainID(con);
            if (t_ID == 0){
                System.out.println("Error Generating ID");
            }
            else {
                PreparedStatement checkDriver = con.prepareStatement("SELECT * FROM DRIVER  WHERE DRIVERCNIC = ?");
                PreparedStatement checkTC = con.prepareStatement("SELECT * FROM TICKET_COLLECTOR  WHERE TC_CNIC = ?");
                checkDriver.setString(1,d_cnic);
                checkTC.setString(1, tc_cnic);

                ResultSet rsd = checkDriver.executeQuery();
                ResultSet rstc = checkTC.executeQuery();
                if (rsd.next() && rstc.next()){
                    PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Train VALUES (?,?,?,'N','admin',?,?)");
                    preparedStatement.setInt(1, t_ID);
                    preparedStatement.setString(2, trainNo);
                    preparedStatement.setInt(3, capacity);
                    preparedStatement.setString(4, d_cnic);
                    preparedStatement.setString(5, tc_cnic);
                    preparedStatement.executeQuery();
                }
                else {
                    System.out.println("Driver CNIC or TC CNIC not Found");
                }
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void view_Train(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("" +
                    "SELECT TRAINID,TRAINNO,CAPACITY,DRIVER_DRIVERCNIC,DRIVERNAME,TICKET_COLLECTOR_TC_CNIC,TCNAME\n" +
                    "FROM Train\n" +
                    "INNER JOIN Ticket_Collector ON  TICKET_COLLECTOR_TC_CNIC=tc_cnic\n" +
                    "INNER JOIN Driver ON DRIVER_DRIVERCNIC=DRIVERCNIC");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s","Train ID","Train No.","Capacity","Driver CNIC","Driver Name","TC CNIC","TC Name"));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s",rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void update_Train() {
        Scanner input = new Scanner(System.in);
        int t_id;
        String dcnic, tccnic;
        System.out.print("Enter Train ID you want to Update : ");
        t_id = input.nextInt();
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkTrain = con.prepareStatement("SELECT * FROM TRAIN  WHERE TRAINID = ?");
            checkTrain.setInt(1,t_id);
            ResultSet rst = checkTrain.executeQuery();
            if(rst.next()){
                System.out.print("Enter New Train Number : ");
                trainNo = input.next();
                System.out.print("Enter New Train Capacity : ");
                capacity = input.nextInt();
                System.out.print("Enter New Driver CNIC : ");
                dcnic = input.next();
                System.out.print("Enter New TC CNIC : ");
                tccnic = input.next();
                PreparedStatement checkDriver = con.prepareStatement("SELECT * FROM DRIVER  WHERE DRIVERCNIC = ?");
                PreparedStatement checkTC = con.prepareStatement("SELECT * FROM TICKET_COLLECTOR  WHERE TC_CNIC = ?");
                checkDriver.setString(1,dcnic);
                checkTC.setString(1, tccnic);
                ResultSet rs_d = checkDriver.executeQuery();
                ResultSet rs_tc = checkTC.executeQuery();

                if (rs_d.next() && rs_tc.next()){
                    PreparedStatement preparedStatement = con.prepareStatement("UPDATE TRAIN SET TRAINNO = ?, CAPACITY = ?, DRIVER_DRIVERCNIC = ?, TICKET_COLLECTOR_TC_CNIC = ? WHERE TRAINID = ?");
                    preparedStatement.setString(1, trainNo);
                    preparedStatement.setInt(2, capacity);
                    preparedStatement.setString(3, dcnic);
                    preparedStatement.setString(4, tccnic);
                    preparedStatement.setInt(5, t_id);
                    preparedStatement.executeQuery();
                }
                else{
                System.out.println("Driver CNIC or TC CNIC not Found or already assigned");
                }
                con.close();
            }
            else{
                System.out.println("Train ID not Found");
            }
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void delete_Train(){
        Scanner input = new Scanner(System.in);
        int t_id;
        System.out.print("Enter Train ID you want to Delete : ");
        t_id = input.nextInt();
        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement checkTrain = con.prepareStatement("SELECT * FROM TRAIN  WHERE TRAINID = ?");
            checkTrain.setInt(1,t_id);
            ResultSet rsc = checkTrain.executeQuery();
            if (rsc.next()) {
                PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM TRAIN WHERE TRAINID = ?");
                preparedStatement.setInt(1, t_id);
                preparedStatement.executeQuery();
            }
            else{
                System.out.println("Train ID not Found");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void show_availableTRAINS(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("" +
                    "SELECT TRAINID,TRAINNO,CAPACITY,DRIVER_DRIVERCNIC,DRIVERNAME,TICKET_COLLECTOR_TC_CNIC,TCNAME\n" +
                    "FROM Train\n" +
                    "INNER JOIN Ticket_Collector ON  TICKET_COLLECTOR_TC_CNIC=tc_cnic\n" +
                    "INNER JOIN Driver ON DRIVER_DRIVERCNIC=DRIVERCNIC\n" +
                    "WHERE TRAINSTATUS='N'");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s","Train ID","Train No.","Capacity","Driver CNIC","Driver Name","TC CNIC","TC Name"));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s",rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public boolean exist_train(int trainID){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkCategory = con.prepareStatement("SELECT * from Train Where TRAINSTATUS='N' and TRAINID=?");
            checkCategory.setInt(1,trainID);
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

    public int auto_generateTrainID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(trainID) from Train");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                trainID = rs.getInt(1) + 1;
            }
            else{
                trainID = 1;
            }
            return trainID;
        }
        catch(Exception e) {
            System.out.println(e);
            return trainID;
        }
    }
}
