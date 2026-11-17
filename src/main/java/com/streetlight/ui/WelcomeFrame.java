package com.streetlight.ui;

import javax.swing.*;
import java.awt.*;

/** Branded entry page for the desktop application. */
public class WelcomeFrame extends JFrame {
    private static final Color NAVY = new Color(12, 30, 48);
    private static final Color TEAL = new Color(36, 193, 174);

    public WelcomeFrame() {
        setTitle("LumaGrid | Smart Street Light System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 590);
        setMinimumSize(new Dimension(820, 520));
        setLocationRelativeTo(null);
        createUI();
    }

    private void createUI() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(NAVY);
        root.setBorder(BorderFactory.createEmptyBorder(28, 42, 28, 42));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel brand = new JLabel("●  LUMAGRID");
        brand.setForeground(TEAL);
        brand.setFont(new Font("SansSerif", Font.BOLD, 18));
        JLabel descriptor = new JLabel("SMART CITY LIGHTING");
        descriptor.setForeground(new Color(160, 181, 196));
        descriptor.setFont(new Font("SansSerif", Font.BOLD, 11));
        top.add(brand, BorderLayout.WEST);
        top.add(descriptor, BorderLayout.EAST);

        JPanel center = new JPanel(new BorderLayout(12, 0));
        center.setOpaque(false);
        center.setBorder(BorderFactory.createEmptyBorder(54, 32, 25, 4));

        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));

        JLabel eyebrow = label("CONNECTED STREETS. BRIGHTER FUTURES.", 12, TEAL, Font.BOLD);
        JLabel title = label("Street Light", 44, Color.WHITE, Font.BOLD);
        JLabel title2 = label("Monitoring System", 44, Color.WHITE, Font.BOLD);
        JLabel subtitle = label("<html><div style='width:420px'>A smarter way to monitor, manage, and automate your city’s lighting.</div></html>", 16,
                new Color(188, 204, 216), Font.PLAIN);
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title2.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        copy.add(eyebrow);
        copy.add(Box.createVerticalStrut(14));
        copy.add(title);
        copy.add(title2);
        copy.add(Box.createVerticalStrut(14));
        copy.add(subtitle);
        copy.add(Box.createVerticalStrut(30));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actions.setOpaque(false);
        JButton login = button("Sign in", TEAL, NAVY);
        JButton register = button("Create account", new Color(30, 52, 69), Color.WHITE);
        actions.add(login);
        actions.add(register);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        copy.add(actions);
        copy.setAlignmentX(Component.LEFT_ALIGNMENT);
        center.add(copy, BorderLayout.CENTER);
        center.add(new CityScenePanel(), BorderLayout.EAST);

        JLabel footer = label("LIGHTING THE WAY TO A MORE EFFICIENT CITY", 10,
                new Color(126, 151, 169), Font.BOLD);
        root.add(top, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);
        setContentPane(root);

        login.addActionListener(e -> openLogin());
        register.addActionListener(e -> openRegister());
    }

    private JLabel label(String text, int size, Color color, int style) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", style, size));
        label.setForeground(color);
        return label;
    }

    private JButton button(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(foreground);
        button.setBackground(background);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(13, 24, 13, 24));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void openLogin() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void openRegister() {
        dispose();
        new RegisterFrame().setVisible(true);
    }

    /** Simple code-drawn illustration so the welcome page needs no image assets. */
    static class CityScenePanel extends JPanel {
        CityScenePanel() {
            setOpaque(false);
            setPreferredSize(new Dimension(250, 330));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            double scale = Math.min(w / 250.0, h / 330.0);
            g.translate((w - 250 * scale) / 2.0, (h - 330 * scale) / 2.0);
            g.scale(scale, scale);

            // Warm lamp glow and a quiet night sky.
            g.setColor(new Color(36, 193, 174, 18));
            g.fillOval(58, 20, 175, 175);
            g.setColor(new Color(36, 193, 174, 10));
            g.fillOval(35, 5, 220, 220);

            // Distant city skyline.
            int[] x = {0, 28, 28, 55, 55, 82, 82, 110, 110, 139, 139, 165, 165, 194, 194, 222, 222, 250};
            int[] top = {212, 212, 178, 178, 198, 198, 160, 160, 188, 188, 172, 172, 198, 198, 168, 168, 205, 205};
            for (int i = 0; i < x.length - 1; i += 2) {
                int bx = x[i];
                int bw = x[i + 1] - bx;
                int by = top[i];
                g.setColor(i % 4 == 0 ? new Color(23, 51, 72) : new Color(19, 43, 63));
                g.fillRoundRect(bx, by, bw, 330 - by, 4, 4);
                g.setColor(new Color(99, 171, 174, 105));
                for (int wy = by + 12; wy < 315; wy += 19) {
                    for (int wx = bx + 7; wx < bx + bw - 5; wx += 13) {
                        g.fillRoundRect(wx, wy, 4, 7, 2, 2);
                    }
                }
            }

            // Road and lamp post.
            g.setColor(new Color(41, 67, 85));
            g.fillRect(0, 308, 250, 22);
            g.setColor(new Color(114, 148, 162, 150));
            g.setStroke(new BasicStroke(2f));
            g.drawLine(10, 319, 70, 319);
            g.drawLine(105, 319, 155, 319);
            g.drawLine(190, 319, 240, 319);

            g.setColor(new Color(177, 205, 212));
            g.setStroke(new BasicStroke(5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(177, 304, 177, 92);
            g.drawArc(151, 67, 52, 50, 5, 88);
            g.setStroke(new BasicStroke(7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawLine(176, 72, 210, 72);
            g.setColor(TEAL);
            g.fillRoundRect(199, 68, 28, 10, 5, 5);
            g.setColor(new Color(239, 202, 125, 130));
            g.fillOval(195, 73, 38, 22);
            g.setColor(new Color(36, 193, 174, 150));
            g.setStroke(new BasicStroke(1.5f));
            g.drawOval(188, 61, 48, 42);

            g.dispose();
        }
    }
}
