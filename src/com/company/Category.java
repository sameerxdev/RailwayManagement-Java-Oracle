package com.company;

import java.sql.*;
import java.util.Scanner;

public class Category {
    int categoryID;
    String categoryName;

    public Category(int Cart,String categoryName){
        this.categoryID = 0;
        this.categoryName = categoryName;
    }

    public Category(int arg){
        Scanner t = new Scanner(System.in);
        while (true) {
            System.out.println("1. Add Category Record");
            System.out.println("2. View Category Record");
            System.out.println("3. Update Category Record");
            System.out.println("4. Delete Category Record");
            System.out.println("Enter 0 to Exit");
            System.out.print("Choose your Option : ");
            int input = t.nextInt();
            if (input == 1) {
                add_category();
            }
            else if (input == 2) {
                view_category();
            }
            else if (input == 3) {
                update_category();
            }
            else if (input == 4) {
                delete_category();
            }
            else if (input == 0) {
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }

    public Category(String arg){

    }

    public int select_Category(){
        view_category();
        int c;
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.print("Select Category ID : ");
            c = input.nextInt();
            try {
                Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
                PreparedStatement checkCategory = con.prepareStatement("SELECT * FROM CATEGORY  WHERE CATEGORYID = ?");
                checkCategory.setInt(1, c);
                ResultSet rsc = checkCategory.executeQuery();
                if (rsc.next()){
                    break;
                }
                else {
                    System.out.println("Category ID not Found");
                }
            }
            catch (Exception e) {
                System.out.println(e);
                e.printStackTrace();
            }
        }
        return c;
    }

    public boolean exist_category(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkCategory = con.prepareStatement("SELECT * FROM CATEGORY");
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

    public void add_category(){
        Scanner input = new Scanner(System.in);
        System.out.print("Enter Category Name : ");
        categoryName = input.next();

        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            int c_ID = auto_generateCategoryID(con);
            if (c_ID == 0){
                System.out.println("Error Generating ID");
            }
            else {
                PreparedStatement preparedStatement = con.prepareStatement("INSERT INTO Category VALUES (?,?,'admin')");
                preparedStatement.setInt(1, c_ID);
                preparedStatement.setString(2, categoryName);
                preparedStatement.executeQuery();
                System.out.println("Category added Successfully");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void view_category(){
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement preparedStatement = con.prepareStatement("SELECT * from Category");
            ResultSet rs = preparedStatement.executeQuery();
            System.out.print(String.format("%-"+20+"s"+"%-"+20+"s"+"%-"+20+"s","Category ID","Category Name","Rate Percent"));
            System.out.println();
            while (rs.next()){
                System.out.println(String.format("%-"+20+"s"+"%-"+20+"s"+"%-"+20+"s",rs.getInt(1),rs.getString(2),rs.getFloat(4)));
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }

    public void update_category() {
        Scanner input = new Scanner(System.in);
        int c_id;
        System.out.print("Enter Category ID you want to Update : ");
        c_id = input.nextInt();
        try {
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "dbproject", "system");
            PreparedStatement checkCategory = con.prepareStatement("SELECT * FROM CATEGORY  WHERE CATEGORYID = ?");
            checkCategory.setInt(1,c_id);
            ResultSet rsc = checkCategory.executeQuery();
            if(rsc.next()){
                System.out.print("Enter New Category Name : ");
                String c_name = input.next();
                PreparedStatement preparedStatement = con.prepareStatement("UPDATE CATEGORY SET CATEGORYNAME = ? WHERE CATEGORYID = ?");
                preparedStatement.setString(1, c_name);
                preparedStatement.setInt(2, c_id);
                preparedStatement.executeQuery();
                System.out.println("Category updated Successfully");
            }
            else{
                System.out.println("Category ID not Found");
            }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public void delete_category(){
        Scanner input = new Scanner(System.in);
        String c_id;
        System.out.print("Enter Category ID you want to Delete : ");
        c_id = input.next();
        try {
            Connection con= DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl","dbproject","system");
            PreparedStatement checkCategory = con.prepareStatement("SELECT * FROM CATEGORY  WHERE CATEGORYID = ?");
            checkCategory.setString(1,c_id);
            ResultSet rsc = checkCategory.executeQuery();
                if (rsc.next()) {
                    PreparedStatement preparedStatement = con.prepareStatement("DELETE FROM CATEGORY WHERE CATEGORYID = ?");
                    preparedStatement.setString(1, c_id);
                    preparedStatement.executeQuery();
                    System.out.println("Category Deleted Successfully");
                }
                else{
                    System.out.println("Category ID not Found");
                }
            con.close();
        }
        catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    public int auto_generateCategoryID(Connection con){
        try {
            PreparedStatement preparedStatement1 = con.prepareStatement("SELECT MAX(categoryID) from Category");
            ResultSet rs = preparedStatement1.executeQuery();
            if (rs.next()) {
                categoryID = rs.getInt(1) + 1;
            }
            else{
                categoryID = 1;
            }
            return categoryID;
        }
        catch(Exception e) {
            System.out.println(e);
            return categoryID;
        }
    }
}
