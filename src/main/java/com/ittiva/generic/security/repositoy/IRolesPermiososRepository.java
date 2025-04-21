package com.ittiva.generic.security.repositoy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ittiva.generic.security.entity.PermisosRolesEntity;


public interface IRolesPermiososRepository extends JpaRepository<PermisosRolesEntity, Integer> {
	@Query("SELECT p FROM PermisosRolesEntity p WHERE p.idRol = :idRol")
	List<PermisosRolesEntity> findByRol(@Param("idRol") Long idRol);

	@Modifying
	@Transactional
	@Query("DELETE FROM PermisosRolesEntity p WHERE p.idRol = :idRol")
	void deleteByRolId(@Param("idRol") Integer idRol);
}

