package com.login.jwt.auth.configuration.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.login.jwt.auth.constants.SecurityConstants;

/*
 * Este filtro verificará a existência e a validade do token de acesso
 * no cabeçalho 'Authorization' da requisição. Especificaremos quais endpoints 
 * estarão sujeitos a esse filtro em nossa classe de Configuração.
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    /*
     * O método 'doFilterInternal' intercepta as solicitações e verifica o cabeçalho
     * 'Authorization'. Se o cabeçalho não estiver presente ou não iniciar com
     * "BEARER" (portador), ele segue com a cadeia de filtros.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);

        if (Objects.isNull(header) || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);

            return;
        }

        /*
         * Se o cabeçalho estiver presente, o método getAuthentication é invocado.
         * Este método verifica o JWT e, se o token for válido, ele retorna um token de
         * acesso que o Spring usará internamente.
         */
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        /*
         * Este novo token, então, é salvo em SecurityContext (contexto de segurança).
         * Você também pode passar privilégios por esse token se precisar de autorização
         * baseada em funções (roles).
         */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(SecurityConstants.HEADER_STRING);

        if (Objects.nonNull(token)) {
            String usuario = JWT.require(Algorithm.HMAC512(SecurityConstants.SECRET.getBytes()))
                    .build().verify(token.replace(SecurityConstants.TOKEN_PREFIX, "")).getSubject();

            if (Objects.nonNull(usuario)) {
                return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
            }

            return null;
        }

        return null;
    }

}
