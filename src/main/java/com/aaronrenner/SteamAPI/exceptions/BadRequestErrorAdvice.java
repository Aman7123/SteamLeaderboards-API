package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@ControllerAdvice
public class BadRequestErrorAdvice  {

	@ResponseBody
	@ExceptionHandler(BadRequestError.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String badRequestError(BadRequestError bRE) {
		return bRE.getMessage();
	}
}
