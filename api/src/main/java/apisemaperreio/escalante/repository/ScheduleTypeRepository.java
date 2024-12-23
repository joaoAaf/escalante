package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import apisemaperreio.escalante.model.ScheduleType;

@Repository
public interface ScheduleTypeRepository extends JpaRepository<ScheduleType, Long> {

    
}
