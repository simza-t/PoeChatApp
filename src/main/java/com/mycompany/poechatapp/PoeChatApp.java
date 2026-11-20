/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poechatapp;

import java.util.Scanner;

public class PoeChatApp {

    private static MessageStore store = new MessageStore();

    private static final String[] MENU_OPTIONS = {
        "1. Register a new account",
        "2. Login",
        "3. Exit"
    };

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Login login = new Login();
        boolean running = true;

        printBanner();

        while (running) {
            printMenu();
            System.out.print("Select an option: ");
            String choice = input.nextLine().trim();

            switch (choice) {
                case "1":
                    registerFlow(input, login);
                    break;
                case "2":
                    loginFlow(input, login);
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye.");
                    break;
                default:
                    System.out.println("Invalid option. Please enter 1, 2 or 3.");
                    break;
            }
            System.out.println();
        }

        input.close();
    }

    private static void printBanner() {
        System.out.println("========================================");
        System.out.println("          QUICKCHAT");
        System.out.println("  Registration, Login and Messaging");
        System.out.println("========================================");
        System.out.println();
    }

    private static void printMenu() {
        for (int i = 0; i < MENU_OPTIONS.length; i++) {
            System.out.println(MENU_OPTIONS[i]);
        }
    }

    private static void registerFlow(Scanner input, Login login) {
        System.out.println();
        System.out.println("--- Create an account ---");

        System.out.print("Enter first name: ");
        String firstName = input.nextLine().trim();
        while (firstName.equals("")) {
            System.out.print("First name cannot be empty. Enter first name: ");
            firstName = input.nextLine().trim();
        }

        System.out.print("Enter last name: ");
        String lastName = input.nextLine().trim();
        while (lastName.equals("")) {
            System.out.print("Last name cannot be empty. Enter last name: ");
            lastName = input.nextLine().trim();
        }

        login.setFirstName(firstName);
        login.setLastName(lastName);

        String username;
        while (true) {
            System.out.print("Enter username (must contain '_' and be no more than 5 characters): ");
            username = input.nextLine().trim();
            System.out.println(login.getUsernameMessage(username));
            if (login.checkUserName(username)) {
                break;
            }
        }

        String password;
        while (true) {
            System.out.print("Enter password (min 8 chars, 1 capital, 1 number, 1 special character): ");
            password = input.nextLine().trim();
            System.out.println(login.getPasswordMessage(password));
            if (login.checkPasswordComplexity(password)) {
                break;
            }
        }

        String cell;
        while (true) {
            System.out.print("Enter South African cell number (example +27838968976): ");
            cell = input.nextLine().trim();
            System.out.println(login.getCellPhoneMessage(cell));
            if (login.checkCellPhoneNumber(cell)) {
                break;
            }
        }

        System.out.println(login.registerUser(username, password, cell));
    }

    private static void loginFlow(Scanner input, Login login) {
        System.out.println();
        System.out.println("--- Login ---");

        if (!login.isRegistered()) {
            System.out.println("No account has been registered yet. Please register first.");
            return;
        }

        System.out.print("Enter username: ");
        String username = input.nextLine().trim();
        System.out.print("Enter password: ");
        String password = input.nextLine();

        boolean success = login.loginUser(username, password);
        System.out.println(login.returnLoginStatus(username, password));

        if (success) {
            quickChatMenu(input, login);
        }
    }

    private static void quickChatMenu(Scanner input, Login login) {
        System.out.println("Welcome to QuickChat.");
        store.loadStoredFromJSON();
        boolean inChat = true;

        while (inChat) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Select an option: ");
            String choice = input.nextLine().trim();

            if (choice.equals("1")) {
                sendMessages(input, login);
            } else if (choice.equals("2")) {
                System.out.println("Coming Soon.");
            } else if (choice.equals("3")) {
                inChat = false;
            } else if (choice.equals("4")) {
                storedMenu(input, login);
            } else {
                System.out.println("Invalid option.");
            }
        }
    }

    private static void sendMessages(Scanner input, Login login) {
        System.out.print("How many messages do you want to send? ");
        int limit = Integer.parseInt(input.nextLine().trim());

        for (int i = 0; i < limit; i++) {
            System.out.println();
            System.out.println("Message " + (i + 1) + " of " + limit);

            Message current = new Message();
            current.setMessageNumber(i);
            System.out.println("Message ID generated: " + current.getMessageID());

            String cellMessage = "";
            while (!cellMessage.equals("Cell phone number successfully captured.")) {
                System.out.print("Enter recipient cell number (example +27718693002): ");
                String cell = input.nextLine().trim();
                cellMessage = current.checkRecipientCell(cell);
                System.out.println(cellMessage);
            }

            String lengthMessage = "";
            while (!lengthMessage.equals("Message ready to send.")) {
                System.out.print("Enter message (max 250 characters): ");
                String text = input.nextLine();
                lengthMessage = current.checkMessageLength(text);
                System.out.println(lengthMessage);
            }

            System.out.println("Message Hash: " + current.createMessageHash());
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Select an option: ");
            int action = Integer.parseInt(input.nextLine().trim());
            System.out.println(current.SentMessage(action));

            if (action == 1) {
                store.addSent(current.getMessageText());
                store.addHashAndID(current.getMessageHash(), current.getMessageID());
                System.out.println("Message ID: " + current.getMessageID());
                System.out.println("Message Hash: " + current.getMessageHash());
                System.out.println("Recipient: " + current.getRecipient());
                System.out.println("Message: " + current.getMessageText());
            } else if (action == 2) {
                store.addDisregarded(current.getMessageText());
            } else if (action == 3) {
                store.addStored(
                        login.getFirstName() + " " + login.getLastName(),
                        current.getRecipient(),
                        current.getMessageText(),
                        current.getMessageHash(),
                        current.getMessageID()
                );
            }
        }

        Message tracker = new Message();
        System.out.println("Total messages sent: " + tracker.returnTotalMessagess());
    }

    private static void storedMenu(Scanner input, Login login) {
        boolean inStore = true;

        while (inStore) {
            System.out.println();
            System.out.println("--- Stored Messages ---");
            System.out.println("a) Display sender and recipient");
            System.out.println("b) Display longest stored message");
            System.out.println("c) Search by message ID");
            System.out.println("d) Search by recipient");
            System.out.println("e) Delete by message hash");
            System.out.println("f) Display full report");
            System.out.println("g) Back");
            System.out.print("Select an option: ");
            String choice = input.nextLine().trim();

            if (choice.equals("a")) {
                System.out.println(store.displaySenderAndRecipient());
            } else if (choice.equals("b")) {
                System.out.println(store.displayLongestMessage());
            } else if (choice.equals("c")) {
                System.out.print("Enter message ID: ");
                System.out.println(store.searchByMessageID(input.nextLine().trim()));
            } else if (choice.equals("d")) {
                System.out.print("Enter recipient: ");
                System.out.println(store.searchByRecipient(input.nextLine().trim()));
            } else if (choice.equals("e")) {
                System.out.print("Enter message hash: ");
                String result = store.deleteByHash(input.nextLine().trim());
                System.out.println(result);
                store.rewriteJSON();
            } else if (choice.equals("f")) {
                System.out.println(store.displayReport());
            } else if (choice.equals("g")) {
                inStore = false;
            } else {
                System.out.println("Invalid option.");
            }
        }
    }
}