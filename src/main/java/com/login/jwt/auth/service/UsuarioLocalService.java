package com.login.jwt.auth.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.jwt.auth.model.Usuario;
import com.login.jwt.auth.service.persistence.UsuarioPersistence;

@Service
public class UsuarioLocalService {
    
    public Usuario criarUsuario(Usuario usuario) {
        return usuarioPersistence.save(usuario);
    }

    public List<Usuario> obterUsuarios() {
        return usuarioPersistence.findAll();
    }

    @Autowired
    private UsuarioPersistence usuarioPersistence;
}
