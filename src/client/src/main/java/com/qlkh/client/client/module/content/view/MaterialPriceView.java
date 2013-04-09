package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.widget.Label;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialPriceConstant;
import com.qlkh.client.client.module.content.view.security.MaterialPriceSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class MaterialPriceView.
 *
 * @author Nguyen Duc Dung
 * @since 4/9/13 7:21 PM
 */
@ViewSecurity(configuratorClass = MaterialPriceSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialPriceConstant.class)
public class MaterialPriceView extends AbstractView<MaterialPriceConstant> {

    @Override
    protected void initializeView() {
        setWidget(new Label("dung ne"));
    }
}
