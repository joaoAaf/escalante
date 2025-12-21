package br.com.appsemaperreio.escalante_api.escalante.model.repository;

import br.com.appsemaperreio.escalante_api.escalante.model.domain.Militar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MilitarRepository extends JpaRepository<Militar, String> {
}
