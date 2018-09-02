package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.PrivilegeNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.impl.Database;
import com.nrdc.managementPanel.jsonModel.jsonRequest.RequestAddUser;
import org.apache.log4j.Logger;

import javax.persistence.*;

@Entity
@Table(name = "PH_USER", schema = Constants.SCHEMA)

public class User extends BaseModel {
    private static Logger logger = Logger.getLogger(User.class.getName());

    private Long id;
    private String password;
    private String username;
    private Boolean isActive;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String nationalId;
    private String policeCode;

    public User() {
    }

    public User(RequestAddUser requestAddUser) {
        this.password = requestAddUser.getPassword();
        this.username = requestAddUser.getUsername();
        this.isActive = true;
        this.phoneNumber = requestAddUser.getPhoneNumber();
        this.firstName = requestAddUser.getFirstName();
        this.lastName = requestAddUser.getLastName();
        this.nationalId = requestAddUser.getNationalId();
        this.policeCode = requestAddUser.getPoliceCode();
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
        User.verify(token, systemName);
        EntityManager entityManager = Database.getEntityManager();
        logger.info("User authentication");
        try {
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.id = (SELECT t.fkUserId FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName))")
                    .setParameter("token", token)
                    .setParameter("systemName", systemName)
                    .getSingleResult();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static User getUser(String token, SystemNames systemName) throws Exception {
        return getUser(token, systemName.name());
    }

    public static User getUser(Token token, System system) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        logger.info("User authentication");
        try {
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.id = (SELECT t.fkUserId FROM Token t WHERE t.id = :tokenId AND t.fkSystemId = :fkSystemId)")
                    .setParameter("tokenId", token.getId())
                    .setParameter("fkSystemId", system.getId())
                    .getSingleResult();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }

    }

    public static User getUser(String token, Long systemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        logger.info("User authentication");
        try {
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.id = (SELECT t.fkUserId FROM Token t WHERE t.token = :token AND t.fkSystemId = :fkSystemId)")
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
        Operation operation = new Operation(fkUserId, p.getId());
        try {
            int size = entityManager.createQuery("SELECT p FROM Privilege p JOIN RolePrivilege rp ON p.id = rp.fkPrivilegeId JOIN UserRole ur ON rp.fkRoleId = ur.fkRoleId WHERE ur.fkUserId = :fkUserId AND p.privilegeText = :privilege")
                    .setParameter("fkUserId", fkUserId)
                    .setParameter("privilege", privilege)
                    .getResultList()
                    .size();
            if (size < 1) {
                operation.setStatusCode(-1L);
                throw new Exception(Constants.PERMISSION_ERROR);
            }
            operation.setStatusCode(1L);
            return true;

        } finally {
            operation.persist();
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

    public void checkKey(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            int size = entityManager.createQuery("SELECT  k FROM Key k WHERE k.fkUserId = :fkUserId AND k.fkSystemId = (SELECT s.id FROM System s WHERE s.systemName = :systemName)")
                    .setParameter("systemName", systemName)
                    .setParameter("fkUserId", this.id)
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
                    .setParameter("fkUserId", this.id)
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID_PH_USER")
    public Long getId() {
        return id;
    }

    public void setId(Long pkUserId) {
        this.id = pkUserId;
    }

    @Basic
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "USERNAME")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "IS_ACTIVE")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Basic
    @Column(name = "PHONE_NUMBER")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "FIRST_NAME")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "LAST_NAME")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "NATIONAL_ID")
    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    @Basic
    @Column(name = "POLICE_CODE")
    public String getPoliceCode() {
        return policeCode;
    }

    public void setPoliceCode(String policeCode) {
        this.policeCode = policeCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", isActive=" + isActive +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", policeCode='" + policeCode + '\'' +
                '}';
    }

    public void checkSystemAccess(Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            boolean hasAccess = entityManager.createQuery("SELECT u FROM SystemUser u WHERE u.fkUserId = :userId  AND u.fkSystemId = :systemId")
                    .setParameter("userId", this.id)
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
        return checkPrivilege(privilege, this.id);
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



