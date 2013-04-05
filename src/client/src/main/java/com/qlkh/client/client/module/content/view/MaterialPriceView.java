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
import com.qlkh.client.client.widget.MyNumberField;
import com.smvp4g.mvp.client.core.i18n.I18nField;
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

    public static final String Q1_UNIT_COLUMN = "q1";
    public static final int Q1_UNIT_WIDTH = 70;
    public static final String Q2_UNIT_COLUMN = "q2";
    public static final int Q2_UNIT_WIDTH = 70;
    public static final String Q3_UNIT_COLUMN = "q3";
    public static final int Q3_UNIT_WIDTH = 70;
    public static final String Q4_UNIT_COLUMN = "q4";
    public static final int Q4_UNIT_WIDTH = 70;

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField(emptyText = true)
    TextField<String> txtSearch = new TextField<String>();

    protected ContentPanel contentPanel = new ContentPanel();

    private ColumnModel materialColumnModel;
    private ContentPanel materialPanel = new ContentPanel();
    private PagingToolBar materialPagingToolBar;
    private Grid<BeanModel> materialGird;

    private ColumnModel priceColumnModel;
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

    public void createPriceGrid(ListStore<BeanModel> listStore) {
        priceColumnModel = new ColumnModel(createPriceColumnConfig());
        priceGird = new EditorGrid<BeanModel>(listStore, priceColumnModel);
        priceGird.getSelectionModel().setSelectionMode(Style.SelectionMode.SINGLE);
        priceGird.setBorders(true);
        priceGird.setLoadMask(true);
        priceGird.setStripeRows(true);
        priceGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        priceGird.getStore().getLoader().setSortField(MATERIAL_CODE_COLUMN);
        priceGird.setWidth(500);
        priceGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 115) {
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        pricePagingToolBar = new PagingToolBar(MATERIAL_LIST_SIZE);
        txtSearch.setWidth(170);
        pricePanel.setHeaderVisible(false);
        pricePanel.setHeight(Window.getClientHeight() - 90);
        pricePanel.setLayout(new FitLayout());
        pricePanel.setWidth("50%");
        pricePanel.add(priceGird);
        pricePanel.setTopComponent(createToolBar());
        pricePanel.setBottomComponent(pricePagingToolBar);
        pricePanel.setBodyBorder(false);
        contentPanel.add(pricePanel, new RowData(-1, 1));
        contentPanel.layout();
    }

    private List<ColumnConfig> createPriceColumnConfig() {
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


        ColumnConfig q1ColumnConfig = new ColumnConfig(Q1_UNIT_COLUMN, getConstant().q1ColumnTitle(), Q1_UNIT_WIDTH);
        MyNumberField q1NumberField = new MyNumberField();
        q1NumberField.setSelectOnFocus(true);
        q1ColumnConfig.setEditor(new CellEditor(q1NumberField));
        columnConfigs.add(q1ColumnConfig);

        ColumnConfig q2ColumnConfig = new ColumnConfig(Q2_UNIT_COLUMN, getConstant().q2ColumnTitle(), Q2_UNIT_WIDTH);
        MyNumberField q2NumberField = new MyNumberField();
        q2NumberField.setSelectOnFocus(true);
        q2ColumnConfig.setEditor(new CellEditor(q2NumberField));
        columnConfigs.add(q2ColumnConfig);

        ColumnConfig q3ColumnConfig = new ColumnConfig(Q3_UNIT_COLUMN, getConstant().q3ColumnTitle(), Q3_UNIT_WIDTH);
        MyNumberField q3NumberField = new MyNumberField();
        q3NumberField.setSelectOnFocus(true);
        q3ColumnConfig.setEditor(new CellEditor(q3NumberField));
        columnConfigs.add(q3ColumnConfig);

        ColumnConfig q4ColumnConfig = new ColumnConfig(Q4_UNIT_COLUMN, getConstant().q4ColumnTitle(), Q4_UNIT_WIDTH);
        MyNumberField q4NumberField = new MyNumberField();
        q4NumberField.setSelectOnFocus(true);
        q4ColumnConfig.setEditor(new CellEditor(q4NumberField));
        columnConfigs.add(q4ColumnConfig);

        return columnConfigs;
    }

    public PagingToolBar getMaterialPagingToolBar() {
        return materialPagingToolBar;
    }

    public Grid<BeanModel> getMaterialGird() {
        return materialGird;
    }

    public EditorGrid<BeanModel> getPriceGird() {
        return priceGird;
    }

    public PagingToolBar getPricePagingToolBar() {
        return pricePagingToolBar;
    }
}
