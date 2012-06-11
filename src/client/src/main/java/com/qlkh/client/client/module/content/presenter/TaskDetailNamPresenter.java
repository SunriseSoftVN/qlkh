/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.presenter;

import com.qlkh.client.client.module.content.place.TaskDetailNamPlace;
import com.qlkh.client.client.module.content.presenter.share.AbstractTaskDetailPresenter;
import com.qlkh.client.client.module.content.view.TaskDetailNamView;
import com.smvp4g.mvp.client.core.presenter.annotation.Presenter;

/**
 * The Class TaskDetailNamPresenter.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 9:06 PM
 */
@Presenter(view = TaskDetailNamView.class, place = TaskDetailNamPlace.class)
public class TaskDetailNamPresenter extends AbstractTaskDetailPresenter<TaskDetailNamView> {
}
