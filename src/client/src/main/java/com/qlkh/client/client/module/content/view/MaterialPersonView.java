package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.widget.Label;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialPersonConstant;
import com.qlkh.client.client.module.content.view.security.MaterialPersonSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class MaterialPersonView.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 3:21 AM
 */
@ViewSecurity(configuratorClass = MaterialPersonSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialPersonConstant.class)
public class MaterialPersonView extends AbstractView<MaterialPersonConstant> {

    @Override
    protected void initializeView() {
        setWidget(new Label("dung ne"));
    }
}
