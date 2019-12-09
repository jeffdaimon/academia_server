/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia;


import static br.edu.ifrs.restinga.trabalho.vitor.academia.ConfiguracaoSeguranca.PASSWORD_ENCODER;
import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.AtendenteDAO;
import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Atendente;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author vitor
 */
@Component
public class Inicializador {

    @Autowired
    AtendenteDAO atendenteDAO;

    @PostConstruct
    public void init() {
        Atendente atendenteRoot = atendenteDAO.findByLogin("admin");
        if (atendenteRoot == null) {
            atendenteRoot = new Atendente();
            atendenteRoot.setNome("admin");
            atendenteRoot.setLogin("admin");
            atendenteRoot.setPassword(PASSWORD_ENCODER.encode("12345"));
            atendenteRoot.setPermissoes(Arrays.asList("administrador"));
            atendenteDAO.save(atendenteRoot);
        }
    }
}
