package br.com.appsemaperreio.escalante_api.seguranca.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "perfis_usuario", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "perfil")
    @Enumerated(EnumType.STRING)
    private Set<Perfil> perfis = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String username, String password, Set<Perfil> perfis) {
        this.username = username;
        this.password = password;
        this.perfis = perfis;
    }
    
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
    }

}
