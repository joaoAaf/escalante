package apisemaperreio.escalante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.model.ScheduledWorker;
import apisemaperreio.escalante.service.ScheduledWorkerService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/schedule")
@RestController
public class ScheduledWorkerController {

    private final ScheduledWorkerService service;

    @GetMapping
    public ResponseEntity<Object> getAllPositions() {
        try {
            List<ScheduledWorker> scheduledWorkers = service.getAll();
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
