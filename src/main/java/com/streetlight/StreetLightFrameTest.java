package com.streetlight;

import com.streetlight.ui.StreetLightFrame;

import javax.swing.SwingUtilities;

public class StreetLightFrameTest {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            StreetLightFrame frame =
                    new StreetLightFrame();

            frame.setVisible(true);
        });
    }
}