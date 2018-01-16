package com.gimnasio.model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Administrador
 */
public class MiRender extends DefaultTableCellRenderer {

    private TablaModelo tabla;

    public MiRender(TablaModelo tabla) {
        super();
        this.tabla = tabla;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        TablaDto dto = (TablaDto) tabla.getData().get(row);
        this.setOpaque(false);
        this.setForeground(Color.BLACK);
        this.setBackground(dto.getColor());
        return this;
    }
}
