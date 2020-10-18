package com.aaronrenner.SteamAPI.services;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aaronrenner.SteamAPI.exceptions.GameExists;
import com.aaronrenner.SteamAPI.exceptions.GameNotFound;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.repositories.GameRepository;

@Service
public class GameServiceIMPL implements GameService {

	@Autowired
	GameRepository gameRepository;

	@Override
	public List<Game> getGameList() {
		return gameRepository.findAll();
	}

	@Override
	public List<Game> getGameByTitle(String gameTitle) {
		return gameRepository.findByTitleContains(gameTitle);
	}

	@Override
	public Optional<Game> getGameByID(long GameAppID) {
		Optional<Game> findAGame= gameRepository.findById(GameAppID);
		if(findAGame.isPresent()) {
			return findAGame;
		} else {
			throw new GameNotFound(GameAppID);
		}
	}

	@Override
	public Game updateGame(long appID, Game updateGame) {
		Game storedGameModel = getGameByID(appID).get();
		if(updateGame.getTitle() != null) {
			storedGameModel.setTitle(updateGame.getTitle());
		}
		return this.gameRepository.save(storedGameModel);
	}

	@Override
	public Game createGame(Game newGame) {
		Optional<Game> checkGame = gameRepository.findById(newGame.getId());
		if(!checkGame.isPresent()) {
			return gameRepository.save(newGame);
		} else {
			throw new GameExists(newGame.getId());
		}
	}

	@Override
	public void deleteGame(long GameAppID) {
		Optional<Game> checkGame = gameRepository.findById(GameAppID);

		if(checkGame.isPresent()) {
			this.gameRepository.deleteById(GameAppID);
		} else {
			throw new GameNotFound(GameAppID);
		}
	}

}
