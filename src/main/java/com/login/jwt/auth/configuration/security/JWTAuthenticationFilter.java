package com.login.jwt.auth.configuration.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.login.jwt.auth.constants.SecurityConstants;
import com.login.jwt.auth.model.Usuario;

/*
 * Esta classe é a extensão da UsernamePasswordAuthenticationFilter, 
 * que é a classe padrão para a autenticação de senha no Spring Security. 
 * Estendemos essa classe para definir nossa lógica de autenticação personalizada.
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        /*
         * O método 'setFilterProcessesUrl' define a URL de login padrão para o
         * parâmetro fornecido. Se removermos essa linha, o Spring Security cria o
         * endpoint "/login" por padrão. Ele define o endpoint de login por nós, motivo
         * pelo qual não definimos um endpoint de login em nosso controller
         * explicitamente.
         */
        setFilterProcessesUrl("/api/services/controller/user/login");
    }

    /*
     * A função 'attemptAuthentication' é executada quando o usuário tentar fazer o
     * login em nossa aplicação. Ela lê as credenciais, cria um POJO de Usuário para
     * elas e as verifica para a autenticação.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            Usuario credenciais = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

            /*
             * Passamos o nome de usuário, a senha e uma lista vazia.
             * A lista vazia representa os privilégios (roles) e a deixamos assim,
             * já que ainda não temos funções (roles) em nossa aplicação.
             */
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credenciais.getLogin(),
                            credenciais.getSenha(),
                            new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * Se a autenticação tiver sucesso, o método successfulAuthentication é
     * executado. Os parâmetros desse método são passados pelo Spring Security
     * internamente.
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authentication) throws IOException {

        /*
         * Deseja-se retornar um token para o usuário após o sucesso da autenticação,
         * por isso criamos o token usando o nome do usuário (login), o SECRET e
         * o EXPIRATION_TIME.
         */
        String token = JWT.create()
                .withSubject(((Usuario) authentication.getPrincipal()).getLogin())
                .withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()));

        String body = ((Usuario) authentication.getPrincipal()).getLogin() + " " + token;

        response.getWriter().write(body);
        response.getWriter().flush();

    }

    private AuthenticationManager authenticationManager;

}
