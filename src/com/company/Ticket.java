package com.company;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Ticket {
    int ticketID;
    int trainID;
    String passengerCNIC;
    int paymentID;
    int rateID;

    public Ticket() {
        this.ticketID = ticketID;
    }

    public Ticket(String passengerCNIC) {
        this.passengerCNIC = passengerCNIC;


    }

    public void populate_Ticket(int trainID, String passengerCNIC, int paymentID, int rateID){
        try{
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            ticketID = auto_generateTicketID(con);
            if (ticketID == 0) {
                System.out.println("Error Generating ID");
            }
            else {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO TICKET (TICKETID, TRAIN_TRAINID, PASSENGER_PASSENGERCNIC, PAYMENT_PAYMENTID, RATES_RATEID) VALUES (?,?,?,?,?)");
                preparedStatement.setInt(1, ticketID);
                preparedStatement.setInt(2, trainID);
                preparedStatement.setString(3, passengerCNIC);
                preparedStatement.setInt(4, paymentID);
                preparedStatement.setInt(5, rateID);
                preparedStatement.executeQuery();
                con.close();
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void view_Ticket(){
        try{
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT TICKETID, PAYMENTSTATUS, STARTINGPOINT, ENDINGPOINT, DEPARTURETIME, ARRIVALTIME, CATEGORYNAME, NO_OF_SEATS, TOTALCOST, TRAINNO\n" +
                    "FROM TICKET\n" +
                    "INNER JOIN PAYMENT ON PAYMENT_PAYMENTID=PAYMENTID\n" +
                    "INNER JOIN PASSENGER ON TICKET.PASSENGER_PASSENGERCNIC=PASSENGERCNIC\n" +
                    "INNER JOIN RATES ON  TICKET.RATES_RATEID = RATEID\n" +
                    "INNER JOIN ROUTE ON ROUTEID=ROUTE_ROUTEID\n" +
                    "INNER JOIN CATEGORY ON CATEGORYID=CATEGORY_CATEGORYID\n" +
                    "INNER JOIN TRAIN ON TICKET.TRAIN_TRAINID=TRAINID\n" +
                    "WHERE PAYMENT.PASSENGER_PASSENGERCNIC = ?");
            preparedStatement.setString(1, passengerCNIC);
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s","Ticket ID","Payment Status","Starting Point","Ending Point","Departure Time","Arrival Time","Category Name","No of Seats","Total Cost","Train No"));
            System.out.println();
            while (rs.next()){
                String Status;
                if (rs.getString(2).equals("N")){
                    Status = "Unpaid";
                }
                else{
                    Status = "Paid";
                }
                System.out.println(String.format("%-"+12+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s"+"%-"+15+"s",rs.getInt(1),Status,rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getInt(8),rs.getInt(9),rs.getString(10)));
            }
            con.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public int auto_generateTicketID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(TicketID) from TICKET");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                ticketID = rs.getInt(1) + 1;
            }
            else{
                ticketID = 1;
            }
            return ticketID;
        }
        catch(Exception e) {
            System.out.println(e);
            return ticketID;
        }
    }
}
