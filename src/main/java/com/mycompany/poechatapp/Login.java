/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poechatapp;

import java.util.regex.Pattern;

/**
 *
 * @author Student
 */
public class Login {

   // Contains an underscore AND is no more than 5 characters long overall.
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^(?=.{1,5}$)(?=.*_).*$");
 
    // At least 8 characters, at least one capital letter, one digit and
    // one special (non-alphanumeric) character.
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).{8,}$");
 
    // '+' followed by a 1-3 digit country code and a 4-10 digit number.
    private static final Pattern CELL_PHONE_PATTERN =
            Pattern.compile("^\\+[0-9]{1,3}[0-9]{4,10}$");
 
    // Output messages (kept as constants so they are consistent between
    // registerUser() and any other place that needs to display them).
    public static final String USERNAME_SUCCESS_MSG =
            "Username successfully captured.";
    public static final String USERNAME_FAIL_MSG =
            "Username is not correctly formatted; please ensure that your "
            + "username contains an underscore and is no more than five "
            + "characters in length.";
 
    public static final String PASSWORD_SUCCESS_MSG =
            "Password successfully captured.";
    public static final String PASSWORD_FAIL_MSG =
            "Password is not correctly formatted; please ensure that the "
            + "password contains at least eight characters, a capital "
            + "letter, a number, and a special character.";
 
    public static final String CELL_SUCCESS_MSG =
            "Cell phone number successfully added.";
    public static final String CELL_FAIL_MSG =
            "Cell phone number incorrectly formatted or does not contain "
            + "international code.";
 
    public static final String LOGIN_FAIL_MSG =
            "Username or password incorrect, please try again.";
 
    // Stored account details (this simple implementation supports a
    // single registered user, which is sufficient for the scope of this
    // task; it can be extended to a collection of Login objects/list if
    // multiple accounts are required).
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;
    private boolean registered = false;
 
    public Login() {
    }
 
    public Login(String username, String password, String cellPhoneNumber,
                 String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }
 
    /**
     * Checks that the username contains an underscore and is no more than
     * five characters long.
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }
 
    /**
     * Checks that the password is at least eight characters long, and
     * contains a capital letter, a number and a special character.
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }
 
    /**
     * Checks that the cell phone number contains the international
     * country code followed by a number that is no more than ten
     * characters long.
     */
    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }
        return CELL_PHONE_PATTERN.matcher(cellPhoneNumber).matches();
    }
 
    /**
     * Registers a new user, validating the username, password and cell
     * phone number (in that order) and returning the appropriate message.
     * If all three checks pass, the user's details are stored and a
     * success message is returned.
     */
    public String registerUser(String username, String password,
                                String cellPhoneNumber, String firstName,
                                String lastName) {
        if (!checkUserName(username)) {
            return USERNAME_FAIL_MSG;
        }
        if (!checkPasswordComplexity(password)) {
            return PASSWORD_FAIL_MSG;
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return CELL_FAIL_MSG;
        }
 
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.registered = true;
 
        return USERNAME_SUCCESS_MSG + " " + PASSWORD_SUCCESS_MSG + " "
                + CELL_SUCCESS_MSG;
    }
 
    /**
     * Verifies that the supplied username and password match the
     * currently registered account.
     */
    public boolean loginUser(String username, String password) {
        if (!registered || username == null || password == null) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }
 
    /**
     * Returns the appropriate message depending on whether the login
     * attempt succeeded or failed.
     */
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }
        return LOGIN_FAIL_MSG;
    }
 
    // --- Simple getters, useful for the console app / future extension ---
    public String getUsername() {
        return username;
    }
 
    public String getFirstName() {
        return firstName;
    }
 
    public String getLastName() {
        return lastName;
    }
 
    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }
 
    public boolean isRegistered() {
        return registered;
    }
}

