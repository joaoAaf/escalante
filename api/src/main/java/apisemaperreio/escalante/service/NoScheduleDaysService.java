package apisemaperreio.escalante.service;

import java.util.List;

import org.springframework.stereotype.Service;

import apisemaperreio.escalante.model.NoScheduleDays;
import apisemaperreio.escalante.repository.NoScheduleDaysRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoScheduleDaysService {

    private final NoScheduleDaysRepository repo;

    public List<NoScheduleDays> getAll() {
        return repo.findAll();
    }

}
