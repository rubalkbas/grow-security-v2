package com.ittiva.generic.security.repositoy;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ittiva.generic.security.entity.PermisoEntity;

public interface IPermisoRepository extends JpaRepository < PermisoEntity, Integer > {
//	@Query("SELECT p FROM PermisoEntity p WHERE p.fechaCreacion BETWEEN :fechaInicial AND :fechaFinal")
//    List<PermisoEntity> findByFechaCreacionBetween(@Param("fechaInicial") LocalDateTime fechaInicial, @Param("fechaFinal") LocalDateTime fechaFinal);
}
