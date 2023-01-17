package com.login.jwt.auth.service.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.login.jwt.auth.model.Usuario;

public interface UsuarioPersistence extends JpaRepository<Usuario, Long> {
    
    Usuario findByLogin(String login);

}
