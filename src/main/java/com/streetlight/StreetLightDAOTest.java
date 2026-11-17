package com.streetlight;

import com.streetlight.dao.StreetLightDAO;
import com.streetlight.model.StreetLight;

public class StreetLightDAOTest {

    public static void main(String[] args) {

        StreetLightDAO dao = new StreetLightDAO();

        // UPDATE TEST
        System.out.println("----- UPDATE TEST -----");

        StreetLight light = new StreetLight();

        light.setLightId(4);
        light.setLightCode("SL004");
        light.setLocation("Market Main Road");
        light.setPowerRating(180);
        light.setStatus("ON");

        boolean updated = dao.updateStreetLight(light);

        System.out.println("Street light updated: " + updated);

        // READ AFTER UPDATE
        System.out.println("\n----- READ AFTER UPDATE -----");

        dao.getAllStreetLights().forEach(item ->
                System.out.println(
                        item.getLightId()
                        + " | "
                        + item.getLightCode()
                        + " | "
                        + item.getLocation()
                        + " | "
                        + item.getPowerRating()
                        + "W | "
                        + item.getStatus()
                )
        );

        // DELETE TEST
        System.out.println("\n----- DELETE TEST -----");

        boolean deleted = dao.deleteStreetLight(4);

        System.out.println("Street light deleted: " + deleted);

        // READ AFTER DELETE
        System.out.println("\n----- READ AFTER DELETE -----");

        dao.getAllStreetLights().forEach(item ->
                System.out.println(
                        item.getLightId()
                        + " | "
                        + item.getLightCode()
                        + " | "
                        + item.getLocation()
                        + " | "
                        + item.getPowerRating()
                        + "W | "
                        + item.getStatus()
                )
        );
    }
}