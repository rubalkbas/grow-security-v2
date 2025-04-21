/**
 * 
 */
package com.ittiva.generic.security.entity;

import java.util.Date;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
import lombok.ToString;

/**
 * @author ITTIVA
 *
 */
@Entity
@Table(name="usuarios", schema="internanueva")
@Getter
@Setter
public class UsuarioInterno implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_usuario")
	private Long idUsuario;
	
	@Column(name = "nombre_usuario")
    private String nombreUsuario;

    @Column(name = "ape_pat_usuario")
    private String apellidoPaterno;

    @Column(name = "ape_mat_usuario")
    private String apellidoMaterno;
	
    @Column(name = "correo_usuario")
    private String correoUsuario;

    @Column(name = "contrasenia" )
    private String contrasenia;
	
	@Column(name = "estatus")
	private Integer estatus;	
	
	@Column(name = "fecha_creacion", insertable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime fechaCreacion;	

	@Column(name = "total_dinero")
	private Double totalDinero;
	
	@Column(name = "margen_libre")
	private Double margenLibre;
	
	@Column(name = "margen")
	private Double margen;
	
	@Column(name = "id_admin")
	private Long idAdmin;
	
	@Column(name = "recuperacion")
	private Integer recuperacion;
	
	@ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolEntity rol;

}
