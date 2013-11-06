package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

import javax.persistence.Transient;

/**
 * The Class MaterialGroup.
 *
 * @author Nguyen Duc Dung
 * @since 4/25/13 4:19 AM
 */
public class MaterialGroup extends AbstractEntity {

    private String code;
    private String name;
    private String regex;
    private String codeDisplay;
    private Integer position;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegex() {
        return regex;
    }

    @Transient
    public String[] getRegexs() {
        if (regex != null) {
            return regex.split(",");
        }
        return null;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public String getCodeDisplay() {
        return codeDisplay;
    }

    public void setCodeDisplay(String codeDisplay) {
        this.codeDisplay = codeDisplay;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer order) {
        this.position = order;
    }
}
