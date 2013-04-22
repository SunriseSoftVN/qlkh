package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.widget.Label;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialGroupConstant;
import com.qlkh.client.client.module.content.view.security.MaterialGroupSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class MaterialGroupView.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:42 AM
 */
@ViewSecurity(configuratorClass = MaterialGroupSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialGroupConstant.class)
public class MaterialGroupView extends AbstractView<MaterialGroupConstant> {
    @Override
    protected void initializeView() {
        setWidget(new Label("dung hehehe"));
    }
}
