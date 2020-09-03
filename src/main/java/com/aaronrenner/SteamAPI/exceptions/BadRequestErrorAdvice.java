package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@ControllerAdvice
public class BadRequestErrorAdvice  {
	
	@ResponseBody
	@ExceptionHandler(BadRequest.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String badRequestError(BadRequest bRE) {
		return bRE.getMessage();
	}
}
