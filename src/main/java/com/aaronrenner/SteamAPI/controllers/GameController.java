package com.aaronrenner.SteamAPI.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.services.GameService;

@RestController
@RequestMapping("/games")
public class GameController {
	
	@Autowired
	private GameService gameService;

	// Root Url
	@PostMapping("")
	public Game createGame(@RequestBody Game gameEntry) {	
		return this.gameService.createGame(gameEntry);
	}
	
	// TODO Edit OAS if I keep delete like this
	@DeleteMapping("/{appID}")
	public void deleteGame(@PathVariable long gameAppID) {
		this.gameService.deleteGame(gameAppID);
	}
	
	@GetMapping("")
	public List getGameByName(@RequestParam(value = "title") String gameTitle) {
		return this.gameService.getGameByTitle(gameTitle);
	}
	
	@GetMapping("")
	public Game getGameByID(@RequestParam(value = "appID") long gameAppID) {
		return this.gameService.getGameByID(gameAppID);
	}
	
}
