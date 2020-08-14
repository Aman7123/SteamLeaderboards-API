package com.aaronrenner.SteamAPI.services;

import java.util.List;
import java.util.Optional;

import com.aaronrenner.SteamAPI.models.Game;

public interface GameService {
	
	List<Game> getGameList();
	List<Game> getGameByTitle(String gameTitle);
	Optional<Game> getGameByID(long GameAppID);
	Game createGame(Game newGame);
	void deleteGame(long GameAppID);
	
}
