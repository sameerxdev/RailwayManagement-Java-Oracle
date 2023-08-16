package com.company;

import java.sql.*;
import java.util.Scanner;

public class Feedback {
    int feedbackID;
    int rating;
    String description;
    String passengerCNIC;

    public Feedback(int rating, String description){
        this.feedbackID = 0;
        this.rating = rating;
        this.description = description;
    }

    public Feedback(String passngerCNIC) {
        this.passengerCNIC = passngerCNIC;
        Scanner t = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Feedback");
            System.out.println("2. View Feedbacks");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1) {
                add_feedback();
            }
            else if (input == 2){
                view_feedback();
            }
            else if (input == 0) {
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public void add_feedback() {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Rating from 1-5 : ");
        rating = input.nextInt();
        System.out.print("Enter your Feedback : ");
        description = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            int f_ID = auto_generateFeedbackID(con);
            if (f_ID == 0){
                System.out.println("Error Generating ID");
            }
            else{
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Feedback VALUES (?,?,?,?)");
                preparedStatement.setInt(1, f_ID);
                preparedStatement.setInt(2, rating);
                preparedStatement.setString(3, description);
                preparedStatement.setString(4, passengerCNIC);
                preparedStatement.executeQuery();
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            }
        }

    public void view_feedback(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * from Feedback");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+7+"s"+"%-"+10+"s"+"%-"+50+"s"+"%-"+15+"s","ID","Rating","Feedback","Passenger CNIC"));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+7+"s"+"%-"+10+"s"+"%-"+50+"s"+"%-"+15+"s",rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public int auto_generateFeedbackID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(feedbackID) from Feedback");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                feedbackID = rs.getInt(1) + 1;
            }
            else{
                feedbackID = 1;
            }
            return feedbackID;
        }
        catch(Exception e) {
            System.out.println(e);
            return feedbackID;
        }
    }
}
