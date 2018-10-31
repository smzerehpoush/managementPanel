package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.customizedModel.RoleWithPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAddUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestEditUser;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestFilterUsers;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestResetPassword;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetRoles;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetRolesWithPrivileges;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.policeHamrah.model.dao.PrivilegeDao;
import com.nrdc.policeHamrah.model.dao.RoleDao;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import com.nrdc.policeHamrah.model.dao.UserDao;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;
import com.nrdc.policeHamrah.model.dto.RoleDto;
import com.nrdc.policeHamrah.model.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.LinkedList;
import java.util.List;

public class UserImpl {
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
        try {
            UserDao user1 = UserDao.validate(token);
            user1.checkPrivilege(PrivilegeNames.ACTIVE_USER, fkSystemId);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            UserDao user2 = UserDao.getUser(fkUserId);
            List<SystemDao> user2SystemList = getUserSystems(user2);
            if (!user2SystemList.contains(systemDao)) {
                throw new Exception(Constants.USER_SYSTEM_ERROR);
            }
            if (transaction != null && !transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE UserDao u SET u.isActive = true WHERE u.id = :fkUserId")
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
        try {
            UserDao user1 = UserDao.validate(token);
            user1.checkPrivilege(PrivilegeNames.DE_ACTIVE_USER, fkSystemId);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            UserDao user2 = UserDao.getUser(fkUserId);
            List<SystemDao> user2SystemList = getUserSystems(user2);
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


    public StandardResponse addUser(String token, RequestAddUser requestAddUser) {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
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
        try {
            //admin user
            UserDao adminUser = UserDao.validate(token);

            SystemDao systemDao = SystemDao.getSystem(requestEditUser.getFkSystemId());
            adminUser.checkPrivilege(PrivilegeNames.EDIT_USER, requestEditUser.getFkSystemId());
            UserDao user = UserDao.getUser(requestEditUser.getFkUserId());
            List<SystemDao> userSystemList = getUserSystems(user);
            if (!userSystemList.contains(systemDao)) {
                throw new Exception(Constants.USER_SYSTEM_ERROR);
            }
            try {
                boolean b = user.checkPrivilege(PrivilegeNames.EDIT_USER, requestEditUser.getFkSystemId());
                if (b)
                    throw new Exception(Constants.CANT_NOT_EDIT_THIS_USER);
            } catch (Exception ex) {
                if (ex.getMessage().equals(Constants.CANT_NOT_EDIT_THIS_USER))
                    throw ex;
            }

            if (!transaction.isActive())
                transaction.begin();
            entityManager.createQuery("UPDATE UserDao u SET u.username = :username , u.phoneNumber= :phoneNumber , u.phoneNumber = :phoneNumber , u.firstName = :firstName , u.lastName = :lastName , u.nationalId = :nationalId , u.policeCode = :policeCode WHERE u.id = :id")
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
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.FILTER_USERS, requestFilterUsers.getFkSystemId());
            StringBuilder query = new StringBuilder();
            query.append("SELECT u FROM UserDao u ");
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

    public StandardResponse<ResponseGetRolesWithPrivileges> getUserRolesWithPrivileges(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            UserDao user = UserDao.validate(token);
            List<RoleWithPrivileges> rolesWithPrivileges = new LinkedList<>();
            List<RoleDao> roles = entityManager.createQuery("SELECT r FROM RoleDao r JOIN UserRoleDao ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", user.getId())
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

    public StandardResponse getUserRoles(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            UserDao user = UserDao.validate(token);
            user.checkPrivilege(PrivilegeNames.GET_ROLES, fkSystemId);
            List<RoleDto> roles = entityManager.createQuery("SELECT r FROM RoleDao r JOIN UserRoleDao ur ON r.id = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkSystemId", fkSystemId)
                    .setParameter("fkUserId", user.getId())
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
