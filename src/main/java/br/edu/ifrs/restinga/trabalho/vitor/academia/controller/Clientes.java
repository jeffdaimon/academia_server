/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.controller;

import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.ClienteDAO;
import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Cliente;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.NaoEncontrado;
import br.edu.ifrs.restinga.trabalho.vitor.academia.erro.RequisicaoInvalida;
import java.util.List;
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
public class Clientes {

    @Autowired
    ClienteDAO clienteDAO;
    
    public void validaCliente(Cliente cliente) {
        if (cliente.getNome()== null || cliente.getNome().isEmpty()) {
            throw new RequisicaoInvalida("Campo nome é obrigatório");
        }
        if (cliente.getEndereco()== null || cliente.getEndereco().isEmpty()) {
            throw new RequisicaoInvalida("Campo endereço é obrigatório");
        }
        if (cliente.getTelefone()== null || cliente.getTelefone().isEmpty()) {
            throw new RequisicaoInvalida("Campo telefone é obrigatório");
        }
        if (cliente.getCpf()== null || cliente.getCpf().isEmpty()) {
            throw new RequisicaoInvalida("Campo cpf é obrigatório");
        }
    } 

    @RequestMapping(path = "/clientes/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Cliente> listar() {
        return clienteDAO.findAll();
    }

    @RequestMapping(path = "/clientes/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Cliente buscar(@PathVariable int id) {
        final Optional<Cliente> findById = clienteDAO.findById(id);
        if (findById.isPresent()) {
            return findById.get();
        } else {
            throw new NaoEncontrado("ID não encontrada!");
        }
    }

    @RequestMapping(path = "/clientes/", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente cadastrar(@RequestBody Cliente cliente) {
        validaCliente(cliente);
        cliente.setId(0);
        return clienteDAO.save(cliente);
    }

    @RequestMapping(path = "/clientes/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable int id) {
        if (clienteDAO.existsById(id)) {
            clienteDAO.deleteById(id);
        } else {
            throw new NaoEncontrado("ID não encontrada!");
        }
    }

    @RequestMapping(path = "/clientes/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void editar(@PathVariable int id, @RequestBody Cliente cliente) {
         if (!clienteDAO.existsById(id)) {
            throw new NaoEncontrado("Cliente não encontrado.");
        }
        validaCliente(cliente);
        cliente.setId(id);
        clienteDAO.save(cliente);
    }
    
    @RequestMapping(path = "/clientes/pesquisar/nome", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> pesquisaNome(@RequestBody Cliente cliente) {
        if (cliente.getNome()!= null) {
            return clienteDAO.findByNome(cliente.getNome());
        } else {
            throw new RequisicaoInvalida("Digite um nome válido");
        }
    }
    
    
    @RequestMapping(path = "/clientes/pesquisar/matricula", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public List<Cliente> pesquisaMatricula(@RequestBody Cliente cliente) {
        if (cliente.getMatricula()!= 0) {
            return clienteDAO.findByMatricula(cliente.getMatricula());
        } else {
            throw new RequisicaoInvalida("Digite uma matricula válida");
        }
    }
}
