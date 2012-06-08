/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.dto;

import com.extjs.gxt.ui.client.data.BaseModelData;

import java.io.Serializable;
import java.util.Date;

/**
 * The Class AbstractDto.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 2:38 PM
 */
public class AbstractDto extends BaseModelData implements Serializable {

    public Long getId() {
        return get("id");
    }

    public void setId(Long id) {
        set("id", id);
    }

    public Date getCreatedDate() {
        return get("createdDate");
    }

    public void setCreatedDate(Date createdDate) {
        set("createdDate", createdDate);
    }

    public Date getUpdatedDate() {
        return get("updatedDate");
    }

    public void setUpdatedDate(Date updatedDate) {
        set("updatedDate", updatedDate);
    }

    public Long getCreateBy() {
        return get("createBy");
    }

    public void setCreateBy(Long createBy) {
        set("createBy", createBy);
    }

    public Long getUpdateBy() {
        return get("updateBy");
    }

    public void setUpdateBy(Long updateBy) {
        set("updateBy", updateBy);
    }
}
