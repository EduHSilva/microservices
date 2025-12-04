package com.edu.silva.crm.controllers;


import com.edu.silva.crm.domain.entities.Client;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PingController {
    @GetMapping("/")
    public String index() {
        return "CRM service on";
    }

    @GetMapping("/clients")
    public List<Client> getClients(@RequestHeader("X-User-ID") String userId) {
        System.out.println(userId);
        return new ArrayList<>();
    }
}
