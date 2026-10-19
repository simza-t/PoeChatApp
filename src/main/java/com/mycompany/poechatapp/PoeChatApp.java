/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poechatapp;

import java.util.Scanner;

/**
 *
 * @author Student
 */

public class PoeChatApp {




/**
 * Entry point of the application.
 *
 * Part 1: the user registers an account and logs in.
 * Part 2: ONLY once the login succeeds, the QuickChat messaging feature
 *         starts, so users can only send messages if they have logged in
 *         successfully.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        boolean running = true;

        System.out.println("Please read through the following before you start:");
        System.out.println("- Username: must contain an underscore (_) "
                + "and be no more than 5 characters long.");
        System.out.println("- Password: at least 8 characters, one "
                + "capital letter, one number and one special character.");
        System.out.println("- Cell phone: international format, e.g. "
                + "+27831234567.");

        while (running) {
            System.out.println("\n=== Registration and Login System ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.print("Enter first name: ");
                    String firstName = scanner.nextLine();
                    System.out.print("Enter last name: ");
                    String lastName = scanner.nextLine();
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter South African cell phone "
                            + "number (e.g. +27831234567): ");
                    String cell = scanner.nextLine();

                    String result = login.registerUser(username, password,
                            cell, firstName, lastName);
                    System.out.println(result);
                }
                case "2" -> {
                    System.out.print("Enter username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();

                    boolean success = login.loginUser(loginUsername, loginPassword);
                    System.out.println(login.returnLoginStatus(success));

                    if (success) {
                        // Messaging is only available after a successful login.
                        new Quickchat(scanner).run();
                        running = false;
                    }
                }
                case "3" -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option, please try again.");
            }
        }

        scanner.close();
    }

}
}
    