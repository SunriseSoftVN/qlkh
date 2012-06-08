/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.core.reader;

import com.extjs.gxt.ui.client.data.BeanModelReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ModelData;
import com.qlkh.core.client.action.grid.LoadGridDataResult;

/**
 * The Class BeanModelReader.
 *
 * @author Nguyen Duc Dung
 * @since 6/1/12, 10:11 AM
 */
public class LoadGridDataReader extends BeanModelReader {
    @Override
    public ListLoadResult<ModelData> read(Object loadConfig, Object data) {
        LoadGridDataResult result = (LoadGridDataResult) data;
        return super.read(loadConfig, result.getResult());
    }
}
