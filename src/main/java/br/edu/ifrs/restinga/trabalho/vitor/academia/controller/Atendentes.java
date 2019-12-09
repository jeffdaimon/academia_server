/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.controller;

import br.edu.ifrs.restinga.trabalho.vitor.academia.ConfiguracaoSeguranca;
import br.edu.ifrs.restinga.trabalho.vitor.academia.auth.AuthUser;
import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.AtendenteDAO;
import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Atendente;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.NaoEncontrado;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.Proibido;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.RequisicaoInvalida;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author vitor
 */
@RestController
@RequestMapping("/api")
public class Atendentes {

    @Autowired
    AtendenteDAO atendenteDAO;

    
    @PreAuthorize("hasAuthority('administrador')")
    @RequestMapping(path = "/atendentes/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Atendente> listar() {
        return atendenteDAO.findAll();
    }
    
    @RequestMapping(path = "/atendentes/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Atendente recuperar(AuthUser usuarioAut,int id) {
    if (usuarioAut.getAtendente().getId() == id
            || usuarioAut.getAtendente().getPermissoes().contains("administrador")) {
        return atendenteDAO.findById(id).get();
    } else {
        throw new Proibido("Não é permitido acessar dados de outro usuário");
    }
}
    
    

    @RequestMapping(path = "/atendentes/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Atendente cadastrar(@AuthenticationPrincipal AuthUser usuarioAut, @RequestBody Atendente atendente) {
        atendente.setId(0);
        atendente.setPassword(ConfiguracaoSeguranca.PASSWORD_ENCODER.encode(atendente.getNewPassword()));
        if (usuarioAut == null || !usuarioAut.getAtendente().getPermissoes().contains("administrador")) {
            ArrayList<String> permissao = new ArrayList<String>();
            permissao.add("usuario");
            atendente.setPermissoes(permissao);
        }
        return atendenteDAO.save(atendente);
    }
    
    public String token(Atendente atendente) throws UnsupportedEncodingException {
        Algorithm algorithm = Algorithm.HMAC256(ConfiguracaoSeguranca.SEGREDO);
        Calendar agora = Calendar.getInstance();
        agora.add(Calendar.MINUTE, 15);
        Date expira = agora.getTime();
        String token = JWT.create()
                .withClaim("id", atendente.getId()).
                withExpiresAt(expira).
                sign(algorithm);
        return token;
    }
    
    public Atendente login(String login, String password) {
        Atendente atendenteBanco = atendenteDAO.findByLogin(login);
        if (atendenteBanco != null) {
            boolean senhasIguais = ConfiguracaoSeguranca.PASSWORD_ENCODER.matches(password, atendenteBanco.getPassword());
            if (senhasIguais) {
                return atendenteBanco;
            }
        }
        throw new NaoEncontrado("Usuário e/ou senha incorreto(s)");
    }
    
    
    @RequestMapping(path = "/atendentes/login/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Atendente> loginToken(@RequestParam String login, @RequestParam String password) throws UnsupportedEncodingException {
        Atendente atendente = login(login, password);
        String token = token(atendente);
        return ResponseEntity.ok().header("token", token).body(atendente);
    }
    
    
    
    
}
