/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.trabalho.vitor.academia;

import br.edu.ifrs.restinga.trabalho.vitor.academia.auth.FiltroPorToken;
import br.edu.ifrs.restinga.trabalho.vitor.academia.dao.AtendenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 *
 * @author vitor
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {
    
    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    public static final String SEGREDO="string grande para c*, usada como chave para assinatura! Queijo!";
    
    @Autowired
    AtendenteDAO atendenteDAO;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            //o GET login pode ser acessado sem autenticação 
            .antMatchers(HttpMethod.GET, "/api/atendentes/login/").permitAll()
            // Caso o sistema permita o autocadastro                
            .antMatchers(HttpMethod.POST, "/api/atendentes/").permitAll()
            // permite o acesso somente se autenticado
            .antMatchers("/api/**").authenticated()
            .and().addFilterBefore(new FiltroPorToken(atendenteDAO), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().csrf().disable();
    }
}
