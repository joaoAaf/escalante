package br.com.appsemaperreio.escalante_api.repository.seguranca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {

}
