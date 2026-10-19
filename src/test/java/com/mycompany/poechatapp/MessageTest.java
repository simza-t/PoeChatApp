/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poechatapp;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for Part 2 (Sending Messages), using the test data from the
 * task sheet:
 *
 *   Test Case 1: recipient +27718693002,
 *                message "Hi Mike, can you join us for dinner tonight?",
 *                user selects Send.
 *   Test Case 2: recipient 08575975889,
 *                message "Hi Keegan, did you receive the payment?",
 *                user selects Discard.
 *   Number of messages: 2
 */
public class MessageTest {

    // Test data for Message 1 and Message 2 (the IDs are fixed here so that
    // the expected hashes are known; the app itself generates random IDs).
    private static final String ID_1 = "0012345678";
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";

    private static final String ID_2 = "0498765432";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    private Path storageFile;

    @BeforeEach
    void setUp(@TempDir Path tempDir) {
        Message.resetSession();
        storageFile = tempDir.resolve("storedMessages.json");
        Message.setStorageFile(storageFile);
    }

    // ----------------------------------------------------------------
    // The message should not be more than 250 characters
    // ----------------------------------------------------------------

    @Test
    void testMessageLengthSuccess() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    void testMessageLengthExactly250IsAllowed() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, "a".repeat(250));
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    void testMessageLengthFailure() {
        // 260 characters, which is 10 over the limit
        Message msg = new Message(ID_1, 0, RECIPIENT_1, "a".repeat(260));
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                msg.checkMessageLength());
    }

    // ----------------------------------------------------------------
    // The recipient number is correctly formatted
    // ----------------------------------------------------------------

    @Test
    void testRecipientCellSuccess() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Cell phone number successfully captured.",
                msg.checkRecipientCell());
    }

    @Test
    void testRecipientCellFailure() {
        // Test Case 2's number has no international code
        Message msg = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);
        assertEquals("Cell phone number is incorrectly formatted or does not "
                + "contain an international code. Please correct the number "
                + "and try again.", msg.checkRecipientCell());
    }

    // ----------------------------------------------------------------
    // Message hash is correct
    // ----------------------------------------------------------------

    @Test
    void testMessageHashTestCase1() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("00:0:HITONIGHT", msg.createMessageHash());
    }

    @Test
    void testMessageHashRemainingMessagesInALoop() {
        // { message ID, message number, message text, expected hash }
        Object[][] data = {
            { ID_1, 0, TEXT_1, "00:0:HITONIGHT" },
            { ID_2, 1, TEXT_2, "04:1:HIPAYMENT" },
            { "9988776655", 2, "Hi, thanks", "99:2:HITHANKS" },
            { "0000000000", 0, "Hi, thanks", "00:0:HITHANKS" },
            { "5512345678", 3, "Thanks", "55:3:THANKSTHANKS" },
            { "7123456789", 4, "  See you   soon!  ", "71:4:SEESOON" }
        };

        for (Object[] row : data) {
            Message msg = new Message((String) row[0], (Integer) row[1],
                    RECIPIENT_1, (String) row[2]);
            assertEquals((String) row[3], msg.createMessageHash(),
                    "Hash for message \"" + row[2] + "\"");
        }
    }

    // ----------------------------------------------------------------
    // Message ID is created
    // ----------------------------------------------------------------

    @Test
    void testMessageIDCreated() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message ID generated: " + ID_1, msg.messageIDCreated());
    }

    @Test
    void testGeneratedMessageIDIsTenDigits() {
        for (int i = 0; i < 50; i++) {
            Message msg = new Message(i);
            assertEquals(10, msg.getMessageID().length());
            assertTrue(msg.getMessageID().matches("[0-9]{10}"));
            assertTrue(msg.checkMessageID());
            assertEquals("Message ID generated: " + msg.getMessageID(),
                    msg.messageIDCreated());
        }
    }

    @Test
    void testCheckMessageIDTrueForTenCharacters() {
        assertTrue(new Message(ID_1, 0, RECIPIENT_1, TEXT_1).checkMessageID());
    }

    @Test
    void testCheckMessageIDFalseForMoreThanTenCharacters() {
        assertFalse(new Message("00123456789", 0, RECIPIENT_1, TEXT_1).checkMessageID());
    }

    // ----------------------------------------------------------------
    // MessageSent: 1) Send  2) Disregard  3) Store
    // ----------------------------------------------------------------

    @Test
    void testUserSelectedSendMessage() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully sent.", msg.sentMessage(1));
    }

    @Test
    void testUserSelectedDisregardMessage() {
        Message msg = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);
        assertEquals("Press 0 to delete the message.", msg.sentMessage(2));
    }

    @Test
    void testUserSelectedStoreMessage() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Message successfully stored.", msg.sentMessage(3));
    }

    @Test
    void testInvalidChoiceIsRejected() {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertEquals("Invalid option, please choose 1, 2 or 3.", msg.sentMessage(9));
    }

    // ----------------------------------------------------------------
    // Total number of messages sent (Test Case 1 sent, Test Case 2 discarded)
    // ----------------------------------------------------------------

    @Test
    void testReturnTotalMessagesBeforeAnythingIsSent() {
        assertEquals(0, Message.returnTotalMessages());
    }

    @Test
    void testReturnTotalMessagesAfterTestCases() {
        Message msg1 = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        Message msg2 = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);

        msg1.sentMessage(1); // Select Send
        msg2.sentMessage(2); // Select Discard

        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void testSendingTheSameMessageTwiceCountsOnce() {
        Message msg1 = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        msg1.sentMessage(1);
        msg1.sentMessage(1);
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    void testStoredMessagesAreNotCountedAsSent() {
        new Message(ID_1, 0, RECIPIENT_1, TEXT_1).sentMessage(3);
        assertEquals(0, Message.returnTotalMessages());
    }

    // ----------------------------------------------------------------
    // printMessages()
    // ----------------------------------------------------------------

    @Test
    void testPrintMessagesWhenNothingSent() {
        assertEquals("No messages have been sent yet.", Message.printMessages());
    }

    @Test
    void testPrintMessagesShowsDetailsInRequiredOrder() {
        Message msg1 = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        msg1.sentMessage(1);

        String nl = System.lineSeparator();
        String expected = "Message ID: " + ID_1 + nl
                + "Message Hash: 00:0:HITONIGHT" + nl
                + "Recipient: " + RECIPIENT_1 + nl
                + "Message: " + TEXT_1;
        assertEquals(expected, Message.printMessages());
    }

    @Test
    void testPrintMessagesOnlyIncludesSentMessages() {
        Message msg1 = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        Message msg2 = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);
        msg1.sentMessage(1);
        msg2.sentMessage(2);

        String printed = Message.printMessages();
        assertTrue(printed.contains(ID_1));
        assertFalse(printed.contains(ID_2));
    }

    // ----------------------------------------------------------------
    // storeMessage() -> JSON file
    // ----------------------------------------------------------------

    @Test
    void testStoreMessageWritesJsonFile() throws IOException {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        assertTrue(msg.storeMessage());
        assertTrue(Files.exists(storageFile));

        String json = read(storageFile);
        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
        assertTrue(json.contains("\"messageID\": \"" + ID_1 + "\""));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(json.contains("\"recipient\": \"" + RECIPIENT_1 + "\""));
        assertTrue(json.contains("\"message\": \"" + TEXT_1 + "\""));
    }

    @Test
    void testStoringTwoMessagesKeepsBoth() throws IOException {
        new Message(ID_1, 0, RECIPIENT_1, TEXT_1).storeMessage();
        new Message(ID_2, 1, "+27718693002", TEXT_2).storeMessage();

        String json = read(storageFile);
        assertTrue(json.contains(ID_1));
        assertTrue(json.contains(ID_2));
        assertEquals(2, json.split("\"messageID\"", -1).length - 1);
        assertTrue(json.startsWith("["));
        assertTrue(json.endsWith("]"));
    }

    @Test
    void testStoreMessageEscapesQuotesInJson() throws IOException {
        Message msg = new Message(ID_1, 0, RECIPIENT_1, "He said \"hi\" there");
        assertTrue(msg.storeMessage());
        assertTrue(read(storageFile).contains("He said \\\"hi\\\" there"));
    }

    private static String read(Path file) throws IOException {
        return new String(Files.readAllBytes(file), StandardCharsets.UTF_8).trim();
    }
}
    

