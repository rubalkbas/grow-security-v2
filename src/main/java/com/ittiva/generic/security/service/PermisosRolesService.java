package com.ittiva.generic.security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ittiva.generic.security.dto.ResponseDTO;
import com.ittiva.generic.security.entity.PermisosRolesEntity;
import com.ittiva.generic.security.repositoy.IPermisoRepository;
import com.ittiva.generic.security.repositoy.IRolRepository;
import com.ittiva.generic.security.repositoy.IRolesPermiososRepository;


@Service
public class PermisosRolesService implements IPermisosRolesService{
	
	@Autowired
	IRolesPermiososRepository rolesPermiso;
	@Autowired
	IRolRepository rolRepository;
	@Autowired
	IPermisoRepository permisoRepository;

	

	@Override
	public ResponseDTO getAllPermisosByRol(Long idRol) {
		ResponseDTO response = new ResponseDTO();

		try {
			List<PermisosRolesEntity> permisosRoles = rolesPermiso.findByRol(idRol);
			response.setEstatus("OK");
			response.setMensaje("Registros encontrados");
			response.setLista(permisosRoles);
			
		} catch (Exception e) {
			response.setEstatus("Error");
			response.setMensaje("Ocurrió un error durante la búsqueda: " + e.getMessage());
		}

		return response;
	}

	
}
