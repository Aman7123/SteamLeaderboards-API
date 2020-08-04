package com.aaronrenner.SteamAPI.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameNotFound extends RuntimeException {
	public GameNotFound(long appID){
        super(String.format("The game associated with ID: %d was not found", appID));
    }
}
