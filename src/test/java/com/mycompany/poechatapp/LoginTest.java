/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.mycompany.poechatapp;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 *
 * @author user
 */


//JUnit 4 tests use the official test data fromthe brief

public class LoginTest {
     private Login login;

    @Before
    public void setUp() {
        login = new Login("Kyle", "Smith");
    }

    @Test
    public void usernameCorrectlyFormattedMessage() {
        assertEquals("Username successfully captured.", login.getUsernameMessage("kyl_1"));
    }

    @Test
    public void usernameIncorrectlyFormattedMessage() {
        assertEquals(
            "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
            login.getUsernameMessage("kyle!!!!!!!")
        );
    }

    @Test
    public void passwordMeetsComplexityMessage() {
        assertEquals("Password successfully captured.", login.getPasswordMessage("Ch&&sec@ke99!"));
    }

    @Test
    public void passwordDoesNotMeetComplexityMessage() {
        assertEquals(
            "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
            login.getPasswordMessage("password")
        );
    }

    @Test
    public void cellPhoneCorrectlyFormattedMessage() {
        assertEquals("Cell number successfully captured.", login.getCellPhoneMessage("+27838968976"));
    }

    @Test
    public void cellPhoneIncorrectlyFormattedMessage() {
        assertEquals(
            "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
            login.getCellPhoneMessage("08966553")
        );
    }

    @Test
    public void successfulLoginWelcomeMessage() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void failedLoginMessage() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Username or password incorrect, please try again.",
                login.returnLoginStatus("kyl_1", "wrongPass1!"));
    }

    @Test
    public void loginSuccessful() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void loginFailed() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("wrong", "password"));
    }

    @Test
    public void usernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void usernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    public void passwordMeetsComplexity() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void passwordDoesNotMeetComplexity() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void cellPhoneCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void cellPhoneIncorrectlyFormatted() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}