package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserExists extends RuntimeException {
	public UserExists(String steamID64){
        super(String.format("The user associated with the SteamID: %d already exists", steamID64));
    }
}
