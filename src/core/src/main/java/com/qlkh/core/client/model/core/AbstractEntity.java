/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.model.core;

import com.extjs.gxt.ui.client.data.BeanModelTag;
import com.smvp4g.reflection.client.marker.Reflection;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * The Class AbstractEntity.
 *
 * @author dungvn3000
 * @since 2/26/11, 6:33 PM
 */
@Reflection
public abstract class AbstractEntity implements Serializable, BeanModelTag {

    private Long id;
    private Date createdDate;
    private Date updatedDate;
    private Long createBy;
    private Long updateBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    //This is a trick for using AliasToBeanResultTransformer of hibernate. @see SqlQueryDaoImpl line 93
    public void setBigId(BigInteger id) {
        setId(id.longValue());
    }
}