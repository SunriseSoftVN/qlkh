package com.qlkh.client.client.module.content.view;

import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.GroupManagerConstant;
import com.qlkh.client.client.module.content.view.security.GroupManagerSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class GroupManagerView.
 *
 * @author Nguyen Duc Dung
 * @since 7/26/13 9:20 AM
 */
@ViewSecurity(configuratorClass = GroupManagerSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = GroupManagerConstant.class)
public class GroupManagerView extends AbstractView<GroupManagerConstant> {
}
