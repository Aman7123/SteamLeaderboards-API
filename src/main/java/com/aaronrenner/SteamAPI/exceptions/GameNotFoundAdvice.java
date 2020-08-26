package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GameNotFoundAdvice  {
	
	@ResponseBody
	@ExceptionHandler(GameNotFound.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String gameNotFound(GameNotFound gNF) {
		return gNF.getMessage();
	}
}
