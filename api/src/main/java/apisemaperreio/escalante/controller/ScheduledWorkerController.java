package apisemaperreio.escalante.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.service.ScheduledWorkerService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/schedule")
@RestController
public class ScheduledWorkerController {

    private final ScheduledWorkerService service;

    @GetMapping("{date}")
    public ResponseEntity<Object> getScheduledWorkerByDate(@PathVariable String date) {
        try {
            var scheduledWorkers = service.getScheduledWorkerByDate(LocalDate.parse(date));
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No scheduled workers found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getRangeScheduledWorkers(@RequestParam(value = "start") String startDate,
            @RequestParam(value = "end") String endDate) {
        try {
            var scheduledWorkers = service.getRangeScheduledWorkers(LocalDate.parse(startDate),
                    LocalDate.parse(endDate));
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No scheduled workers found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Object> scheduler(@RequestParam(value = "start") String startDate,
            @RequestParam(value = "end") String endDate, @RequestParam(value = "days") @Max(3) @Positive Integer days) {
        try {
            var scheduledWorkers = service.scheduler(LocalDate.parse(startDate), LocalDate.parse(endDate), days);
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.CREATED);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
