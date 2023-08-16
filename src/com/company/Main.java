package com.company;
import java.util.Scanner;

public class Main {
    Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            Scanner input = new Scanner(System.in);
            System.out.println("---WELCOME TO RAILWAY MANAGEMENT SYSTEM---");
            System.out.println("For Admin Login, Press 1");
            System.out.println("For Passenger Login/Signup, Press 2");
            System.out.println("Press 0 to exit");
            System.out.print("Enter your Option : ");
            int menu;
            menu = input.nextInt();
            if (menu == 1) {
                    Admin a = new Admin();
                    a.login_admin();
                    break;
            }
            else if (menu == 2){
                    Passenger p = new Passenger("arg");
                    break;
            }
            else if (menu == 0){
                break;
            }
            else{
                System.out.println("Invalid Input");
            }
        }
    }
}

