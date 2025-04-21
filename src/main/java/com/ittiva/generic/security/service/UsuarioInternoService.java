/**
 * 
 */
package com.ittiva.generic.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ittiva.generic.security.entity.UsuarioInterno;
import com.ittiva.generic.security.repositoy.UsuarioInternoRepository;

/**
 *  @author ITTIVA
 *
 */
@Service
@Transactional
public class UsuarioInternoService {

	@Autowired
	UsuarioInternoRepository usuarioRepository;

	public Optional<UsuarioInterno> getByEmail(String correoUsuario) {
		return usuarioRepository.findByCorreoUsuario(correoUsuario);
	}


	public boolean existsByEmail(String correoUsuario) {
		return usuarioRepository.existsByCorreoUsuario(correoUsuario);
	}

	public UsuarioInterno save(UsuarioInterno usuario) {

		return usuarioRepository.save(usuario);
	}


}
