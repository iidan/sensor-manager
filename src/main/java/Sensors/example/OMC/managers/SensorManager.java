package Sensors.example.OMC.managers;

import Sensors.example.OMC.objects.Sensor;

public interface SensorManager {

    void addSensor(Sensor sensor);

    void removeSensor(int id);
}
