package com.streetlight.ui;

import javax.swing.*;
import com.streetlight.dao.StreetLightDAO;
import com.streetlight.model.StreetLight;
import com.streetlight.service.AutomaticControlService;

import java.awt.*;
import java.util.List;

public class DashboardFrame extends JFrame {

    private static final Color NAVY = new Color(12, 30, 48);
    private static final Color TEAL = new Color(36, 193, 174);

    private JPanel contentPanel;
    private JPanel summaryPanel;
    private Timer dashboardRefreshTimer;

    public DashboardFrame(String username) {
        setTitle("LumaGrid | Dashboard");
        setSize(980, 800);
        setMinimumSize(new Dimension(820, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createUI(username);
    }

    private void createUI(String username) {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 24));
        mainPanel.setBackground(new Color(244, 248, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(22, 28, 28, 28));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(NAVY);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel titleLabel = new JLabel("LUMAGRID  /  CONTROL CENTER");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 17));
        titleLabel.setForeground(TEAL);

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + username + "   •   SYSTEM ONLINE");
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        welcomeLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel("Lighting operations");
        heading.setFont(new Font("SansSerif", Font.BOLD, 24));
        heading.setForeground(NAVY);

        JLabel caption = new JLabel(
                "Manage your network and keep every street connected.");
        caption.setFont(new Font("SansSerif", Font.PLAIN, 14));
        caption.setForeground(new Color(94, 115, 130));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        caption.setAlignmentX(Component.LEFT_ALIGNMENT);
        titleBlock.add(heading);
        titleBlock.add(Box.createVerticalStrut(5));
        titleBlock.add(caption);

        summaryPanel = createSummaryPanel();

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 16, 16));
        buttonPanel.setOpaque(false);

        JButton manageLightsButton = new JButton("Manage Street Lights");
        JButton viewLightsButton = new JButton("View Street Lights");
        JButton automaticControlButton = new JButton("Automatic ON/OFF");
        JButton activityHistoryButton = new JButton("Activity History");
        JButton scheduleButton = new JButton("Day / Night Schedule");
        JButton logoutButton = new JButton("Logout");

        styleAction(manageLightsButton, "01  /  ASSETS",
                "Add, update, or remove lights");
        styleAction(viewLightsButton, "02  /  NETWORK",
                "Review your street light list");
        styleAction(automaticControlButton, "03  /  AUTOMATION",
                "Run time-based light control");
        styleAction(activityHistoryButton, "04  /  HISTORY",
                "Review recent light changes");
        styleAction(scheduleButton, "05  /  SCHEDULE",
                "View day and night switch times");
        styleAction(logoutButton, "06  /  SESSION",
                "Sign out of LumaGrid");

        buttonPanel.add(manageLightsButton);
        buttonPanel.add(viewLightsButton);
        buttonPanel.add(automaticControlButton);
        buttonPanel.add(activityHistoryButton);
        buttonPanel.add(scheduleButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        titleBlock.setAlignmentX(Component.LEFT_ALIGNMENT);
        summaryPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(titleBlock);
        contentPanel.add(Box.createVerticalStrut(16));
        contentPanel.add(summaryPanel);
        contentPanel.add(Box.createVerticalStrut(18));
        contentPanel.add(buttonPanel);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel);

        manageLightsButton.addActionListener(e ->
                new StreetLightFrame().setVisible(true));

        viewLightsButton.addActionListener(e ->
                new StreetLightListFrame().setVisible(true));

        automaticControlButton.addActionListener(e -> automaticControl());

        activityHistoryButton.addActionListener(e ->
                new StreetLightHistoryFrame().setVisible(true));

        scheduleButton.addActionListener(e -> showScheduleInfo());
        logoutButton.addActionListener(e -> logout());

        dashboardRefreshTimer = new Timer(60_000, e -> refreshSummaryPanel());
        dashboardRefreshTimer.start();
    }

    private JPanel createSummaryPanel() {
        List<StreetLight> lights = new StreetLightDAO().getAllStreetLights();

        long onCount = lights.stream()
                .filter(light -> "ON".equalsIgnoreCase(light.getStatus()))
                .count();

        long offCount = lights.stream()
                .filter(light -> "OFF".equalsIgnoreCase(light.getStatus()))
                .count();

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel cards = new JPanel(new GridLayout(1, 3, 14, 0));
        cards.setOpaque(false);

        cards.add(summaryCard("NETWORK LIGHTS",
                String.valueOf(lights.size()),
                "Registered in your system", NAVY));
        cards.add(summaryCard("LIGHTS ON",
                String.valueOf(onCount),
                "Currently marked ON", new Color(21, 143, 112)));
        cards.add(summaryCard("LIGHTS OFF",
                String.valueOf(offCount),
                "Currently marked OFF", new Color(190, 70, 70)));

        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));

        int maximum = Math.max(1, lights.size());

        JProgressBar onBar = new JProgressBar(0, maximum);
        onBar.setValue((int) onCount);
        onBar.setString(onCount + " lights ON");
        styleStatusBar(onBar, new Color(21, 143, 112));

        JProgressBar offBar = new JProgressBar(0, maximum);
        offBar.setValue((int) offCount);
        offBar.setString(offCount + " lights OFF");
        styleStatusBar(offBar, new Color(190, 70, 70));

        JPanel bars = new JPanel(new GridLayout(1, 2, 14, 0));
        bars.setOpaque(false);
        bars.add(onBar);
        bars.add(offBar);
        bars.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));

        panel.add(cards);
        panel.add(Box.createVerticalStrut(10));
        panel.add(bars);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 136));

        return panel;
    }

    private void styleStatusBar(JProgressBar bar, Color color) {
        bar.setStringPainted(true);
        bar.setForeground(color);
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createLineBorder(
                new Color(222, 231, 236)));
        bar.setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    private JPanel summaryCard(
            String heading, String value, String detail, Color accent) {

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel headingLabel = new JLabel(heading);
        headingLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        headingLabel.setForeground(new Color(94, 115, 130));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        valueLabel.setForeground(accent);

        JLabel detailLabel = new JLabel(detail);
        detailLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        detailLabel.setForeground(new Color(110, 128, 140));

        card.add(headingLabel);
        card.add(Box.createVerticalStrut(1));
        card.add(valueLabel);
        card.add(detailLabel);

        return card;
    }

    private void refreshSummaryPanel() {
        int position = contentPanel.getComponentZOrder(summaryPanel);
        JPanel updatedSummary = createSummaryPanel();
        updatedSummary.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.remove(summaryPanel);
        contentPanel.add(updatedSummary, position);
        summaryPanel = updatedSummary;
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    @Override
    public void dispose() {
        if (dashboardRefreshTimer != null) {
            dashboardRefreshTimer.stop();
        }
        super.dispose();
    }

    private void styleAction(JButton button, String eyebrow, String detail) {
        button.setLayout(new BorderLayout(0, 8));
        button.setText("<html><div style='font-size:11px;color:#24c1ae;"
                + "font-weight:bold'>" + eyebrow
                + "</div><div style='font-size:17px;font-weight:bold;"
                + "color:#0c1e30;margin-top:9px'>"
                + button.getText()
                + "</div><div style='font-size:12px;color:#5e7382;"
                + "margin-top:5px'>" + detail + "</div></html>");

        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 231, 236)),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void logout() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }

    private void automaticControl() {
        AutomaticControlService service = new AutomaticControlService();
        String status = service.controlLightsAutomatically();

        if (status != null) {
            refreshSummaryPanel();
            JOptionPane.showMessageDialog(
                    this,
                    "Automatic control executed successfully!\n"
                            + "Current time-based status: " + status,
                    "Automatic Control",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Automatic control failed!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showScheduleInfo() {
        JOptionPane.showMessageDialog(
                this,
                "Automatic schedule (computer local time):\n\n"
                        + "ON: 6:00 PM to 6:00 AM\n"
                        + "OFF: 6:00 AM to 6:00 PM\n\n"
                        + "The schedule updates saved statuses while the app is open.\n"
                        + "It does not control physical lamp hardware.",
                "Day / Night Schedule",
                JOptionPane.INFORMATION_MESSAGE);
    }
}