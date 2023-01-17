package com.login.jwt.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.login.jwt.auth.model.Usuario;
import com.login.jwt.auth.service.UsuarioLocalService;

@RestController
@RequestMapping(value = "/api/v1/usuarios")
public class UsuarioController {

    @PostMapping
    public ResponseEntity<Usuario> criarNovoUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioLocalService.criarUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<List<Usuario>> obterUsuarios(@RequestBody Usuario usuario) {
        return ResponseEntity.status(HttpStatus.OK).body(usuarioLocalService.obterUsuarios());
    }

    @Autowired
    private UsuarioLocalService usuarioLocalService;

}