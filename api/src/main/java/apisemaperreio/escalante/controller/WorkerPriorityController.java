package apisemaperreio.escalante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.model.WorkerPriority;
import apisemaperreio.escalante.service.WorkerPriorityService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@RequestMapping("/worker/priority")
@RestController
public class WorkerPriorityController {

    private final WorkerPriorityService service;

    @GetMapping
    public ResponseEntity<Object> getAllRoles() {
        try {
            List<WorkerPriority> priorities = service.getAll();
            return new ResponseEntity<>(priorities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    

}
