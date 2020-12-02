package com.aaronrenner.SteamAPI.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import com.aaronrenner.SteamAPI.exceptions.GameNotFound;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.repositories.GameRepository;

@SpringBootTest
public class GameServiceTest {
	
	@InjectMocks
	private GameServiceIMPL gameService;
	
	@Mock
	private GameRepository gameRepository;
	
	
	private List<Game> fakeGameList;
	private Game fakeGame;
	
	@BeforeEach
	public void setUp() {
		fakeGameList = fakeGameList();
		fakeGame = fakeGame(3);
		//getGameList
		Mockito.when(gameRepository.findAll()).thenReturn(fakeGameList);
		//getGameByTitle
		Mockito.when(gameRepository.findByTitleContains(Mockito.anyString())).thenReturn(fakeGameList);
		//getGameByID
		Optional<Game> fakeGame_Optional = Optional.of(fakeGame);
		Mockito.when(gameRepository.findById(Mockito.anyLong())).thenReturn(fakeGame_Optional);
		//createGame
		Mockito.when(gameRepository.save(Mockito.any(Game.class))).thenReturn(fakeGame);
	}
	
	@AfterEach
	public void tearDown() {
		this.fakeGameList = null;
		this.fakeGame = null;
	}
	
	@Test
	public void getGameList_Success() {
		List<Game> realGameList = gameService.getGameList();
		
		assertNotNull(realGameList);
		assert(fakeGameList.size() == realGameList.size());
	}
	
	@Test
	public void getGameByTitle_Success() {
		List<Game> realGameList = gameService.getGameByTitle("anything here should work");
		
		assertNotNull(realGameList);
		assert(fakeGameList.size() == realGameList.size());
	}
	
	@Test
	public void getGameByID_Success() {
		Game realGame = gameService.getGameByID(3).get();
		
		assertNotNull(realGame);
		assert(fakeGame.getTitle() == realGame.getTitle());
	}
	
	@Test
	public void createGame_Success() {
		Mockito.when(gameRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Game realGame = gameService.createGame(fakeGame);
		int gameCountBefore = fakeGameList.size();
		fakeGameList.add(fakeGame);

		assert(realGame.getTitle() == fakeGame.getTitle());
		assert(gameCountBefore < fakeGameList.size());
	}
	
	@Test 
	public void updateGame_Success() {
		Game fakeGame_updated = fakeGame;
		fakeGame_updated.setTitle("updated_name");
		fakeGame = fakeGame(3);
		
		fakeGame_updated = gameService.updateGame(fakeGame_updated.getId(), fakeGame_updated);
		
		assert(fakeGame_updated.getTitle() != fakeGame.getTitle());
	}
	
	@Test
	public void deleteGame_Executed() {
		Mockito.when(gameRepository.findById((long) 100)).thenReturn(Optional.empty());
		gameService.deleteGame(110);
		// For Error
		assertThrows(GameNotFound.class, () -> {
			gameService.deleteGame(100);
		});
	}
	
	private List<Game> fakeGameList() {
		List<Game> bufferGames = new ArrayList<>();
		bufferGames.add(fakeGame(1));
		bufferGames.add(fakeGame(2));
		return bufferGames;
	}
	
	private Game fakeGame(long id) {
		return new Game(id, "test_game");
	}
}
