package com.qlkh.client.client.module.content.view;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.*;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.qlkh.client.client.constant.DomIdConstant;
import com.qlkh.client.client.module.content.view.i18n.MaterialInViewConstant;
import com.qlkh.client.client.module.content.view.security.MaterialInSecurity;
import com.qlkh.client.client.widget.MyFitLayout;
import com.qlkh.client.client.widget.MyFormPanel;
import com.smvp4g.mvp.client.core.i18n.I18nField;
import com.smvp4g.mvp.client.core.security.ViewSecurity;
import com.smvp4g.mvp.client.core.view.AbstractView;
import com.smvp4g.mvp.client.core.view.annotation.View;
import com.smvp4g.mvp.client.widget.TextField;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class MaterialInView.
 *
 * @author Nguyen Duc Dung
 * @since 4/23/13 4:05 AM
 */
@ViewSecurity(configuratorClass = MaterialInSecurity.class)
@View(constantsClass = MaterialInViewConstant.class, parentDomId = DomIdConstant.CONTENT_PANEL)
public class MaterialInView extends AbstractView<MaterialInViewConstant> {

    public static final int LIST_SIZE = 200;

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

    private MyFormPanel editPanel = new MyFormPanel();

    private PagingToolBar pagingToolBar;
    private Grid<BeanModel> gird;
    private ContentPanel contentPanel = new ContentPanel();

    public void createGrid(ListStore<BeanModel> listStore) {
        CheckBoxSelectionModel<BeanModel> selectionModel = new CheckBoxSelectionModel<BeanModel>();
        ColumnModel cm = new ColumnModel(createColumnConfig(selectionModel));
        gird = new Grid<BeanModel>(listStore, cm);
        gird.setBorders(true);
        gird.setLoadMask(true);
        gird.setStripeRows(true);
        gird.setSelectionModel(selectionModel);
        gird.addPlugin(selectionModel);
        gird.getStore().getLoader().setSortDir(Style.SortDir.ASC);
        gird.getStore().getLoader().setSortField("id");
        gird.addListener(Events.OnKeyDown, new KeyListener() {
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

        pagingToolBar = new PagingToolBar(LIST_SIZE);
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
        contentPanel.add(gird);
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
        ColumnConfig sttColumnConfig = new ColumnConfig("stt", getConstant().sttColumnTitle(), 40);
        sttColumnConfig.setRenderer(new GridCellRenderer<BeanModel>() {
            @Override
            public Object render(BeanModel model, String property, ColumnData config, int rowIndex, int colIndex,
                                 ListStore<BeanModel> beanModelListStore, Grid<BeanModel> beanModelGrid) {
                if (model.get("stt") == null) {
                    model.set("stt", rowIndex + 1);
                }
                return new Text(String.valueOf(model.get("stt")));
            }
        });
        columnConfigs.add(sttColumnConfig);

        ColumnConfig materialCodeColumnConfig = new ColumnConfig("material.code", getConstant().codeColumnTitle(), 100);
        columnConfigs.add(materialCodeColumnConfig);

        ColumnConfig materialNameColumnConfig = new ColumnConfig("material.name", getConstant().nameColumnTitle(), 200);
        columnConfigs.add(materialNameColumnConfig);

        ColumnConfig materialUnitColumnConfig = new ColumnConfig("material.unit", getConstant().unitColumnTitle(), 50);
        columnConfigs.add(materialUnitColumnConfig);

        ColumnConfig totalColumnConfig = new ColumnConfig("total", getConstant().totalColumnTitle(), 100);
        columnConfigs.add(totalColumnConfig);

        ColumnConfig reasonColumnConfig = new ColumnConfig("materialGroup.name", getConstant().reasonColumnTitle(), 200);
        columnConfigs.add(reasonColumnConfig);

        ColumnConfig groupCodeColumnConfig = new ColumnConfig("materialGroup.code", getConstant().groupCodeColumnTitle(), 200);
        columnConfigs.add(groupCodeColumnConfig);

        ColumnConfig dateColumnConfig = new ColumnConfig("createdDate", getConstant().dateColumnTitle(), 200);
        columnConfigs.add(dateColumnConfig);

        ColumnConfig personColumnConfig = new ColumnConfig("materialPerson.personName", getConstant().personColumnTitle(), 200);
        columnConfigs.add(personColumnConfig);

        ColumnConfig weightColumnConfig = new ColumnConfig("weight", getConstant().weightColumnTitle(), 200);
        columnConfigs.add(weightColumnConfig);

        ColumnConfig remainColumnConfig = new ColumnConfig("remain", getConstant().remainColumnTitle(), 100);
        columnConfigs.add(remainColumnConfig);

        return columnConfigs;
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

    public MyFormPanel getEditPanel() {
        return editPanel;
    }

    public PagingToolBar getPagingToolBar() {
        return pagingToolBar;
    }

    public Grid<BeanModel> getGird() {
        return gird;
    }

    public ContentPanel getContentPanel() {
        return contentPanel;
    }
}
