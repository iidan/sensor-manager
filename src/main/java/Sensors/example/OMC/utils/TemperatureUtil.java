package Sensors.example.OMC.utils;

import java.util.concurrent.ThreadLocalRandom;

public class TemperatureUtil {

    public static double getRandomTemperature() {
        return ThreadLocalRandom.current().nextDouble(-20.0, 50.0);
    }

}
