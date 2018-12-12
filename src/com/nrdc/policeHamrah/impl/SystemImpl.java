package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.customizedModel.RoleWithPrivileges;
import com.nrdc.policeHamrah.jsonModel.customizedModel.SystemReportDto;
import com.nrdc.policeHamrah.jsonModel.customizedModel.SystemWithVersion;
import com.nrdc.policeHamrah.jsonModel.jsonRequest.RequestReportSystem;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.*;
import com.nrdc.policeHamrah.model.dao.*;
import com.nrdc.policeHamrah.model.dto.RoleDto;
import com.nrdc.policeHamrah.model.dto.SystemDto;
import com.nrdc.policeHamrah.model.dto.SystemVersionDto;
import com.nrdc.policeHamrah.model.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.LinkedList;
import java.util.List;

public class SystemImpl {
    public StandardResponse getSystemReports(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List<SystemReportDao> systemReportDaoList = entityManager.createQuery("SELECT s FROM SystemReportDao s WHERE s.fkSystemId = :fkSystemId ORDER BY s.time ")
                    .setParameter("fkSystemId", fkSystemId)
                    .getResultList();
            List<com.nrdc.policeHamrah.jsonModel.customizedModel.SystemReportDto> systemReportDtoList = new LinkedList<>();
            SystemReportDto systemReportDto;
            for (SystemReportDao report : systemReportDaoList) {
                UserDao userDao = UserDao.getUser(report.getFkUserId());
                systemReportDto = new SystemReportDto(report, userDao.getFirstName(), userDao.getLastName());
                systemReportDtoList.add(systemReportDto);
            }
            ResponseGetSystemReports responseGetSystemReports = new ResponseGetSystemReports();
            responseGetSystemReports.setSystemReportList(systemReportDtoList);
            StandardResponse<ResponseGetSystemReports> response = new StandardResponse<>();
            response.setResponse(responseGetSystemReports);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse incrementDownloadCount(String token, Long fkSystemId, Long versionCode) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            if (!transaction.isActive())
                transaction.begin();
            UserDao user = UserDao.validate(token);
            SystemDownloadDao systemDownload = new SystemDownloadDao();
            systemDownload.setFkSystemId(fkSystemId);
            systemDownload.setFkUserId(user.getId());
            systemDownload.setVersionCode(versionCode);
            entityManager.createQuery("UPDATE SystemDao s SET s.downloadCount = s.downloadCount+1 WHERE s.id = :fkSystemId ")
                    .setParameter("fkSystemId", fkSystemId)
                    .executeUpdate();
            entityManager.persist(systemDownload);
            if (transaction.isActive())
                transaction.commit();
            return new StandardResponse();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse reportSystem(String token, RequestReportSystem requestReportSystem) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            if (!transaction.isActive())
                transaction.begin();
            UserDao user = UserDao.validate(token);
            SystemReportDao systemReportDao = new SystemReportDao(requestReportSystem, user.getId());
            Long rateCount;
            Double rate;
            try {
                SystemDao systemDao = (SystemDao) entityManager.createQuery("SELECT s FROM SystemDao s WHERE s.id = :fkSystemId")
                        .setParameter("fkSystemId", requestReportSystem.getFkSystemId())
                        .getSingleResult();
                rateCount = systemDao.getRateCount();
                rate = systemDao.getRate();
            } catch (Exception ex) {
                throw new Exception(Constants.NOT_VALID_SYSTEM);
            }
            Double newRate = (rate * rateCount + requestReportSystem.getRate()) / (rateCount + 1L);
            entityManager.createQuery("UPDATE SystemDao s SET s.rate = :newRate  WHERE s.id = :systemId ")
                    .setParameter("systemId", requestReportSystem.getFkSystemId())
                    .setParameter("newRate", newRate)
                    .executeUpdate();
            entityManager.createQuery("UPDATE SystemDao s SET s.rateCount = s.rateCount +1 WHERE s.id = :systemId ")
                    .setParameter("systemId", requestReportSystem.getFkSystemId())
                    .executeUpdate();
            entityManager.persist(systemReportDao);
            if (transaction.isActive())
                transaction.commit();
            return new StandardResponse();
        } catch (Exception ex) {
            if (transaction != null && transaction.isActive())
                transaction.rollback();
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse<ResponseGetSystems> getAllSystems(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List<SystemDto> systems = entityManager.createQuery("SELECT distinct (s) FROM SystemDao s ")
                    .getResultList();
            ResponseGetSystems responseGetSystems = new ResponseGetSystems();
            responseGetSystems.setSystemDtos(systems);
            StandardResponse<ResponseGetSystems> response = new StandardResponse<>();


            response.setResponse(responseGetSystems);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse<ResponseGetRolesWithPrivileges> getSystemRolesWithPrivileges(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List<RoleWithPrivileges> rolesWithPrivileges = new LinkedList<>();
            List<RoleDao> roles = entityManager.createQuery("SELECT r FROM RoleDao r WHERE r.fkSystemId = :fkSystemId")
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

    public StandardResponse<ResponseGetSystems> getUserSystems(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDto user = UserDao.validate(token);
//            user.checkPrivilege(PrivilegeNames.GET_SYSTEMS);
            List<SystemDto> systems = entityManager.createQuery("SELECT s FROM SystemDao s JOIN SystemUserDao su ON s.id = su.fkSystemId WHERE su.fkUserId = :fkUserId ")
                    .setParameter("fkUserId", user.getId())
                    .getResultList();
            ResponseGetSystems responseGetSystems = new ResponseGetSystems();
            responseGetSystems.setSystemDtos(systems);
            StandardResponse<ResponseGetSystems> response = new StandardResponse<>();


            response.setResponse(responseGetSystems);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse<ResponseGetSystems> getUserSystems(String token, Long fkUserId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List<SystemDto> systems = entityManager.createQuery("SELECT s FROM SystemDao s JOIN SystemUserDao su ON s.id = su.fkSystemId WHERE su.fkUserId = :fkUserId ")
                    .setParameter("fkUserId", fkUserId)
                    .getResultList();
            ResponseGetSystems responseGetSystems = new ResponseGetSystems();
            responseGetSystems.setSystemDtos(systems);
            StandardResponse<ResponseGetSystems> response = new StandardResponse<>();


            response.setResponse(responseGetSystems);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse<ResponseGetSystemWithVersions> getSystemVersions(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user = UserDao.validate(token);
            List<SystemDto> systemDtos = entityManager.createQuery("SELECT s FROM SystemDao s JOIN SystemUserDao su ON s.id = su.fkSystemId WHERE su.fkUserId = :fkUserId ")
                    .setParameter("fkUserId", user.getId())
                    .getResultList();
            List<SystemWithVersion> systemWithVersions = createSystemWithVersions(systemDtos);
            ResponseGetSystemWithVersions responseGetSystems = new ResponseGetSystemWithVersions();
            responseGetSystems.setSystemWithVersions(systemWithVersions);
            StandardResponse<ResponseGetSystemWithVersions> response = new StandardResponse<>();
            response.setResponse(responseGetSystems);
            return response;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    private List<SystemWithVersion> createSystemWithVersions(List<SystemDto> systemDtos) throws Exception {
        List<SystemWithVersion> systemWithVersions = new LinkedList<>();
        SystemWithVersion systemWithVersion;
        for (SystemDto systemDto : systemDtos) {
            systemWithVersion = new SystemWithVersion();
            systemWithVersion.setSystemDto(systemDto);
            systemWithVersion.setSystemVersionDto(getSystemVersion(systemDto.getId()));
            systemWithVersions.add(systemWithVersion);
        }
        return systemWithVersions;
    }

    private SystemVersionDto getSystemVersion(Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            return (SystemVersionDto) entityManager.createQuery("SELECT v FROM SystemVersionDao v WHERE v.fkSystemId = :fkSystemId AND v.versionCode = (SELECT MAX (s.versionCode) FROM SystemVersionDao s WHERE s.fkSystemId = :fkSystemId)")
                    .setParameter("fkSystemId", fkSystemId)
                    .getSingleResult();
        } catch (Exception ex) {
            throw new Exception(Constants.NOT_VALID_SYSTEM + " بدون ورژن ");
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse<ResponseGetUsers> getSystemUsers(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao user = UserDao.validate(token);
            SystemDao systemDao = SystemDao.getSystem(fkSystemId);
            user.checkPrivilege(PrivilegeNames.GET_SYSTEM_USERS, fkSystemId);
            List<UserDao> users = entityManager.createQuery("SELECT distinct (u) FROM UserDao u JOIN SystemUserDao us ON u.id = us.fkUserId WHERE us.fkSystemId = :fkSystemId")
                    .setParameter("fkSystemId", systemDao.getId())
                    .getResultList();
            List<UserDto> newUsers = new LinkedList<>();
            for (UserDao u : users) {
                UserDto newUser = (UserDto) u.clone();
                newUser.setPassword(null);
                newUsers.add(newUser);
            }
            ResponseGetUsers responseGetUsers = new ResponseGetUsers();
            responseGetUsers.setUsers(newUsers);
            StandardResponse<ResponseGetUsers> response = new StandardResponse<>();


            response.setResponse(responseGetUsers);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse getSystemRoles(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();
        try {
            UserDao.validate(token);
            List<RoleDto> roles = entityManager.createQuery("SELECT r FROM RoleDao r WHERE r.fkSystemId = :fkSystemId")
                    .setParameter("fkSystemId", fkSystemId)
                    .getResultList();
            ResponseGetRoles responseGetRoles = new ResponseGetRoles();
            responseGetRoles.setRoles(roles);
            StandardResponse<ResponseGetRoles> response = new StandardResponse<>();


            response.setResponse(responseGetRoles);
            return response;
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }
}
