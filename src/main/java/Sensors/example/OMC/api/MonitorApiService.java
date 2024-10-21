package Sensors.example.OMC.api;

import Sensors.example.OMC.objects.Sensor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class MonitorApiService {
    WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:7889")
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
            .build();

    public void sendBatchedDataToMonitor(Collection<Sensor> sensors) {
        log.info("Sending data for {} sensors", sensors.size());

        webClient.post()
                .uri("/sensorMonitor/data/batch")
                .bodyValue(sensors)  // Send the entire sensor list as JSON
                .retrieve()
                .bodyToMono(String.class)
                .subscribe(response -> {
                    log.info("Batch data sent successfully: {}", response);
                }, error -> {
                    log.error("Error sending batch data: {}", error.getMessage());
                });
    }

    public List<Sensor> getSensorList() {
        try {
            List<Sensor> sensors = webClient.post()
                    .uri("/sensorMonitor/getSensors")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Sensor>>() {
                    })
                    .block(); // Block until result is available (synchronous)

            // Log success message with the number of sensors retrieved
            log.info("Successfully retrieved {} sensors", sensors != null ? sensors.size() : 0);
            return sensors;
        } catch (Exception e) {
            // Log error message
            log.error("Failed to retrieve sensors: {}", e.getMessage(), e);
        }
        return null;
    }
}