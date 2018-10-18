package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.impl.Database;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAddUser;
import com.nrdc.policeHamrah.model.dto.UserDto;
import org.apache.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "PH_USER", schema = Constants.SCHEMA)
public class UserDao extends com.nrdc.policeHamrah.model.dto.UserDto {
    public static final String tableName = "PH_USER";
    private static Logger logger = Logger.getLogger(UserDao.class.getName());


    public UserDao() {
    }

    public UserDao(RequestAddUser requestAddUser) {
        this.setPassword(requestAddUser.getPassword());
        this.setUsername(requestAddUser.getUsername());
        this.setIsActive(true);
        this.setPhoneNumber(requestAddUser.getPhoneNumber());
        this.setFirstName(requestAddUser.getFirstName());
        this.setLastName(requestAddUser.getLastName());
        this.setNationalId(requestAddUser.getNationalId());
        this.setPoliceCode(requestAddUser.getPoliceCode());
    }

    public static UserDao validate(String token) throws Exception {
        return validate(token, SystemNames.POLICE_HAMRAH);
    }

    public static UserDao validate(String token, SystemNames systemName) throws Exception {
        return validate(token, systemName.name());
    }

    public static UserDao validate(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            TokenDao.validateToken(token, systemName);
            checkActivation(token, systemName);
            return (UserDao) entityManager.createQuery("SELECT u FROM UserDao u WHERE u.id = (SELECT t.fkUserId FROM TokenDao t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM SystemDao s WHERE s.systemName = :systemName))")
                    .setParameter("token", token)
                    .setParameter("systemName", systemName)
                    .getSingleResult();
        } catch (NoResultException ex1) {
            throw new Exception(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (NonUniqueResultException ex2) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static UserDao getUser(Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (UserDao) entityManager.createQuery("SELECT u FROM UserDao u WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", fkUserId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static UserDao getUser(String username, String password, String phoneNumber) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT u FROM UserDao u WHERE (u.username = :username and u.password = :password) OR (u.phoneNumber= :phoneNumber and u.password = :password)")
                    .setParameter("username", username)
                    .setParameter("phoneNumber", phoneNumber)
                    .setParameter("password", password);
            UserDao user = (UserDao) query.getSingleResult();
            if (!user.getIsActive()) {
                throw new Exception(Constants.NOT_ACTIVE_USER);
            }
            return user;
        } catch (NoResultException ex1) {
            throw new Exception(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (NonUniqueResultException ex2) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

    }

    public static UserDao getUser(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            TokenDao.validateToken(token, systemName);
            return (UserDao) entityManager.createQuery("SELECT u FROM UserDao u WHERE u.id = (SELECT t.fkUserId FROM TokenDao t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM SystemDao s WHERE s.systemName = :systemName))")
                    .setParameter("token", token)
                    .setParameter("systemName", systemName)
                    .getSingleResult();
        } catch (NoResultException ex1) {
            throw new Exception(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (NonUniqueResultException ex2) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static UserDao getUser(String token) throws Exception {
        return getUser(token, SystemNames.POLICE_HAMRAH);
    }

    public static UserDao getUser(String token, SystemNames systemName) throws Exception {
        return getUser(token, systemName.name());
    }

    public static UserDao getUser(TokenDao token) throws Exception {
        return getUser(token.getToken(), token.getFkSystemId());
    }

    public static UserDao getUser(String token, Long systemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        logger.info("UserDao authentication");
        try {
            return (UserDao) entityManager.createQuery("SELECT u FROM UserDao u JOIN TokenDao t ON t.fkUserId = u.id where t.token = :token AND t.fkSystemId = :fkSystemId")
                    .setParameter("token", token)
                    .setParameter("fkSystemId", systemId)
                    .getSingleResult();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static void verify(String token, SystemDao systemDao) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            int size = entityManager.createQuery("SELECT t FROM TokenDao t WHERE t.token = :token AND t.fkSystemId = :systemId")
                    .setParameter("token", token)
                    .setParameter("systemId", systemDao.getId())
                    .getResultList()
                    .size();
            if (size != 1) {
                logger.info("token is not valid");
                throw new Exception(Constants.NOT_VALID_TOKEN);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static void verify(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            int size = entityManager.createQuery("SELECT t FROM TokenDao t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM SystemDao s WHERE s.systemName = :systemName )")
                    .setParameter("token", token)
                    .setParameter("systemName", systemName)
                    .getResultList()
                    .size();
            if (size != 1) {
                logger.info("token is not valid");
                throw new Exception(Constants.NOT_VALID_TOKEN);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static boolean checkPrivilege(String privilege, Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        PrivilegeDao p = PrivilegeDao.getPrivilege(privilege);
        try {
            int size = entityManager.createQuery("SELECT p FROM PrivilegeDao p JOIN RolePrivilegeDao rp ON p.id = rp.fkPrivilegeId JOIN UserRoleDao ur ON rp.fkRoleId = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND p.privilegeText = :privilege")
                    .setParameter("fkUserId", fkUserId)
                    .setParameter("privilege", privilege)
                    .getResultList()
                    .size();
            if (size < 1) {
                throw new Exception(Constants.PERMISSION_ERROR);
            }
            return true;

        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static KeyDao getKey(String token, String systemName) throws Exception {
        TokenDao.validateToken(token, systemName);
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (KeyDao) entityManager.createQuery("SELECT k FROM KeyDao k JOIN TokenDao t ON k.fkUserId = t.fkUserId WHERE t.token = :token AND k.fkSystemId = (SELECT s.id FROM SystemDao s WHERE s.systemName = :systemName )")
                    .setParameter("systemName", systemName)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static KeyDao getKey(String token, SystemNames systemName) throws Exception {
        return getKey(token, systemName.name());
    }

    public static KeyDao getKey(String token) throws Exception {
        return getKey(token);
    }

    public static KeyDao getKeyByUsername(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (KeyDao) entityManager.createQuery("SELECT k FROM KeyDao k JOIN UserDao u ON u.id = k.fkUserId WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static void checkActivation(String token, SystemNames systemName) throws Exception {
        checkActivation(token, systemName.name());
    }

    public static void checkActivation(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            Long size = (Long) entityManager.createQuery("SELECT count (t) FROM TokenDao t JOIN SystemDao s ON s.id = t.fkSystemId JOIN UserDao  u ON t.fkUserId = u.id WHERE t.token = :token AND s.systemName = :systemName AND u.isActive = true")
                    .setParameter("systemName", systemName)
                    .setParameter("token", token)
                    .getSingleResult();
            if (!size.equals(1L)) {
                throw new Exception(Constants.NOT_ACTIVE_USER);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }


    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_USER", table = tableName)
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "PASSWORD", table = tableName)
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    @Basic
    @Column(name = "USERNAME", table = tableName)
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @Basic
    @Column(name = "IS_ACTIVE", table = tableName)
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Override
    @Basic
    @Column(name = "PHONE_NUMBER", table = tableName)
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    @Basic
    @Column(name = "FIRST_NAME", table = tableName)
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    @Basic
    @Column(name = "LAST_NAME", table = tableName)
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    @Basic
    @Column(name = "NATIONAL_ID", table = tableName)
    public String getNationalId() {
        return super.getNationalId();
    }

    @Override
    @Basic
    @Column(name = "POLICE_CODE", table = tableName)
    public String getPoliceCode() {
        return super.getPoliceCode();
    }

    public void checkKey(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            int size = entityManager.createQuery("SELECT  k FROM KeyDao k WHERE k.fkUserId = :fkUserId AND k.fkSystemId = (SELECT s.id FROM SystemDao s WHERE s.systemName = :systemName)")
                    .setParameter("systemName", systemName)
                    .setParameter("fkUserId", super.getId())
                    .getResultList()
                    .size();
            if (size != 0)
                throw new Exception(Constants.ACTIVE_USER_EXISTS + "کلید تکراری");
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public void checkKey(SystemDao systemDao) throws Exception {
        checkKey(systemDao.getSystemName());
    }

    public void checkKey(SystemNames systemNames) throws Exception {
        checkKey(systemNames.name());
    }

    public void checkToken(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {

            int size = entityManager.createQuery("SELECT  t FROM TokenDao t WHERE t.fkUserId = :fkUserId AND t.fkSystemId = (SELECT s.id FROM SystemDao s WHERE s.systemName = :systemName)")
                    .setParameter("systemName", systemName)
                    .setParameter("fkUserId", super.getId())
                    .getResultList()
                    .size();
            if (size != 0)
                throw new Exception(Constants.ACTIVE_USER_EXISTS + "توکن تکراری");
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public void checkToken(SystemDao systemDao) throws Exception {
        checkToken(systemDao.getSystemName());
    }

    public void checkToken(SystemNames system) throws Exception {
        checkToken(system.name());
    }

    public void checkSystemAccess(SystemDao systemDao) throws Exception {
        checkSystemAccess(systemDao.getId());

    }

    public void checkSystemAccess(Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            boolean hasAccess = entityManager.createQuery("SELECT u FROM SystemUserDao u WHERE u.fkUserId = :userId  AND u.fkSystemId = :systemId")
                    .setParameter("userId", super.getId())
                    .setParameter("systemId", fkSystemId)
                    .getResultList()
                    .size() == 1;
            if (!hasAccess) {
                throw new Exception(Constants.PERMISSION_ERROR);
            }
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public boolean checkPrivilege(String privilege, UserDao user) throws Exception {
        return checkPrivilege(privilege, user.getId());
    }

    public boolean checkPrivilege(String privilege) throws Exception {
        return checkPrivilege(privilege, super.getId());
    }


    public void checkPrivilege(PrivilegeNames privilegeName) throws Exception {
        checkPrivilege(privilegeName.name());
    }

    public UserDto createCustomUser() throws CloneNotSupportedException {
        UserDto user = (UserDto) this.clone();
        user.setPassword("");
        return user;
    }

}



