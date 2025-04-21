/**
 * 
 */
package com.ittiva.generic.security.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

/**
 *  @author ITTIVA 
 *
 */
@Getter
@Setter
public class UsuarioPrincipal implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nombre;
	private String nombreUsuario;
	private String apellidoPaterno;
	private String apellidoMaterno;
	private String correoUsuario;
	private String contrasenia;
	private RolEntity idRol;
	private List<?> permisos;
	private Collection<? extends GrantedAuthority> authorities;

	public UsuarioPrincipal(Long id, String nombre, String apellidoPaterno, String apellidoMaterno,
			 String correoUsuario, String password,RolEntity idRol, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.nombre = nombre;
		this.apellidoPaterno = apellidoPaterno;
		this.apellidoMaterno = apellidoMaterno;
		this.correoUsuario = correoUsuario;
		this.contrasenia = password;
		this.idRol = idRol;
		this.authorities = authorities;
	}

	public static UsuarioPrincipal build(UsuarioInterno usuario) {
		GrantedAuthority auth = new SimpleGrantedAuthority("usuario toli");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(auth);

		return new UsuarioPrincipal(usuario.getIdUsuario(),usuario.getNombreUsuario(), usuario.getApellidoPaterno(), usuario.getApellidoMaterno(),
				 usuario.getCorreoUsuario(), usuario.getContrasenia(),usuario.getRol(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return contrasenia;
	}

	@Override
	public String getUsername() {
		return nombreUsuario;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
