package com.qlkh.server.dao;

import com.qlkh.core.client.model.Settings;
import com.qlkh.server.dao.core.Dao;

public interface SettingDao extends Dao<Settings> {
    Settings findByName(String name);
}
