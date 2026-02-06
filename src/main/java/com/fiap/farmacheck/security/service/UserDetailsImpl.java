package com.fiap.farmacheck.security.service;

import com.fiap.farmacheck.model.entity.Usuario;
import com.fiap.farmacheck.model.enums.TipoUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImpl implements UserDetails {

    private Usuario usuario;

    public UserDetailsImpl(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUser() {
        return usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.usuario.getTipoUsuario().equals(TipoUsuario.ADMINISTRADOR)) {
            return List.of(
                    new SimpleGrantedAuthority("ADMINISTRADOR"),
                    new SimpleGrantedAuthority("PACIENTE")
            );
        } else return List.of(new SimpleGrantedAuthority("PACIENTE"));
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
