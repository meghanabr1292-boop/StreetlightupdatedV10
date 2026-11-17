package com.streetlight.ui;

import com.streetlight.model.User;
import com.streetlight.service.LoginService;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField usernameField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final LoginService loginService = new LoginService();
    private static final Color NAVY = new Color(12, 30, 48);
    private static final Color TEAL = new Color(36, 193, 174);

    public LoginFrame() {
        setTitle("Sign in | LumaGrid");
        setSize(790, 520);
        setMinimumSize(new Dimension(700, 480));
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
        header.setBorder(BorderFactory.createEmptyBorder(26, 38, 24, 38));
        JLabel brand = new JLabel("LUMAGRID  /  ACCOUNT ACCESS");
        brand.setForeground(TEAL);
        brand.setFont(new Font("SansSerif", Font.BOLD, 12));
        JLabel title = new JLabel("Welcome back");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        header.add(brand);
        header.add(Box.createVerticalStrut(12));
        header.add(title);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(245, 248, 250));
        form.setBorder(BorderFactory.createEmptyBorder(26, 40, 18, 40));
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0; c.gridy = 0; c.weightx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 7, 0);
        form.add(fieldLabel("Username"), c);
        c.gridy++; c.insets = new Insets(0, 0, 18, 0);
        form.add(usernameField, c);
        c.gridy++; c.insets = new Insets(0, 0, 7, 0);
        form.add(fieldLabel("Password"), c);
        c.gridy++; c.insets = new Insets(0, 0, 4, 0);
        form.add(passwordField, c);
        styleField(usernameField);
        styleField(passwordField);
        JCheckBox showPassword = new JCheckBox("Show password");
        styleCheckBox(showPassword);
        c.gridy++; c.insets = new Insets(0, 0, 0, 0);
        form.add(showPassword, c);
        char passwordEcho = passwordField.getEchoChar();
        showPassword.addActionListener(e -> passwordField.setEchoChar(
                showPassword.isSelected() ? (char) 0 : passwordEcho));

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        bottom.setBackground(new Color(245, 248, 250));
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 40, 28, 40));
        JButton login = new JButton("Sign in");
        login.setBackground(TEAL);
        login.setForeground(NAVY);
        login.setFont(new Font("SansSerif", Font.BOLD, 14));
        login.setFocusPainted(false);
        login.setBorder(BorderFactory.createEmptyBorder(11, 23, 11, 23));
        JButton register = new JButton("Create an account");
        register.setBorderPainted(false);
        register.setContentAreaFilled(false);
        bottom.add(login);
        bottom.add(register);

        JPanel content = new JPanel(new BorderLayout(12, 0));
        content.setBackground(new Color(245, 248, 250));
        content.add(form, BorderLayout.CENTER);
        content.add(cityArtwork(), BorderLayout.EAST);
        root.add(header, BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);
        setContentPane(root);
        login.addActionListener(e -> login());
        passwordField.addActionListener(e -> login());
        register.addActionListener(e -> { dispose(); new RegisterFrame().setVisible(true); });
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(NAVY);
        return label;
    }

    private void styleField(JComponent field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 15));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(205, 216, 224)),
                BorderFactory.createEmptyBorder(10, 11, 10, 11)));
    }

    private void styleCheckBox(JCheckBox checkBox) {
        checkBox.setOpaque(false);
        checkBox.setForeground(new Color(94, 115, 130));
        checkBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        checkBox.setFocusPainted(false);
    }

    private JPanel cityArtwork() {
        JPanel artwork = new JPanel(new BorderLayout());
        artwork.setBackground(NAVY);
        artwork.setPreferredSize(new Dimension(235, 330));
        artwork.add(new WelcomeFrame.CityScenePanel(), BorderLayout.CENTER);
        return artwork;
    }

    private void login() {
        User user = loginService.login(usernameField.getText().trim(), new String(passwordField.getPassword()));
        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "We couldn’t sign you in. Check your details and try again.",
                    "Sign in failed", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dispose();
        new DashboardFrame(user.getUsername()).setVisible(true);
    }
}
