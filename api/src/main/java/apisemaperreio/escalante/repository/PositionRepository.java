package apisemaperreio.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import apisemaperreio.escalante.model.Position;

public interface PositionRepository extends JpaRepository<Position, Integer> {

}
