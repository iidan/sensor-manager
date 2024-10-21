package Sensors.example.OMC.objects;

import Sensors.example.OMC.enums.BuildingFaceEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Sensor {

    private Integer id;                             // Sensor numeric ID
    private long time;                          // Unix timestamp
    private BuildingFaceEnum face;              // Sensor facing direction
    private double temperature;                 // Temperature value in Celsius

    public Sensor(Integer id, long time, BuildingFaceEnum face, double temperature) {
        this.id = id;
        this.time = time;
        this.face = face;
        this.temperature = temperature;
    }
}
