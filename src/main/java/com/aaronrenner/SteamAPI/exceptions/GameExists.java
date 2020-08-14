package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class GameExists extends RuntimeException {
	public GameExists(long appID){
        super(String.format("The game associated with ID: %d already exists", appID));
    }
}
