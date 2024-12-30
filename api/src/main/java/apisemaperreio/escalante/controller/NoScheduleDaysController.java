package apisemaperreio.escalante.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.model.NoScheduleDays;
import apisemaperreio.escalante.service.NoScheduleDaysService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/schedule/no-days")
@RestController
public class NoScheduleDaysController {

    private final NoScheduleDaysService service;
    
    @GetMapping
    public ResponseEntity<Object> getAllScheduleType() {
        try {
            List<NoScheduleDays> noScheduleDays = service.getAll();
            return new ResponseEntity<>(noScheduleDays, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
