package com.nrdc.managementPanel.model;

import com.nrdc.managementPanel.helper.Constants;
import com.nrdc.managementPanel.helper.RoleNames;
import com.nrdc.managementPanel.helper.SystemNames;
import com.nrdc.managementPanel.impl.Database;
import org.apache.log4j.Logger;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "PH_USER", schema = Constants.SCHEMA)

public class User implements Serializable {
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
            throw new Exception(Constants.INCORRECT_USER_OR_PASSWORD);
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
            return (User) entityManager.createQuery("SELECT u FROM User u WHERE u.id = (SELECT t.fkUserId FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM Systems s WHERE s.systemName = :systemName))")
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

    public static User getUser(Token token, Systems system) throws Exception {
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

    public static void verify(String token, Systems system) throws Exception {
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
            int size = entityManager.createQuery("SELECT t FROM Token t WHERE t.token = :token AND t.fkSystemId = (SELECT s.id FROM Systems s WHERE s.systemName = :systemName )")
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

    public void checkKey(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            int size = entityManager.createQuery("SELECT  k FROM Key k WHERE k.fkUserId = :fkUserId AND k.fkSystemId = (SELECT s.id FROM Systems s WHERE s.systemName = :systemName)")
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

    public void checkKey(Systems system) throws Exception {
        checkKey(system.getSystemName());
    }

    public void checkKey(SystemNames systemNames) throws Exception {
        checkKey(systemNames.name());
    }

    public void checkToken(String systemName) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {

            int size = entityManager.createQuery("SELECT  t FROM Token t WHERE t.fkUserId = :fkUserId AND t.fkSystemId = (SELECT s.id FROM Systems s WHERE s.systemName = :systemName)")
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

    public void checkToken(Systems system) throws Exception {
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
            boolean hasAccess = entityManager.createQuery("SELECT u FROM UserSystem u WHERE u.fkUserId = :userId  AND u.fkSystemId = :systemId")
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
    public void checkPrivilege(String privilege){
        EntityManager entityManager = Database.getEntityManager();
        try {
            int size  = entityManager.createQuery("SELECT ur FROM UserRole ur JOIN RolePrivilege rp ON ur.fkRoleId = rp.fkRoleId JOIN Privilege p ON rp.fkPrivilegeId = p.id WHERE p.privilege = :privilege AND ur.fkUserId = :fkUserId")
                    .setParameter("fkUserId", this.id)
                    .setParameter("privilege", privilege)
                    .getResultList()
                    .size() ;
            if (size < 1) {
                throw new Exception(Constants.PERMISSION_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }
    public void checkPrivilege(RoleNames roleName){
        checkPrivilege(roleName.name());
    }
}



