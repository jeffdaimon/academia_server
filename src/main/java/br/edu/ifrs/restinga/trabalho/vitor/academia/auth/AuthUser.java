/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia.auth;

import br.edu.ifrs.restinga.trabalho.vitor.academia.entidade.Atendente;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author vitor
 */
public class AuthUser extends User {
    private Atendente atendente;
    public AuthUser(Atendente atendente) {
        super(atendente.getLogin(),
                atendente.getPassword(),
                AuthorityUtils.createAuthorityList(
                    atendente.getPermissoes().toArray(new String[]{})));
        this.atendente=atendente;
    }
    public Atendente getAtendente() {
        return atendente;
    }
}
