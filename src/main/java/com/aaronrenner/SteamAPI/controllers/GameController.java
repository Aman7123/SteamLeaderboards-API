package com.aaronrenner.SteamAPI.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.aaronrenner.SteamAPI.exceptions.AuthorizationError;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.GameService;
import com.aaronrenner.SteamAPI.services.LoginService;

@RestController
public class GameController {
	
	final private String GAMEURL = "/games";
	final private String SELECTGAMEURL = GAMEURL + "/{appID}";
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private LoginService loginService;

	// Root Url
	@GetMapping(GAMEURL)
	public List<Game> getGameList(@RequestParam(value = "title") Optional<String> gameTitle) {
		if(gameTitle.isPresent()) {
		 return this.gameService.getGameByTitle(gameTitle.get());
		}
		
		return this.gameService.getGameList();
	}
	
	@GetMapping(SELECTGAMEURL)
	public Optional<Game> getGameByID(@PathVariable long appID) {
		return this.gameService.getGameByID(appID);
	}
	
	@PatchMapping(SELECTGAMEURL)
	@ResponseStatus(value= HttpStatus.ACCEPTED)
	public void updateGame(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable long appID, @RequestBody Game gameModel) {
		if(oAuthToken.isPresent()) { // if authorization
			User approvedeUser = getUserFromAuth(oAuthToken.get());
			if(approvedeUser.getRole().equals("admin")) {
				this.gameService.updateGame(appID, gameModel);
			}
		} else {
			// Catch no included OAuth header
			throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");			
		}

	}
	
	@PostMapping(GAMEURL)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createGame(@RequestHeader("Authorization") Optional<String> oAuthToken, @RequestBody Game gameEntry) {
		if(oAuthToken.isPresent()) { // if authorization
			User approvedeUser = getUserFromAuth(oAuthToken.get());
			if(approvedeUser.getRole().equals("admin")) {
				this.gameService.createGame(gameEntry);
			} 
		} else {
			// Catch no included OAuth header
			throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");			
		}

	}
	
	@DeleteMapping(SELECTGAMEURL)
	public void deleteGame(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable long appID) {
		if(oAuthToken.isPresent()) { // if authorization
			User approvedeUser = getUserFromAuth(oAuthToken.get());
			if(approvedeUser.getRole().equals("admin")) {
				this.gameService.deleteGame(appID);
			} else {
				throw new AuthorizationError("Missing permission, you cannot delete a game");
			}
		} else {
			// Catch no included OAuth header
			throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");			
		}

	}
	
	private User getUserFromAuth(String bearerPass) {
		String[] parseToken = bearerPass.split(" "); // split [0]="bearer", [1]="xxx"
		if(parseToken[0].equals("Bearer")) { // if Bearer is Bearer
			return loginService.validateToken(parseToken[1]);
		}
		// Catch wrong auth format
		throw new AuthorizationError("JWT token bad format, check header");

	}
	
}
