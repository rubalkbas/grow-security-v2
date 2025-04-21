package com.ittiva.generic.security.repositoy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ittiva.generic.security.entity.RolEntity;


@Repository
public interface IRolRepository extends JpaRepository<RolEntity, Integer>{

}
