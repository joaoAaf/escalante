package br.com.appsemaperreio.escalante_api.seguranca.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Perfil;
import br.com.appsemaperreio.escalante_api.seguranca.model.domain.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByPerfisContaining(Perfil perfil);

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    /**
     * Verifica se o usuário informado é o único que possui o perfil especificado.
     * Retorna true se e somente se existir exatamente um usuário com o perfil e ele
     * for o usuário informado.
     */
    @Query("""
            select (count(u) = 1)
            from Usuario u
            join u.perfis p
            where p = :perfil
              and u.username = :username
              and not exists (
                  select 1 from Usuario u2
                  join u2.perfis p2
                  where p2 = :perfil and u2.username <> :username
              )
            """)
    boolean isOnlyUserWithPerfil(@Param("username") String username, @Param("perfil") Perfil perfil);

}
