package com.nrdc.managementPanel.impl;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.jsonModel.StandardResponse;
import com.nrdc.managementPanel.jsonModel.customizedModel.RoleWithPrivileges;
import com.nrdc.managementPanel.jsonModel.jsonRequest.*;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseGetRoles;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseGetRolesWithPrivileges;
import com.nrdc.managementPanel.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.managementPanel.model.dao.*;
import com.nrdc.managementPanel.model.dao.System;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UserImpl {
    public StandardResponse resetPassword(String token, RequestResetPassword requestResetPassword) throws Exception {
//        Operation operation = new Operation();
        Privilege privilege = Privilege.getPrivilege(PrivilegeNames.RESET_PASSWORD);
//        operation.setFkPrivilegeId(privilege.getId());
        User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
//        operation.setUserToken(token);
//        operation.setFkUserId(user.getId());
//        operation.setTime(new Date());
        try {
            if (!checkUserOldPassword(user.getUsername(), requestResetPassword))
                throw new Exception(Constants.INCORRECT_USERNAME_OR_PASSWORD);
            checkPassword(requestResetPassword);
            setUserNewPassword(user.getUsername(), requestResetPassword);
            StandardResponse response = StandardResponse.getOKResponse();
//            operation.setStatusCode(1L);
            String description = createResetPasswordLog(user);
//            operation.setDescription(description);
//            operation.persist();
            return response;
        } catch (Exception ex) {
//            operation.setStatusCode(-1L);
            String description = createResetPasswordLog(user, ex.getMessage());
//            operation.setDescription(description);
//            operation.persist();
            return StandardResponse.getNOKExceptions(ex);
        }
    }

    private String createResetPasswordLog(User user) {
        StringBuilder stringBuilder = new StringBuilder();
        user.setPassword("");
        stringBuilder.append("User {")
                .append(user)
                .append("}")
                .append(" changed his password .");
        return stringBuilder.toString();
    }

    private String createResetPasswordLog(User user, String exception) {
        StringBuilder stringBuilder = new StringBuilder();
        user.setPassword("");
        stringBuilder.append("User {")
                .append(user)
                .append("}")
                .append(" attempts to change his password . failed with exception :")
                .append(exception);
        return stringBuilder.toString();
    }

    private boolean checkUserOldPassword(String username, RequestResetPassword requestResetPassword) {
        EntityManager entityManager = Database.getEntityManager();
        try {
            Long size = (Long) entityManager.createQuery("SELECT count (u) FROM User u WHERE u.username = :username AND u.password = :password ")
                    .setParameter("username", username)
                    .setParameter("password", requestResetPassword.getOldPassword())
                    .getSingleResult();
            return size.equals(1L);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    private boolean checkPassword(RequestResetPassword requestResetPassword) {
        String password = requestResetPassword.getNewPassword();
        if (password.length() < 8)
            return false;
//        if (!password.matches("[[a-z][0-9]]"))
//            return false;
        return true;
    }

    private void setUserNewPassword(String username, RequestResetPassword requestResetPassword) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.password = :newPassword WHERE u.username = :username")
                    .setParameter("username", username)
                    .setParameter("newPassword", requestResetPassword.getNewPassword())
                    .executeUpdate();
            if (transaction.isActive())
                transaction.commit();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }


    public StandardResponse activeUser(String token, RequestActiveUser request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            User user1 = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            System system = System.getSystem(request.getFkSystemId());
            String privilegeName = "ACTIVATE_" + system.getSystemName() + "_USERS";
            user1.checkPrivilege(privilegeName);
            User user2 = User.getUser(request.getFkUserId());
            List<System> user2Systems = getUserSystems(user2);
            if (!user2Systems.contains(system)) {
                throw new Exception(Constants.USER_SYSTEM_ERROR);
            }
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.isActive = true WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", request.getFkUserId())
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();

            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            if (entityManager.isOpen())
                entityManager.close();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse deActiveUser(String token, RequestDeActiveUser request) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            User user1 = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            System system = System.getSystem(request.getFkSystemId());
            String privilegeName = "DEACTIVATE_" + system.getSystemName() + "_USERS";
            user1.checkPrivilege(privilegeName);
            User user2 = User.getUser(request.getFkUserId());
            if (user1.equals(user2)) {
                throw new Exception(Constants.CANT_DE_ACTIVE_YOURSELF);
            }
            List<System> user2Systems = getUserSystems(user2);
            if (!user2Systems.contains(system)) {
                throw new Exception(Constants.USER_SYSTEM_ERROR);
            }
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.isActive = false WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", request.getFkUserId())
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();

            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;

        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            if (entityManager.isOpen())
                entityManager.close();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse addUser(String token, RequestAddUser requestAddUser) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.ADD_USERS);
            if (!transaction.isActive())
                transaction.begin();
            User u = new User(requestAddUser);
            entityManager.persist(u);
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public List<System> getUserSystems(User user) {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return entityManager.createQuery("SELECT s FROM System s JOIN SystemUser us ON us.fkSystemId = s.id WHERE us.fkUserId = :fkUserId")
                    .setParameter("fkUserId", user.getId())
                    .getResultList();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse editUser(String token, RequestEditUser requestEditUser) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            User user1 = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            System system = System.getSystem(requestEditUser.getFkSystemId());
            String privilegeName = "EDIT_" + system.getSystemName() + "_USERS";
            user1.checkPrivilege(privilegeName);
            User user2 = User.getUser(requestEditUser.getFkUserId());
            List<System> user2Systems = getUserSystems(user2);
            if (!user2Systems.contains(system)) {
                throw new Exception(Constants.USER_SYSTEM_ERROR);
            }
            try {
                boolean b = user2.checkPrivilege(privilegeName);
                if (b)
                    throw new Exception(Constants.CANT_NOT_EDIT_THIS_USER);
            } catch (Exception ex) {
                if (ex.getMessage().equals(Constants.CANT_NOT_EDIT_THIS_USER))
                    throw ex;
            }

            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE User u SET u.username = :username , u.phoneNumber= :phoneNumber , u.phoneNumber = :phoneNumber , u.firstName = :firstName , u.lastName = :lastName , u.nationalId = :nationalId , u.policeCode = :policeCode WHERE u.id = :id")
                    .setParameter("username", requestEditUser.getUsername())
                    .setParameter("phoneNumber", requestEditUser.getPhoneNumber())
                    .setParameter("firstName", requestEditUser.getFirstName())
                    .setParameter("lastName", requestEditUser.getLastName())
                    .setParameter("nationalId", requestEditUser.getNationalId())
                    .setParameter("policeCode", requestEditUser.getPoliceCode())
                    .setParameter("id", requestEditUser.getFkUserId())
                    .executeUpdate();
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getUsers(String token, RequestGetUsers requestAddUser) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            System system = System.getSystem(requestAddUser.getFkSystemId());
            String privilegeName = "GET_" + system.getSystemName() + "_USERS";
            user.checkPrivilege(privilegeName);
            List<User> users = entityManager.createQuery("SELECT u FROM User u JOIN SystemUser us ON u.id = us.fkUserId WHERE us.fkSystemId = :fkSystemId")
                    .setParameter("fkSystemId", system.getId())
                    .getResultList();
            for (User u : users) {
                u.setPassword(null);
            }
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(users);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetUsers);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse filterUsers(String token, RequestFilterUsers requestFilterUsers) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.FILTER_USERS);
            StringBuilder query = new StringBuilder();
            query.append("SELECT u FROM User u ");
            if (requestFilterUsers.getFkSystemId() != null) {
                query.append(" JOIN SystemUser us ON u.id = us.fkUserId ");
            }
            query.append(" WHERE 1=1 ");
            if (requestFilterUsers.getFkSystemId() != null) {
                query.append(" AND us.fkSystemId = ");
                query.append(requestFilterUsers.getFkSystemId());
            }
            if (requestFilterUsers.getUsername() != null) {
                query.append(" AND u.username LIKE '");
                query.append(requestFilterUsers.getUsername());
                query.append("%' ");
            }
            if (requestFilterUsers.getActive() != null) {
                query.append(" AND u.isActive = ");
                query.append(requestFilterUsers.getActive());
            }
            if (requestFilterUsers.getPhoneNumber() != null) {
                query.append(" AND u.phoneNumber LIKE '");
                query.append(requestFilterUsers.getPhoneNumber());
                query.append("%' ");
            }
            if (requestFilterUsers.getFirstName() != null) {
                query.append(" AND u.firstName LIKE '");
                query.append(requestFilterUsers.getFirstName());
                query.append("%' ");
            }
            if (requestFilterUsers.getLastName() != null) {
                query.append(" AND u.lastName LIKE '");
                query.append(requestFilterUsers.getLastName());
                query.append("%' ");
            }
            if (requestFilterUsers.getNationalId() != null) {
                query.append(" AND u.nationalId LIKE '");
                query.append(requestFilterUsers.getNationalId());
                query.append("%' ");
            }
            if (requestFilterUsers.getPoliceCode() != null) {
                query.append(" AND u.policeCode LIKE '");
                query.append(requestFilterUsers.getPoliceCode());
                query.append("%' ");
            }


            List<User> users = entityManager.createQuery(query.toString())
                    .getResultList();
            for (User u : users) {
                u.setPassword(null);
            }
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(users);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetUsers);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getRolesWithPrivileges(String token, RequestGetUserRolesWithPrivileges requestGetUserRolesWithPrivileges) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.GET_PRIVILEGES);
            List<RoleWithPrivileges> rolesWithPrivileges = new LinkedList<>();
            List<Role> roles = entityManager.createQuery("SELECT r FROM Role r JOIN UserRole ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId")
                    .setParameter("fkUserId", requestGetUserRolesWithPrivileges.getFkUserId())
                    .getResultList();
            RoleWithPrivileges roleWithPrivileges;
            for (Role role : roles) {
                roleWithPrivileges = new RoleWithPrivileges();
                roleWithPrivileges.setId(role.getId());
                roleWithPrivileges.setRole(role.getRole());
                roleWithPrivileges.setPrivileges(
                        entityManager.createQuery("SELECT p FROM Privilege p JOIN RolePrivilege rp ON p.id=rp.fkPrivilegeId WHERE rp.fkRoleId = :fkRoleId")
                                .setParameter("fkRoleId", role.getId()).getResultList());
                rolesWithPrivileges.add(roleWithPrivileges);
            }
            ResponseGetRolesWithPrivileges responseGetRolesWithPrivileges = new ResponseGetRolesWithPrivileges();
            responseGetRolesWithPrivileges.setRoleWithPrivileges(rolesWithPrivileges);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetRolesWithPrivileges);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getRoles(String token, RequestGetUserRolesWithPrivileges requestGetUserRolesWithPrivileges) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            User user = User.validate(token, SystemNames.MANAGEMENT_PANEL);
            user.checkPrivilege(PrivilegeNames.GET_ROLES);
            List roles = entityManager.createQuery("SELECT r FROM Role r JOIN UserRole ur ON r.id=ur.fkRoleId WHERE ur.fkUserId = :fkUserId")
                    .setParameter("fkUserId", requestGetUserRolesWithPrivileges.getFkUserId())
                    .getResultList();
            ResponseGetRoles responseGetRoles = new ResponseGetRoles();
            responseGetRoles.setRoles(roles);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
            response.setResponse(responseGetRoles);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
