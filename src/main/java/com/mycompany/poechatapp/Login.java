/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poechatapp;

public class Login {
    /**
 * Login handles registration validation and authentication for QuickChat.
 *
 * Required methods from the PROG5121 Part 1 brief:
 * checkUserName, checkPasswordComplexity, checkCellPhoneNumber,
 * registerUser, loginUser and returnLoginStatus.
 *
 * Cell number check uses startsWith, length and a loop (no regex).
 * South African international format example: +27838968976
 * Reference: Telephone numbers in South Africa
 */


    private String firstName;
    private String lastName;
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCellPhone;
    private boolean registered;
    private boolean loggedIn;

    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|;:'\",.<>?/`~\\";

    public Login() {
        this.firstName = "";
        this.lastName = "";
        this.registeredUsername = "";
        this.registeredPassword = "";
        this.registeredCellPhone = "";
        this.registered = false;
        this.loggedIn = false;
    }

    public Login(String firstName, String lastName) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegisteredUsername() {
        return registeredUsername;
    }

    public String getRegisteredPassword() {
        return registeredPassword;
    }

    public String getRegisteredCellPhone() {
        return registeredCellPhone;
    }

    public boolean isRegistered() {
        return registered;
    }

    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        char[] characters = password.toCharArray();
        for (int i = 0; i < characters.length; i++) {
            char current = characters[i];
            if (Character.isUpperCase(current)) {
                hasCapital = true;
            } else if (Character.isDigit(current)) {
                hasNumber = true;
            } else if (SPECIAL_CHARACTERS.indexOf(current) >= 0 || (!Character.isLetterOrDigit(current))) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellPhoneNumber) {
        if (cellPhoneNumber == null) {
            return false;
        }

        String cell = cellPhoneNumber.trim();

        if (!cell.startsWith("+27")) {
            return false;
        }

        if (cell.length() != 12) {
            return false;
        }

        for (int i = 3; i < cell.length(); i++) {
            if (!Character.isDigit(cell.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public String getUsernameMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    public String getPasswordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    public String getCellPhoneMessage(String cellPhoneNumber) {
        if (checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell number successfully captured.";
        }
        return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    }

    public String registerUser(String username, String password, String cellPhoneNumber) {
        if (!checkUserName(username)) {
            return getUsernameMessage(username);
        }
        if (!checkPasswordComplexity(password)) {
            return getPasswordMessage(password);
        }
        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return getCellPhoneMessage(cellPhoneNumber);
        }

        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCellPhone = cellPhoneNumber.trim();
        this.registered = true;

        return "Username successfully captured.\n"
                + "Password successfully captured.\n"
                + "Cell number successfully captured.\n"
                + "User registered successfully.";
    }

    public boolean loginUser(String username, String password) {
        if (!registered || username == null || password == null) {
            loggedIn = false;
            return false;
        }
        loggedIn = registeredUsername.equals(username) && registeredPassword.equals(password);
        return loggedIn;
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName +", " +  lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
    public String getFullName() {
        return firstName + " " + lastName;
    }
    public boolean isLoggedIn() {
        return loggedIn ;
    }
}