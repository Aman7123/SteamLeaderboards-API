package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class UserExistsAdvice  {

	@ResponseBody
	@ExceptionHandler(UserExists.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String userExists(UserExists uE) {
		return uE.getMessage();
	}
}
