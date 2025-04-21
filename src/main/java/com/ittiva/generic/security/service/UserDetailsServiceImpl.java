/**
 * 
 */
package com.ittiva.generic.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ittiva.generic.security.entity.UsuarioInterno;
import com.ittiva.generic.security.entity.UsuarioPrincipal;

/**
 *  @author ITTIVA
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UsuarioInternoService usuarioService;

	@Override
	public UserDetails loadUserByUsername(String correoUsuario) throws UsernameNotFoundException {
		UsuarioInterno usuario = usuarioService.getByEmail(correoUsuario).get();

		return UsuarioPrincipal.build(usuario);
	}
}
