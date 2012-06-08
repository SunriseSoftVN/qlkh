/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.core.client.dto;

import com.qlkh.core.client.model.Branch;

/**
 * The Class SubTaskDetailDto.
 *
 * @author Nguyen Duc Dung
 * @since 1/3/12, 8:44 PM
 */
public class SubTaskDetailDto extends AbstractDto {

    /**
     * This variable declare for Serializable.
     */
    private TaskDetailDto taskDetailDto;
    
    public TaskDetailDto getTaskDetail() {
        return get("taskDetail");
    }

    public void setTaskDetail(TaskDetailDto taskDetail) {
        set("taskDetail", taskDetail);
    }

    public Branch getBranch() {
        return get("branch");
    }

    public void setBranch(Branch branch) {
        set("branch", branch);
    }

    public Double getQ1() {
        return get("q1");
    }

    public void setQ1(Double q1) {
        set("q1", q1);
    }

    public Double getQ2() {
        return get("q2");
    }

    public void setQ2(Double q2) {
        set("q2", q2);
    }

    public Double getQ3() {
        return get("q3");
    }

    public void setQ3(Double q3) {
        set("q3", q3);
    }

    public Double getQ4() {
        return get("q4");
    }

    public void setQ4(Double q4) {
        set("q4", q4);
    }
}
