package apisemaperreio.escalante.escalante.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import apisemaperreio.escalante.escalante.domain.AdminAutenticado;
import apisemaperreio.escalante.escalante.repository.AdminRepository;

@Service
public class AdminDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;

    public AdminDetailsService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return adminRepository.findById(username)
                .map(AdminAutenticado::new)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("O usuário %s não foi encontrado.", username)));
    }

}
