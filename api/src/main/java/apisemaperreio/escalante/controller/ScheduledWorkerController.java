package apisemaperreio.escalante.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apisemaperreio.escalante.dto.SaveScheduledWorkerDTO;
import apisemaperreio.escalante.dto.UpdateScheduledWorkerDTO;
import apisemaperreio.escalante.service.ScheduledWorkerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllScheduledWorkersRangeDate(@RequestParam(value = "start") String startDate,
            @RequestParam(value = "end") String endDate) {
        try {
            var scheduledWorkers = service.getAllScheduledWorkersRangeDate(LocalDate.parse(startDate),
                    LocalDate.parse(endDate));
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No scheduled workers found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/worker/{workerRegistration}")
    public ResponseEntity<Object> getScheduledWorkerRangeDate(@PathVariable String workerRegistration,
            @RequestParam(value = "start") String startDate, @RequestParam(value = "end") String endDate) {
        try {
            var scheduledWorkers = service.getScheduledWorkerRangeDate(workerRegistration, LocalDate.parse(startDate),
                    LocalDate.parse(endDate));
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No scheduled workers found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/count/{workerRegistration}")
    public ResponseEntity<Object> getWorkerCountDays(@PathVariable String workerRegistration,
            @RequestParam(value = "start") String startDate, @RequestParam(value = "end") String endDate) {
        try {
            var scheduledWorkers = service.getWorkerCountDays(workerRegistration, LocalDate.parse(startDate),
                    LocalDate.parse(endDate));
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No worker found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Object> getAllWorkersCountDays(@RequestParam(value = "start") String startDate,
            @RequestParam(value = "end") String endDate) {
        try {
            var scheduledWorkers = service.getAllWorkersCountDays(LocalDate.parse(startDate),
                    LocalDate.parse(endDate));
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No workers found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/inconsistencies")
    public ResponseEntity<Object> getInconsistencies(@RequestParam(value = "start") String startDate,
            @RequestParam(value = "end") String endDate) {
        try {
            var scheduledWorkers = service.getInconsistencies(LocalDate.parse(startDate),
                    LocalDate.parse(endDate));
            if (scheduledWorkers.isEmpty()) {
                return new ResponseEntity<>("No inconsistencies found", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping
    public ResponseEntity<Object> scheduler(@RequestParam(value = "start") String startDate,
            @RequestParam(value = "end") String endDate, @RequestParam(value = "days") @Max(5) @Positive Integer days) {
        try {
            var scheduledWorkers = service.scheduler(LocalDate.parse(startDate), LocalDate.parse(endDate), days);
            if (scheduledWorkers.isEmpty())
                return new ResponseEntity<>(
                        "No workers scheduled: Check if there are workers registered or if there is a schedule for later dates",
                        HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(scheduledWorkers, HttpStatus.CREATED);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<Object> saveScheduledWorker(
            @RequestBody @Valid SaveScheduledWorkerDTO saveScheduledWorkerDTO) {
        try {
            var scheduledWorker = service.saveScheduledWorker(saveScheduledWorkerDTO);
            return new ResponseEntity<>(scheduledWorker, HttpStatus.CREATED);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Object> updateScheduledWorker(@PathVariable @Positive Integer id,
            @RequestBody UpdateScheduledWorkerDTO updateScheduledWorkerDto) {
        try {
            service.updateScheduledWorker(id, updateScheduledWorkerDto);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> swapTwoScheduledWorkers(@RequestParam(value = "id1") @Positive Integer id1,
            @RequestParam(value = "id2") @Positive Integer id2) {
        try {
            service.swapTwoScheduledWorkers(id1, id2);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>("Invalid date format: yyyy-MM-dd", HttpStatus.BAD_REQUEST);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error: " + e.getMessage());
            return new ResponseEntity<>("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

}
