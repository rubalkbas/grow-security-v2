package com.ittiva.generic.security.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles_permisos", schema = "creciendo")
public class PermisosRolesEntity {
	
	@Id
	@Column(name = "id_permiso")
	private Long idPermiso;

	@Column(name = "id_rol")
	private Long idRol;
	
	
	@ManyToOne(fetch = FetchType.EAGER)  // Trae los datos completos del permiso
    @JoinColumn(name = "id_permiso", referencedColumnName = "id_permiso", insertable = false, updatable = false)
	private PermisoEntity permiso;

	@ManyToOne(fetch = FetchType.EAGER)  // Trae los datos completos del rol
    @JoinColumn(name = "id_rol", referencedColumnName = "id_rol", insertable = false, updatable = false)
	private RolEntity rol;

}
