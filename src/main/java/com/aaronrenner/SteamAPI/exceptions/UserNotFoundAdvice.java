package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class UserNotFoundAdvice  {

	@ResponseBody
	@ExceptionHandler(UserNotFound.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String userNotFound(UserNotFound uNF) {
		return uNF.getMessage();
	}
}
