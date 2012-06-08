/*
 * Copyright (C) 2012 - 2013 Nguyen Duc Dung (dungvn3000@gmail.com)
 */

package com.qlkh.client.client.widget;

import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.DomEvent;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.google.gwt.event.dom.client.KeyCodes;

/**
 * The Class MyCheckBoxSelectionModel only use for EditorGrid.
 *
 * @author Nguyen Duc Dung
 * @since 1/11/12, 12:57 PM
 */
public class MyCheckBoxSelectionModel<M extends ModelData> extends CheckBoxSelectionModel<M> {

    @Override
    public void onEditorKey(DomEvent e) {
        if (e.getKeyCode() == KeyCodes.KEY_TAB) {
            EditorGrid editorGrid = (EditorGrid) grid;
            int activeCol = editorGrid.getActiveEditor().col;
            int activeRow = editorGrid.getActiveEditor().row;
            int countCol = editorGrid.getColumnModel().getColumnCount();
            int nextCol = 0;
            for (int i = 0; i < countCol; i++) {
                if (!e.isShiftKey()) {
                    ColumnConfig columnConfig = editorGrid.getColumnModel().getColumn(i);
                    if (i > activeCol && columnConfig.getEditor() != null) {
                        nextCol = i;
                        break;
                    }
                } else {
                    ColumnConfig columnConfig = editorGrid.getColumnModel().getColumn(countCol - i - 1);
                    if (i < activeCol && columnConfig.getEditor() != null) {
                        nextCol = i;
                        break;
                    }
                }
            }
            editorGrid.getView().ensureVisible(activeRow, nextCol, true);
        }
        super.onEditorKey(e);
    }
}
