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
        // Test Data: "kyl_1"
        assertTrue(login.checkUserName("kyl_1"));
    }
 
    @Test
    void testUsernameIncorrectlyFormatted() {
        // Test Data: "kyle!!!!!!!"
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }
    
}
