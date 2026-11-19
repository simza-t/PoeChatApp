/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poechatapp;

import java.util.Scanner;

/** Main menu option 4: "Stored Messages". */
public class StoredMessagesMenu {

    private StoredMessagesMenu() {
    }

    public static void run(Scanner scanner, MessageStore store) {
        boolean back = false;

        while (!back) {
            System.out.println();
            System.out.println("=== Stored Messages ===");
            System.out.println("a) Display the sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search for all messages for a recipient");
            System.out.println("e) Delete a message using its message hash");
            System.out.println("f) Display a report of all stored messages");
            System.out.println("g) Back to the main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a" -> System.out.println(store.displaySenderAndRecipient());
                case "b" -> {
                    String longest = store.longestMessage();
                    System.out.println(longest.isEmpty()
                            ? "There are no stored messages."
                            : "Longest message: " + longest);
                }
                case "c" -> {
                    System.out.print("Enter the message ID: ");
                    System.out.println(store.searchByMessageID(scanner.nextLine().trim()));
                }
                case "d" -> {
                    System.out.print("Enter the recipient number (e.g. +27838884567): ");
                    System.out.println(store.searchByRecipient(scanner.nextLine().trim()));
                }
                case "e" -> {
                    System.out.print("Enter the message hash: ");
                    System.out.println(store.deleteByHash(scanner.nextLine().trim()));
                }
                case "f" -> System.out.println(store.buildReport());
                case "g" -> back = true;
                default -> System.out.println("Invalid option. Please choose a to g.");
            }
        }
    }
}
