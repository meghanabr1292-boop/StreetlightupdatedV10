package com.streetlight;

import com.streetlight.service.AutomaticControlService;

public class AutomaticControlTest {

    public static void main(String[] args) {

        AutomaticControlService service =
                new AutomaticControlService();

        String status =
                service.controlLightsAutomatically();

        if (status != null) {

            System.out.println(
                    "Automatic control executed successfully!"
            );

            System.out.println(
                    "Current light status: " + status
            );

        } else {

            System.out.println(
                    "Automatic control failed!"
            );
        }
    }
}