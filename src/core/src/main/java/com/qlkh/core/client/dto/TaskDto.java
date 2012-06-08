/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
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
