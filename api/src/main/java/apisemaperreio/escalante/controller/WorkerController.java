package apisemaperreio.escalante.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.model.Worker;
import apisemaperreio.escalante.service.WorkerService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/worker")
@RestController
public class WorkerController {

    private final WorkerService service;

    @GetMapping
    public ResponseEntity<Object> getAllPositions() {
        try {
            var workers = service.getAll();
            return new ResponseEntity<>(workers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/drivers")
    public ResponseEntity<Object> getAvailableDrivers() {
        try {
            List<Worker> workers = service.getAvailableDrivers(LocalDate.parse("2024-09-14"));
            return new ResponseEntity<>(workers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<Object> getAvailableWorkers() {
        try {
            List<Worker> workers = service.getAvailableWorkers(LocalDate.parse("2024-09-14"), 5,false);
            return new ResponseEntity<>(workers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
