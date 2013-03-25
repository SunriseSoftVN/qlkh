package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialConstant;
import com.qlkh.client.client.module.content.view.security.MaterialSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.core.client.constant.TaskTypeEnum;
import com.qlkh.core.client.model.Task;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.utils.StringUtils;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialView.
 *
 * @author Nguyen Duc Dung
 * @since 3/25/13 1:34 PM
 */

@ViewSecurity(configuratorClass = MaterialSecurity.class)
@View(parentDomId = DomIdConstant.CONTENT_PANEL, constantsClass = MaterialConstant.class)
public class MaterialView extends AbstractView<MaterialConstant> {

    public static final int TASK_LIST_SIZE = 200;
    public static final String STT_COLUMN = "stt";
    public static final int STT_COLUMN_WIDTH = 40;
    public static final String CODE_COLUMN = "code";
    public static final int CODE_COLUMN_WIDTH = 100;
    public static final String NAME_COLUMN = "name";
    public static final int NAME_COLUMN_WIDTH = 200;
    public static final String UNIT_COLUMN = "unit";
    public static final int UNIT_COLUMN_WIDTH = 100;
    public static final String PRICE_COLUMN = "price";
    public static final int PRICE_COLUMN_WIDTH = 100;
    public static final String NOTE_COLUMN = "note";
    public static final int NOTE_COLUMN_WIDTH = 200;

    @I18nField
    Button btnAdd = new Button(null, IconHelper.createPath("assets/images/icons/fam/add.png"));

    @I18nField
    Button btnEdit = new Button(null, IconHelper.createPath("assets/images/icons/fam/disk.png"));

    @I18nField
    Button btnDelete = new Button(null, IconHelper.createPath("assets/images/icons/fam/delete.png"));

    @I18nField
    Button btnRefresh = new Button(null, IconHelper.createPath("assets/images/icons/fam/arrow_refresh.png"));

    @I18nField(emptyText = true)
    TextField<String> txtNameSearch = new TextField<String>();

    @I18nField(emptyText = true)
    TextField<String> txtCodeSearch = new TextField<String>();

    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> taskGird;

    private ContentPanel contentPanel = new ContentPanel();

    /**
     * Create Grid on View.
     */
    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        taskGird = new Grid<BeanModel>(listStore, cm);
        taskGird.setBorders(true);
        taskGird.setLoadMask(true);
        taskGird.setStripeRows(true);
        taskGird.setSelectionModel(selectionModel);
        taskGird.addPlugin(selectionModel);
        taskGird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        taskGird.getStore().getLoader().setSortField(CODE_COLUMN);
        taskGird.addListener(Events.OnKeyDown, new KeyListener() {
            @Override
            public void handleEvent(ComponentEvent e) {
                if (e.getKeyCode() == 112) {
                    //F1
                    btnAdd.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 113 || e.getKeyCode() == KeyCodes.KEY_ENTER) {
                    //F2 or Enter
                    btnEdit.fireEvent(Events.Select);
                } else if (e.getKeyCode() == 115) {
                    //F4
                    btnRefresh.fireEvent(Events.Select);
                }
            }
        });

        pagingToolBar = new PagingToolBar(TASK_LIST_SIZE);
        ToolBar toolBar = new ToolBar();
        txtCodeSearch.setWidth(100);
        toolBar.add(txtCodeSearch);
        toolBar.add(new SeparatorToolItem());
        txtNameSearch.setWidth(150);
        toolBar.add(txtNameSearch);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnAdd);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnEdit);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnDelete);
        toolBar.add(new SeparatorToolItem());
        toolBar.add(btnRefresh);

        contentPanel.setLayout(new MyFitLayout());
        contentPanel.add(taskGird);
        contentPanel.setTopComponent(toolBar);
        contentPanel.setBottomComponent(pagingToolBar);
        contentPanel.setHeaderVisible(false);
        contentPanel.setHeight(Window.getClientHeight() - 90);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                contentPanel.layout(true);
            }
        });
        setWidget(contentPanel);
    }

    private List<ColumnConfig> createColumnConfig(CheckBoxSelectionModel<BeanModel> selectionModel) {
        List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>();
        columnConfigs.add(selectionModel.getColumn());
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

        ColumnConfig codeColumnConfig = new ColumnConfig(CODE_COLUMN, getConstant().codeColumnTitle(), CODE_COLUMN_WIDTH);
        columnConfigs.add(codeColumnConfig);

        ColumnConfig nameColumnConfig = new ColumnConfig(NAME_COLUMN, getConstant().nameColumnTitle(),
                NAME_COLUMN_WIDTH);
        columnConfigs.add(nameColumnConfig);

        ColumnConfig unitColumnConfig = new ColumnConfig(UNIT_COLUMN, getConstant().unitColumnTitle(),
                UNIT_COLUMN_WIDTH);
        columnConfigs.add(unitColumnConfig);

        ColumnConfig priceColumnConfig = new ColumnConfig(PRICE_COLUMN, getConstant().priceColumnTitle(),
                PRICE_COLUMN_WIDTH);
        priceColumnConfig.setNumberFormat(NumberFormat.getDecimalFormat());
        columnConfigs.add(priceColumnConfig);

        ColumnConfig noteColumnConfig = new ColumnConfig(NOTE_COLUMN, getConstant().noteColumnTitle(),
                NOTE_COLUMN_WIDTH);
        columnConfigs.add(noteColumnConfig);

        return columnConfigs;
    }

    public Grid<BeanModel> getTaskGird() {
        return taskGird;
    }

    public Button getBtnAdd() {
        return btnAdd;
    }

    public Button getBtnEdit() {
        return btnEdit;
    }

    public Button getBtnDelete() {
        return btnDelete;
    }

    public Button getBtnRefresh() {
        return btnRefresh;
    }

    public TextField<String> getTxtNameSearch() {
        return txtNameSearch;
    }

    public TextField<String> getTxtCodeSearch() {
        return txtCodeSearch;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }
}
