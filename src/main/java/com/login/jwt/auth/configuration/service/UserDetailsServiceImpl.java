package com.login.jwt.auth.configuration.service;

import java.util.Objects;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.login.jwt.auth.model.Usuario;
import com.login.jwt.auth.service.persistence.UsuarioPersistence;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetailsServiceImpl(UsuarioPersistence usuarioPersistence) {
        this.usuarioPersistence = usuarioPersistence;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Usuario usuario = usuarioPersistence.findByLogin(username);

        if (Objects.isNull(usuario)) {
            throw new UsernameNotFoundException(username);
        }

        UserDetails userDetails = User.withUsername(
                usuario.getLogin()).password(usuario.getSenha()).roles("USER").build();

        return userDetails;
    }

    private UsuarioPersistence usuarioPersistence;

}
