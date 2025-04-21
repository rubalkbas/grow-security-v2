/**
 *
 */
package com.ittiva.generic.security.jwt;

import com.ittiva.generic.security.config.JwtConfig;
import com.ittiva.generic.security.dto.ResponseDTO;
import com.ittiva.generic.security.service.RedisService;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

/**
 * @author ITTIVA
 */
@Slf4j
@Component
public class JwtProvider {

    private final Key secretKey;

    private final JwtConfig jwtConfig;

    @Autowired
    private RedisService redisService;

    public JwtProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.secretKey = Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes());
    }

    public String generateToken(String userId, String username) {
        //UsuarioPrincipal usuarioPrincipal = (UsuarioPrincipal) authentication.getPrincipal();
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", userId);
        Date expiration = new Date(System.currentTimeMillis() + (jwtConfig.getExpiration() * 1000));
        log.info("Expiration:{}", expiration.getTime());
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        //redisService.storeToken(String.valueOf(userId), jwt, expiration.getTime());

        return jwt;
    }

    public String getNombreUsuarioFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public ResponseDTO validateToken(String token) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            // Parsear y validar el token
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            if (!redisService.isTokenValid(String.valueOf(claims.get("userId")))) {
                log.warn("El token no se encuentra en Redis o ha sido revocado.");
                setErrorResponse(responseDTO, "El token no es válido o ha sido revocado.", HttpStatus.UNAUTHORIZED);
                return responseDTO;
            }


            // Validar el issuer del token
            if (jwtConfig.getIssuer().equals(claims.getIssuer())) {
                responseDTO.setEstatus(HttpStatus.ACCEPTED.name());
                responseDTO.setMensaje(String.format("Token válido para el usuario: %s", getNombreUsuarioFromToken(token)));
                responseDTO.setLista(List.of("Claims", claims));
                log.debug("Token válido y firmado por: {}", claims.getIssuer());
                return responseDTO;
            }

            // Respuesta en caso de que el issuer no coincida
            responseDTO.setEstatus(HttpStatus.UNAUTHORIZED.name());
            responseDTO.setMensaje(String.format("Token inválido. No está firmado por la app esperada: %s", jwtConfig.getIssuer()));
            responseDTO.setCodError(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
            log.warn("Token no firmado por la app: {}", jwtConfig.getIssuer());

        } catch (ExpiredJwtException e) {
            log.warn("El token ha expirado: {}", e.getMessage());
            setErrorResponse(responseDTO, "El token ha expirado", HttpStatus.UNAUTHORIZED);

        } catch (io.jsonwebtoken.security.SignatureException e) {
            log.warn("La firma del token no es válida: {}", e.getMessage());
            setErrorResponse(responseDTO, "La firma del token no es válida", HttpStatus.UNAUTHORIZED);

        } catch (MalformedJwtException e) {
            log.warn("El token está mal formado: {}", e.getMessage());
            setErrorResponse(responseDTO, "El token está mal formado", HttpStatus.UNAUTHORIZED);

        } catch (UnsupportedJwtException e) {
            log.warn("El token no es compatible: {}", e.getMessage());
            setErrorResponse(responseDTO, "El token no es compatible", HttpStatus.UNAUTHORIZED);

        } catch (IllegalArgumentException e) {
            log.warn("El token es nulo o está vacío: {}", e.getMessage());
            setErrorResponse(responseDTO, "El token es nulo o está vacío", HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            log.error("Error inesperado al validar el token: {}", e.getMessage());
            setErrorResponse(responseDTO, "Error inesperado al validar el token", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseDTO;
    }

    public ResponseDTO refreshToken(String token) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            // Validar y extraer claims del token
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Validar si el token aún está vigente
            if (claims.getExpiration().before(new Date())) {
                log.warn("El token ha expirado y no puede ser refrescado.");
                setErrorResponse(responseDTO, "El token ha expirado. Solicita un nuevo inicio de sesión.", HttpStatus.UNAUTHORIZED);
                return responseDTO;
            }
            // Obtener información necesaria de los claims
            String userId = String.valueOf(claims.get("userId"));
            String username = claims.getSubject();
            var jwt = generateToken(userId, username);

            // Respuesta con el nuevo token
            responseDTO.setEstatus(HttpStatus.OK.name());
            responseDTO.setMensaje("Token refrescado exitosamente.");
            responseDTO.setLista(Collections.singletonList(jwt));
            log.info("Token refrescado para el usuario: {}", userId);

        } catch (ExpiredJwtException e) {
            log.warn("El token ha expirado y no puede ser refrescado: {}", e.getMessage());
            setErrorResponse(responseDTO, "El token ha expirado. Solicita un nuevo inicio de sesión.", HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            log.error("Error inesperado al refrescar el token: {}", e.getMessage());
            setErrorResponse(responseDTO, "Error inesperado al refrescar el token.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseDTO;
    }
    
    

    public ResponseDTO deleteToken(String token) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userId = String.valueOf(claims.get("userId"));

            if (redisService.getToken(userId) == null) {
                log.warn("El token ya ha sido eliminado o no existe en Redis.");
                setErrorResponse(responseDTO, "El token ya ha sido eliminado o no existe.", HttpStatus.NOT_FOUND);
            } else {
                redisService.deleteToken(userId);
                log.info("Token eliminado correctamente para el usuario: {}", userId);
                responseDTO.setEstatus(HttpStatus.OK.name());
                responseDTO.setMensaje("Token eliminado correctamente.");
            }

        } catch (IllegalArgumentException e) {
            log.warn("El token es nulo o vacío: {}", e.getMessage());
            setErrorResponse(responseDTO, "El token es nulo o vacío.", HttpStatus.BAD_REQUEST);

        } catch (RedisConnectionFailureException e) {
            log.error("Error de conexión con Redis: {}", e.getMessage());
            setErrorResponse(responseDTO, "Error de conexión con Redis.", HttpStatus.SERVICE_UNAVAILABLE);

        } catch (Exception e) {
            log.error("Error inesperado al eliminar el token: {}", e.getMessage());
            setErrorResponse(responseDTO, "Error inesperado al eliminar el token.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseDTO;
    }

    // Método auxiliar para configurar la respuesta de error
    private void setErrorResponse(ResponseDTO responseDTO, String mensaje, HttpStatus status) {
        responseDTO.setEstatus(status.name());
        responseDTO.setMensaje(mensaje);
        responseDTO.setCodError(String.valueOf(status.value()));
    }


}