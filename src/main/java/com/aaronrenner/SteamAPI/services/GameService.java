package com.aaronrenner.SteamAPI.services;

import java.util.List;

import com.aaronrenner.SteamAPI.models.Game;

public interface GameService {
	
	List getGameByTitle(String gameTitle);
	Game getGameByID(long GameAppID);
	Game createGame(Game newGame);
	void deleteGame(long GameAppID);
	
}
