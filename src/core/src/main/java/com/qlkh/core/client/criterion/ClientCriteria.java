/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.criterion;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * The Class ClientCriteria.
 *
 * @author Nguyen Duc Dung
 * @since 6/2/12, 9:52 PM
 */
public class ClientCriteria implements IsSerializable {

    private String propertyName;
    private String stringValue;
    private Integer intValue;
    private Long longValue;
    private Boolean boolValue;
    private Date dateValue;
    private Operation operation;

    public ClientCriteria() {
    }

    public ClientCriteria(String propertyName, Operation operation) {
        this.operation = operation;
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public Long getLongValue() {
        return longValue;
    }

    public void setLongValue(Long longValue) {
        this.longValue = longValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public enum Operation {
        EQ, NE
    }
}
