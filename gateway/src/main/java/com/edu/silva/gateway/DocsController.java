package com.edu.silva.gateway;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/docs")
public class DocsController {

    private final DiscoveryClient discoveryClient;

    public DocsController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/services")
    public List<String> services() {
        return discoveryClient.getServices();
    }

    @GetMapping("/instances/{serviceId}")
    public List<ServiceInstance> instances(@PathVariable String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

    @GetMapping("/openapi/{service}")
    public ResponseEntity<?> proxy(@PathVariable String service) {
        try {
            String url = "http://localhost:8080/" + service + "/v3/api-docs";

            RestTemplate rest = new RestTemplate();
            Object json = rest.getForObject(url, Object.class);

            return ResponseEntity.ok(json);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch docs from " + service);
        }
    }
}
