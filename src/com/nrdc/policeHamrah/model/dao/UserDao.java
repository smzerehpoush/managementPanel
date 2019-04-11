package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.exceptions.ServerException;
import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.helper.SystemNames;
import com.nrdc.policeHamrah.impl.Database;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestAddUser;
import com.nrdc.policeHamrah.model.dto.PrivilegeDto;
import com.nrdc.policeHamrah.model.dto.UserDto;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "PH_USER", schema = Constants.SCHEMA)
public class UserDao extends com.nrdc.policeHamrah.model.dto.UserDto {
    public static final String tableName = "PH_USER";
    private static Logger logger = Logger.getLogger(UserDao.class.getName());

    public UserDao() {
    }


    public UserDao(RequestAddUser requestAddUser) throws Exception {
        this.setUsername(requestAddUser.getUsername());
        this.setPassword(requestAddUser.getPassword());
        this.setPhoneNumber(requestAddUser.getPhoneNumber());
        this.setFirstName(requestAddUser.getFirstName());
        this.setLastName(requestAddUser.getLastName());
        this.setNationalId(requestAddUser.getNationalId());
        this.setPoliceCode(requestAddUser.getPoliceCode());
        this.setIsActive(true);
    }

    //validate user and return it
    public static UserDao validate(String token) throws Exception {
        return validate(token, SystemNames.POLICE_HAMRAH);
    }

    public static UserDao validate(String token, SystemNames systemNameEnum) throws Exception {
        return validate(token, systemNameEnum.name());
    }

