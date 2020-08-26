package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class FriendExistsAdvice  {
	
	@ResponseBody
	@ExceptionHandler(FriendExists.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	String friendExists(FriendExists fE) {
		return fE.getMessage();
	}
}
