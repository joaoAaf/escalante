package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import apisemaperreio.escalante.model.NoScheduleDays;

@Repository
public interface NoScheduleDaysRepository extends JpaRepository<NoScheduleDays, Integer> {

}
