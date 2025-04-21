package com.ittiva.generic.security.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * ENTITY
 * 
 * @author 
 * 
 */
@Entity
@Table(name="roles", schema="internanueva")
@Getter
@Setter
public class RolEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_rol")
	private Long idRol;
	
	@Column(name = "nombre_rol")
	private String nombreRol;
	
	@Column(name = "descripcion")
	private String descripcion;

}
