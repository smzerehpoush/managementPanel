package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.*;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.customizedModel.RoleWithPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.*;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetRoles;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetRolesWithPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.policeHamrah.model.dao.*;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;
import com.nrdc.policeHamrah.model.dto.RoleDto;
import com.nrdc.policeHamrah.model.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserImpl {
    public StandardResponse assignUserToSystem(String token, RequestAssignSystemToUser requestAssignSystemToUser) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            UserDao adminUser = UserDao.validate(token);
            UserDao user = UserDao.getUser(requestAssignSystemToUser.getFkUserId());
            for (Long fkSystemId : requestAssignSystemToUser.getFkSystemIdList()) {
                adminUser.checkPrivilege(PrivilegeNames.ASSIGN_SYSTEM, fkSystemId);
                Long size = (Long) entityManager.createQuery("SELECT COUNT (su) FROM SystemUserDao su WHERE su.fkSystemId = :fkSystemId AND su.fkUserId = :fkUserId")
                        .setParameter("fkSystemId", fkSystemId)
                        .setParameter("fkUserId", user.getId())
                        .getSingleResult();
                if (!size.equals(1L)) {
                    SystemUserDao systemUserDao = new SystemUserDao();
                    systemUserDao.setFkUserId(requestAssignSystemToUser.getFkUserId());
                    systemUserDao.setFkSystemId(fkSystemId);
                    SystemDao systemDao = SystemDao.getSystem(fkSystemId);
                    if (systemDao.getSystemName().equals(SystemNames.VEHICLE_TICKET.name())) {
                        entityManager.persist(systemUserDao);
                        addUserToSystem(user, fkSystemId);
                    } else if (systemDao.getSystemName().equals(SystemNames.VT_REPORT.name())) {
                        entityManager.persist(systemUserDao);
                        SystemDao VTSystem = SystemDao.getSystem(SystemNames.VEHICLE_TICKET);
                        addUserToSystem(user, VTSystem.getId());
                    } else if (systemDao.getSystemName().equals(SystemNames.AGAHI.name()) || systemDao.getSystemName().equals(SystemNames.GASHT.name()) || systemDao.getSystemName().equals(SystemNames.CRASHES.name())) {
                        entityManager.persist(systemUserDao);
                    } else if (systemDao.getSystemName().equals(SystemNames.NAZER.name())) {
                        checkUserInNazer(user);
                        entityManager.persist(systemUserDao);
                    } else {
                        throw new ServerException(Constants.FEATURE_NOT_SUPPORTED);
                    }
                }
            }
            if (transaction.isActive())
                transaction.commit();
            return new StandardResponse();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private class RequestNazerAuth {
        private String phoneNumber;
        private String password;

        public RequestNazerAuth(String phoneNumber, String password) {
            this.phoneNumber = phoneNumber;
            this.password = password;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    private void checkUserInNazer(UserDao user) throws Exception {
        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty())
            throw new Exception(Constants.PHONE_NUMBER + Constants.USER + Constants.IS_REQUIRED);
        SystemDao systemDao = SystemDao.getSystem(SystemNames.NAZER);
        RequestNazerAuth request = new RequestNazerAuth(user.getPhoneNumber(), user.getPassword());
        String output = CallWebService.callPostService(systemDao.getSystemPath() + "/authenticateUser", request);
        StandardResponse response = MyGsonBuilder.build().fromJson(output, StandardResponse.class);
        if (response.getResultCode() == -1) {
            if (response.getResultMessage().trim().equals("1"))
                throw new ServerException(Constants.IN + systemDao.getTitle() + Constants.UNKNOWN_USER );
            else if (response.getResultMessage().trim().equals("3"))
                throw new ServerException(Constants.NAZER_NOT_APN_USER);
        }
    }

    private void addUserToSystem(UserDao user, Long fkSystemId) throws Exception {
        RequestAddUser requestAddUser = new RequestAddUser();
        requestAddUser.setFirstName(user.getFirstName());
        requestAddUser.setLastName(user.getLastName());
        requestAddUser.setNationalId(user.getNationalId());
        requestAddUser.setPassword(user.getPassword());
        requestAddUser.setPhoneNumber(user.getPhoneNumber());
        requestAddUser.setPoliceCode(user.getPoliceCode());
        requestAddUser.setUsername(user.getUsername());
        String path = SystemDao.getSystem(fkSystemId).getSystemPath() + "/addUser";
        String output = CallWebService.callPostService(path, requestAddUser);
        StandardResponse response = MyGsonBuilder.build().fromJson(output, StandardResponse.class);
        if (response.getResultCode() != 1)
            throw new ServerException(response.getResultMessage());

    }

    private void mergeAndRemove(List<Long> A, List<Long> B, boolean isSysAdmin, Long userId, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            if (!transaction.isActive())
                transaction.begin();
            List<Long> B2 = (List<Long>) ((ArrayList<Long>) B).clone();
            B2.removeAll(A);
            for (Long b : B2) {
                RoleDao role = getRole(b);
                if (role.getRole().equals(Constants.SYS_ADMIN)) {
                    if (!isSysAdmin)
                        throw new ServerException(Constants.CAN_NOT_ASSIGN_THIS_ROLE);
                }
                if (!role.getFkSystemId().equals(fkSystemId))
                    throw new ServerException(Constants.NOT_VALID_ROLE_SYSTEM);
                UserRoleDao ur = new UserRoleDao();
                ur.setFkRoleId(b);
                ur.setFkUserId(userId);
                entityManager.persist(ur);
            }
            A.removeAll(B);
            for (Long a : A) {
                RoleDao role = getRole(a);
                if (role.getRole().equals(Constants.SYS_ADMIN)) {
                    if (!isSysAdmin)
                        throw new ServerException(Constants.CAN_NOT_ASSIGN_THIS_ROLE);
                }
                if (!role.getFkSystemId().equals(fkSystemId))
                    throw new ServerException(Constants.NOT_VALID_ROLE_SYSTEM);
                entityManager.createQuery("DELETE FROM UserRoleDao ur WHERE ur.fkRoleId = :fkRoleId AND ur.fkUserId = :fkUserId ")
                        .setParameter("fkUserId", userId)
                        .setParameter("fkRoleId", a)
                        .executeUpdate();
            }
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

    private RoleDao getRole(Long roleId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            roleValidation(roleId);
            return (RoleDao) entityManager.createQuery("SELECT r FROM RoleDao r WHERE r.id = :roleId ")
                    .setParameter("roleId", roleId)
                    .getSingleResult();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

    }

    public StandardResponse assignRole(String token, RequestAssignRole requestAssignRole) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.ASSIGN_ROLE, requestAssignRole.getFkSystemId());
            boolean isUserSysAdmin = isUserSysAdmin(user.getId(), requestAssignRole.getFkSystemId());
            List roleIdListInDb = entityManager.createQuery("SELECT r.id FROM RoleDao r JOIN UserRoleDao ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", requestAssignRole.getFkUserId())
                    .setParameter("fkSystemId", requestAssignRole.getFkSystemId())
                    .getResultList();

            mergeAndRemove(roleIdListInDb, requestAssignRole.getFkRoleIdList(), isUserSysAdmin, requestAssignRole.getFkUserId(), requestAssignRole.getFkSystemId());
            return new StandardResponse();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private boolean isUserSysAdmin(Long userId, Long fkSystemId) {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            Long size = (Long) entityManager.createQuery("SELECT COUNT (r) FROM RoleDao r JOIN UserRoleDao ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND r.role = :role AND r.fkSystemId = :fkSystemId ")
                    .setParameter("fkUserId", userId)
                    .setParameter("fkSystemId", fkSystemId)
                    .setParameter("role", Constants.SYS_ADMIN)
                    .getSingleResult();
            return size.equals(1L);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

    }

    private void roleValidation(Long roleId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            Long size = (Long) entityManager.createQuery("SELECT COUNT (r) FROM RoleDao r WHERE r.id = :roleId ")
                    .setParameter("roleId", roleId)
                    .getSingleResult();
            if (!size.equals(1L))
                throw new ServerException(Constants.ROLE + Constants.IS_NOT_VALID);

        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse resetPassword(String token, RequestResetPassword requestResetPassword) throws Exception {
//        OperationDao operation = new OperationDao();
//        operation.setFkPrivilegeId(privilege.getId());
        UserDao userDao = UserDao.validate(token);
        SystemDao systemDao = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
        userDao.checkPrivilege(PrivilegeNames.RESET_PASSWORD, systemDao.getId());
//        operation.setUserToken(token);
//        operation.setFkUserId(user.getId());
//        operation.setTime(new Date());
        UserDao user = UserDao.getUser(requestResetPassword.getFkUserId());
        try {
            if (!checkUserPassword(user.getUsername(), requestResetPassword.getOldPassword()))
                throw new ServerException(Constants.INCORRECT_USERNAME_OR_PASSWORD);
            if (!checkPassword(requestResetPassword.getNewPassword()))
                throw new ServerException(Constants.PASSWORD + Constants.IS_NOT_VALID);
            setUserNewPassword(user.getUsername(), requestResetPassword.getNewPassword());
            StandardResponse response = new StandardResponse();
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
            throw ex;
        }
    }

    public StandardResponse resetPassword(String token, Long fkUserId) throws Exception {
//        OperationDao operation = new OperationDao();
        UserDao user = UserDao.validate(token);
        SystemDao phSystem = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
        user.checkPrivilege(PrivilegeNames.RESET_PASSWORD, phSystem.getId());
        UserDao user2 = UserDao.getUser(fkUserId);
//        operation.setFkPrivilegeId(privilege.getId());
//        operation.setUserToken(token);
//        operation.setFkUserId(user.getId());
//        operation.setTime(new Date());
        try {
            setUserNewPassword(user2.getUsername(), "123456");
            StandardResponse response = new StandardResponse();
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

    private String createResetPasswordLog(UserDao user) {
        StringBuilder stringBuilder = new StringBuilder();
        user.setPassword("");
        stringBuilder.append("UserDao {")
                .append(user)
                .append("}")
                .append(" changed his password .");
        return stringBuilder.toString();
    }

    private String createResetPasswordLog(UserDao user, String exception) {
        StringBuilder stringBuilder = new StringBuilder();
        user.setPassword("");
        stringBuilder.append("UserDao {")
                .append(user)
                .append("}")
                .append(" attempts to change his password . failed with exception :")
                .append(exception);
        return stringBuilder.toString();
    }

    private boolean checkUserPassword(String username, String password) {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            Long size = (Long) entityManager.createQuery("SELECT COUNT (u) FROM UserDao u WHERE u.username = :username AND u.password = :password ")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
            return size.equals(1L);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private boolean checkPassword(String password) throws Exception {
        return password.matches("^[a-zA-Z0-9]{5,10}$");
    }

    private void setUserNewPassword(String username, String password) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE UserDao u SET u.password = :password WHERE u.username = :username")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .executeUpdate();
            if (transaction.isActive())
                transaction.commit();
            else {
                transaction = entityManager.getTransaction();
                transaction.begin();
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }


    public StandardResponse activeUser(String token, Long fkUserId, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user1 = UserDao.validate(token);
            if (user1.getId().equals(fkUserId))
                throw new ServerException(Constants.CAN_NOT_DE_ACTIVE_THIS_USER);
            user1.checkPrivilege(PrivilegeNames.ACTIVE_USER, fkSystemId);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            UserDao user2 = UserDao.getUser(fkUserId);
            List<SystemDao> user2SystemList = user2.systems();
            if (!user2SystemList.contains(systemDao)) {
                throw new ServerException(Constants.USER_SYSTEM_ERROR);
            }
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE UserDao u SET u.isActive = true WHERE u.id = :fkUserId ")
                    .setParameter("fkUserId", fkUserId)
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();

            StandardResponse response = new StandardResponse<>();
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

    public StandardResponse deActiveUser(String token, Long fkUserId, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user1 = UserDao.validate(token);
            user1.checkPrivilege(PrivilegeNames.DE_ACTIVE_USER, fkSystemId);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            UserDao user2 = UserDao.getUser(fkUserId);
            if (user1.getId().equals(user2.getId()))
                throw new ServerException(Constants.CAN_NOT_DE_ACTIVE_YOURSELF);
            List<SystemDao> user2SystemList = user2.systems();
            if (!user2SystemList.contains(systemDao)) {
                throw new ServerException(Constants.USER_SYSTEM_ERROR);
            }
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE UserDao u SET u.isActive = false WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", fkUserId)
                    .executeUpdate();
            if (transaction != null && transaction.isActive())
                transaction.commit();

            return new StandardResponse<>();

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


    public StandardResponse addUser(String token, RequestAddUser requestAddUser, boolean checkPrivilege) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            if (checkPrivilege) {
                UserDao user = UserDao.validate(token);
                user.checkPrivilege(PrivilegeNames.ADD_USER, requestAddUser.getFkSystemId());
            }
            if (!transaction.isActive())
                transaction.begin();
            Long userId = (Long) entityManager.createQuery("SELECT MAX (u.id) FROM UserDao u").getSingleResult() + 1;
//            if (!requestAddUser.getPhoneNumber().equals("09121316873") && !requestAddUser.getPoliceCode().equals("270030"))
            checkRequestAddUser(requestAddUser);
            UserDao u = new UserDao(requestAddUser);
            u.setId(userId);
            entityManager.persist(u);
            if (transaction.isActive()) {
                transaction.commit();
                entityManager.close();
                entityManager = Database.getEntityManager();
                transaction = entityManager.getTransaction();
                transaction.begin();
                SystemUserDao systemUser = new SystemUserDao();
                systemUser.setFkSystemId(requestAddUser.getFkSystemId());
                systemUser.setFkUserId(userId);
                SystemUserDao systemUser2 = new SystemUserDao();
                SystemDao phSystem = SystemDao.getSystem(SystemNames.POLICE_HAMRAH);
                entityManager.persist(systemUser);
                if (!phSystem.getId().equals(requestAddUser.getFkSystemId())) {
                    systemUser2.setFkSystemId(phSystem.getId());
                    systemUser2.setFkUserId(userId);
                    entityManager.persist(systemUser2);
                }
                transaction.commit();
                return new StandardResponse<>();
            } else {
                throw new ServerException(Constants.UNKNOWN_ERROR);
            }
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkRequestAddUser(RequestAddUser requestAddUser) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        Long size;
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            //check username
            if (requestAddUser.getUsername() == null || requestAddUser.getUsername().equals(""))
                throw new ServerException(Constants.USERNAME + Constants.IS_REQUIRED);
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.username = :username")
                    .setParameter("username", requestAddUser.getUsername())
                    .getSingleResult();
            if (size > 0)
                throw new ServerException(Constants.USERNAME + Constants.DUPLICATED);
            //check policeCode
            if (requestAddUser.getPoliceCode() != null && !requestAddUser.getPoliceCode().equals(""))
                size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.policeCode= :policeCode")
                        .setParameter("policeCode", requestAddUser.getPoliceCode())
                        .getSingleResult();
            if (size > 0)
                throw new ServerException(Constants.POLICE_CODE + Constants.DUPLICATED);
            //check phoneNumber
            if (requestAddUser.getPhoneNumber() != null && !requestAddUser.getPhoneNumber().equals("")) {
                size = (Long) entityManager.createQuery("SELECT COUNT (u) FROM UserDao u WHERE u.phoneNumber= :phoneNumber")
                        .setParameter("phoneNumber", requestAddUser.getPhoneNumber())
                        .getSingleResult();
                if (size > 0)
                    throw new ServerException(Constants.PHONE_NUMBER + Constants.DUPLICATED);
            }
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse editUser(String token, RequestEditUser requestEditUser) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            //admin user
            UserDao adminUser = UserDao.validate(token);

            SystemDao systemDao = SystemDao.getSystem(requestEditUser.getFkSystemId());
            adminUser.checkPrivilege(PrivilegeNames.EDIT_USER, requestEditUser.getFkSystemId());
            UserDao user = UserDao.getUser(requestEditUser.getFkUserId());
            List<SystemDao> userSystemList = user.systems();
            if (!userSystemList.contains(systemDao)) {
                throw new ServerException(Constants.USER_SYSTEM_ERROR);
            }
//            checkRequestEditUser(requestEditUser);
            if (!transaction.isActive())
                transaction.begin();
            try {
                adminUser.checkPrivilege(PrivilegeNames.FULL_EDIT, requestEditUser.getFkSystemId());
                entityManager.createQuery("UPDATE UserDao u SET " +
                        " u.username = :username ," +
                        " u.nationalId = :nationalId ," +
                        " u.phoneNumber = :phoneNumber ," +
                        " u.policeCode = :policeCode ," +
                        " u.firstName = :firstName , " +
                        " u.lastName = :lastName " +
                        " WHERE u.id = :fkUserId")
                        .setParameter("firstName", requestEditUser.getFirstName())
                        .setParameter("lastName", requestEditUser.getLastName())
                        .setParameter("username", requestEditUser.getUsername())
                        .setParameter("nationalId", requestEditUser.getNationalId())
                        .setParameter("phoneNumber", requestEditUser.getPhoneNumber())
                        .setParameter("policeCode", requestEditUser.getPoliceCode())
                        .setParameter("fkUserId", requestEditUser.getFkUserId())
                        .executeUpdate();
            } catch (Exception ignored) {
                entityManager.createQuery("UPDATE UserDao u SET " +
                        " u.firstName = :firstName ," +
                        " u.lastName = :lastName " +
                        " WHERE u.id = :fkUserId")
                        .setParameter("firstName", requestEditUser.getFirstName())
                        .setParameter("lastName", requestEditUser.getLastName())
                        .setParameter("fkUserId", requestEditUser.getFkUserId())
                        .executeUpdate();
            }
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();


            return response;
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkUsername(String username, Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Long size;
        try {
            //check username
            if (username == null || username.equals(""))
                throw new ServerException(Constants.USERNAME + Constants.IS_REQUIRED);
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
            if (size.equals(1L)) {
                Long id = (Long) entityManager.createQuery("SELECT (u.id) FROM UserDao u WHERE u.username = :username")
                        .setParameter("username", username)
                        .getSingleResult();
                if (!id.equals(fkUserId))
                    throw new ServerException(Constants.USERNAME + Constants.DUPLICATED);

            }
            if (size > 1)
                throw new ServerException(Constants.USERNAME + Constants.DUPLICATED);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkNationalId(String nationalId, Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Long size;
        try {
            //check nationalId
            if (nationalId == null || nationalId.equals(""))
                throw new ServerException(Constants.NATIONAL_ID + Constants.IS_REQUIRED);
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.nationalId = :username")
                    .setParameter("username", nationalId)
                    .getSingleResult();
            if (size.equals(1L)) {
                Long id = (Long) entityManager.createQuery("SELECT (u.id) FROM UserDao u WHERE u.nationalId = :nationalId")
                        .setParameter("nationalId", nationalId)
                        .getSingleResult();
                if (!id.equals(fkUserId))
                    throw new ServerException(Constants.NATIONAL_ID + Constants.IS_NOT_VALID);

            }
            if (size > 1)
                throw new ServerException(Constants.NATIONAL_ID + Constants.DUPLICATED);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkPhoneNumber(String phoneNumber, Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Long size;
        try {
            //check phoneNumber
            if (phoneNumber == null || phoneNumber.equals(""))
                throw new ServerException(Constants.PHONE_NUMBER + Constants.IS_REQUIRED);
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.phoneNumber = :phoneNumber")
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            if (size.equals(1L)) {
                Long id = (Long) entityManager.createQuery("SELECT (u.id) FROM UserDao u WHERE u.phoneNumber= :phoneNumber")
                        .setParameter("phoneNumber", phoneNumber)
                        .getSingleResult();
                if (!id.equals(fkUserId))
                    throw new ServerException(Constants.PHONE_NUMBER + Constants.IS_NOT_VALID);

            }
            if (size > 1)
                throw new ServerException(Constants.PHONE_NUMBER + Constants.DUPLICATED);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkPoliceCode(String policeCode, Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Long size;
        try {
            //check policeCode
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.policeCode = :policeCode")
                    .setParameter("policeCode", policeCode)
                    .getSingleResult();
            if (size.equals(1L)) {
                Long id = (Long) entityManager.createQuery("SELECT (u.id) FROM UserDao u WHERE u.policeCode = :policeCode")
                        .setParameter("policeCode", policeCode)
                        .getSingleResult();
                if (!id.equals(fkUserId))
                    throw new ServerException(Constants.POLICE_CODE + Constants.IS_NOT_VALID);

            }
            if (size > 1)
                throw new ServerException(Constants.POLICE_CODE + Constants.DUPLICATED);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private void checkRequestEditUser(RequestEditUser requestEditUser) throws Exception {
        checkUsername(requestEditUser.getUsername(), requestEditUser.getFkUserId());
        checkNationalId(requestEditUser.getNationalId(), requestEditUser.getFkUserId());
        checkPoliceCode(requestEditUser.getPoliceCode(), requestEditUser.getFkUserId());
        checkPhoneNumber(requestEditUser.getPhoneNumber(), requestEditUser.getFkUserId());

    }


    public StandardResponse<ResponseGetUsers> filterUsers(String token, RequestFilterUsers requestFilterUsers) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            StringBuilder query = new StringBuilder();
            query.append("SELECT distinct(u) FROM UserDao u ");
            if (requestFilterUsers.getFkSystemId() != null) {
                query.append(" JOIN SystemUserDao us ON u.id = us.fkUserId ");
            }
            query.append(" WHERE 1=1 ");
            if (requestFilterUsers.getFkSystemId() != null) {
                query.append(" AND us.fkSystemId = ");
                query.append(requestFilterUsers.getFkSystemId());
            }
            if (requestFilterUsers.getUsername() != null) {
                query.append(" AND u.username LIKE '%");
                query.append(requestFilterUsers.getUsername());
                query.append("%' ");
            }
            if (requestFilterUsers.getIsActive() != null) {
                query.append(" AND u.isActive = ");
                query.append(requestFilterUsers.getIsActive() ? "true" : "false");
            }
            if (requestFilterUsers.getPhoneNumber() != null) {
                query.append(" AND u.phoneNumber LIKE '%");
                query.append(requestFilterUsers.getPhoneNumber());
                query.append("%' ");
            }
            if (requestFilterUsers.getFirstName() != null) {
                query.append(" AND u.firstName LIKE '%");
                query.append(requestFilterUsers.getFirstName());
                query.append("%' ");
            }
            if (requestFilterUsers.getLastName() != null) {
                query.append(" AND u.lastName LIKE '%");
                query.append(requestFilterUsers.getLastName());
                query.append("%' ");
            }
            if (requestFilterUsers.getNationalId() != null) {
                query.append(" AND u.nationalId LIKE '%");
                query.append(requestFilterUsers.getNationalId());
                query.append("%' ");
            }
            if (requestFilterUsers.getPoliceCode() != null) {
                query.append(" AND u.policeCode LIKE '%");
                query.append(requestFilterUsers.getPoliceCode());
                query.append("%' ");
            }


            List<UserDto> users = entityManager.createQuery(query.toString())
                    .getResultList();
            for (UserDto u : users) {
                u.setPassword(null);
            }
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(users);
            StandardResponse<ResponseGetUsers> response = new StandardResponse<>();


            response.setResponse(responseGetUsers);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse<ResponseGetRolesWithPrivileges> getUserRolesWithPrivileges(String token, Long fkUserId, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List<RoleWithPrivileges> rolesWithPrivileges = new LinkedList<>();
            List<RoleDao> roles = entityManager.createQuery("SELECT r FROM RoleDao r JOIN UserRoleDao ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", fkUserId)
                    .setParameter("fkSystemId", fkSystemId)
                    .getResultList();
            RoleWithPrivileges roleWithPrivileges;
            for (RoleDao role : roles) {
                roleWithPrivileges = new RoleWithPrivileges();
                roleWithPrivileges.setId(role.getId());
                roleWithPrivileges.setRole(role.getRole());
                roleWithPrivileges.setPrivileges(
                        entityManager.createQuery("SELECT p FROM PrivilegeDao p JOIN RolePrivilegeDao rp ON p.id = rp.fkPrivilegeId WHERE rp.fkRoleId = :fkRoleId ")
                                .setParameter("fkRoleId", role.getId())
                                .getResultList());
                rolesWithPrivileges.add(roleWithPrivileges);
            }
            ResponseGetRolesWithPrivileges responseGetRolesWithPrivileges = new ResponseGetRolesWithPrivileges();
            responseGetRolesWithPrivileges.setRoleWithPrivileges(rolesWithPrivileges);
            StandardResponse<ResponseGetRolesWithPrivileges> response = new StandardResponse<>();
            response.setResponse(responseGetRolesWithPrivileges);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getUserRoles(String token, Long fkUserId, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List<RoleDto> roles = entityManager.createQuery("SELECT r FROM RoleDao r JOIN UserRoleDao ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkSystemId", fkSystemId)
                    .setParameter("fkUserId", fkUserId)
                    .getResultList();
            ResponseGetRoles responseGetRoles = new ResponseGetRoles();
            responseGetRoles.setRoles(roles);
            StandardResponse response = new StandardResponse<>();


            response.setResponse(responseGetRoles);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }


    public StandardResponse getPrivileges(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user = UserDao.validate(token);
            List<PrivilegeDto> privileges = entityManager.createQuery("SELECT distinct (p) " +
                    "FROM PrivilegeDao p " +
                    "JOIN RolePrivilegeDao rp ON p.id=rp.fkPrivilegeId " +
                    "JOIN RoleDao r ON r.id = rp.fkRoleId " +
                    "JOIN UserRoleDao ur ON rp.fkRoleId=ur.fkRoleId " +
                    "WHERE ur.fkUserId = :fkUserId " +
                    "AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", user.getId())
                    .setParameter("fkSystemId", fkSystemId)
                    .getResultList();
            ResponseGetPrivileges responseGetPrivileges = new ResponseGetPrivileges();
            responseGetPrivileges.setPrivileges(privileges);
            StandardResponse response = new StandardResponse<>();


            response.setResponse(responseGetPrivileges);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse isManager(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user = UserDao.validate(token);
            Long size = (Long) entityManager.createQuery("SELECT COUNT (ur) FROM UserRoleDao ur WHERE ur.fkUserId = :fkUserId ")
                    .setParameter("fkUserId", user.getId())
                    .getSingleResult();
            StandardResponse<Boolean> response = new StandardResponse<>();
            response.setResponse(!size.equals(0L));
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
