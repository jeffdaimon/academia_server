/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.auth;

import br.edu.ifrs.restinga.trabalho.vitor.academia.ConfiguracaoSeguranca;
import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.AtendenteDAO;
import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Atendente;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 *
 * @author vitor
 */
public class FiltroPorToken extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String token = request.getHeader("token");
        if (token != null && !token.isEmpty()) {
            Algorithm algorithm = Algorithm.HMAC256(ConfiguracaoSeguranca.SEGREDO);
            DecodedJWT decode = JWT.require(algorithm).build().verify(token);
            Integer id = decode.getClaim("id").asInt();
            Atendente atendente = atendenteDAO.findById(id).get();
            AuthUser usuarioAut = new AuthUser(atendente);
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(usuarioAut, null, usuarioAut.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        chain.doFilter(request, response);
    }
    AtendenteDAO atendenteDAO;

    public FiltroPorToken(AtendenteDAO atendenteDAO) {
        super();
        this.atendenteDAO = atendenteDAO;

    }

}
