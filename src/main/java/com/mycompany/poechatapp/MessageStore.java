/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poechatapp;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;



/**
 *
 * @author Student
 */
public class MessageStore {

    // ----- flags -----
    public static final String FLAG_SENT = "Sent";
    public static final String FLAG_STORED = "Stored";
    public static final String FLAG_DISREGARD = "Disregard";

    // ----- the five required arrays -----
    private final List<String> sentMessages = new ArrayList<>();
    private final List<String> disregardedMessages = new ArrayList<>();
    private final List<String> storedMessages = new ArrayList<>();
    private final List<String> messageHashes = new ArrayList<>();
    private final List<String> messageIDs = new ArrayList<>();

    // ----- master list used by the menu operations -----
    private final List<Entry> entries = new ArrayList<>();

    // The logged-in user (shown as the "sender"). Set this after login.
    private String sender = "You";

    /** One message with all of its details. */
    private static class Entry {
        final String id;
        final String hash;
        final String recipient;
        final String text;
        final String flag;

        Entry(String id, String hash, String recipient, String text, String flag) {
            this.id = id;
            this.hash = hash;
            this.recipient = recipient;
            this.text = text;
            this.flag = flag;
        }
    }

    
    public void setSender(String sender) {
        if (sender != null && !sender.trim().isEmpty()) {
            this.sender = sender.trim();
        }
    }

    public String getSender() {
        return sender;
    }

   
    public void addSent(String id, String hash, String recipient, String text) {
        add(id, hash, recipient, text, FLAG_SENT);
    }

    public void addStored(String id, String hash, String recipient, String text) {
        add(id, hash, recipient, text, FLAG_STORED);
    }

    public void addDisregarded(String id, String hash, String recipient, String text) {
        add(id, hash, recipient, text, FLAG_DISREGARD);
    }

    /** Adds a message and puts it in the correct arrays. Duplicate hashes are ignored. */
    public void add(String id, String hash, String recipient, String text, String flag) {
        if (hash != null && messageHashes.contains(hash)) {
            return; // already loaded (e.g. stored this session AND read back from the JSON file)
        }

        entries.add(new Entry(id, hash, recipient, text, flag));
        messageIDs.add(id);
        messageHashes.add(hash);

        if (FLAG_SENT.equalsIgnoreCase(flag)) {
            sentMessages.add(text);
        } else if (FLAG_STORED.equalsIgnoreCase(flag)) {
            storedMessages.add(text);
        } else {
            disregardedMessages.add(text);
        }
    }

    // Read-only copies of the arrays (handy for the unit tests).
    public List<String> getSentMessages() {
        return new ArrayList<>(sentMessages);
    }

    public List<String> getDisregardedMessages() {
        return new ArrayList<>(disregardedMessages);
    }

    public List<String> getStoredMessages() {
        return new ArrayList<>(storedMessages);
    }

    public List<String> getMessageHashes() {
        return new ArrayList<>(messageHashes);
    }

    public List<String> getMessageIDs() {
        return new ArrayList<>(messageIDs);
    }

    private static final String[] ID_KEYS = {"messageID", "messageId", "id", "message_id"};
    private static final String[] HASH_KEYS = {"messageHash", "hash", "message_hash"};
    private static final String[] RECIPIENT_KEYS = {"recipient", "recipientCell", "cell", "to", "recipientNumber"};
    private static final String[] TEXT_KEYS = {"message", "messageText", "text", "content"};
    private static final String[] FLAG_KEYS = {"flag", "status"};

    public int loadStoredFromJson(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            return 0;
        }

        String json;
        try {
            json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return 0;
        }

