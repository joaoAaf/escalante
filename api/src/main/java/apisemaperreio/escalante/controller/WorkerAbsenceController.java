package apisemaperreio.escalante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.model.WorkerAbsence;
import apisemaperreio.escalante.service.WorkerAbsenceService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/worker/absence")
@RestController
public class WorkerAbsenceController {

    private final WorkerAbsenceService service;

    @GetMapping
    public ResponseEntity<Object> getAllPositions() {
        try {
            List<WorkerAbsence> absences = service.getAll();
            return new ResponseEntity<>(absences, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
