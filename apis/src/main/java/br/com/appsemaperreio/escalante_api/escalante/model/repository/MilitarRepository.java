package br.com.appsemaperreio.escalante_api.escalante.model.repository;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface MilitarRepository extends JpaRepository<Militar, String> {

    boolean existsByAntiguidadeIn(Collection<Integer> antiguidades);

    @Query("SELECT m.id FROM Militar m WHERE m.id IN :matriculas")
    List<String> findMatriculasByIdIn(@Param("matriculas") Collection<String> matriculas);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Militar> findByAntiguidadeGreaterThanEqualOrderByAntiguidadeDesc(Integer antiguidade);

    @Query("SELECT COALESCE(MAX(m.antiguidade), 0) FROM Militar m")
    int maxAntiguidade();
}
