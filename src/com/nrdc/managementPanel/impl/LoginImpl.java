package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestLogin;

public class LoginImpl {
    public StandardResponse login(String token, RequestLogin requestLogin){
    private String getUserPassword(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (String) entityManager.createQuery("SELECT u.password FROM User u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();

        } catch (Exception ex) {
            throw new Exception(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }
}