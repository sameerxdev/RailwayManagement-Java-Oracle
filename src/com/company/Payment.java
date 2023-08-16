package com.company;

import jdk.jshell.Snippet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Payment {
    int paymentID;
    String passengerCNIC;
    int rateID;

    public Payment(int arg){
        view_All_PaymentforAdmin();
    }

    public Payment(String passengerCNIC){
        this.passengerCNIC = passengerCNIC;
        Scanner t = new Scanner(System.in);
        while (true) {
            System.out.println("1. Proceed Payment");
            System.out.println("2. View Payments");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1) {
                System.out.println("1. Add Payment");
                System.out.println("2. Pay Charges");
                System.out.print("Choose your Option : ");
                int input1 = t.nextInt();
                if (input1 == 1) {
                    add_Payment();
                }
                else if(input1 == 2){
                    pay_Dues();
                }
            }
            else if (input == 2) {
                view_All_Payment();
            }
            else if (input == 0) {
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public void add_Payment(){
        Rates r = new Rates(passengerCNIC, "a");
        r.view_Rates();
        Scanner input = new Scanner(System.in);
        System.out.print("Select Rate ID : ");
        rateID = input.nextInt();
        if (r.exist_rate(rateID)){
            if(r.rate_notInPayment(rateID)){
                try {
                    Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
                    paymentID = auto_generatePaymentID(con);
                    if (paymentID == 0) {
                        System.out.println("Error Generating ID");
                    }
                    else {
                        PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO PAYMENT (PAYMENTID, PAYMENTSTATUS, PASSENGER_PASSENGERCNIC, RATES_RATEID) VALUES (?,'N',?,?)");
                        preparedStatement.setInt(1,paymentID );
                        preparedStatement.setString(2, passengerCNIC);
                        preparedStatement.setInt(3, rateID);
                        preparedStatement.executeQuery();
                    }
                    con.close();
                }
                catch (Exception e) {
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Payment already Proceeded for this Rate ID");
            }
        }
        else {
            System.out.println("Rate ID not Found");
        }
    }

    public void view_Payment(){
        try{
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT PAYMENTID, PAYMENTSTATUS, TOTALCOST, STARTINGPOINT, ENDINGPOINT, NO_OF_SEATS, CATEGORYNAME, TRAINNO\n" +
                    "FROM PAYMENT\n" +
                    "INNER JOIN RATES ON RATES_RATEID=RATEID\n" +
                    "INNER JOIN ROUTE ON  ROUTE_ROUTEID=ROUTEID\n" +
                    "INNER JOIN CATEGORY ON CATEGORY_CATEGORYID=CATEGORYID\n" +
                    "INNER JOIN Train ON  TRAIN_TRAINID=TRAINID\n" +
                    "WHERE PAYMENT.PASSENGER_PASSENGERCNIC = ? AND PAYMENTSTATUS='N'");
            preparedStatement.setString(1, passengerCNIC);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s","Payment ID","Payment Status","Total Cost","From","To","No of Seats","Category Name","Train No."));
            System.out.println();
            while (rs.next()){
                String Status;
                if (rs.getString(2).equals("N")){
                    Status = "Unpaid";
                }
                else{
                    Status = "Paid";
                }
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s",rs.getInt(1),Status,rs.getInt(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8)));
            }
            con.close();
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void view_All_Payment(){
        try{
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT PAYMENTID, PAYMENTSTATUS, TOTALCOST, STARTINGPOINT, ENDINGPOINT, NO_OF_SEATS, CATEGORYNAME, TRAINNO\n" +
                    "FROM PAYMENT\n" +
                    "INNER JOIN RATES ON RATES_RATEID=RATEID\n" +
                    "INNER JOIN ROUTE ON  ROUTE_ROUTEID=ROUTEID\n" +
                    "INNER JOIN CATEGORY ON CATEGORY_CATEGORYID=CATEGORYID\n" +
                    "INNER JOIN Train ON  TRAIN_TRAINID=TRAINID\n" +
                    "WHERE PAYMENT.PASSENGER_PASSENGERCNIC = ?");
            preparedStatement.setString(1, passengerCNIC);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s","Payment ID","Payment Status","Total Cost","From","To","No of Seats","Category Name","Train No."));
            System.out.println();
            while (rs.next()){
                String Status;
                if (rs.getString(2).equals("N")){
                    Status = "Unpaid";
                }
                else{
                    Status = "Paid";
                }
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s",rs.getInt(1),Status,rs.getInt(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8)));
            }
            con.close();
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void view_All_PaymentforAdmin(){
        try{
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT PAYMENTID, PAYMENTSTATUS, TOTALCOST, STARTINGPOINT, ENDINGPOINT, NO_OF_SEATS, CATEGORYNAME, TRAINNO\n" +
                    "FROM PAYMENT\n" +
                    "INNER JOIN RATES ON RATES_RATEID=RATEID\n" +
                    "INNER JOIN ROUTE ON  ROUTE_ROUTEID=ROUTEID\n" +
                    "INNER JOIN CATEGORY ON CATEGORY_CATEGORYID=CATEGORYID\n" +
                    "INNER JOIN Train ON  TRAIN_TRAINID=TRAINID");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s","Payment ID","Payment Status","Total Cost","From","To","No of Seats","Category Name","Train No."));
            System.out.println();
            while (rs.next()){
                String Status;
                if (rs.getString(2).equals("N")){
                    Status = "Unpaid";
                }
                else{
                    Status = "Paid";
                }
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s",rs.getInt(1),Status,rs.getInt(3),rs.getString(4),rs.getString(5),rs.getInt(6),rs.getString(7),rs.getString(8)));
            }
            con.close();
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void pay_Dues(){
        view_Payment();
        Scanner input = new Scanner(System.in);
        System.out.print("Select Payment ID : ");
        paymentID = input.nextInt();
        int a;
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkPayment = con.prepareStatement("SELECT * FROM PAYMENT  WHERE PAYMENTID = ? AND PAYMENTSTATUS ='N'");
            checkPayment.setInt(1,paymentID);
            ResultSet rsp = checkPayment.executeQuery();
            if (rsp.next()){
                System.out.print("Enter 1 to Pay : ");
                a = input.nextInt();
                if(a==1){
                    PreparedStatement preparedStatement = con.prepareStatement("UPDATE PAYMENT SET PAYMENTSTATUS='Y' WHERE PAYMENTID = ?");
                    preparedStatement.setInt(1,paymentID);
                    preparedStatement.executeQuery();
                    System.out.println("Payment Paid Successfully");
                    PreparedStatement preparedStatement1 = con.prepareStatement("SELECT TRAIN_TRAINID\n" +
                            "FROM RATES\n" +
                            "INNER JOIN ROUTE ON ROUTEID=ROUTE_ROUTEID\n" +
                            "WHERE RATEID = ?");
                    preparedStatement1.setInt(1, rateID);
                    ResultSet rs = preparedStatement1.executeQuery();
                    if (rs.next()){
                        int trainID = rs.getInt(1);
                        Ticket t1 = new Ticket(passengerCNIC);
                        t1.populate_Ticket(trainID,passengerCNIC, paymentID, rateID);
                    }
                }
                else{
                    System.out.println("Invalid Input");
                }
            }
            else{
                System.out.println("Payment ID not Found or already Paid");
            }
        }
        catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public int auto_generatePaymentID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(paymentID) from PAYMENT");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                paymentID = rs.getInt(1) + 1;
            }
            else{
                paymentID = 1;
            }
            return paymentID;
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
            return paymentID;
        }
    }
}
