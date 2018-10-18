package com.nrdc.policeHamrah.impl;

import com.nrdc.policeHamrah.jsonModel.StandardResponse;
import com.nrdc.policeHamrah.jsonModel.customizedModel.SystemWithVersion;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystemWithVersions;
import com.nrdc.policeHamrah.jsonModel.jsonResponse.ResponseGetSystems;
import com.nrdc.policeHamrah.model.dao.UserDao;
import com.nrdc.policeHamrah.model.dto.SystemDto;
import com.nrdc.policeHamrah.model.dto.SystemVersionDto;
import com.nrdc.policeHamrah.model.dto.UserDto;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

public class SystemImpl {
    public StandardResponse getUserSystems(String token) throws Exception {
        EntityManager entityManager = Database.getEntityManager();
        try {
            UserDto user = UserDao.validate(token);
//            user.checkPrivilege(PrivilegeNames.GET_SYSTEMS);
            List<SystemDto> systems = entityManager.createQuery("SELECT s FROM SystemDao s JOIN SystemUserDao su ON s.id = su.fkSystemId WHERE su.fkUserId = :fkUserId ")
                    .setParameter("fkUserId", user.getId())
                    .getResultList();
            ResponseGetSystems responseGetSystems = new ResponseGetSystems();
            responseGetSystems.setSystemDtos(systems);
            StandardResponse response = new StandardResponse<>();
            response.setResultCode(1);
            response.setResultMessage("OK");
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
}
