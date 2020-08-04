package com.aaronrenner.SteamAPI.services;

import java.util.List;

import com.aaronrenner.SteamAPI.models.Game;

public interface GameService {
	
	List<Game> getGameList();
	List<Game> getGameByTitle(String gameTitle);
	Game getGameByID(long GameAppID);
	Game createGame(Game newGame);
	boolean deleteGame(long GameAppID);
	
}
