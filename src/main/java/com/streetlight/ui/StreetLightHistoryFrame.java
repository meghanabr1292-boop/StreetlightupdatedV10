package com.streetlight.ui;

import com.streetlight.dao.StreetLightHistoryDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/** Read-only history of street light activity. */
public class StreetLightHistoryFrame extends JFrame {
    private static final Color NAVY = new Color(12, 30, 48);
    private static final Color TEAL = new Color(36, 193, 174);
    private final StreetLightHistoryDAO historyDAO = new StreetLightHistoryDAO();
    private final DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Time", "Light", "Event", "Previous", "Current", "Details"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public StreetLightHistoryFrame() {
        setTitle("LumaGrid | Activity History");
        setSize(980, 560);
        setMinimumSize(new Dimension(760, 440));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createUI();
        loadEvents();
    }

    private void createUI() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(new Color(244, 248, 250));
        root.setBorder(BorderFactory.createEmptyBorder(22, 26, 24, 26));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JLabel brand = new JLabel("LUMAGRID  /  ACTIVITY HISTORY");
        brand.setFont(new Font("SansSerif", Font.BOLD, 16));
        brand.setForeground(TEAL);
        header.add(brand, BorderLayout.WEST);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Recent light activity");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("See when lights were added, changed, removed, or switched by the schedule.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(94, 115, 130));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(5));
        titleBlock.add(subtitle);

        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getColumnModel().getColumn(3).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(5).setPreferredWidth(260);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 231, 236)));

        JLabel count = new JLabel("Newest 500 events");
        count.setForeground(new Color(94, 115, 130));
        count.setFont(new Font("SansSerif", Font.PLAIN, 12));
        JButton refresh = new JButton("Refresh history");
        refresh.setBackground(TEAL);
        refresh.setForeground(NAVY);
        refresh.setFont(new Font("SansSerif", Font.BOLD, 13));
        refresh.setFocusPainted(false);
        refresh.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        refresh.addActionListener(e -> loadEvents());
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(count, BorderLayout.WEST);
        JPanel action = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        action.setOpaque(false);
        action.add(refresh);
        footer.add(action, BorderLayout.EAST);

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);
        content.add(titleBlock, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(footer, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void loadEvents() {
        model.setRowCount(0);
        List<Object[]> events = historyDAO.getRecentEvents();
        for (Object[] event : events) {
            model.addRow(event);
        }
        if (events.isEmpty()) {
            setTitle("LumaGrid | Activity History (no events yet)");
        } else {
            setTitle("LumaGrid | Activity History");
        }
    }
}
