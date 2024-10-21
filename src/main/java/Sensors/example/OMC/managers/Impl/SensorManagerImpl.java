package Sensors.example.OMC.managers.Impl;

import Sensors.example.OMC.api.MonitorApiService;
import Sensors.example.OMC.enums.BuildingFaceEnum;
import Sensors.example.OMC.managers.SensorManager;
import Sensors.example.OMC.objects.Sensor;
import Sensors.example.OMC.utils.TemperatureUtil;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class SensorManagerImpl implements SensorManager {

    private final Map<Integer, Sensor> sensors = new ConcurrentHashMap<>();
    private static final int SENSORS_PER_FACE = 2500; // can set in config or in the admin panel
    private final BuildingFaceEnum[] FACES = BuildingFaceEnum.values();
    private final MonitorApiService monitorApiService;

    @PostConstruct
    public void init() {
        try {
            List<Sensor> sensorsList = monitorApiService.getSensorList();

            if (sensorsList != null) {
                sensorsList.forEach(sensor -> sensors.put(sensor.getId(), sensor));
            }

            if (sensorsList == null || sensorsList.isEmpty() || sensorsList.size() < SENSORS_PER_FACE * FACES.length) {
                int sensorId = 0;
                for (BuildingFaceEnum face : FACES) {
                    for (int i = 0; i < SENSORS_PER_FACE; i++) {
                        Sensor sensor = new Sensor(sensorId, System.currentTimeMillis(), face, TemperatureUtil.getRandomTemperature());
                        sensors.put(sensorId, sensor);
                        sensorId++;
                    }
                }
            }

            startSendingSensorData();

        } catch (Exception e) {
            log.error("Unable to init sensors:", e);
        }
    }

    @Override
    public void addSensor(Sensor sensor) {
        if (sensors.containsKey(sensor.getId())) {
            System.out.println("Sensor already exists : " + sensor.getId());
        } else {
            sensor.setTime(System.currentTimeMillis());
            sensors.put(sensor.getId(), sensor);
            System.out.println("Sensor added: " + sensor.getId());
        }
    }

    @Override
    public void removeSensor(int id) {
        if (sensors.containsKey(id)) {

            sensors.remove(id); // Remove sensor by ID
            System.out.println("Sensor removed: " + id);
        }
    }

    private void startSendingSensorData() {
        Runnable task = () -> {
            while (true) {
                // Update temperature for all sensors
                for (Sensor sensor : sensors.values()) {
                    sensor.setTemperature(TemperatureUtil.getRandomTemperature());
                }

                // Send batch data to monitor service
                monitorApiService.sendBatchedDataToMonitor(sensors.values());

                // Sleep for 1 second before the next batch
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        };

        // Run the task in a separate thread to avoid blocking the main thread
        new Thread(task).start();
    }
}
