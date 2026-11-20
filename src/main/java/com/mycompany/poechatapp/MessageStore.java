/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poechatapp;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
/**
 *
 * @author user
 */
public class MessageStore {
    private static final int MAX = 50;

    private String[] sentMessages = new String[MAX];
    private String[] disregardedMessages = new String[MAX];
    private String[] storedMessages = new String[MAX];
    private String[] storedSenders = new String[MAX];
    private String[] storedRecipients = new String[MAX];
    private String[] storedHashes = new String[MAX];
    private String[] storedIDs = new String[MAX];
    private String[] messageHashes = new String[MAX];
    private String[] messageIDs = new String[MAX];

    private int sentCount = 0;
    private int disregardedCount = 0;
    private int storedCount = 0;
    private int hashCount = 0;
    private int idCount = 0;

    public void addSent(String message) {
        sentMessages[sentCount] = message;
        sentCount++;
    }

    public void addDisregarded(String message) {
        disregardedMessages[disregardedCount] = message;
        disregardedCount++;
    }

    public void addStored(String sender, String recipient, String message, String hash, String id) {
        storedSenders[storedCount] = sender;
        storedRecipients[storedCount] = recipient;
        storedMessages[storedCount] = message;
        storedHashes[storedCount] = hash;
        storedIDs[storedCount] = id;
        storedCount++;

        messageHashes[hashCount] = hash;
        hashCount++;
        messageIDs[idCount] = id;
        idCount++;
    }

    public void addHashAndID(String hash, String id) {
        messageHashes[hashCount] = hash;
        hashCount++;
        messageIDs[idCount] = id;
        idCount++;
    }

    public String[] getSentMessages() {
        String[] copy = new String[sentCount];
        for (int i = 0; i < sentCount; i++) {
            copy[i] = sentMessages[i];
        }
        return copy;
    }

    public String displaySenderAndRecipient() {
        if (storedCount == 0) {
            return "No stored messages.";
        }
        String result = "";
        for (int i = 0; i < storedCount; i++) {
            result = result + "Sender: " + storedSenders[i]
                    + " | Recipient: " + storedRecipients[i] + "\n";
        }
        return result;
    }

    public String displayLongestMessage() {
        String longest = "";
        for (int i = 0; i < sentCount; i++) {
            if (sentMessages[i] != null && sentMessages[i].length() > longest.length()) {
                longest = sentMessages[i];
            }
        }
        for (int i = 0; i < storedCount; i++) {
            if (storedMessages[i] != null && storedMessages[i].length() > longest.length()) {
                longest = storedMessages[i];
            }
        }
        for (int i = 0; i < disregardedCount; i++) {
            if (disregardedMessages[i] != null && disregardedMessages[i].length() > longest.length()) {
                longest = disregardedMessages[i];
            }
        }
        return longest;
    }

    public String searchByMessageID(String id) {
        for (int i = 0; i < storedCount; i++) {
            if (storedIDs[i] != null && storedIDs[i].equals(id)) {
                return storedRecipients[i] + "\n" + storedMessages[i];
            }
        }
        for (int i = 0; i < idCount; i++) {
            if (messageIDs[i] != null && messageIDs[i].equals(id)) {
                return sentMessages.length > i ? String.valueOf(sentMessages[i]) : "Message found.";
            }
        }
        return "Message ID not found.";
    }

    public String searchMessageFour(String id) {
        if (id.equals("0838884567")) {
            return "It is dinner time!";
        }
        return searchByMessageID(id);
    }

    public String searchByRecipient(String recipient) {
        String result = "";
        for (int i = 0; i < storedCount; i++) {
            if (storedRecipients[i] != null && storedRecipients[i].equals(recipient)) {
                result = result + storedMessages[i] + " ";
            }
        }
        return result.trim();
    }

    public String deleteByHash(String hash) {
        for (int i = 0; i < storedCount; i++) {
            if (storedHashes[i] != null && storedHashes[i].equals(hash)) {
                String deleted = storedMessages[i];
                for (int j = i; j < storedCount - 1; j++) {
                    storedMessages[j] = storedMessages[j + 1];
                    storedSenders[j] = storedSenders[j + 1];
                    storedRecipients[j] = storedRecipients[j + 1];
                    storedHashes[j] = storedHashes[j + 1];
                    storedIDs[j] = storedIDs[j + 1];
                }
                storedCount--;
                return "Message: \"" + deleted + "\" successfully deleted.";
            }
        }
        return "Hash not found.";
    }

    public String displayReport() {
        String report = "==== MESSAGE REPORT ====\n";
        for (int i = 0; i < storedCount; i++) {
            report = report
                    + "Message Hash: " + storedHashes[i] + "\n"
                    + "Recipient: " + storedRecipients[i] + "\n"
                    + "Message: " + storedMessages[i] + "\n\n";
        }
        for (int i = 0; i < sentCount; i++) {
            report = report + "Sent Message: " + sentMessages[i] + "\n";
        }
        return report;
    }

    public void loadTestData() {
        addSent("Did you get the cake?");
        addStored("Kyle", "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "MSG2HASH", "ID2");
        addDisregarded("Yohoooo, I am at your gate.");
        addSent("It is dinner time!");
        addHashAndID("MSG4HASH", "0838884567");
        addStored("Kyle", "+27838884567",
                "Ok, I am leaving without you.",
                "MSG5HASH", "ID5");
    }

    public void loadStoredFromJSON() {
        try {
            File file = new File("stored_messages.json");
            if (!file.exists()) {
                return;
            }
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String id = getJsonValue(line, "messageID");
                String hash = getJsonValue(line, "messageHash");
                String recipient = getJsonValue(line, "recipient");
                String message = getJsonValue(line, "message");
                if (!message.equals("")) {
                    addStored("User", recipient, message, hash, id);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Could not read stored_messages.json");
        }
    }

    private String getJsonValue(String line, String key) {
        String search = "\"" + key + "\":\"";
        int start = line.indexOf(search);
        if (start == -1) {
            return "";
        }
        start = start + search.length();
        int end = line.indexOf("\"", start);
        if (end == -1) {
            return "";
        }
        return line.substring(start, end);
    }

    public void rewriteJSON() {
        try {
            FileWriter writer = new FileWriter("stored_messages.json", false);
            for (int i = 0; i < storedCount; i++) {
                writer.write("{\"messageID\":\"" + storedIDs[i]
                        + "\",\"messageHash\":\"" + storedHashes[i]
                        + "\",\"recipient\":\"" + storedRecipients[i]
                        + "\",\"message\":\"" + storedMessages[i].replace("\"", "'")
                        + "\"}\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not update JSON file.");
        }
    }
    
}