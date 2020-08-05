package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends RuntimeException {
	public UserNotFound(String steamID64){
        super(String.format("The user associated with steamID: %d was not found", steamID64));
    }
}
