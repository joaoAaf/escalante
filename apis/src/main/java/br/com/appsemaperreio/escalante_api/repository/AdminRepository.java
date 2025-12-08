package br.com.appsemaperreio.escalante_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appsemaperreio.escalante_api.domain.seguranca.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

}
