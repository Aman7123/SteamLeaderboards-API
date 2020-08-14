package com.aaronrenner.SteamAPI.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
	public Optional<Game> getGameByID(@PathVariable long appID) {
		return this.gameService.getGameByID(appID);
	}
	
	@PostMapping(GAMEURL)
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createGame(@RequestBody Game gameEntry) {	
		this.gameService.createGame(gameEntry);
	}
	
	@DeleteMapping(SELECTGAMEURL)
	public void deleteGame(@PathVariable long appID) {
		this.gameService.deleteGame(appID);
	}
	
}
