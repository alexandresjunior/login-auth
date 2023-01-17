package com.login.jwt.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.jwt.auth.configuration.security.PasswordEncoder;
import com.login.jwt.auth.model.Usuario;
import com.login.jwt.auth.service.persistence.UsuarioPersistence;

@Service
public class UsuarioLocalService {

    public Usuario criarUsuario(Usuario usuario) {
        Usuario usuarioDB = new Usuario();

        usuarioDB.setLogin(usuario.getLogin());
        usuarioDB.setSenha(passwordEncoder.bCryptPasswordEncoder().encode(usuario.getSenha()));
        usuarioDB.setNome(usuario.getNome());
        usuarioDB.setSenha(usuario.getSenha());
        usuarioDB.setEmail(usuario.getEmail());

        return usuarioPersistence.save(usuarioDB);
    }

    public List<Usuario> obterUsuarios() {
        return usuarioPersistence.findAll();
    }

    @Autowired
    private UsuarioPersistence usuarioPersistence;

    @Autowired
    private PasswordEncoder passwordEncoder;
}
