package com.aaronrenner.SteamAPI.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.TokenTest;
import com.aaronrenner.SteamAPI.services.GameServiceIMPL;
import com.aaronrenner.SteamAPI.services.GameServiceTest;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {
	
	private String adminToken = "Bearer " + new TokenTest().getToken();
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@InjectMocks
	
	@MockBean
	private GameServiceIMPL gameService;

	
	private List<Game> fakeGameList;
	private Game fakeGame;
	
	@BeforeEach
	public void SetUp() {
		fakeGameList = fakeGameList();
		fakeGame = fakeGame(3);
		Optional<Game> fakeGame_Optional = Optional.of(fakeGame);
		Mockito.when(gameService.getGameList()).thenReturn(fakeGameList);
		Mockito.when(gameService.getGameByID(Mockito.anyLong())).thenReturn(fakeGame_Optional);
		Mockito.when(gameService.getGameByTitle(Mockito.anyString())).thenReturn(fakeGameList);
		Mockito.when(gameService.createGame(Mockito.any(Game.class))).thenReturn(fakeGame);
		Mockito.when(gameService.updateGame(Mockito.anyLong(), Mockito.any(Game.class))).thenReturn(fakeGame);
	}
	
	@AfterEach
	public void tearDown() {
		fakeGameList = null;
		fakeGame = null;
	}
	
	@Test
	private void injectedComponentsAreNotNull() {
		assertNotNull(mockMvc);
		assertNotNull(gameService);
	}
	
	
	@Test
	public void lookupAllGames() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/games")).andReturn();
		Game[] realGameList = om.readValue(mvcResult.getResponse().getContentAsString(), Game[].class);
		
		assert(200 == mvcResult.getResponse().getStatus());
		assert(realGameList.length > 0);
	}
	
	@Test
	public void lookupGamesByTitle() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/games").queryParam("title", "Half")).andReturn();
		Game[] realGameList = om.readValue(mvcResult.getResponse().getContentAsString(), Game[].class);
		
		assert(200 == mvcResult.getResponse().getStatus());
		assert(realGameList.length > 0);
	}
	
	@Test
	public void lookupGamesByID() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/games/252490")).andReturn();	
		Game realGame = om.readValue(mvcResult.getResponse().getContentAsString(), Game.class);
		
		assert(200 == mvcResult.getResponse().getStatus());
		assert(realGame.getId() == 3);
		
	}
	
	@Test
	public void updateGame() throws Exception {
		String updatedGame_AsJSON = om.writeValueAsString(fakeGame);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/games/3").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(updatedGame_AsJSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isAccepted()).andReturn();
		
		assert(202 == mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void addGame() throws Exception {
		String game_AsJSON = om.writeValueAsString(fakeGame);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/games").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(game_AsJSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
		assert(201 == mvcResult.getResponse().getStatus());
	}
	
	// TODO add DELETE CRUD method check
	
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
