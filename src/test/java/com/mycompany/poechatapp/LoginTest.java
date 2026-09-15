/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.mycompany.poechatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Student
 */
public class LoginTest {
    
    private Login login;
 
    @BeforeEach
    void setUp() {
        login = new Login();
    }
 
   
 
    @Test
    void testUsernameCorrectlyFormatted() {
        assertTrue(login.checkUserName("kyl_1"));
    }
 
    @Test
    void testUsernameIncorrectlyFormatted() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }
    @Test
    void testPasswordMeetsComplexityRequirements() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }
 
    @Test
    void testPasswordDoesNotMeetComplexityRequirements() {
        assertFalse(login.checkPasswordComplexity("password"));
    }
    
    @Test
    void testCellPhoneCorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }
 
    @Test
    void testCellPhoneIncorrectlyFormatted() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
 
    
    @Test
    void testRegisterUserUsernameIncorrectlyFormatted() {
        String result = login.registerUser("kyle!!!!!!!", "Ch&&sec@ke99!",
                "+27838968976", "Kyle", "Naidoo");
        assertEquals(Login.USERNAME_FAIL_MSG, result);
    }
 
    @Test
    void testRegisterUserPasswordDoesNotMeetRequirements() {
        String result = login.registerUser("kyl_1", "password",
                "+27838968976", "Kyle", "Naidoo");
        assertEquals(Login.PASSWORD_FAIL_MSG, result);
    }
    
}
