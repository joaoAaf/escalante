package apisemaperreio.escalante.controller;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.service.ScheduledWorkerService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/schedule")
@RestController
public class ScheduledWorkerController {

    private final ScheduledWorkerService service;
    
    // Para Testes (Excluir depois)
    LocalDate date = LocalDate.parse("2024-09-09");

    @GetMapping("/drivers")
    public ResponseEntity<Object> getDrivers() {
        try {
            var scheduledWorkers = service.getDrivers(date);
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Object> getTest() {
        try {
            var scheduledWorkers = service.selectWorker(date);
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
