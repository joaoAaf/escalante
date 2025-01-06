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
    LocalDate date = LocalDate.parse("2024-09-12");

    @GetMapping
    public ResponseEntity<Object> getAll() {
        try {
            var scheduledWorkers = service.getAll();
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/workers")
    public ResponseEntity<Object> getWorkers() {
        try {
            var workers = service.getAvailableWorkers(date, 5, false);
            return new ResponseEntity<>(workers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/drivers")
    public ResponseEntity<Object> getDrivers() {
        try {
            var drivers = service.getAvailableDrivers(date);
            return new ResponseEntity<>(drivers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<Object> getTest() {
        try {
            var scheduledWorkers = service.selectWorker(date);
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No workers available", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
