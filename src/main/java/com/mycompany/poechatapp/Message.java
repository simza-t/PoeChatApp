/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
package com.mycompany.poechatapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 *
 * @author user
 */
/**
 * Message class for QuickChat Part 2.
 * Handles message ID, recipient, length check, hash, send/store/disregard.
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    private static int totalMessagesSent = 0;
    private static String allSentMessages = "";

    public Message() {
        this.messageID = generateMessageID();
        this.messageNumber = totalMessagesSent;
        this.recipient = "";
        this.messageText = "";
        this.messageHash = "";
    }

    public Message(String messageID, int messageNumber, String recipient, String messageText) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    public String generateMessageID() {
        Random random = new Random();
        String id = "";
        for (int i = 0; i < 10; i++) {
            id = id + random.nextInt(10);
        }
        return id;
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell(String cell) {
        if (cell == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        String number = cell.trim();

        if (!number.startsWith("+27") || number.length() != 12) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        for (int i = 3; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) {
                return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
            }
        }

        this.recipient = number;
        return "Cell phone number successfully captured.";
    }

    public String checkMessageLength(String text) {
        if (text == null) {
            text = "";
        }

        if (text.length() <= 250) {
            this.messageText = text;
            return "Message ready to send.";
        }

        int extra = text.length() - 250;
        return "Message exceeds 250 characters by " + extra + "; please reduce the size.";
    }

    public String createMessageHash() {
        String firstTwo = "00";
        if (messageID != null && messageID.length() >= 2) {
            firstTwo = messageID.substring(0, 2);
        }

        String firstWord = "";
        String lastWord = "";

        if (messageText != null && !messageText.trim().equals("")) {
            String cleaned = messageText.replace(",", " ").replace("?", " ").replace(".", " ").replace("!", " ");
            String[] words = cleaned.trim().split(" ");

            for (int i = 0; i < words.length; i++) {
                if (!words[i].equals("")) {
                    if (firstWord.equals("")) {
                        firstWord = words[i];
                    }
                    lastWord = words[i];
                }
            }
        }

        messageHash = (firstTwo + ":" + messageNumber + ":" + firstWord + lastWord).toUpperCase();
        return messageHash;
    }

    public String SentMessage(int choice) {
        if (choice == 1) {
            totalMessagesSent = totalMessagesSent + 1;
            messageHash = createMessageHash();
            allSentMessages = allSentMessages
                    + "Message ID: " + messageID + "\n"
                    + "Message Hash: " + messageHash + "\n"
                    + "Recipient: " + recipient + "\n"
                    + "Message: " + messageText + "\n\n";
                   
            return "Message successfully sent.";
        } else if (choice == 2) {
            return "Press 0 to delete the message.";
        } else if (choice == 3) {
            storeMessage();
            return "Message successfully stored.";
        }
        return "Invalid option.";
    }

    public String printMessages() {
        if (allSentMessages.equals("")) {
            return "No messages have been sent.";
        }
        return allSentMessages;
    }

    public int returnTotalMessagess() {
        return totalMessagesSent;
    }
   /**
     * Stores the message in a JSON file.
     * Research reference:
     * Oracle Java Tutorials - Reading and Writing Files
     * https://docs.oracle.com/javase/tutorial/essential/io/file.html
     * JSON format structure researched from:
     * https://www.json.org/json-en.html
     */
    public void storeMessage() {
        try {
            FileWriter writer = new FileWriter("stored_messages.json", true);
            writer.write("{");
            writer.write("\"messageID\":\"" + messageID + "\",");
            writer.write("\"messageHash\":\"" + createMessageHash() + "\",");
            writer.write("\"recipient\":\"" + recipient + "\",");
            writer.write("\"message\":\"" + messageText.replace("\"", "'") + "\"");
            writer.write("}\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not store the message.");
        }
    }

    public String getMessageID() {
        return messageID;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }
    
    
}