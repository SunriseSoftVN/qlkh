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

package com.qlkh.core.client.dto;

/**
 * The Class TaskDto.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 5:59 PM
 */
public class TaskDto extends AbstractDto {

    public String getName() {
        return get("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public Integer getCode() {
        return get("code");
    }

    public void setCode(Integer code) {
        set("code", code);
    }

    public Double getDefaultValue() {
        return get("defaultValue");
    }

    public void setDefaultValue(Double defaultValue) {
        set("defaultValue", defaultValue);
    }

    public String getUnit() {
        return get("unit");
    }

    public void setUnit(String unit) {
        set("unit", unit);
    }

}
