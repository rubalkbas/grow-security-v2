/**
 * 
 */
package com.ittiva.generic.security.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.ittiva.generic.security.dto.ResponseDTO;
import com.ittiva.generic.util.GenericasConstantes;

/**
 * @author ittiva
 *
 */
@EnableWebMvc
@ControllerAdvice
public class ExceptionHandler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);
	
	@org.springframework.web.bind.annotation.ExceptionHandler(ServiceException.class)
	public ResponseEntity<ResponseDTO<String>> handleServiceException(ServiceException ex) {
		LOGGER.error("Error ", ex);
		LOGGER.error("Mensage ", ex.getMessage());

		ResponseDTO<String> response = new ResponseDTO<>();
		response.setEstatus(GenericasConstantes.ERROR);
		response.setMensaje(ex.getMessage());
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(response,headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
