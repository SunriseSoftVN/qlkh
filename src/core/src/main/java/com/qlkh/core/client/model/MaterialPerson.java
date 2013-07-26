package com.qlkh.core.client.model;

import com.qlkh.core.client.model.core.AbstractEntity;

/**
 * The Class MaterialPerson.
 *
 * @author Nguyen Duc Dung
 * @since 4/24/13 1:41 AM
 */
public class MaterialPerson extends AbstractEntity {

    private Station station;
    private Group group;
    private String personName;

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
