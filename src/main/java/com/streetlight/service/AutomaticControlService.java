package com.streetlight.service;

import com.streetlight.dao.StreetLightDAO;

import java.time.LocalTime;

public class AutomaticControlService {

    private final StreetLightDAO streetLightDAO;

    public AutomaticControlService() {
        streetLightDAO = new StreetLightDAO();
    }

    public String controlLightsAutomatically() {

        LocalTime currentTime = LocalTime.now();

        LocalTime dayStart = LocalTime.of(6, 0);
        LocalTime nightStart = LocalTime.of(18, 0);

        String status;

        if (!currentTime.isBefore(dayStart)
                && currentTime.isBefore(nightStart)) {

            status = "OFF";

        } else {

            status = "ON";
        }

        boolean updated =
                streetLightDAO.updateAllLightStatus(status);

        if (updated) {
            return status;
        }

        return null;
    }
}