package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class AuthorizationErrorAdvice  {

	@ResponseBody
	@ExceptionHandler(AuthorizationError.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String authError(AuthorizationError aE) {
		return aE.getMessage();
	}
}
