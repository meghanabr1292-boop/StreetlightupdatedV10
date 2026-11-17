package com.streetlight.ui;

import com.streetlight.service.RegistrationService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JPasswordField confirmField = new JPasswordField(20);
    private final RegistrationService registrationService = new RegistrationService();
    private static final Color NAVY = new Color(12, 30, 48);
    private static final Color TEAL = new Color(36, 193, 174);

    public RegisterFrame() {
        setTitle("Create account | LumaGrid");
        setSize(810, 600);
        setMinimumSize(new Dimension(720, 540));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createUI();
    }

    private void createUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(new Color(245, 248, 250));
        JPanel header = new JPanel();
        header.setBackground(NAVY);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(24, 38, 22, 38));
        JLabel brand = new JLabel("LUMAGRID  /  NEW ACCOUNT");
        brand.setForeground(TEAL);
        brand.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel title = new JLabel("Join your lighting team");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        header.add(brand);
        header.add(Box.createVerticalStrut(10));
        header.add(title);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 248, 250));
        form.setBorder(BorderFactory.createEmptyBorder(18, 40, 8, 40));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        addField(form, c, "Username (at least 3 characters)", usernameField);
        c.gridy += 2;
        addField(form, c, "Password (at least 6 characters)", passwordField);
        c.gridy += 2;
        addField(form, c, "Confirm password", confirmField);
        JCheckBox showPassword = new JCheckBox("Show passwords");
        showPassword.setOpaque(false);
        showPassword.setForeground(new Color(94, 115, 130));
        showPassword.setFont(new Font("SansSerif", Font.PLAIN, 12));
        showPassword.setFocusPainted(false);
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        form.add(showPassword, c);
        char passwordEcho = passwordField.getEchoChar();
        char confirmEcho = confirmField.getEchoChar();
        showPassword.addActionListener(e -> {
            char echo = showPassword.isSelected() ? (char) 0 : passwordEcho;
            passwordField.setEchoChar(echo);
            confirmField.setEchoChar(showPassword.isSelected() ? (char) 0 : confirmEcho);
        });

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bottom.setBackground(new Color(245, 248, 250));
        bottom.setBorder(BorderFactory.createEmptyBorder(4, 40, 24, 40));
        JButton create = new JButton("Create account");
        create.setBackground(TEAL);
        create.setForeground(NAVY);
        create.setFont(new Font("SansSerif", Font.BOLD, 14));
        create.setFocusPainted(false);
        create.setBorder(BorderFactory.createEmptyBorder(11, 20, 11, 20));
        JButton back = new JButton("Back to sign in");
        back.setBorderPainted(false);
        back.setContentAreaFilled(false);
        bottom.add(create);
        bottom.add(back);

        JPanel content = new JPanel(new BorderLayout(12, 0));
        content.setBackground(new Color(245, 248, 250));
        content.add(form, BorderLayout.CENTER);
        JPanel artwork = new JPanel(new BorderLayout());
        artwork.setBackground(NAVY);
        artwork.setPreferredSize(new Dimension(235, 330));
        artwork.add(new WelcomeFrame.CityScenePanel(), BorderLayout.CENTER);
        content.add(artwork, BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
        create.addActionListener(e -> register());
        back.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
    }

    private void addField(JPanel form, GridBagConstraints c, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(NAVY);
        c.insets = new Insets(0, 0, 6, 0);
        form.add(label, c);
        c.gridy++;
        c.insets = new Insets(0, 0, 13, 0);
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 216, 224)),
                BorderFactory.createEmptyBorder(9, 11, 9, 11)));
        form.add(field, c);
    }

    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmation = new String(confirmField.getPassword());
        if (username.length() < 3) {
            showError("Username must contain at least 3 characters.");
            return;
        }
        if (password.length() < 6) {
            showError("Password must contain at least 6 characters.");
            return;
        }
        if (!password.equals(confirmation)) {
            showError("The passwords do not match.");
            return;
        }
        if (registrationService.register(username, password)) {
            JOptionPane.showMessageDialog(this, "Account created. You can now sign in.",
                    "Registration complete", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new LoginFrame().setVisible(true);
        } else {
            showError("Could not create the account. The username may already exist, or the database is unavailable.");
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Check your details", JOptionPane.WARNING_MESSAGE);
    }
}
