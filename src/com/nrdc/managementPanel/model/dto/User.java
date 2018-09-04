package com.nrdc.managementPanel.model.dto;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestAddUser;
import com.nrdc.managementPanel.model.dao.UserDAO;
import org.apache.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "PH_USER", schema = Constants.SCHEMA)
public class User extends UserDAO {
    private static Logger logger = Logger.getLogger(User.class.getName());

    public User() {
    }

    public User(RequestAddUser requestAddUser) {
        this.setPassword(requestAddUser.getPassword());
        this.setUsername(requestAddUser.getUsername());
        this.setIsActive(true);
        this.setPhoneNumber(requestAddUser.getPhoneNumber());
        this.setFirstName(requestAddUser.getFirstName());
        this.setLastName(requestAddUser.getLastName());
        this.setNationalId(requestAddUser.getNationalId());
        this.setPoliceCode(requestAddUser.getPoliceCode());
    }

    public static User validate(String token, SystemNames systemName) throws Exception {
        return validate(token, systemName.name());
    }

    public static User validate(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            Token.validateToken(token, systemName);
            checkActivation(token, systemName);
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.id = (SELECT t.fkUserId FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName))")
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

    public static User getUser(Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.id = :fkUserId")
                    .setParameter("fkUserId", fkUserId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new Exception(Constants.NOT_VALID_USER);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    public static User getUser(String username, String password, String phoneNumber) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            Query query = entityManager.createQuery("SELECT u FROM User u WHERE (u.username = :username and u.password = :password) OR (u.phoneNumber= :phoneNumber and u.password = :password)")
                    .setParameter("username", username)
                    .setParameter("phoneNumber", phoneNumber)
                    .setParameter("password", password);
            User user = (User) query.getSingleResult();
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

    public static User getUser(String token, String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            Token.validateToken(token, systemName);
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.id = (SELECT t.fkUserId FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName))")
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

    public static User getUser(String token, SystemNames systemName) throws Exception {
        return getUser(token, systemName.name());
    }

    public static User getUser(Token token) throws Exception {
        return getUser(token.getToken(), token.getFkSystemId());
    }

    public static User getUser(String token, Long systemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        logger.info("User authentication");
        try {
            return (User) entityManager.createQuery("SELECT u FROM User u JOIN Token t ON t.fkUserId = u.id where t.token = :token AND t.fkSystemId = :fkSystemId")
                    .setParameter("token", token)
                    .setParameter("fkSystemId", systemId)
                    .getSingleResult();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static void verify(String token, System system) throws Exception {
        EntityManager entityManager = Database.getEntityManager();

        try {
            int size = entityManager.createQuery("SELECT t FROM Token t WHERE t.token = :token AND t.fkSystemId = :systemId")
                    .setParameter("token", token)
                    .setParameter("systemId", system.getId())
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
            int size = entityManager.createQuery("SELECT t FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName )")
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
        Privilege p = Privilege.getPrivilege(privilege);
        try {
            int size = entityManager.createQuery("SELECT p FROM Privilege p JOIN RolePrivilege rp ON p.id = rp.fkPrivilegeId JOIN UserRole ur ON rp.fkRoleId = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND p.privilegeText = :privilege")
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

    public static Key getKey(String token, String systemName) throws Exception {
        Token.validateToken(token, systemName);
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (Key) entityManager.createQuery("SELECT k FROM Key k JOIN Token t ON k.fkUserId = t.fkUserId WHERE t.token = :token AND k.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName )")
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

    public static Key getKey(String token, SystemNames systemName) throws Exception {
        return getKey(token, systemName.name());
    }

    public static Key getKey(String token) throws Exception {
        return getKey(token, SystemNames.MANAGEMENT_PANEL);
    }

    public static Key getKeyByUsername(String username) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            return (Key) entityManager.createQuery("SELECT k FROM Key k JOIN User u ON u.id = k.fkUserId WHERE u.username = :username")
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
            Long size = (Long) entityManager.createQuery("SELECT count (t) FROM Token t JOIN System s ON s.id = t.fkSystemId JOIN User  u ON t.fkUserId = u.id WHERE t.token = :token AND s.systemName = :systemName AND u.isActive = true")
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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_USER")
    public Long getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    @Basic
    @Column(name = "USERNAME")
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    @Basic
    @Column(name = "IS_ACTIVE")
    public Boolean getIsActive() {
        return super.getIsActive();
    }

    @Override
    @Basic
    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return super.getPhoneNumber();
    }

    @Override
    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    @Basic
    @Column(name = "NATIONAL_ID")
    public String getNationalId() {
        return super.getNationalId();
    }

    @Override
    @Basic
    @Column(name = "POLICE_CODE")
    public String getPoliceCode() {
        return super.getPoliceCode();
    }

    public void checkKey(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            int size = entityManager.createQuery("SELECT  k FROM Key k WHERE k.fkUserId = :fkUserId AND k.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName)")
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

    public void checkKey(System system) throws Exception {
        checkKey(system.getSystemName());
    }

    public void checkKey(SystemNames systemNames) throws Exception {
        checkKey(systemNames.name());
    }

    public void checkToken(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {

            int size = entityManager.createQuery("SELECT  t FROM Token t WHERE t.fkUserId = :fkUserId AND t.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName)")
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

    public void checkToken(System system) throws Exception {
        checkToken(system.getSystemName());
    }

    public void checkToken(SystemNames system) throws Exception {
        checkToken(system.name());
    }

    public void checkSystemAccess(Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            boolean hasAccess = entityManager.createQuery("SELECT u FROM SystemUser u WHERE u.fkUserId = :userId  AND u.fkSystemId = :systemId")
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

    public boolean checkPrivilege(String privilege, User user) throws Exception {
        return checkPrivilege(privilege, user.getId());
    }

    public boolean checkPrivilege(String privilege) throws Exception {
        return checkPrivilege(privilege, super.getId());
    }


    public void checkPrivilege(PrivilegeNames privilegeName) throws Exception {
        checkPrivilege(privilegeName.name());
    }

    public User createCustomUser() throws CloneNotSupportedException {
        User user = (User) this.clone();
        user.setPassword("");
        return user;
    }

}



