/*
 * Copyright (C) 2011 - 2012 SMVP4G.COM
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 3 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
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
