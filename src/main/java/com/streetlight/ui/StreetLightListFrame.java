package com.streetlight.ui;

import com.streetlight.dao.StreetLightDAO;
import com.streetlight.model.StreetLight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/** Read-only overview of all street lights. */
public class StreetLightListFrame extends JFrame {
    private static final Color NAVY = new Color(12, 30, 48);
    private static final Color TEAL = new Color(36, 193, 174);
    private final StreetLightDAO streetLightDAO = new StreetLightDAO();
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Light Code", "Location", "Power (W)", "Status", "Created At"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> statusFilter;
    private JLabel countLabel;

    public StreetLightListFrame() {
        setTitle("LumaGrid | Street Light Network");
        setSize(900, 540);
        setMinimumSize(new Dimension(700, 420));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        createUI();
        loadStreetLights();
    }

    private void createUI() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(new Color(244, 248, 250));
        root.setBorder(BorderFactory.createEmptyBorder(22, 26, 24, 26));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));
        JLabel brand = new JLabel("LUMAGRID  /  NETWORK OVERVIEW");
        brand.setFont(new Font("SansSerif", Font.BOLD, 16));
        brand.setForeground(TEAL);
        header.add(brand, BorderLayout.WEST);

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("Street lights");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(NAVY);
        JLabel subtitle = new JLabel("Read-only view of the lights in your network.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitle.setForeground(new Color(94, 115, 130));
        titleBlock.add(title);
        titleBlock.add(Box.createVerticalStrut(5));
        titleBlock.add(subtitle);

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(222, 231, 236)));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Search code or location");
        searchLabel.setForeground(NAVY);
        searchLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        searchField = new JTextField(20);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 13));
        searchField.setToolTipText("Type a light code or location");
        JLabel statusLabel = new JLabel("Status");
        statusLabel.setForeground(NAVY);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        statusFilter = new JComboBox<>(new String[]{"All statuses", "ON", "OFF"});
        filterPanel.add(searchLabel);
        filterPanel.add(searchField);
        filterPanel.add(statusLabel);
        filterPanel.add(statusFilter);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applyFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { applyFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { applyFilter(); }
        });
        statusFilter.addActionListener(e -> applyFilter());

        JPanel content = new JPanel(new BorderLayout(0, 16));
        content.setOpaque(false);

        JButton refreshButton = new JButton("Refresh list");
        refreshButton.setBackground(TEAL);
        refreshButton.setForeground(NAVY);
        refreshButton.setFont(new Font("SansSerif", Font.BOLD, 13));
        refreshButton.setFocusPainted(false);
        refreshButton.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        refreshButton.addActionListener(e -> loadStreetLights());
        countLabel = new JLabel("Showing 0 lights");
        countLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        countLabel.setForeground(new Color(94, 115, 130));
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.add(countLabel, BorderLayout.WEST);
        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        refreshPanel.setOpaque(false);
        refreshPanel.add(refreshButton);
        footer.add(refreshPanel, BorderLayout.EAST);

        JPanel topContent = new JPanel(new BorderLayout(0, 14));
        topContent.setOpaque(false);
        topContent.add(titleBlock, BorderLayout.NORTH);
        topContent.add(filterPanel, BorderLayout.SOUTH);
        content.add(topContent, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(footer, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        setContentPane(root);
    }

    private void loadStreetLights() {
        tableModel.setRowCount(0);
        List<StreetLight> lights = streetLightDAO.getAllStreetLights();
        for (StreetLight light : lights) {
            tableModel.addRow(new Object[]{
                    light.getLightId(), light.getLightCode(), light.getLocation(),
                    light.getPowerRating(), light.getStatus(), light.getCreatedAt()
            });
        }
        updateCount();
    }

    private void applyFilter() {
        String query = searchField.getText().trim().toLowerCase();
        String selectedStatus = (String) statusFilter.getSelectedItem();
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        if (!query.isEmpty()) {
            filters.add(new RowFilter<>() {
                @Override
                public boolean include(Entry<?, ?> entry) {
                    return entry.getStringValue(1).toLowerCase().contains(query)
                            || entry.getStringValue(2).toLowerCase().contains(query);
                }
            });
        }
        if (selectedStatus != null && !"All statuses".equals(selectedStatus)) {
            filters.add(RowFilter.regexFilter("^" + Pattern.quote(selectedStatus) + "$", 4));
        }
        sorter.setRowFilter(filters.isEmpty() ? null : RowFilter.andFilter(filters));
        updateCount();
    }

    private void updateCount() {
        if (countLabel != null && table != null) {
            countLabel.setText("Showing " + table.getRowCount() + " of " + tableModel.getRowCount() + " lights");
        }
    }
}
