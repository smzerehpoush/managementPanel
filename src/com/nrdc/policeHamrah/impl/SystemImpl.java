package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.helper.PrivilegeNames;
import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.customizedModel.SystemWithVersion;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystemWithVersions;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystems;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetUsers;
import com.nrdc.policeHamrah.model.dao.SystemDao;
import com.nrdc.policeHamrah.model.dao.UserDao;
import com.nrdc.policeHamrah.model.dto.SystemDto;
import com.nrdc.policeHamrah.model.dto.SystemVersionDto;
import com.nrdc.policeHamrah.model.dto.UserDto;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

public class SystemImpl {
    public StandardResponse<ResponseGetSystems> getUserSystems(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
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

    public StandardResponse<ResponseGetSystemWithVersions> getSystemVersions(String token) {
        EntityManager entityManager = Database.getEntityManager();
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
            return StandardResponse.getNOKExceptions(ex);
        } finally {
            if (entityManager != null && entityManager.isOpen())
                entityManager.close();
        }
    }

    private List<SystemWithVersion> createSystemWithVersions(List<SystemDto> systemDtos) {
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

    private SystemVersionDto getSystemVersion(Long fkSystemId) {
        EntityManager entityManager = Database.getEntityManager();
        entityManager.getEntityManagerFactory().getCache().evictAll();

        try {
            return (SystemVersionDto) entityManager.createQuery("SELECT v FROM SystemVersionDao v WHERE v.fkSystemId = :fkSystemId AND v.versionCode = (SELECT MAX (s.versionCode) FROM SystemVersionDao s WHERE s.fkSystemId = :fkSystemId)")
                    .setParameter("fkSystemId", fkSystemId)
                    .getSingleResult();
        } finally {
            if (entityManager.isOpen())
                entityManager.close();
        }
    }

    public StandardResponse<ResponseGetUsers> getSystemUsers(String token, Long fkSystemId) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
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
}
