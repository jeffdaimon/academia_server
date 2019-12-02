/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.controller;

import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.PlanoDAO;
import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Plano;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.NaoEncontrado;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.RequisicaoInvalida;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
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
public class Planos {

    @Autowired
    PlanoDAO planoDAO;
    
    
    public void validaPlano(Plano plano) {
        if (plano.getPlano()== null || plano.getPlano().isEmpty()) {
            throw new RequisicaoInvalida("Campo plano é obrigatório");
        }
        
        if (plano.getValor()<= 0) {
            throw new RequisicaoInvalida("Valor não pode ser menor que 0");
        }
    }
    
    

    @RequestMapping(path = "/planos/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Plano> listar() {
        return planoDAO.findAll();
    }

    @RequestMapping(path = "/planos/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Plano buscar(@PathVariable int id) {
        final Optional<Plano> findById = planoDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("ID não encontrada!");
        }
    }

    @RequestMapping(path = "/planos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Plano cadastrar(@RequestBody Plano plano) {
        validaPlano(plano);
        plano.setId(0);
        return planoDAO.save(plano);
    }

    @RequestMapping(path = "/planos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable int id) {
        if (planoDAO.existsById(id)) {
            planoDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("ID não encontrada!");
        }
    }

    @RequestMapping(path = "/planos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editar(@PathVariable int id, @RequestBody Plano plano) {
        if (!planoDAO.existsById(id)) {
            throw new NaoEncontrado("Cliente não encontrado.");
        }
        validaPlano(plano);
        plano.setId(id);
        planoDAO.save(plano);
    }

}

