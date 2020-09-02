package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class LoginErrorAdvice  {
	
	@ResponseBody
	@ExceptionHandler(LoginError.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	String loginError(LoginError lE) {
		return lE.getMessage();
	}
}
