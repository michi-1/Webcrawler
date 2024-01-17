package org.example.services;

import org.example.doa.SearchCriteriaDAO;
import org.example.model.User;
import org.example.doa.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserService {
    private UserDAO userDAO;
    private SearchCriteriaDAO searchCriteriaDAO;
    private String loggedInUserEmail;

    public UserService() {
        this.userDAO = new UserDAO();
        this.searchCriteriaDAO = new SearchCriteriaDAO();
    }

    public User getUser(int userId) throws SQLException {
        return userDAO.getUser(userId);
    }

    public boolean registerUser(String email, String password) {
        try {
            if (userDAO.userExists(email)) {
                return false;
            }
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            return userDAO.createUser(email, hashedPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User loginUser(String email, String password) {
        try {
            User user = userDAO.getUserByEmail(email);

            if (user != null) {
                // Das in der Datenbank gespeicherte gehashte Passwort abrufen
                String storedHashedPassword = user.getPassword();

                // Das eingegebene Passwort mit dem gehashten Passwort vergleichen
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    // Einloggen erfolgreich
                    this.loggedInUserEmail = email;
                    return user;
                }
            }
            // Einloggen fehlgeschlagen
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            // Fehler beim Einloggen
            return null;
        }
    }

    public void logoutUser() {
        this.loggedInUserEmail = null;
    }

    public boolean deleteUser(int userId) {
        try {
            String email = getUser(userId).getEmail();
            if (email != null) {
                searchCriteriaDAO.deleteSearchCriteria(email);
            }
            return userDAO.deleteUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getUserIdByEmail(String email) {
        try {
            return userDAO.getUserIdByEmail(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
