/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.qlvt.core.client.dto;

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
