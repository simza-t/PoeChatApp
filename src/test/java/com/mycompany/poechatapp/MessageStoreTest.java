/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.poechatapp;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/** Part 3 unit tests, using the five test data messages from the brief. */
public class MessageStoreTest {

    // ----- Test data (from the brief) -----
    private static final String ID1 = "1000000001";
    private static final String ID2 = "1000000002";
    private static final String ID3 = "1000000003";
    private static final String ID4 = "1000000004";
    private static final String ID5 = "1000000005";

    private static final String HASH1 = "10:0:DIDCAKE?";
    private static final String HASH2 = "10:1:WHERETIME.";
    private static final String HASH3 = "10:2:YOHOOOO,GATE.";
    private static final String HASH4 = "10:3:ITTIME!";
    private static final String HASH5 = "10:4:OK,YOU.";

    private static final String RECIPIENT1 = "+27834557896";
    private static final String RECIPIENT2 = "+27838884567";
    private static final String RECIPIENT3 = "+27834484567";
    private static final String RECIPIENT4 = "0838884567"; // developer entry
    private static final String RECIPIENT5 = "+27838884567";

    private static final String MSG1 = "Did you get the cake?";
    private static final String MSG2 = "Where are you? You are late! I have asked you to be on time.";
    private static final String MSG3 = "Yohoooo, I am at your gate.";
    private static final String MSG4 = "It is dinner time!";
    private static final String MSG5 = "Ok, I am leaving without you.";

    private MessageStore store;

    @Before
    public void setUp() {
        store = new MessageStore();
        store.setSender("Kyle Naidoo");

        store.addSent(ID1, HASH1, RECIPIENT1, MSG1);          // Message 1 - Sent
        store.addStored(ID2, HASH2, RECIPIENT2, MSG2);        // Message 2 - Stored
        store.addDisregarded(ID3, HASH3, RECIPIENT3, MSG3);   // Message 3 - Disregard
        store.addSent(ID4, HASH4, RECIPIENT4, MSG4);          // Message 4 - Sent
        store.addStored(ID5, HASH5, RECIPIENT5, MSG5);        // Message 5 - Stored
    }

    // ----- Sent Messages array correctly populated -----
    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        List<String> expected = Arrays.asList("Did you get the cake?", "It is dinner time!");
        assertEquals(expected, store.getSentMessages());
    }

    // ----- Other arrays are populated correctly too -----
    @Test
    public void testDisregardedMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(MSG3), store.getDisregardedMessages());
    }

    @Test
    public void testStoredMessagesArrayCorrectlyPopulated() {
        assertEquals(Arrays.asList(MSG2, MSG5), store.getStoredMessages());
    }

    @Test
    public void testMessageHashAndIDArraysCorrectlyPopulated() {
        assertEquals(Arrays.asList(HASH1, HASH2, HASH3, HASH4, HASH5), store.getMessageHashes());
        assertEquals(Arrays.asList(ID1, ID2, ID3, ID4, ID5), store.getMessageIDs());
    }

    // ----- Display the longest message -----
    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                store.longestMessage());
    }

    // ----- Search for message ID (test data: message 4) -----
    @Test
    public void testSearchForMessageID() {
        assertEquals("It is dinner time!", store.getMessageTextByID(ID4));
        assertEquals("Recipient: 0838884567 | Message: It is dinner time!",
                store.searchByMessageID(ID4));
    }

    @Test
    public void testSearchForMessageIDNotFound() {
        assertEquals("No message was found with the message ID: 9999999999",
                store.searchByMessageID("9999999999"));
    }

    // ----- Search all messages for a particular recipient -----
    @Test
    public void testSearchMessagesForRecipient() {
        List<String> expected = Arrays.asList(
                "Where are you? You are late! I have asked you to be on time.",
                "Ok, I am leaving without you.");
        assertEquals(expected, store.getMessagesForRecipient("+27838884567"));
    }

    // ----- Delete a message using the message hash (test message 2) -----
    @Test
    public void testDeleteMessageUsingHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                store.deleteByHash(HASH2));

        // it must be gone from the arrays as well
        assertEquals(Arrays.asList(MSG5), store.getStoredMessages());
        assertEquals(Arrays.asList(HASH1, HASH3, HASH4, HASH5), store.getMessageHashes());
        assertEquals(Arrays.asList(ID1, ID3, ID4, ID5), store.getMessageIDs());
    }

    @Test
    public void testDeleteMessageHashNotFound() {
        assertEquals("No message was found with the message hash: 00:0:NOPE",
                store.deleteByHash("00:0:NOPE"));
    }

    // ----- Display report -----
    @Test
    public void testDisplayReport() {
        String report = store.buildReport();

        // hash, recipient and message of the sent messages must all be in the report
        assertTrue(report.contains(HASH1));
        assertTrue(report.contains(RECIPIENT1));
        assertTrue(report.contains(MSG1));
        assertTrue(report.contains(HASH4));
        assertTrue(report.contains(RECIPIENT4));
        assertTrue(report.contains(MSG4));

        // a disregarded message must NOT be in the report
        assertTrue(!report.contains(MSG3));
    }

    // ----- Sender and recipient display -----
    @Test
    public void testDisplaySenderAndRecipient() {
        String result = store.displaySenderAndRecipient();
        assertTrue(result.contains("Sender: Kyle Naidoo | Recipient: +27834557896"));
        assertTrue(result.contains("Recipient: 0838884567"));
        assertTrue(!result.contains("+27834484567")); // disregarded message's recipient
    }
}
