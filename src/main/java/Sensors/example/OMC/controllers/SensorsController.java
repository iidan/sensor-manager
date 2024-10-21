package Sensors.example.OMC.controllers;

import Sensors.example.OMC.managers.SensorManager;
import Sensors.example.OMC.objects.Sensor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/sensor")
@Slf4j
@AllArgsConstructor
public class SensorsController {

    private final SensorManager sensorManager;

    @PostMapping("/addSensor")
    public @ResponseBody void addSensor(@RequestBody Sensor sensor) {
        try {
            sensorManager.addSensor(sensor);
            log.info("Sensor added successfully: {}", sensor);
        } catch (Exception e) {
            log.error("Unable to add new sensor sensorId: %s". formatted(sensor.getId()), e);
        }
    }

    @PostMapping("/removeSensor")
    public @ResponseBody void removeSensor(@RequestBody Sensor sensor) {
        try {
            sensorManager.removeSensor(sensor.getId()); // Use the ID to remove the sensor
        } catch (Exception e) {
            log.error("Unable to remove sensor sensorId: %s". formatted(sensor.getId()), e);
        }
    }
}