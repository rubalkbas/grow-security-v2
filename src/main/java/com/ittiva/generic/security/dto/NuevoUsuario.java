/**
 * 
 */
package com.ittiva.generic.security.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ITTIVA
 *
 */
@Getter
@Setter
public class NuevoUsuario implements Serializable {

	private static final long serialVersionUID = 8640153550939223590L;

	private Long idUsuario;

    private String nombreUsuario;

    private String apellidoPaterno;

    private String apellidoMaterno;

    private String correoUsuario;

    private String contrasenia;

	private Integer estatus;	

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaCreacion;	

	private Double totalDinero;

	private Double margenLibre;

	private Double margen;

	private Long idAdmin;

	private Integer recuperacion;

    private Long rol;
}
