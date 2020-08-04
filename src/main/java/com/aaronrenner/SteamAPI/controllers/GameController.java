package com.aaronrenner.SteamAPI.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.services.GameService;

@RestController
public class GameController {
	
	final private String GAMEURL = "/games";
	final private String SELECTGAMEURL = "/{appID}";
	
	@Autowired
	private GameService gameService;

	// Root Url
	@GetMapping(GAMEURL)
	public List getGameByName(@RequestParam(value = "title") String gameTitle) {
		return this.gameService.getGameByTitle(gameTitle);
	}
	
	@GetMapping(GAMEURL)
	public Game getGameByID(@RequestParam(value = "appID") long gameAppID) {
		return this.gameService.getGameByID(gameAppID);
	}
	
	@PostMapping(GAMEURL)
	public Game createGame(@RequestBody Game gameEntry) {	
		return this.gameService.createGame(gameEntry);
	}
	
	// TODO Edit OAS if I keep delete like this
	@DeleteMapping(SELECTGAMEURL)
	public boolean deleteGame(@PathVariable long gameAppID) {
		return this.gameService.deleteGame(gameAppID);
	}
	
}
