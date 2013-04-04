package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialPriceConstants;
import com.qlkh.client.client.module.content.view.security.MaterialPriceSecurity;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialPriceView.
 *
 * @author Nguyen Duc Dung
 * @since 4/4/13 11:42 PM
 */

@ViewSecurity(configuratorClass = MaterialPriceSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialPriceConstants.class)
public class MaterialPriceView extends AbstractView<MaterialPriceConstants> {

    public static final int MATERIAL_LIST_SIZE = 200;

    public static final String ID_COLUMN = "id";
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 35;

    public static final String MATERIAL_CODE_COLUMN = "code";
    public static final int MATERIAL_CODE_COLUMN_WIDTH = 100;
    public static final String MATERIAL_NAME_COLUMN = "name";
    public static final int MATERIAL_NAME_COLUMN_WIDTH = 200;
    public static final String MATERIAL_UNIT_COLUMN = "unit";
    public static final int MATERIAL_UNIT_COLUMN_WIDTH = 100;
    public static final String MATERIAL_NOTE_COLUMN = "note";
    public static final int MATERIAL_NOTE_COLUMN_WIDTH = 200;

    private Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));
    private TextField<String> txtSearch = new TextField<String>();

    protected ContentPanel contentPanel = new ContentPanel();

    private ColumnModel materialColumnModel;
    private ContentPanel materialPanel = new ContentPanel();
    private PagingToolBar materialPagingToolBar;
    private Grid<BeanModel> materialGird;

    private ContentPanel pricePanel = new ContentPanel();
    private PagingToolBar pricePagingToolBar;
    private EditorGrid<BeanModel> priceGird;

    @Override
    protected void initializeView() {
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        contentPanel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
        setWidget(contentPanel);
    }

    public void createMaterialGrid(ListStore<BeanModel> listStore) {
        materialColumnModel = new ColumnModel(createMaterialColumnConfig());
        materialGird = new Grid<BeanModel>(listStore, materialColumnModel);
        materialGird.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        materialGird.setBorders(true);
        materialGird.setLoadMask(true);
        materialGird.setStripeRows(true);
        materialGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        materialGird.getStore().getLoader().setSortField(MATERIAL_CODE_COLUMN);
        materialGird.setWidth(500);
        materialGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        materialPagingToolBar = new PagingToolBar(MATERIAL_LIST_SIZE);
        txtSearch.setWidth(170);
        materialPanel.setHeaderVisible(false);
        materialPanel.setHeight(Window.getClientHeight() - 90);
        materialPanel.setLayout(new FitLayout());
        materialPanel.setWidth("50%");
        materialPanel.add(materialGird);
        materialPanel.setTopComponent(createToolBar());
        materialPanel.setBottomComponent(materialPagingToolBar);
        materialPanel.setBodyBorder(false);
        contentPanel.add(materialPanel, new RowData(-1, 1, new Margins(0, 2, 0, 0)));
        contentPanel.layout();
    }

    protected ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();
        toolBar.add(txtSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);
        return toolBar;
    }

    private List<ColumnConfig> createMaterialColumnConfig() {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        ColumnConfig sttColumnConfig = new ColumnConfig(STT_COLUMN, getConstant().sttColumnTitle(), STT_COLUMN_WIDTH);
        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                if (model.get(STT_COLUMN) == null) {
                    model.set(STT_COLUMN, rowIndex + 1);
                }
                return new Text(String.valueOf(model.get(STT_COLUMN)));
            }
        });
        columnConfigs.add(sttColumnConfig);

        ColumnConfig codeColumnConfig = new ColumnConfig(MATERIAL_CODE_COLUMN, getConstant().codeColumnTitle(), MATERIAL_CODE_COLUMN_WIDTH);
        columnConfigs.add(codeColumnConfig);

        ColumnConfig nameColumnConfig = new ColumnConfig(MATERIAL_NAME_COLUMN, getConstant().nameColumnTitle(),
                MATERIAL_NAME_COLUMN_WIDTH);
        columnConfigs.add(nameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(MATERIAL_UNIT_COLUMN, getConstant().unitColumnTitle(),
                MATERIAL_UNIT_COLUMN_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig noteColumnConfig = new ColumnConfig(MATERIAL_NOTE_COLUMN, getConstant().noteColumnTitle(),
                MATERIAL_NOTE_COLUMN_WIDTH);
        columnConfigs.add(noteColumnConfig);

        return columnConfigs;
    }

    public PagingToolBar getMaterialPagingToolBar() {
        return materialPagingToolBar;
    }

    public Grid<BeanModel> getMaterialGird() {
        return materialGird;
    }
}
