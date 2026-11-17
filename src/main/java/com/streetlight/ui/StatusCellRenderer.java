package com.streetlight.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/** Gives ON and OFF values clear, consistent colors in street light tables. */
public class StatusCellRenderer extends DefaultTableCellRenderer {
    private static final Color ON_TEXT = new Color(21, 116, 89);
    private static final Color ON_BACKGROUND = new Color(220, 245, 235);
    private static final Color OFF_TEXT = new Color(158, 63, 63);
    private static final Color OFF_BACKGROUND = new Color(252, 232, 232);

    public StatusCellRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        String status = value == null ? "" : value.toString().trim().toUpperCase();
        setFont(table.getFont().deriveFont(Font.BOLD));
        if ("ON".equals(status)) {
            setForeground(ON_TEXT);
            setBackground(ON_BACKGROUND);
        } else if ("OFF".equals(status)) {
            setForeground(OFF_TEXT);
            setBackground(OFF_BACKGROUND);
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        return cell;
    }
}
