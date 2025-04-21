/**
 *
 */
package com.ittiva.generic.security.controller;


import com.ittiva.generic.security.dto.JwtDto;
import com.ittiva.generic.security.dto.LoginUsuario;
import com.ittiva.generic.security.dto.NuevoUsuario;
import com.ittiva.generic.security.dto.ResponseDTO;
import com.ittiva.generic.security.dto.SesionDto;
import com.ittiva.generic.security.entity.RolEntity;
import com.ittiva.generic.security.entity.UsuarioInterno;
import com.ittiva.generic.security.entity.UsuarioPrincipal;
import com.ittiva.generic.security.exception.ServiceException;
import com.ittiva.generic.security.jwt.JwtProvider;
import com.ittiva.generic.security.service.IPermisosRolesService;
import com.ittiva.generic.security.service.RedisService;
import com.ittiva.generic.security.service.UsuarioInternoService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author ITTIVA
 */
@Slf4j
@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UsuarioInternoService usuarioInternoService;

    @Autowired
    IPermisosRolesService permisosRoles;

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<SesionDto>> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult)
            throws ServiceException {
        ResponseDTO<SesionDto> response = new ResponseDTO<SesionDto>();
        SesionDto sesion = new SesionDto();

        log.info("-");
        log.info("INICIA LOGIN USUARIO !!!");

        log.info("-");
        log.info("CORREO : " + loginUsuario.getCorreo());

        log.info("-");
        log.info("CONTRA : ********** ");


        if (!usuarioInternoService.existsByEmail(loginUsuario.getCorreo())) {

            log.info("DATOS INCORRECTOS FAVOR DE VERIFICAR");

            response.setEstatus("ERROR");
            response.setMensaje("DATOS INCORRECTOS FAVOR DE VERIFICAR");
            response.setDto(new SesionDto());
            return new ResponseEntity<ResponseDTO<SesionDto>>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<UsuarioInterno> UsuarioInterno2 = usuarioInternoService.getByEmail(loginUsuario.getCorreo());

        if (!UsuarioInterno2.get().getContrasenia().equals(loginUsuario.getPassword())) {

            log.info("DATOS INCORRECTOS FAVOR DE VERIFICAR");

            response.setEstatus("ERROR");
            response.setMensaje("DATOS INCORRECTOS FAVOR DE VERIFICAR");
            response.setDto(new SesionDto());
            return new ResponseEntity<ResponseDTO<SesionDto>>(response, HttpStatus.BAD_REQUEST);

        }

//	    	Optional<UsuarioInterno> UsuarioInterno = usuarioInternoService.getByEmailAndEstatus(loginUsuario.getNombreUsuario(),"PENDIENTE");
//    	 
//	    	 if( !UsuarioInterno.isEmpty() ) {
//		        	
//		        	log.info("USUARIO EN REVISION");
//		        	
//		    		response.setEstatus("OK");
//		    		response.setMensaje("USUARIO EN REVISION - PENDIENTE");
//		    		response.setDto(new SesionDto());
//		        	return new ResponseEntity<ResponseDTO<SesionDto>>(response, HttpStatus.BAD_REQUEST);        	
//		        }

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getCorreo(), loginUsuario.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        String jwt = jwtProvider.generateToken(String.valueOf(usuarioPrincipal.getId()), usuarioPrincipal.getUsername());


        UsuarioPrincipal userDetails = (UsuarioPrincipal) authentication.getPrincipal();
        userDetails.setContrasenia(null);
        JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
        sesion.setJwt(jwtDto);
        sesion.setUsuario(userDetails);

        //ObtenerPermisos Rol
//        ResponseDTO responsePermisos = new ResponseDTO();
//        responsePermisos = permisosRoles.getAllPermisosByRol(UsuarioInterno2.get().getRol().getIdRol());
//
//        // Asignar permisos al usuario
//        userDetails.setPermisos(responsePermisos.getLista());

//        response.setLista(responsePermisos.getLista());
        response.setEstatus("OK");
        response.setMensaje("JWT Generado!");
        response.setDto(sesion);

        log.info("USUARIO QUE INGRESO : ");
        log.info(userDetails.getNombreUsuario());
        log.info(userDetails.getNombre());
        log.info(userDetails.getApellidoPaterno());
        log.info(userDetails.getApellidoMaterno());

        log.info("TERMINA LOGIN USUARIO !!!");
        log.info("-");

        return new ResponseEntity<ResponseDTO<SesionDto>>(response, HttpStatus.OK);
    }


    @PostMapping("/nuevo")
    public ResponseEntity<ResponseDTO<UsuarioInterno>> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario) throws ServiceException {


        log.info("-");
        log.info("INICIA REGISTRO USUARIO !!!");

        log.info("-");


        ResponseDTO<UsuarioInterno> response = new ResponseDTO<UsuarioInterno>();

        if (usuarioInternoService.existsByEmail(nuevoUsuario.getCorreoUsuario())) {

            log.info("ESTE CORREO YA EXISTE");

            response.setEstatus("ERROR");
            response.setMensaje("ESTE CORREO YA EXISTE");
            response.setDto(new UsuarioInterno());
            return new ResponseEntity<ResponseDTO<UsuarioInterno>>(response, HttpStatus.BAD_REQUEST);
        }


        UsuarioInterno usuario = new UsuarioInterno();

        usuario.setNombreUsuario(nuevoUsuario.getNombreUsuario());
        usuario.setApellidoPaterno(nuevoUsuario.getApellidoPaterno());
        usuario.setApellidoMaterno(nuevoUsuario.getApellidoPaterno());
        
        usuario.setCorreoUsuario(nuevoUsuario.getCorreoUsuario());
        usuario.setContrasenia(nuevoUsuario.getContrasenia());
        
        usuario.setEstatus(1);
        usuario.setFechaCreacion(LocalDateTime.now());
    
        usuario.setTotalDinero(0.0);
        usuario.setMargenLibre(0.0);
        usuario.setMargen(0.0);
        
        RolEntity rol = new RolEntity();
        rol.setIdRol(nuevoUsuario.getRol());
        usuario.setRol(rol);

        usuario.setIdAdmin(nuevoUsuario.getIdAdmin());
        usuario.setRecuperacion(nuevoUsuario.getRecuperacion());

        log.info("EJECUTANDO QUERY REGISTRO !!!");

        UsuarioInterno nvoUsuario = usuarioInternoService.save(usuario);

        log.info("-");

        response.setEstatus("OK");
        response.setMensaje("Guardado correcto");
        response.setDto(nvoUsuario);

        log.info("TERMINA REGISTRO PROVEEDOR !!!");

        return new ResponseEntity<ResponseDTO<UsuarioInterno>>(response, HttpStatus.CREATED);
    }

    @PostMapping("/validateToken")
    public ResponseEntity<ResponseDTO> validateToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            if (!authHeader.startsWith("Bearer")) {
                throw new RuntimeException("Missing auth informacion");
            }

            String[] parts = authHeader.split(" ");
            responseDTO = jwtProvider.validateToken(parts[1]);
        } catch (Exception e) {
            responseDTO = new ResponseDTO();
            responseDTO.setEstatus(HttpStatus.UNAUTHORIZED.name());
            responseDTO.setMensaje("Error al procesar el token: " + e.getMessage());
        }


        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ResponseDTO> refreshToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        ResponseDTO responseDTO;
        try {
            if (!authHeader.startsWith("Bearer")) {
                throw new RuntimeException("Missing auth informacion");
            }

            String[] parts = authHeader.split(" ");

            responseDTO = jwtProvider.refreshToken(parts[1]);
        } catch (Exception e) {
            responseDTO = new ResponseDTO();
            responseDTO.setEstatus(HttpStatus.UNAUTHORIZED.name());
            responseDTO.setMensaje("Error al procesar el token: " + e.getMessage());
        }
        return new ResponseEntity<>(responseDTO, HttpStatus.valueOf(responseDTO.getEstatus()));
    }
    
    // Método para eliminar el token de un usuario
    @DeleteMapping("/deleteToken")
    public ResponseEntity<ResponseDTO> deleteToken(@RequestHeader(value = "Authorization") String authHeader) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            // Llamamos al servicio para eliminar el token correspondiente al userId
            

            // Respuesta exitosa
           // responseDTO.setEstatus(HttpStatus.OK.name());
          //  responseDTO.setMensaje("Token eliminado correctamente");
        	String token = authHeader.substring(7);
            responseDTO = jwtProvider.deleteToken(token);

        } catch (Exception e) {
            // Manejo de errores
            responseDTO.setEstatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
            responseDTO.setMensaje("Error al eliminar el token: " + e.getMessage());
        }

        return new ResponseEntity<>(responseDTO, HttpStatus.valueOf(responseDTO.getEstatus()));
    }
}