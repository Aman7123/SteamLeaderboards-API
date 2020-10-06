package com.aaronrenner.SteamAPI.controllers;

import java.util.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.TokenTest;
import com.aaronrenner.SteamAPI.services.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GameControllerTest {
	
	
	private String adminToken = "Bearer " + new TokenTest().getToken();
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private GameService gameService;
	
	@Before
	@Test
	private void setup() {
	}
	
	@Test
	public void lookupAllGames() throws Exception {
		List<Game> fakeGameList = fakeGameList();
		Mockito.when(gameService.getGameList()).thenReturn(fakeGameList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/games")).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
		Game[] games = om.readValue(mvcResult.getResponse().getContentAsString(), Game[].class);
		assert(games.length > 0);
	}
	
	@Test
	public void lookupGamesByTitle() throws Exception {
		List<Game> fakeGameList = fakeGameList();
		Mockito.when(gameService.getGameList()).thenReturn(fakeGameList);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/games").queryParam("title", "Half")).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
		Game[] games = om.readValue(mvcResult.getResponse().getContentAsString(), Game[].class);
		assert(games.length > 0);
	}
	
	@Test
	public void lookupGamesByID() throws Exception {
		Game fakeGame = fakeGame(0);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/games/252490")).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
		Game games = om.readValue(mvcResult.getResponse().getContentAsString(), Game.class);
		assert(games.getTitle() != null);
	}
	
	@Test
	public void updateGame() throws Exception {
		Game rustGame = new Game(252490, "Rust");
		
		String rustGame_JSON = om.writeValueAsString(rustGame);
		Mockito.when(gameService.updateGame(252490, rustGame)).thenReturn(rustGame);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/games/252490").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(rustGame_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isAccepted()).andReturn();
	}
	
	@Test
	public void addGameAndRemove() throws Exception {
		//Test if the damn game exists
		gameService.deleteGame(0);
			
		Game testGame = fakeGame(0);
		String testGame_JSON = om.writeValueAsString(testGame);
		
		Mockito.when(gameService.createGame(Mockito.any(Game.class))).thenReturn(testGame);
		
		//MvcResult createGame = mockMvc.perform(MockMvcRequestBuilders.post("/games").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(testGame_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
		
		//MvcResult deleteGameAgain = mockMvc.perform(MockMvcRequestBuilders.delete("/games/0").header("Authorization", adminToken)).andDo(print()).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
		
	}
	
	private List<Game> fakeGameList() {
		List<Game> bufferGames = new ArrayList<>();
		bufferGames.add(fakeGame(0));
		bufferGames.add(fakeGame(1));
		return bufferGames;
	}
	
	private Game fakeGame(long id) {
		return new Game(id, "test_game");
	}

}
