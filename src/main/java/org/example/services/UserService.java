package org.example.services;

import org.example.doa.SearchCriteriaDAO;
import org.example.model.RegistrationStatus;
import org.example.model.User;
import org.example.doa.UserDAO;
import org.example.util.PasswordValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserService {
    private final UserDAO userDAO;
    private final SearchCriteriaDAO searchCriteriaDAO;
    private String loggedInUserEmail;

    private final PasswordValidator passwordValidator;

    public UserService() {
        this.userDAO = new UserDAO();
        this.searchCriteriaDAO = new SearchCriteriaDAO();
        this.passwordValidator = new PasswordValidator();
    }
    public void logoutUser() {
        this.loggedInUserEmail = null;
    }

    public User getUser(int userId) throws SQLException {
        return userDAO.getUser(userId);
    }


    public RegistrationStatus registerUser(String email, String password) {
        if (!passwordValidator.isValidPassword(password)) {
            return RegistrationStatus.invalidPassword();
        }

        try {
            if (userDAO.userExists(email)) {
                return RegistrationStatus.emailExists();
            }
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            if (userDAO.createUser(email, hashedPassword)) {
                return RegistrationStatus.success();
            } else {
                return RegistrationStatus.invalidPassword();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return RegistrationStatus.invalidPassword();
        }
    }

    public User loginUser(String email, String password) {
        try {
            User user = userDAO.getUserByEmail(email);

            if (user != null) {
                String storedHashedPassword = user.getPassword();


                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    this.loggedInUserEmail = email;
                    return user;
                }
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
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
