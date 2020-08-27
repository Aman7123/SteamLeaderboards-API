package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class SteamErrorAdvice  {
	
	@ResponseBody
	@ExceptionHandler(SteamError.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String steamError(SteamError sE) {
		return sE.getMessage();
	}
}
