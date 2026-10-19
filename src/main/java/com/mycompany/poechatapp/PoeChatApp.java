/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poechatapp;

import java.util.Scanner;

/**
 * Main class of the application.
 *
 * Part 1: the user registers an account and logs in.
 * Part 2: ONLY after a successful login, QuickChat starts, so users can
 *         only send messages if they have logged in successfully.
 */
public class PoeChatApp {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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

                    System.out.println(login.registerUser(username, password,
                            cell, firstName, lastName));
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
                        quickChat();
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
    }

    // ------------------------------------------------------------------
    // Part 2: QuickChat
    // ------------------------------------------------------------------

    private static void quickChat() {
        Message.resetSession();

        System.out.println();
        System.out.println("Welcome to QuickChat.");

        int limit = readPositiveInt("How many messages do you wish to enter? ");
        int entered = 0;
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    if (entered >= limit) {
                        System.out.println("You have already entered all "
                                + limit + " message(s) you planned to send.");
                    } else {
                        sendMessages(entered, limit);
                        entered = limit;
                    }
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
            }
        }
    }

    /**
     * Lets the user enter every outstanding message (only the number they
     * set at the start), then shows everything sent and the total.
     */
    private static void sendMessages(int alreadyEntered, int limit) {
        for (int i = alreadyEntered; i < limit; i++) {
            enterOneMessage(i, limit);
        }

        System.out.println();
        System.out.println("===== Messages sent =====");
        System.out.println(Message.printMessages());
        System.out.println();
        System.out.println("Total number of messages sent: "
                + Message.returnTotalMessages());
    }

    private static void enterOneMessage(int index, int limit) {
        Message msg = new Message(index);

        System.out.println();
        System.out.println("--- Message " + (index + 1) + " of " + limit + " ---");

        if (!msg.checkMessageID()) {
            System.out.println("The message ID could not be created; "
                    + "this message was skipped.");
            return;
        }
        System.out.println(msg.messageIDCreated());

        // Recipient: keep asking until the number is correctly formatted.
        while (true) {
            System.out.print("Enter the recipient's cell number "
                    + "(e.g. +27718693002): ");
            msg.setRecipient(scanner.nextLine().trim());
            String result = msg.checkRecipientCell();
            System.out.println(result);
            if (result.equals(Message.RECIPIENT_SUCCESS_MSG)) {
                break;
            }
        }

        // Message text: keep asking until it is 250 characters or fewer.
        while (true) {
            System.out.print("Enter your message (max 250 characters): ");
            msg.setMessage(scanner.nextLine());
            String result = msg.checkMessageLength();
            System.out.println(result);
            if (result.equals(Message.MESSAGE_READY_MSG)) {
                break;
            }
            System.out.println("Please enter a message of less than 250 characters.");
        }

        System.out.println("Message Hash: " + msg.createMessageHash());

        handleChoice(msg);
    }

    private static void handleChoice(Message msg) {
        while (true) {
            System.out.println();
            System.out.println("What would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Choose an option: ");
            String input = scanner.nextLine().trim();

            if (!input.equals("1") && !input.equals("2") && !input.equals("3")) {
                System.out.println(Message.INVALID_OPTION_MSG);
                continue;
            }

            int choice = Integer.parseInt(input);
            System.out.println(msg.sentMessage(choice));

            if (choice == 1) {
                System.out.println();
                System.out.println(msg.toDisplayString());
            } else if (choice == 2) {
                // Wait for the user to confirm the deletion with 0.
                while (true) {
                    String confirm = scanner.nextLine().trim();
                    if (confirm.equals("0")) {
                        System.out.println("Message deleted.");
                        break;
                    }
                    System.out.println(Message.MESSAGE_DISREGARD_MSG);
                }
            }
            return;
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value >= 1) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // fall through to the error message below
            }
            System.out.println("Please enter a whole number of 1 or more.");
        }
    }
}