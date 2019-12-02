/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.dao;

import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Plano;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vitor
 */

@Repository
public interface PlanoDAO extends CrudRepository<Plano, Integer>{
    
}
