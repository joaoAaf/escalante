package apisemaperreio.escalante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.model.WorkerPosition;
import apisemaperreio.escalante.service.WorkerPositionService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/worker/position")
@RestController
public class WorkerPositionController {

    private final WorkerPositionService service;

    @GetMapping
    public ResponseEntity<Object> getAllPositions() {
        try {
            List<WorkerPosition> positions = service.getAll();
            return new ResponseEntity<>(positions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    

}
