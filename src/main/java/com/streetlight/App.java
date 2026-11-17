package com.streetlight;

import com.streetlight.ui.WelcomeFrame;
import com.streetlight.service.AutomaticControlScheduler;
import com.streetlight.util.DatabaseInitializer;

import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();
        AutomaticControlScheduler.start();

        SwingUtilities.invokeLater(() -> {

            new WelcomeFrame().setVisible(true);
        });
    }
}
