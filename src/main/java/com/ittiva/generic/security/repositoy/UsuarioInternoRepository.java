/**
 * 
 */
package com.ittiva.generic.security.repositoy;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ittiva.generic.security.entity.UsuarioInterno;


/**
 * @author ITTIVA
 *
 */
public interface UsuarioInternoRepository  extends JpaRepository<UsuarioInterno, Integer> {

    Optional<UsuarioInterno> findByCorreoUsuario(String correoUsuario);

    boolean existsByCorreoUsuario(String correoUsuario);

}
