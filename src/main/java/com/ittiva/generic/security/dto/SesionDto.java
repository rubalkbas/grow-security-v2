/**
 * 
 */
package com.ittiva.generic.security.dto;

import com.ittiva.generic.security.entity.UsuarioPrincipal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ITTIVA
 *
 */
@Getter
@Setter
public class SesionDto {
	
	UsuarioPrincipal usuario;
	
	JwtDto jwt;
}