    public static UserDao validate(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();

        try {
            //first validating token
            AuthDao.validateToken(token, systemName);
            //check user activation
            UserDao user = (UserDao) entityManager.createQuery("SELECT u FROM UserDao u JOIN AuthDao a ON a.fkUserId = u.id JOIN SystemDao s ON s.id = a.fkSystemId WHERE a.token = :token AND s.systemName = :systemName")
                    .setParameter("token", token)
                    .setParameter("systemName", systemName)
                    .getSingleResult();
            if (!user.getIsActive())
                throw new ServerException(Constants.USER + Constants.IS_NOT_ACTIVE);
            return user;
        } catch (NoResultException ex1) {
            throw new ServerException(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (NonUniqueResultException ex2) {
            throw new ServerException(Constants.USER + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public static UserDao getUser(Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (UserDao) entityManager.createQuery("SELECT u FROM UserDao u WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", fkUserId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new ServerException(Constants.USER + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public void checkUser(boolean isEditing) throws Exception {
        checkUser(this,isEditing);
    }

    public static void checkUser(UserDto user,boolean isEditing) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            checkNationalId(user, entityManager);
            checkUsername(user, entityManager,isEditing);
            checkPoliceCode(user, entityManager);
            checkPhoneNumber(user, entityManager);
            checkPassword(user);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public static UserDao getUser(String username, String password, String phoneNumber) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            Query query = entityManager.createQuery("SELECT u FROM UserDao u WHERE (u.username = :username and u.password = :password) OR (u.phoneNumber= :phoneNumber and u.password = :password)")
                    .setParameter("username", username)
                    .setParameter("phoneNumber", phoneNumber)
                    .setParameter("password", password);
            UserDao user = (UserDao) query.getSingleResult();
            if (!user.getIsActive())
                throw new ServerException(Constants.USER + Constants.IS_NOT_ACTIVE);
            return user;
        } catch (NoResultException ex1) {
            throw new ServerException(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (NonUniqueResultException ex2) {
            throw new ServerException(Constants.USER + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

    }

    public static UserDao getUser(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();

        try {
            AuthDao.validateToken(token, systemName);
            return (UserDao) entityManager.createQuery("SELECT u FROM UserDao u JOIN AuthDao a ON a.fkUserId = u.id JOIN SystemDao s ON s.id = a.fkSystemId WHERE s.systemName = :systemName AND a.token = :token")
                    .setParameter("token", token)
                    .setParameter("systemName", systemName)
                    .getSingleResult();
        } catch (NoResultException ex1) {
            throw new ServerException(Constants.INCORRECT_USERNAME_OR_PASSWORD);
        } catch (NonUniqueResultException ex2) {
            throw new ServerException(Constants.USER + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

    }

    public static UserDao getUser(String token) throws Exception {
        return getUser(token, SystemNames.POLICE_HAMRAH);
    }

    public static UserDao getUser(String token, SystemNames systemName) throws Exception {
        return getUser(token, systemName.name());
    }

    public static UserDao getUser(String token, Long systemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        logger.info("UserDao authentication");
        try {
            return (UserDao) entityManager.createQuery("SELECT u FROM UserDao u JOIN AuthDao t ON t.fkUserId = u.id where t.token = :token AND t.fkSystemId = :fkSystemId")
                    .setParameter("token", token)
                    .setParameter("fkSystemId", systemId)
                    .getSingleResult();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }

    }

    public static AuthDao getKey(String token, String systemName) throws Exception {
        AuthDao.validateToken(token, systemName);
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (AuthDao) entityManager.createQuery("SELECT k FROM AuthDao k JOIN SystemDao  s ON s.id = k.fkSystemId WHERE k.token = :token AND s.systemName = :systemName")
                    .setParameter("systemName", systemName)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new ServerException(Constants.USER + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public static AuthDao getKey(String token, SystemNames systemName) throws Exception {
        return getKey(token, systemName.name());
    }

    public static AuthDao getKey(String token) throws Exception {
        return getKey(token, SystemNames.POLICE_HAMRAH);
    }

    public static AuthDao getKeyByUsername(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (AuthDao) entityManager.createQuery("SELECT k FROM AuthDao k JOIN UserDao u ON u.id = k.fkUserId WHERE u.username = :username")
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NonUniqueResultException | NoResultException ex) {
            throw new ServerException(Constants.USER + Constants.IS_NOT_VALID);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private static void checkUsername(UserDto user, EntityManager entityManager, boolean isEditing) throws Exception {
        //start : check username
        if (user.getUsername() == null || user.getUsername().isEmpty())
            throw new ServerException(Constants.USERNAME + Constants.IS_REQUIRED);
        if (!user.getUsername().matches("^[a-zA-Z0-9]{5,10}$"))
            throw new ServerException(Constants.NATIONAL_ID + Constants.IS_NOT_VALID);
        Long size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.username = :username")
                .setParameter("username", user.getUsername())
                .getSingleResult();

        if (!isEditing && size > 0)
            throw new ServerException(Constants.USERNAME + Constants.DUPLICATED);
    }

    private static void checkPoliceCode(UserDto user, EntityManager entityManager) throws ServerException {
        if (user.getPoliceCode() != null && !user.getPoliceCode().isEmpty()) {
            if (!user.getPoliceCode().matches("[0-9]{2,}"))
                throw new ServerException(Constants.POLICE_CODE + Constants.IS_NOT_VALID);
        }
        Long size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.policeCode= :policeCode")
                .setParameter("policeCode", user.getPoliceCode())
                .getSingleResult();
        if (size > 0)
            throw new ServerException(Constants.POLICE_CODE + Constants.DUPLICATED);
    }

    private static void checkPassword(UserDto user) throws ServerException {
        if (user.getPassword() == null || user.getPassword().isEmpty())
            throw new ServerException(Constants.PASSWORD + Constants.IS_REQUIRED);
        if (!user.getPassword().matches("^[a-zA-Z0-9]{5,10}$"))
            throw new ServerException(Constants.PASSWORD + Constants.IS_NOT_VALID);
    }

    private static void checkPhoneNumber(UserDto user, EntityManager entityManager) throws Exception {
        if (user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty()) {
            if (!user.getPhoneNumber().matches("0\\d{10}"))
                throw new ServerException(Constants.PHONE_NUMBER + Constants.IS_NOT_VALID);
            Long size = (Long) entityManager.createQuery("SELECT COUNT (u) FROM UserDao u WHERE u.phoneNumber= :phoneNumber")
                    .setParameter("phoneNumber", user.getPhoneNumber())
                    .getSingleResult();
            if (size > 0)
                throw new ServerException(Constants.PHONE_NUMBER + Constants.DUPLICATED);
        }
    }

    private static void checkNationalId(UserDto user, EntityManager entityManager) throws Exception {
        //start : check national id
        if (user.getNationalId() != null && !user.getNationalId().isEmpty()) {
            if (!user.getNationalId().matches("^[0-9]{10}$"))
                throw new ServerException(Constants.NATIONAL_ID + Constants.IS_NOT_VALID);
            Long size = (Long) entityManager.createQuery("SELECT count (u) FROM UserDao u WHERE u.nationalId = :nationalId")
                    .setParameter("nationalId", user.getNationalId())
                    .getSingleResult();
            if (size > 0)
                throw new ServerException(Constants.NATIONAL_ID + Constants.DUPLICATED);
        }
        //end : check national id
    }

    public List<SystemDao> systems() {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            List<SystemDao> systemDaoList = entityManager.createQuery("SELECT s FROM SystemDao s JOIN SystemUserDao su ON s.id = su.fkSystemId WHERE su.fkUserId = :fkUserId")
                    .setParameter("fkUserId", this.getId())
                    .getResultList();
            return systemDaoList;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public boolean checkPrivilege(PrivilegeDto privilege, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            int size = entityManager.createQuery(
                    "SELECT p FROM PrivilegeDao p " +
                            "JOIN RolePrivilegeDao rp ON p.id = rp.fkPrivilegeId " +
                            "JOIN UserRoleDao ur ON rp.fkRoleId = ur.fkRoleId " +
                            "JOIN RoleDao r ON r.id = ur.fkRoleId " +
                            "WHERE ur.fkUserId = :fkUserId AND p.id = :privilegeId AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", this.getId())
                    .setParameter("privilegeId", privilege.getId())
                    .setParameter("fkSystemId", fkSystemId)
                    .getResultList()
                    .size();
            if (size < 1) {
                throw new ServerException(Constants.PRIVILEGE + Constants.IS_NOT_VALID);
            }
            return true;

        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public boolean checkPrivilege(String privilegeName, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            int size = entityManager.createQuery("" +
                    "SELECT p FROM PrivilegeDao p " +
                    "JOIN RolePrivilegeDao rp ON p.id = rp.fkPrivilegeId " +
                    "JOIN UserRoleDao ur ON rp.fkRoleId = ur.fkRoleId " +
                    "JOIN RoleDao r ON ur.fkRoleId = r.id " +
                    "WHERE ur.fkUserId = :fkUserId AND p.privilegeText = :privilegeName AND r.fkSystemId = :fkSystemId")
                    .setParameter("fkUserId", this.getId())
                    .setParameter("privilegeName", privilegeName)
                    .setParameter("fkSystemId", fkSystemId)
                    .getResultList()
                    .size();
            if (size < 1) {
                throw new ServerException(Constants.PRIVILEGE + Constants.IS_NOT_VALID);
            }
            return true;

        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public boolean checkPrivilege(PrivilegeNames privilegeName, Long fkSystemId) throws Exception {
        return checkPrivilege(privilegeName.name(), fkSystemId);
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

    public void checkKey(SystemNames systemNames) throws Exception {
        checkKey(systemNames.name());
    }

    public void checkKey(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            Long size = (Long) entityManager.createQuery("SELECT count (a)  FROM AuthDao a JOIN SystemDao s ON a.fkSystemId = s.id WHERE s.systemName = :systemName AND a.fkUserId = :fkUserId")
                    .setParameter("systemName", systemName)
                    .setParameter("fkUserId", super.getId())
                    .getSingleResult();
            if (!size.equals(0L))
                throw new ServerException(Constants.USER + Constants.IS_ACTIVE);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public void checkKey(SystemDao system) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            Long size = (Long) entityManager.createQuery("SELECT count (a) FROM AuthDao a WHERE a.fkSystemId = :fkSystemId AND a.fkUserId = :fkUserId")
                    .setParameter("fkSystemId", system.getId())
                    .setParameter("fkUserId", super.getId())
                    .getSingleResult();

            if (!size.equals(0L))
                throw new ServerException(Constants.USER + Constants.IS_ACTIVE);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }


    public void checkToken(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {

            Long size = (Long) entityManager.createQuery("SELECT  count (t) FROM AuthDao t JOIN SystemDao s ON t.fkSystemId = s.id WHERE t.fkUserId = :fkUserId AND s.systemName = :systemName")
                    .setParameter("systemName", systemName)
                    .setParameter("fkUserId", super.getId())
                    .getSingleResult();
            if (!size.equals(0L))
                throw new ServerException(Constants.USER + Constants.IS_ACTIVE);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public void checkToken(SystemDao systemDao) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {

            Long size = (Long) entityManager.createQuery("SELECT  count (t) FROM AuthDao t WHERE t.fkUserId = :fkUserId AND t.fkSystemId = :fkSystemId ")
                    .setParameter("fkSystemId", systemDao.getId())
                    .setParameter("fkUserId", super.getId())
                    .getSingleResult();
            if (!size.equals(0L))
                throw new ServerException(Constants.USER + Constants.IS_ACTIVE + Constants.TOKEN + Constants.EXISTS);
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public void checkToken(SystemNames system) throws Exception {
        checkToken(system.name());
    }

    public void checkSystemAccess(SystemDao systemDao) throws Exception {
        checkSystemAccess(systemDao.getId());

    }

    public void checkSystemAccess(Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            boolean hasAccess = entityManager.createQuery("SELECT u FROM SystemUserDao u WHERE u.fkUserId = :userId  AND u.fkSystemId = :systemId")
                    .setParameter("userId", super.getId())
                    .setParameter("systemId", fkSystemId)
                    .getResultList()
                    .size() == 1;
            if (!hasAccess) {
                throw new ServerException(Constants.PRIVILEGE + Constants.IS_NOT_VALID);
            }
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public UserDto createCustomUser() throws CloneNotSupportedException {
        UserDto user = (UserDto) this.clone();
        user.setPassword("");
        return user;
    }

}



