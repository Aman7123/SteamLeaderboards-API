package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class FriendNotFoundAdvice  {

	@ResponseBody
	@ExceptionHandler(FriendNotFound.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String friendNotFound(FriendNotFound fNF) {
		return fNF.getMessage();
	}
}
