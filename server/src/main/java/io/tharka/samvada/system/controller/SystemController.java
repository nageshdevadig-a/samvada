package io.tharka.samvada.system.controller;

import io.tharka.samvada.system.dto.SystemStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

    @GetMapping("/warmup")
    public ResponseEntity<SystemStatus> coldStart() {
        return ResponseEntity.status(HttpStatus.OK).body(new SystemStatus("UP", "System is up and running"));
    }

}
