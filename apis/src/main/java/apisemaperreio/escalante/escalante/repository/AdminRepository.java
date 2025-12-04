package apisemaperreio.escalante.escalante.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import apisemaperreio.escalante.escalante.domain.Admin;

public interface AdminRepository extends JpaRepository<Admin, String> {

}
