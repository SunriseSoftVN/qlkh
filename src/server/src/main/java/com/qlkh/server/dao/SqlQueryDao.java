/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.server.dao;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.qlkh.core.client.constant.QuarterEnum;
import com.qlkh.core.client.model.Material;
import com.qlkh.core.client.model.Task;
import com.qlkh.core.client.model.view.TaskDetailDKDataView;
import com.qlkh.core.client.model.view.TaskDetailKDKDataView;
import com.qlkh.core.client.model.view.TaskDetailNamDataView;
import com.qlkh.core.client.model.view.TaskMaterialDataView;
import com.qlkh.core.client.report.MaterialReportBean;
import com.qlkh.server.dao.core.Dao;

import java.util.List;

/**
 * The Class SqlQueryDao.
 *
 * @author Nguyen Duc Dung
 * @since 6/6/12, 9:58 PM
 */
public interface SqlQueryDao extends Dao {
    List<TaskDetailKDKDataView> getTaskDetailKDK(List<Long> stationIds, int year);

    List<TaskDetailDKDataView> getTaskDetailDK(List<Long> stationIds, int year);

    List<TaskDetailNamDataView> getTaskDetailNam(List<Long> stationIds, int year);

    BasePagingLoadResult<Task> getTasks(boolean hasLimit, boolean hasNoLimit, BasePagingLoadConfig config);

    BasePagingLoadResult<Material> getMaterials(int year, QuarterEnum quarter, BasePagingLoadConfig config);

    BasePagingLoadResult<Material> getMaterials(long taskId, BasePagingLoadConfig config);

    BasePagingLoadResult<Material> getMaterialTasks(int year, int quarter, BasePagingLoadConfig config);

    List<TaskMaterialDataView> getTaskMaterial(int year, int quarter);

    List<TaskMaterialDataView> getTaskMaterial(int year);

    List<TaskMaterialDataView> getTaskMaterialByMaterialId(long materialId, int year, int quarter);

    List<Material> getMaterialsMissingPrice(int year, int quarter);

    public List<Material> getMaterialPrices(final int year, final int quarter);

    List<MaterialReportBean> getMaterialOut(int form, int to, int year);
}
