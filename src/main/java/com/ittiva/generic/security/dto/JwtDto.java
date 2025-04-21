/**
 * 
 */
package com.ittiva.generic.security.dto;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

/**
 *  @author ITTIVA
 *
 */
@Getter
@Setter
public class JwtDto implements Serializable {

	private static final long serialVersionUID = -3882676400461012238L;
		private String token;
	    private String bearer = "Bearer";
	    private String nombreUsuario;
	    private Collection<? extends GrantedAuthority> authorities;
	    
	    public JwtDto() {}
	    
	    public JwtDto(String token, String nombreUsuario, Collection<? extends GrantedAuthority> authorities) {
	        this.token = token;
	        this.nombreUsuario = nombreUsuario;
	        this.authorities = authorities;
	    }	    

}
