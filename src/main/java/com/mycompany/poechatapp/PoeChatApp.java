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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        boolean running = true;


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
