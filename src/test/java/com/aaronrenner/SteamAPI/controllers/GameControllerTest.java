package com.aaronrenner.SteamAPI.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.TokenTest;
import com.aaronrenner.SteamAPI.services.GameServiceIMPL;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class GameControllerTest {
	
	private String adminToken = "Bearer " + new TokenTest().getToken();
	private String userToken = "Bearer " + new TokenTest().getToken_user();
	
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
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
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/games/3").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(updatedGame_AsJSON).accept(MediaType.APPLICATION_JSON)).andReturn();
		
		assert(202 == mvcResult.getResponse().getStatus());
		
		// For Error
		MvcResult mvcResult_Error_NoToken = mockMvc.perform(MockMvcRequestBuilders.patch("/games/3").contentType(MediaType.APPLICATION_JSON).content(updatedGame_AsJSON).accept(MediaType.APPLICATION_JSON)).andReturn();
		assert(401 == mvcResult_Error_NoToken.getResponse().getStatus());
		MvcResult mvcResult_Error_NoPermission = mockMvc.perform(MockMvcRequestBuilders.patch("/games/3").header("Authorization", userToken).contentType(MediaType.APPLICATION_JSON).content(updatedGame_AsJSON).accept(MediaType.APPLICATION_JSON)).andReturn();
		assert(401 == mvcResult_Error_NoPermission.getResponse().getStatus());
	}
	
	@Test
	public void addGame() throws Exception {
		String game_AsJSON = om.writeValueAsString(fakeGame);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/games").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).content(game_AsJSON).accept(MediaType.APPLICATION_JSON)).andReturn();
		assert(201 == mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void deleteGame() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/games/03").header("Authorization", adminToken)).andReturn();

	}
	
	@Test
	public void noLoginToken() throws Exception {
		MvcResult mvcResult_DeleteGame = mockMvc.perform(MockMvcRequestBuilders.delete("/games/9898")).andReturn();
		assert(401 == mvcResult_DeleteGame.getResponse().getStatus());
		MvcResult mvcResult_AddGame = mockMvc.perform(MockMvcRequestBuilders.post("/games/").contentType(MediaType.APPLICATION_JSON).content("{}")).andReturn();
		assert(401 == mvcResult_AddGame.getResponse().getStatus());
		MvcResult mvcResult_UpdateGame = mockMvc.perform(MockMvcRequestBuilders.patch("/users/9898").contentType(MediaType.APPLICATION_JSON).content("{}")).andReturn();
		assert(401 == mvcResult_UpdateGame.getResponse().getStatus());
		
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
