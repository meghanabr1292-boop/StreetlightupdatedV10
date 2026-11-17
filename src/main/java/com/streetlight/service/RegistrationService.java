package com.streetlight.service;

import com.streetlight.dao.UserDAO;

public class RegistrationService {
    private final UserDAO userDAO = new UserDAO();

    public boolean register(String username, String password) {
        if (username == null || username.trim().length() < 3
                || password == null || password.length() < 6) {
            return false;
        }
        return userDAO.register(username.trim(), password);
    }
}
