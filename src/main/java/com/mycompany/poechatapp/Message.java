/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */
package com.mycompany.poechatapp;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Message class for Part 2 (Sending Messages).
 *
 * Each Message holds:
 *  - a randomly generated ten-digit Message ID,
 *  - a message number (0 for the first message, then incremented),
 *  - the recipient's cell number,
 *  - the message text (max 250 characters),
 *  - an auto-generated Message Hash.
 *
 * The messages that have been SENT while the program is running are kept in
 * a shared (static) list so that printMessages() and returnTotalMessages()
 * can report on the whole session. Call {@link #resetSession()} to clear
 * that state (the unit tests do this before every test).
 *
 * Storing a message writes it into a JSON file (see {@link #storeMessage()}).
 * The JSON is written by hand (no external library needed). If your
 * lecturer expects a library such as Gson or org.json, replace the body of
 * storeMessage() with that library, and cite the resource you used.
 */
public final class Message {

    public static final int MAX_MESSAGE_LENGTH = 250;

    // Messages the system must return (taken from the task sheet).
    public static final String RECIPIENT_SUCCESS_MSG =
            "Cell phone number successfully captured.";
    public static final String RECIPIENT_FAIL_MSG =
            "Cell phone number is incorrectly formatted or does not contain "
            + "an international code. Please correct the number and try again.";

    public static final String MESSAGE_READY_MSG = "Message ready to send.";

    public static final String MESSAGE_SENT_MSG = "Message successfully sent.";
    public static final String MESSAGE_DISREGARD_MSG = "Press 0 to delete the message.";
    public static final String MESSAGE_STORED_MSG = "Message successfully stored.";
    public static final String MESSAGE_STORE_FAIL_MSG = "Message could not be stored.";
    public static final String INVALID_OPTION_MSG =
            "Invalid option, please choose 1, 2 or 3.";

    public static final String NO_MESSAGES_MSG = "No messages have been sent yet.";

    // Session state (shared by all Message objects while the program runs).
    private static final List<Message> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;
    private static Path storageFile = Paths.get("storedMessages.json");

    // Reuses the cell phone rules written for Part 1 (see the task hint).
    private static final Login CELL_VALIDATOR = new Login();

    private final String messageID;
    private final int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    /**
     * Creates a new message with an auto-generated ten-digit Message ID.
     * The recipient and message text are filled in afterwards with the
     * setters.
     */
    public Message(int messageNumber) {
        this(generateMessageID(), messageNumber, "", "");
    }

    /**
     * Creates a message with every detail supplied (used by the unit tests
     * so that the Message ID is known in advance).
     */
    public Message(String messageID, int messageNumber, String recipient,
                   String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    // ------------------------------------------------------------------
    // Required methods
    // ------------------------------------------------------------------

    /** Generates a random ten-digit number (leading zeros are allowed). */
    public static String generateMessageID() {
        long number = ThreadLocalRandom.current().nextLong(10_000_000_000L);
        return String.format("%010d", number);
    }

    /** Returns the text shown to the user when a Message ID is created. */
    public String messageIDCreated() {
        return "Message ID generated: " + messageID;
    }

    /** Ensures that the message ID is not more than ten characters. */
    public boolean checkMessageID() {
        return messageID != null
                && !messageID.isEmpty()
                && messageID.length() <= 10;
    }

    /**
     * Ensures that the recipient cell number is no more than ten characters
     * long (after the international code) and starts with a code. Reuses the
     * cell phone check from the Login class.
     */
    public String checkRecipientCell() {
        if (CELL_VALIDATOR.checkCellPhoneNumber(recipient)) {
            return RECIPIENT_SUCCESS_MSG;
        }
        return RECIPIENT_FAIL_MSG;
    }

    /**
     * Ensures that the message does not exceed 250 characters.
     */
    public String checkMessageLength() {
        int length = (message == null) ? 0 : message.length();
        if (length <= MAX_MESSAGE_LENGTH) {
            return MESSAGE_READY_MSG;
        }
        int excess = length - MAX_MESSAGE_LENGTH;
        return "Message exceeds 250 characters by " + excess
                + "; please reduce the size.";
    }

    /**
     * Creates and returns the Message Hash: the first two characters of the
     * message ID, a colon, the message number, a colon, then the first and
     * last words of the message, all in capitals. For example 00:0:HITHANKS.
     * Punctuation around the words is ignored.
     */
    public String createMessageHash() {
        String id = (messageID == null) ? "" : messageID;
        String prefix = id.length() >= 2 ? id.substring(0, 2) : id;

        String firstWord = "";
        String lastWord = "";
        if (message != null) {
            String trimmed = message.trim();
            if (!trimmed.isEmpty()) {
                String[] words = trimmed.split("\\s+");
                firstWord = cleanWord(words[0]);
                lastWord = cleanWord(words[words.length - 1]);
            }
        }

        messageHash = (prefix + ":" + messageNumber + ":" + firstWord + lastWord)
                .toUpperCase(Locale.ROOT);
        return messageHash;
    }

    /**
     * Lets the user choose what happens to the message:
     *   1 = send, 2 = disregard, 3 = store to send later.
     * Returns the message the system must display for that choice.
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                if (!sentMessages.contains(this)) {
                    sentMessages.add(this);
                    totalMessagesSent++;
                }
                return MESSAGE_SENT_MSG;
            case 2:
                return MESSAGE_DISREGARD_MSG;
            case 3:
                return storeMessage() ? MESSAGE_STORED_MSG : MESSAGE_STORE_FAIL_MSG;
            default:
                return INVALID_OPTION_MSG;
        }
    }

    /** Returns every message sent while the program has been running. */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return NO_MESSAGES_MSG;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            if (i > 0) {
                sb.append(System.lineSeparator());
            }
            sb.append(sentMessages.get(i).toDisplayString());
        }
        return sb.toString();
    }

    /** Returns the total number of messages sent. */
    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /**
     * Stores this message in a JSON file (an array of message objects).
     * New messages are appended to the array so earlier stored messages
     * are kept, even between runs of the program.
     *
     * @return true if the message was stored successfully
     */
    public boolean storeMessage() {
        try {
            createMessageHash();
            String entry = toJson();

            String existing = "";
            if (Files.exists(storageFile)) {
                existing = new String(Files.readAllBytes(storageFile),
                        StandardCharsets.UTF_8).trim();
            }

            String output;
            if (existing.isEmpty() || existing.equals("[]")) {
                output = "[" + System.lineSeparator() + entry
                        + System.lineSeparator() + "]" + System.lineSeparator();
            } else if (existing.startsWith("[") && existing.endsWith("]")) {
                String body = existing.substring(0, existing.length() - 1).trim();
                output = body + "," + System.lineSeparator() + entry
                        + System.lineSeparator() + "]" + System.lineSeparator();
            } else {
                // Not a JSON array: refuse to overwrite somebody's file.
                return false;
            }

            Path parent = storageFile.toAbsolutePath().getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(storageFile, output.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // ------------------------------------------------------------------
    // Helpers, getters and setters
    // ------------------------------------------------------------------

    /**
     * Full details of the message in the required order:
     * Message ID, Message Hash, Recipient, Message.
     */
    public String toDisplayString() {
        String nl = System.lineSeparator();
        return "Message ID: " + messageID + nl
                + "Message Hash: " + createMessageHash() + nl
                + "Recipient: " + recipient + nl
                + "Message: " + message;
    }

    /** Clears the messages sent so far (used when a new session starts). */
    public static void resetSession() {
        sentMessages.clear();
        totalMessagesSent = 0;
    }

    /** Changes the file that storeMessage() writes to. */
    public static void setStorageFile(Path file) {
        storageFile = file;
    }

    public static Path getStorageFile() {
        return storageFile;
    }

    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.messageHash = createMessageHash();
    }

    public String getMessageHash() {
        return createMessageHash();
    }

    /** Removes punctuation from the start/end/middle of a word. */
    private static String cleanWord(String word) {
        return word.replaceAll("[^\\p{L}\\p{N}]", "");
    }

    private String toJson() {
        String nl = System.lineSeparator();
        return "  {" + nl
                + "    \"messageID\": \"" + escapeJson(messageID) + "\"," + nl
                + "    \"messageNumber\": " + messageNumber + "," + nl
                + "    \"messageHash\": \"" + escapeJson(messageHash) + "\"," + nl
                + "    \"recipient\": \"" + escapeJson(recipient) + "\"," + nl
                + "    \"message\": \"" + escapeJson(message) + "\"" + nl
                + "  }";
    }

    private static String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }
}

