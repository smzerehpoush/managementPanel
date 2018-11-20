package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
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
                        throw new Exception(Constants.CAN_NOT_ASSIGN_THIS_ROLE);
                }
                if (!role.getFkSystemId().equals(fkSystemId))
                    throw new Exception(Constants.NOT_VALID_ROLE_SYSTEM);
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
                        throw new Exception(Constants.CAN_NOT_ASSIGN_THIS_ROLE);
                }
                if (!role.getFkSystemId().equals(fkSystemId))
                    throw new Exception(Constants.NOT_VALID_ROLE_SYSTEM);
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
            RoleDao role = (RoleDao) entityManager.createQuery("SELECT r FROM RoleDao r WHERE r.id = :roleId ")
                    .setParameter("roleId", roleId)
                    .getSingleResult();
            return role;
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
            boolean isUserSysAdmin = isUserSysAdmin(user.getId(), requestAssignRole.getFkSystemId());
            List roleIdListInDb = entityManager.createQuery("SELECT r.id FROM RoleDao r JOIN UserRoleDao ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", user.getId())
                    .setParameter("fkSystemId", requestAssignRole.getFkSystemId())
                    .getResultList();

            mergeAndRemove(roleIdListInDb, requestAssignRole.getFkRoleIdList(), isUserSysAdmin, user.getId(), requestAssignRole.getFkSystemId());
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
                throw new Exception(Constants.NOT_VALID_ROLE);

        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse resetPassword(String token, RequestResetPassword requestResetPassword) throws Exception {
//        OperationDao operation = new OperationDao();
        PrivilegeDao privilege = PrivilegeDao.getPrivilege(PrivilegeNames.RESET_PASSWORD);
//        operation.setFkPrivilegeId(privilege.getId());
        UserDao user = UserDao.validate(token);
//        operation.setUserToken(token);
//        operation.setFkUserId(user.getId());
//        operation.setTime(new Date());
        try {
            if (!checkUserOldPassword(user.getUsername(), requestResetPassword))
                throw new Exception(Constants.INCORRECT_USERNAME_OR_PASSWORD);
            checkPassword(requestResetPassword);
            setUserNewPassword(user.getUsername(), requestResetPassword);
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

    private boolean checkUserOldPassword(String username, RequestResetPassword requestResetPassword) {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            Long size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.username = :username AND u.password = :password ")
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
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE UserDao u SET u.password = :newPassword WHERE u.username = :username")
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


    public StandardResponse activeUser(String token, Long fkUserId, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user1 = UserDao.validate(token);
            if (user1.getId().equals(fkUserId))
                throw new Exception(Constants.CAN_NOT_DE_ACTIVE_THIS_USER);
            user1.checkPrivilege(PrivilegeNames.ACTIVE_USER, fkSystemId);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            UserDao user2 = UserDao.getUser(fkUserId);
            List<SystemDao> user2SystemList = user2.systems();
            if (!user2SystemList.contains(systemDao)) {
                throw new Exception(Constants.USER_SYSTEM_ERROR);
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
                throw new Exception(Constants.CAN_NOT_DE_ACTIVE_YOURSELF);
            List<SystemDao> user2SystemList = user2.systems();
            if (!user2SystemList.contains(systemDao)) {
                throw new Exception(Constants.USER_SYSTEM_ERROR);
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


    public StandardResponse addUser(String token, RequestAddUser requestAddUser) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.ADD_USER, requestAddUser.getFkSystemId());
            if (!transaction.isActive())
                transaction.begin();
            checkRequestAddUser(requestAddUser);
            UserDao u = new UserDao(requestAddUser);
            entityManager.persist(u);
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();


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

    private void checkRequestAddUser(RequestAddUser requestAddUser) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        Long size;
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            //check username
            if (requestAddUser.getUsername() == null || requestAddUser.getUsername().equals(""))
                throw new Exception("نام کاربری اجباری می باشد;");
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.username = :username")
                    .setParameter("username", requestAddUser.getUsername())
                    .getSingleResult();
            if (size > 0)
                throw new Exception("نام کاربری تکراری می باشد.");
            //check nationalId
            if (requestAddUser.getUsername() == null || requestAddUser.getUsername().equals(""))
                throw new Exception("کد ملی اجباری شده");
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.nationalId = :nationalId")
                    .setParameter("nationalId", requestAddUser.getNationalId())
                    .getSingleResult();
            if (size > 0)
                throw new Exception("کد ملی تکراری می باشد.");
            //check policeCode
            if (requestAddUser.getPoliceCode() != null && !requestAddUser.getPoliceCode().equals(""))
                size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.policeCode= :policeCode")
                        .setParameter("policeCode", requestAddUser.getPoliceCode())
                        .getSingleResult();
            if (size > 0)
                throw new Exception("کد پلیس تکراری می باشد.");
            //check phoneNumber
            if (requestAddUser.getPhoneNumber() == null || requestAddUser.getPhoneNumber().equals(""))
                throw new Exception("شماره تلفن اجباری شده");
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.phoneNumber= :phoneNumber")
                    .setParameter("phoneNumber", requestAddUser.getPhoneNumber())
                    .getSingleResult();
            if (size > 0)
                throw new Exception("شماره تلفن تکراری می باشد.");
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse editUser(String token, RequestEditUser requestEditUser) {
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
                throw new Exception(Constants.USER_SYSTEM_ERROR);
            }
            try {
                boolean b = user.checkPrivilege(PrivilegeNames.EDIT_USER, requestEditUser.getFkSystemId());
                if (b)
                    throw new Exception(Constants.CAN_NOT_EDIT_THIS_USER);
            } catch (Exception ex) {
                if (ex.getMessage().equals(Constants.CAN_NOT_EDIT_THIS_USER))
                    throw ex;
            }
            checkRequestEditUser(requestEditUser);
            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE UserDao u SET u.username = :username , u.phoneNumber= :phoneNumber , u.phoneNumber = :phoneNumber , u.firstName = :firstName , u.lastName = :lastName , u.nationalId = :nationalId , u.policeCode = :policeCode WHERE u.id = :fkUserId")
                    .setParameter("username", requestEditUser.getUsername())
                    .setParameter("phoneNumber", requestEditUser.getPhoneNumber())
                    .setParameter("firstName", requestEditUser.getFirstName())
                    .setParameter("lastName", requestEditUser.getLastName())
                    .setParameter("nationalId", requestEditUser.getNationalId())
                    .setParameter("policeCode", requestEditUser.getPoliceCode())
                    .setParameter("fkUserId", requestEditUser.getFkUserId())
                    .executeUpdate();
            if (transaction.isActive())
                transaction.commit();
            StandardResponse response = new StandardResponse<>();


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

    private void checkUsername(String username, Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        Long size;
        try {
            //check username
            if (username == null || username.equals(""))
                throw new Exception("نام کاربری اجباری می باشد;");
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
            if (size.equals(1L)) {
                Long id = (Long) entityManager.createQuery("SELECT (u.id) FROM UserDao u WHERE u.username = :username")
                        .setParameter("username", username)
                        .getSingleResult();
                if (!id.equals(fkUserId))
                    throw new Exception("نام کاربری تکراری می باشد.");

            }
            if (size > 1)
                throw new Exception("نام کاربری تکراری می باشد.");
        } finally {
            if (entityManager != null && entityManager.isOpen())
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
                throw new Exception("کد ملی اجباری می باشد;");
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.nationalId = :username")
                    .setParameter("username", nationalId)
                    .getSingleResult();
            if (size.equals(1L)) {
                Long id = (Long) entityManager.createQuery("SELECT (u.id) FROM UserDao u WHERE u.nationalId = :nationalId")
                        .setParameter("nationalId", nationalId)
                        .getSingleResult();
                if (!id.equals(fkUserId))
                    throw new Exception("کد ملی تکراری می باشد.");

            }
            if (size > 1)
                throw new Exception("کد ملی تکراری می باشد.");
        } finally {
            if (entityManager != null && entityManager.isOpen())
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
                throw new Exception("شماره تلفن اجباری می باشد;");
            size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.phoneNumber = :phoneNumber")
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            if (size.equals(1L)) {
                Long id = (Long) entityManager.createQuery("SELECT (u.id) FROM UserDao u WHERE u.phoneNumber= :phoneNumber")
                        .setParameter("phoneNumber", phoneNumber)
                        .getSingleResult();
                if (!id.equals(fkUserId))
                    throw new Exception("شماره تلفن تکراری می باشد.");

            }
            if (size > 1)
                throw new Exception("شماره تلفن تکراری می باشد.");
        } finally {
            if (entityManager != null && entityManager.isOpen())
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
                    throw new Exception("کد پلیس  تکراری می باشد.");

            }
            if (size > 1)
                throw new Exception("کد پلیس تکراری می باشد.");
        } finally {
            if (entityManager != null && entityManager.isOpen())
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
//            user.checkPrivilege(PrivilegeNames.FILTER_USERS, requestFilterUsers.getFkSystemId());
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
                query.append(" AND u.username LIKE '");
                query.append(requestFilterUsers.getUsername());
                query.append("%' ");
            }
            if (requestFilterUsers.getIsActive() != null) {
                query.append(" AND u.isActive = ");
                query.append(requestFilterUsers.getIsActive() ? "true" : "false");
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
}
