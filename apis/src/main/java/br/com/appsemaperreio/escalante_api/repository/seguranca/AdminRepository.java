package br.com.appsemaperreio.escalante_api.repository.seguranca;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

}
