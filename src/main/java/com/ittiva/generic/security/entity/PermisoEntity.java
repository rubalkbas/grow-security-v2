package com.ittiva.generic.security.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


import lombok.Getter;
import lombok.Setter;

/**
 * @author ITTIVA
 * @version 1.0
 * @since 2024
 *
 */

@Getter
@Setter
@Entity
@Table(name = "permisos", schema = "creciendo")
public class PermisoEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_permiso")
	private Long idPermiso;
	
	@Column(name = "tipo_permiso")
	private String tipoPermiso;
	
	@Column(name = "recurso")
	private String recurso;
	
	@Column(name = "accion")
	private String accion;	
	
	@Column(name = "id_permiso_padre")
	private Long idPermisoPadre;
	
}
