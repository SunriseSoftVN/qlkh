/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.module.content.view;

import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.TaskDetailNamConstant;
import com.qlkh.client.client.module.content.view.security.TaskDetailNamSecurity;
import com.qlkh.client.client.module.content.view.share.AbstractTaskDetailView;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class TaskDetailNamView.
 *
 * @author Nguyen Duc Dung
 * @since 6/11/12, 9:03 PM
 */
@ViewSecurity(configuratorClass = TaskDetailNamSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = TaskDetailNamConstant.class)
public class TaskDetailNamView extends AbstractTaskDetailView<TaskDetailNamConstant> {
}
