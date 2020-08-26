package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GameExistsAdvice  {
	
	@ResponseBody
	@ExceptionHandler(GameExists.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String gameExists(GameExists gE) {
		return gE.getMessage();
	}
}
