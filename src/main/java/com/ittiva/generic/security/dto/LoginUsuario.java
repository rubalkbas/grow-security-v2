/**
 * 
 */
package com.ittiva.generic.security.dto;



import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
 *  @author ITTIVA
 *
 */
@Getter
@Setter
public class LoginUsuario implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 482016261392380420L;
	@NotBlank
    private String correo;
    @NotBlank
    private String password;	
}
