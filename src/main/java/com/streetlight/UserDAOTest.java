package com.streetlight;

import com.streetlight.dao.UserDAO;
import com.streetlight.model.User;

public class UserDAOTest {

    public static void main(String[] args) {

        UserDAO userDAO = new UserDAO();

        User user = userDAO.login("admin", "admin123");

        if (user != null) {
            System.out.println("Login test successful!");
            System.out.println("User ID: " + user.getUserId());
            System.out.println("Username: " + user.getUsername());
            System.out.println("Role: " + user.getRole());
        } else {
            System.out.println("Login test failed!");
        }
    }
}