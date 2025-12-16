package br.com.appsemaperreio.escalante_api.seguranca.application.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import br.com.appsemaperreio.escalante_api.seguranca.domain.Perfil;

@Component
public class PerfilMapper {

    @Named("setStringToPerfis")
    public Set<Perfil> setStringToPerfis(Set<String> perfisStr) {
        return perfisStr.stream()
                .map(this::stringToPerfil)
                .collect(Collectors.toSet());
    }

    public Perfil stringToPerfil(String perfilStr) {
        try {
            return Perfil.valueOf(perfilStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Perfil inválido: " + perfilStr);
        }
    }

}
