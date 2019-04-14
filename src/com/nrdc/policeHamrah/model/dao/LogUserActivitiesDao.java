package com.nrdc.policeHamrah.model.dao;

import com.nrdc.policeHamrah.helper.Constants;
import com.nrdc.policeHamrah.model.dto.LogUserActivitiesDto;

import javax.persistence.*;

/**
 * Created by mhd.zerehpoosh on 4/14/2019.
 */
@Entity
@Table(name = "PH_LOG_USER_ACTIVIRIES", schema = Constants.SCHEMA)
public class LogUserActivitiesDao extends LogUserActivitiesDto {
    public static final String tableName = "PH_LOG_USER_ACTIVITIES";

    @Override
    @Basic
    @Column(name = "FK_USER_ID", table = tableName)
    public Long getFkUserId() {
        return super.getFkUserId();
    }

    @Override
    @Id
    @GeneratedValue
    @Column(name = "ID_PH_LOG_USER_ACTIVITIES", table = tableName)
    public String getId() {
        return super.getId();
    }

    @Override
    @Basic
    @Column(name = "ACTION_TYPE", table = tableName)
    public String getActionType() {
        return super.getActionType();
    }

    @Override
    @Basic
    @Column(name = "ACTION_ID", table = tableName)
    public String getActionId() {
        return super.getActionId();
    }
}
