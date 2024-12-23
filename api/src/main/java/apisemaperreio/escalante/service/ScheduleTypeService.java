package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.ScheduleType;
import apisemaperreio.escalante.repository.ScheduleTypeRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScheduleTypeService {

    private final ScheduleTypeRepository repo;

    public List<ScheduleType> getAll() {
        return repo.findAll();
    }

}
