/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.controller;

import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.AtendenteDAO;
import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Atendente;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.RequisicaoInvalida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    
     public void validaAtendente(Atendente atendente) {
        if (atendente.getNome()== null || atendente.getNome().isEmpty()) {
            throw new RequisicaoInvalida("Campo nome é obrigatório");
        }
        if (atendente.getLogin()== null || atendente.getLogin().isEmpty()) {
            throw new RequisicaoInvalida("Campo endereço é obrigatório");
        }
        if (atendente.getPassword()== null || atendente.getPassword().isEmpty()) {
            throw new RequisicaoInvalida("Campo telefone é obrigatório");
        }
    } 
    
    @RequestMapping(path = "/atendentes/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Atendente cadastrar(@RequestBody Atendente atendente) {
        atendente.setId(0);
        validaAtendente(atendente);
        return atendenteDAO.save(atendente);
    }
    
    @RequestMapping(path = "/atendentes/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Atendente> listar() {
        return atendenteDAO.findAll();
    }
}
