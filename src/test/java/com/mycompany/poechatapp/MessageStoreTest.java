/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poechatapp;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author user
 */
public class MessageStoreTest {
    private MessageStore store;

    @Before
    public void setUp() {
        store = new MessageStore();
        store.loadTestData();
    }

    @Test
    public void sentMessagesArrayPopulated() {
        String[] sent = store.getSentMessages();
        assertEquals("Did you get the cake?", sent[0]);
        assertEquals("It is dinner time!", sent[1]);
    }

    @Test
    public void longestMessage() {
        assertEquals(
            "Where are you? You are late! I have asked you to be on time.",
            store.displayLongestMessage()
        );
    }

    @Test
    public void searchMessageID() {
        assertEquals("It is dinner time!", store.searchMessageFour("0838884567"));
    }

    @Test
    public void searchByRecipient() {
        String result = store.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void deleteByHash() {
        String result = store.deleteByHash("MSG2HASH");
        assertEquals(
            "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
            result
        );
    }

    @Test
    public void displayReport() {
        String report = store.displayReport();
        assertTrue(report.contains("Message Hash"));
        assertTrue(report.contains("Recipient"));
        assertTrue(report.contains("Message"));
    }
    
}
