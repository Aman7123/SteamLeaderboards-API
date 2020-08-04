package com.aaronrenner.SteamAPI.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.aaronrenner.SteamAPI.models.Game;

@Service
public class GameServiceIMPL implements GameService {
	
	private List<Game> gameList;
	
	public GameServiceIMPL() {
		this.gameList = new ArrayList<>();
	}

	@Override
	public List<Game> getGameList() {
		return gameList;
	}
	
	@Override
	public List<Game> getGameByTitle(String gameTitle) {
		
		List<Game> bufferList = new ArrayList<>();
		
		for(Game newGame: gameList) {
			if(newGame.getTitle().equalsIgnoreCase(gameTitle)) {
				bufferList.add(newGame);
			}
		}
		
		return bufferList;
	}

	@Override
	public Game getGameByID(long GameAppID) {
		
		for(Game newGame: gameList) {
			if(newGame.getId() == GameAppID) {
				return newGame;
			}
		}
		
		return null;
	}

	@Override
	public Game createGame(Game newGame) {
		//Test if it exists
		if(gameList.contains(newGame)) {
		} else {
			gameList.add(newGame);
			return newGame;
		}
		return null;
	}

	@Override
	public boolean deleteGame(long GameAppID) {
		for(Game newGame: gameList) {
			if(newGame.getId() == GameAppID) {
				return gameList.remove(newGame);
			}
		}
		return false;
	}

}
