package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.BranchManagerConstant;
import com.qlkh.client.client.module.content.view.i18n.LimitJobConstant;
import com.qlkh.client.client.module.content.view.security.BranchManagerSecurity;
import com.qlkh.client.client.module.content.view.security.LimitJobSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class LimitJobView.
 *
 * @author Nguyen Duc Dung
 * @since 3/20/13 10:10 AM
 */
@ViewSecurity(configuratorClass = LimitJobSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = LimitJobConstant.class)
public class LimitJobView extends AbstractView<LimitJobConstant> {

    private ContentPanel contentPanel = new ContentPanel();

    @Override
    protected void initializeView() {
        contentPanel.add(new Label("dung ne"));
        setWidget(contentPanel);
    }
}