        int before = entries.size();
        for (java.util.Map<String, String> obj : parseObjects(json)) {
            String id = pick(obj, ID_KEYS);
            String hash = pick(obj, HASH_KEYS);
            String recipient = pick(obj, RECIPIENT_KEYS);
            String text = pick(obj, TEXT_KEYS);
            String flag = pick(obj, FLAG_KEYS);

            if (text == null) {
                continue; // not a message object
            }
            if (flag == null || flag.trim().isEmpty()) {
                flag = FLAG_STORED; // everything in the Part 2 file was stored
            }
            add(id, hash, recipient, text, flag);
        }
        return entries.size() - before;
    }

    /** Returns the value for the first key that exists (case-insensitive), or null. */
    private static String pick(java.util.Map<String, String> obj, String[] keys) {
        for (String wanted : keys) {
            for (java.util.Map.Entry<String, String> e : obj.entrySet()) {
                if (e.getKey().equalsIgnoreCase(wanted)) {
                    return e.getValue();
                }
            }
        }
        return null;
    }

    /** Tiny JSON reader: finds every { ... } object and returns its key/value pairs as text. */
    private static List<java.util.Map<String, String>> parseObjects(String s) {
        List<java.util.Map<String, String>> result = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '{') {
                java.util.Map<String, String> map = new java.util.LinkedHashMap<>();
                i = readObject(s, i + 1, map);
                result.add(map);
            } else {
                i++;
            }
        }
        return result;
    }

    // Reads one object body (the part after '{'); returns the index just after '}'.
    private static int readObject(String s, int i, java.util.Map<String, String> map) {
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == '}') {
                return i + 1;
            }
            if (c == '"') {
                int[] pos = {i};
                String key = readString(s, pos);
                i = pos[0];
                while (i < s.length() && s.charAt(i) != ':') {
                    i++;
                }
                i++; // skip ':'
                while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
                    i++;
                }
                if (i < s.length() && s.charAt(i) == '"') {
                    pos[0] = i;
                    map.put(key, readString(s, pos));
                    i = pos[0];
                } else {
                    int start = i;
                    while (i < s.length() && s.charAt(i) != ',' && s.charAt(i) != '}') {
                        i++;
                    }
                    map.put(key, s.substring(start, i).trim());
                }
            } else {
                i++;
            }
        }
        return i;
    }

    // Reads a JSON string starting at the opening quote; pos[0] ends after the closing quote.
    private static String readString(String s, int[] pos) {
        StringBuilder sb = new StringBuilder();
        int i = pos[0] + 1;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (c == '"') {
                i++;
                break;
            }
            if (c == '\\' && i + 1 < s.length()) {
                char n = s.charAt(i + 1);
                switch (n) {
                    case 'n': sb.append('\n'); break;
                    case 't': sb.append('\t'); break;
                    case 'r': sb.append('\r'); break;
                    case 'u':
                        if (i + 5 < s.length()) {
                            sb.append((char) Integer.parseInt(s.substring(i + 2, i + 6), 16));
                            i += 4;
                        }
                        break;
                    default: sb.append(n);
                }
                i += 2;
            } else {
                sb.append(c);
                i++;
            }
        }
        pos[0] = i;
        return sb.toString();
    }

   private List<Entry> activeEntries() {
        List<Entry> list = new ArrayList<>();
        for (Entry e : entries) {
            if (!FLAG_DISREGARD.equalsIgnoreCase(e.flag)) {
                list.add(e);
            }
        }
        return list;
    }

    /** 2a. Sender and recipient of all stored messages. */
    public String displaySenderAndRecipient() {
        List<Entry> list = activeEntries();
        if (list.isEmpty()) {
            return "There are no stored messages.";
        }
        StringBuilder sb = new StringBuilder();
        int n = 1;
        for (Entry e : list) {
            sb.append(n++).
                    append(". Sender: ").append(sender).
                    append(" | Recipient: ").append(e.recipient).
                    append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    
    public String longestMessage() {
        String longest = "";
        for (Entry e : activeEntries()) {
            if (e.text != null && e.text.length() > longest.length()) {
                longest = e.text;
            }
        }
        return longest;
    }

    
    public String getMessageTextByID(String messageID) {
        for (Entry e : activeEntries()) {
            if (e.id != null && e.id.equals(messageID)) {
                return e.text;
            }
        }
        return null;
    }

   
    public String searchByMessageID(String messageID) {
        for (Entry e : activeEntries()) {
            if (e.id != null && e.id.equals(messageID)) {
                return "Recipient: " + e.recipient + " | Message: " + e.text;
            }
        }
        return "No message was found with the message ID: " + messageID;
    }

    
    public List<String> getMessagesForRecipient(String recipient) {
        List<String> found = new ArrayList<>();
        for (Entry e : activeEntries()) {
            if (e.recipient != null && e.recipient.equals(recipient)) {
                found.add(e.text);
            }
        }
        return found;
    }

    
    public String searchByRecipient(String recipient) {
        List<String> found = getMessagesForRecipient(recipient);
        if (found.isEmpty()) {
            return "No messages were found for the recipient: " + recipient;
        }
        StringBuilder sb = new StringBuilder();
        int n = 1;
        for (String text : found) {
            sb.append(n++).append(". ").append(text).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    
    public String deleteByHash(String hash) {
        for (int i = 0; i < entries.size(); i++) {
            Entry e = entries.get(i);
            if (e.hash != null && e.hash.equals(hash)) {
                entries.remove(i);

                int idx = messageHashes.indexOf(e.hash);
                if (idx >= 0) {
                    messageHashes.remove(idx);
                    if (idx < messageIDs.size()) {
                        messageIDs.remove(idx);
                    }
                }
                sentMessages.remove(e.text);
                storedMessages.remove(e.text);
                disregardedMessages.remove(e.text);

                return "Message: \"" + e.text + "\" successfully deleted.";
            }
        }
        return "No message was found with the message hash: " + hash;
    }

    
    public String buildReport() {
        List<Entry> list = activeEntries();
        StringBuilder sb = new StringBuilder();
        sb.append("========== MESSAGE REPORT ==========").append(System.lineSeparator());
        if (list.isEmpty()) {
            sb.append("There are no messages to report.").append(System.lineSeparator());
        }
        int n = 1;
        for (Entry e : list) {
            sb.append("Message ").append(n++).append(" (").append(e.flag).append(")").
                    append(System.lineSeparator());
            sb.append("  Message ID  : ").append(e.id).append(System.lineSeparator());
            sb.append("  Message Hash: ").append(e.hash).append(System.lineSeparator());
            sb.append("  Sender      : ").append(sender).append(System.lineSeparator());
            sb.append("  Recipient   : ").append(e.recipient).append(System.lineSeparator());
            sb.append("  Message     : ").append(e.text).append(System.lineSeparator());
            sb.append("------------------------------------").append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
    

