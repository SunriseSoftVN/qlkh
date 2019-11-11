package com.qlkh.server.dao.impl;

import com.qlkh.core.client.constant.SettingEnum;
import com.qlkh.core.client.model.Settings;
import com.qlkh.server.dao.SettingDao;
import com.qlkh.server.dao.core.AbstractDao;
import com.smvp4g.mvp.client.core.utils.CollectionsUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class SettingDaoImpl extends AbstractDao<Settings> implements SettingDao {

    @Override
    public Settings findByName(String name) {
        DetachedCriteria criteria = DetachedCriteria
                .forClass(Settings.class)
                .add(Restrictions.eq("name", name));
        List<Settings> settings = getHibernateTemplate().findByCriteria(criteria);
        if (CollectionsUtils.isNotEmpty(settings)) {
            return settings.get(0);
        }
        return null;
    }

    @Override
    public String getCompanyName() {
        Settings settings = findByName(SettingEnum.COMPANY_NAME.getName());
        if (settings != null) {
            return settings.getValue();
        } else {
            return StringUtils.EMPTY;
        }
    }

    @Override
    public boolean isShowDSTNNDReport() {
        Settings settings = findByName(SettingEnum.HIEN_DS_TNND.getName());
        if (settings != null) {
            return StringUtils.equalsIgnoreCase(settings.getValue(), "true");
        } else {
            return true;
        }
    }
}
