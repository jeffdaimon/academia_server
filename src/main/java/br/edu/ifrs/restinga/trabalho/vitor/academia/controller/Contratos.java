/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.controller;

import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.ContratoDAO;
import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Contrato;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.NaoEncontrado;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.RequisicaoInvalida;
import java.util.List;
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
public class Contratos {
    
    @Autowired
    ContratoDAO contratoDAO;
    
    
    public void validaContrato(Contrato contrato) {
        if (contrato.getMeioPagamento()== null || contrato.getMeioPagamento().isEmpty()) {
            throw new RequisicaoInvalida("Campo meio de pagamento é obrigatório");
        }
        if (contrato.getTempoContrato()== null || contrato.getTempoContrato().isEmpty()) {
            throw new RequisicaoInvalida("Campo de tempo contratado é obrigatório");
        }
    }
    
    
    @RequestMapping(path = "/contratos/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Contrato cadastrar(@RequestBody Contrato contrato) {
        validaContrato(contrato);
        contrato.setId(0);
        return contratoDAO.save(contrato);
    }
    
    @RequestMapping(path = "/contratos/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Contrato> listar() {
        return contratoDAO.findAll();
    }
    
    
    @RequestMapping(path = "/contratos/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable int id) {
        if (contratoDAO.existsById(id)) {
            contratoDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("ID não encontrado!");
        }
    }
    
    @RequestMapping(path = "/contratos/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editar(@PathVariable int id, @RequestBody Contrato contrato) {
         if (!contratoDAO.existsById(id)) {
            throw new NaoEncontrado("Cliente não encontrado.");
        } 
        validaContrato(contrato);
        contrato.setId(id);
        contratoDAO.save(contrato);
    }
    
    
}