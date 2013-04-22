package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.widget.Label;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialInViewConstant;
import com.qlkh.client.client.module.content.view.security.MaterialInSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class MaterialInView.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 4:05 AM
 */
@ViewSecurity(configuratorClass = MaterialInSecurity.class)
@View(constantsClass = MaterialInViewConstant.class, parentDomId = DomIdConstant.CONTENT_PANEL)
public class MaterialInView extends AbstractView<MaterialInViewConstant> {

    @Override
    protected void initializeView() {
        setWidget(new Label("121323"));
    }
}
