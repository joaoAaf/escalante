package apisemaperreio.escalante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.service.ScheduleTypeService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/schedule/type")
@RestController
public class ScheduleTypeController {

    private final ScheduleTypeService service;
    
    @GetMapping
    public ResponseEntity<Object> getAllScheduleType() {
        try {
            List<ScheduleType> scheduleType = service.getAll();
            return new ResponseEntity<>(scheduleType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
