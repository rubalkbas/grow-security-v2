/**
 * 
 */
package com.ittiva.generic.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ittiva.generic.security.service.UserDetailsServiceImpl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;



import lombok.extern.slf4j.Slf4j;

/**
 *  @author ITTIVA
 *
 */
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {


    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) 
    		throws ServletException, IOException {
        try {
            String token = getToken(req);
            if(token != null && "ACCEPTED".equals(jwtProvider.validateToken(token).getEstatus())){
                String nombreUsuario = jwtProvider.getNombreUsuarioFromToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(nombreUsuario);

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e){
            log.error("fail en el método doFilter " + e.getMessage());
        }
        filterChain.doFilter(req, res);
    }
    
    private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer"))
            return header.replace("Bearer ", "");
        return null;
    }	
	
    
}
