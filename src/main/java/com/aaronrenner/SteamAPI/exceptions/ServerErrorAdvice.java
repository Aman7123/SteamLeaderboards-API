package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@ControllerAdvice
public class ServerErrorAdvice  {

	@ResponseBody
	@ExceptionHandler(ServerError.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	String serverError(ServerError sE) {
		return sE.getMessage();
	}
}
