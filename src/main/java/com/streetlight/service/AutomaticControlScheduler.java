package com.streetlight.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** Keeps database light statuses aligned with the local day/night schedule while the app runs. */
public final class AutomaticControlScheduler {
    private static final ScheduledExecutorService EXECUTOR =
            Executors.newSingleThreadScheduledExecutor(task -> {
                Thread thread = new Thread(task, "street-light-auto-control");
                thread.setDaemon(true);
                return thread;
            });

    private AutomaticControlScheduler() { }

    public static void start() {
        EXECUTOR.scheduleAtFixedRate(() -> {
            try {
                new AutomaticControlService().controlLightsAutomatically();
            } catch (RuntimeException exception) {
                System.err.println("Automatic street light update could not run: " + exception.getMessage());
            }
        }, 0, 1, TimeUnit.MINUTES);
    }
}
