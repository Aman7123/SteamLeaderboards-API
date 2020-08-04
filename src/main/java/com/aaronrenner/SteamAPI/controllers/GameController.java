package com.aaronrenner.SteamAPI.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aaronrenner.SteamAPI.exceptions.GameNotFound;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.services.GameService;

@RestController
public class GameController {
	
	final private String GAMEURL = "/games";
	final private String SELECTGAMEURL = GAMEURL + "/{appID}";
	
	@Autowired
	private GameService gameService;

	// Root Url
	@GetMapping(GAMEURL)
	public List<Game> getGameList(@RequestParam(value = "title") Optional<String> gameTitle) {
		if(gameTitle.isPresent()) {
		 return this.gameService.getGameByTitle(gameTitle.get());
		}
		
		return this.gameService.getGameList();
	}
	
	@GetMapping(SELECTGAMEURL)
	public Game getGameByID(@PathVariable long appID) {
		Game gameSearch = this.gameService.getGameByID(appID);
		if(gameSearch == null) {
			throw new GameNotFound(appID);
		}
		return gameSearch;
	}
	
	@PostMapping(GAMEURL)
	public Game createGame(@RequestBody Game gameEntry) {	
		return this.gameService.createGame(gameEntry);
	}
	
	@DeleteMapping(SELECTGAMEURL)
	public void deleteGame(@PathVariable long appID) {
		boolean action = this.gameService.deleteGame(appID);
		if(!action) {
			throw new GameNotFound(appID);
		}
	}
	
}
