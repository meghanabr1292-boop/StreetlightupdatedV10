package com.streetlight;

import com.streetlight.model.User;
import com.streetlight.service.LoginService;

public class LoginServiceTest {

    public static void main(String[] args) {

        LoginService loginService = new LoginService();

        User user = loginService.login("admin", "admin123");

        if (user != null) {
            System.out.println("LoginService test successful!");
            System.out.println("Welcome, " + user.getUsername());
            System.out.println("Role: " + user.getRole());
        } else {
            System.out.println("LoginService test failed!");
        }
    }
}