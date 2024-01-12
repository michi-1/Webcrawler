package org.example.services;

import org.example.doa.SearchCriteriaDAO;
import org.example.model.SearchCriteria;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SearchCriteriaService {
    private SearchCriteriaDAO searchCriteriaDAO;
    private String loggedInUserEmail;

    public SearchCriteriaService() {
        this.searchCriteriaDAO = new SearchCriteriaDAO();
    }

    public void setLoggedInUserEmail(String email) {
        this.loggedInUserEmail = email;
    }


    public boolean saveOrUpdateSearchCriteria(String brand, String model, String year, String price) {
        try {
            if (searchCriteriaDAO.userHasCriteria(loggedInUserEmail)) {
                return false;
            } else {
                searchCriteriaDAO.saveSearchCriteria(loggedInUserEmail, brand, model, year, price);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void deleteSearchCriteria() {
        try {
            searchCriteriaDAO.deleteSearchCriteria(loggedInUserEmail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<SearchCriteria> getAllSearchCriteria() {
        try {
            return searchCriteriaDAO.getAllSearchCriteria();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
