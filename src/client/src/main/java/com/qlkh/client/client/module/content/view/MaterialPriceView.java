package com.qlkh.client.client.module.content.view;

import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialPriceConstants;
import com.qlkh.client.client.module.content.view.security.MaterialPriceSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;

/**
 * The Class MaterialPriceView.
 *
 * @author Nguyen Duc Dung
 * @since 4/4/13 11:42 PM
 */

@ViewSecurity(configuratorClass = MaterialPriceSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialPriceConstants.class)
public class MaterialPriceView extends AbstractView<MaterialPriceConstants> {

}
