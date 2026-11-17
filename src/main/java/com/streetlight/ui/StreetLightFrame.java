package com.streetlight.ui;

import com.streetlight.dao.StreetLightDAO;
import com.streetlight.model.StreetLight;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StreetLightFrame extends JFrame {

    private JTextField lightCodeField;
    private JTextField locationField;
    private JTextField powerRatingField;
    private JComboBox<String> statusComboBox;

    private JTable streetLightTable;
    private DefaultTableModel tableModel;

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    private final StreetLightDAO streetLightDAO;

    public StreetLightFrame() {

        streetLightDAO = new StreetLightDAO();

        setTitle("Manage Street Lights");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
        loadStreetLights();
    }

    private void createUI() {

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // -------------------------
        // Form Panel
        // -------------------------

        JPanel formPanel = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder("Street Light Details")
        );

        lightCodeField = new JTextField();
        locationField = new JTextField();
        powerRatingField = new JTextField();

        statusComboBox = new JComboBox<>(
                new String[]{"OFF", "ON"}
        );

        formPanel.add(new JLabel("Light Code:"));
        formPanel.add(lightCodeField);

        formPanel.add(new JLabel("Location:"));
        formPanel.add(locationField);

        formPanel.add(new JLabel("Power Rating (W):"));
        formPanel.add(powerRatingField);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusComboBox);

        // -------------------------
        // Buttons
        // -------------------------

        JPanel buttonPanel = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 5)
        );

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        // -------------------------
        // Table
        // -------------------------

        String[] columns = {
                "ID",
                "Light Code",
                "Location",
                "Power (W)",
                "Status",
                "Created At"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(
                    int row,
                    int column
            ) {
                return false;
            }
        };

        streetLightTable = new JTable(tableModel);
        streetLightTable.getColumnModel().getColumn(4)
                .setCellRenderer(new StatusCellRenderer());

        streetLightTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane =
                new JScrollPane(streetLightTable);

        scrollPane.setBorder(
                BorderFactory.createTitledBorder(
                        "Street Lights"
                )
        );

        // -------------------------
        // Add components
        // -------------------------

        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // -------------------------
        // Button Actions
        // -------------------------

        addButton.addActionListener(e -> addStreetLight());

        updateButton.addActionListener(
                e -> updateStreetLight()
        );

        deleteButton.addActionListener(
                e -> deleteStreetLight()
        );

        clearButton.addActionListener(
                e -> clearForm()
        );

        // -------------------------
        // Table Selection
        // -------------------------

        streetLightTable
                .getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {
                        loadSelectedStreetLight();
                    }
                });
    }

    // -------------------------
    // Load Street Lights
    // -------------------------

    private void loadStreetLights() {

        tableModel.setRowCount(0);

        List<StreetLight> lights =
                streetLightDAO.getAllStreetLights();

        for (StreetLight light : lights) {

            tableModel.addRow(
                    new Object[]{
                            light.getLightId(),
                            light.getLightCode(),
                            light.getLocation(),
                            light.getPowerRating(),
                            light.getStatus(),
                            light.getCreatedAt()
                    }
            );
        }
    }

    // -------------------------
    // Add Street Light
    // -------------------------

    private void addStreetLight() {

        try {

            String lightCode =
                    lightCodeField.getText().trim();

            String location =
                    locationField.getText().trim();

            int powerRating =
                    Integer.parseInt(
                            powerRatingField.getText().trim()
                    );

            String status =
                    statusComboBox
                            .getSelectedItem()
                            .toString();

            if (lightCode.isEmpty()
                    || location.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            StreetLight light =
                    new StreetLight();

            light.setLightCode(lightCode);
            light.setLocation(location);
            light.setPowerRating(powerRating);
            light.setStatus(status);

            boolean success =
                    streetLightDAO
                            .addStreetLight(light);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Street light added successfully!"
                );

                clearForm();
                loadStreetLights();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add street light.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Power rating must be a number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // -------------------------
    // Load Selected Light
    // -------------------------

    private void loadSelectedStreetLight() {

        int selectedRow =
                streetLightTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        lightCodeField.setText(
                tableModel
                        .getValueAt(selectedRow, 1)
                        .toString()
        );

        locationField.setText(
                tableModel
                        .getValueAt(selectedRow, 2)
                        .toString()
        );

        powerRatingField.setText(
                tableModel
                        .getValueAt(selectedRow, 3)
                        .toString()
        );

        statusComboBox.setSelectedItem(
                tableModel
                        .getValueAt(selectedRow, 4)
                        .toString()
        );
    }

    // -------------------------
    // Update Street Light
    // -------------------------

    private void updateStreetLight() {

        int selectedRow =
                streetLightTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a street light first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        try {

            int lightId =
                    Integer.parseInt(
                            tableModel
                                    .getValueAt(
                                            selectedRow,
                                            0
                                    )
                                    .toString()
                    );

            String lightCode =
                    lightCodeField.getText().trim();

            String location =
                    locationField.getText().trim();

            int powerRating =
                    Integer.parseInt(
                            powerRatingField
                                    .getText()
                                    .trim()
                    );

            String status =
                    statusComboBox
                            .getSelectedItem()
                            .toString();

            if (lightCode.isEmpty()
                    || location.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            StreetLight light =
                    new StreetLight();

            light.setLightId(lightId);
            light.setLightCode(lightCode);
            light.setLocation(location);
            light.setPowerRating(powerRating);
            light.setStatus(status);

            boolean success =
                    streetLightDAO
                            .updateStreetLight(light);

            if (success) {

                JOptionPane.showMessageDialog(
                        this,
                        "Street light updated successfully!"
                );

                clearForm();
                loadStreetLights();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update street light.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Power rating must be a number.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // -------------------------
    // Delete Street Light
    // -------------------------

    private void deleteStreetLight() {

        int selectedRow =
                streetLightTable.getSelectedRow();

        if (selectedRow == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a street light first.",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int lightId =
                Integer.parseInt(
                        tableModel
                                .getValueAt(
                                        selectedRow,
                                        0
                                )
                                .toString()
                );

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to delete this street light?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success =
                streetLightDAO
                        .deleteStreetLight(lightId);

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Street light deleted successfully!"
            );

            clearForm();
            loadStreetLights();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to delete street light.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // -------------------------
    // Clear Form
    // -------------------------

    private void clearForm() {

        lightCodeField.setText("");
        locationField.setText("");
        powerRatingField.setText("");

        statusComboBox.setSelectedItem("OFF");

        streetLightTable.clearSelection();
    }
}
