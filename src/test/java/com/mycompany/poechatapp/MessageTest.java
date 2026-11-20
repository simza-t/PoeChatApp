/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.poechatapp;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 */
public class MessageTest {

    @Test
    public void messageLengthSuccess() {
        Message message = new Message();
        String result = message.checkMessageLength("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", result);
    }

    @Test
    public void messageLengthFailure() {
        Message message = new Message();
        String longText = "";
        for (int i = 0; i < 260; i++) {
            longText = longText + "a";
        }
        String result = message.checkMessageLength(longText);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", result);
    }

    @Test
    public void recipientCorrect() {
        Message message = new Message();
        String result = message.checkRecipientCell("+27718693002");
        assertEquals("Cell phone number successfully captured.", result);
    }

    @Test
    public void recipientIncorrect() {
        Message message = new Message();
        String result = message.checkRecipientCell("08575975889");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", result);
    }

    @Test
    public void messageHashCorrect() {
        Message message = new Message("0012345678", 0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", message.createMessageHash());
    }

    @Test
    public void messageIDCreated() {
        Message message = new Message();
        assertTrue(message.checkMessageID());
        System.out.println("Message ID generated: " + message.getMessageID());
    }

    @Test
    public void sendMessage() {
        Message message = new Message();
        assertEquals("Message successfully sent.", message.SentMessage(1));
    }

    @Test
    public void disregardMessage() {
        Message message = new Message();
        assertEquals("Press 0 to delete the message.", message.SentMessage(2));
    }

    @Test
    public void storeMessage() {
        Message message = new Message();
        assertEquals("Message successfully stored.", message.SentMessage(3));
    }
    
    @Test
    public void returnTotalMessageAfter(){
        Message message= new Message();
        int before= message.returnTotalMessagess();
        message.SentMessage(1);
        assertEquals(before + 1, message.returnTotalMessagess());
        
    }
    
    
    
}