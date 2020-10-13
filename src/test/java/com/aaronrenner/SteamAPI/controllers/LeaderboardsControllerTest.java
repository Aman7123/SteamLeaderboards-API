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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.aaronrenner.SteamAPI.models.SteamProfile;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;
import com.aaronrenner.SteamAPI.models.TokenTest;
import com.aaronrenner.SteamAPI.services.LeaderboardServiceIMPL;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LeaderboardsControllerTest {
	
	private String adminToken = "Bearer " + new TokenTest().getToken();
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LeaderboardServiceIMPL leaderboardsService;
	
	@BeforeEach
	public void SetUp() {

		Mockito.when(leaderboardsService.getSteamProfile(Mockito.anyString())).thenReturn(new ArrayList<SteamProfile>());
		Mockito.when(leaderboardsService.getSteamStats(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<SteamUserStatInfo>());
		Mockito.when(leaderboardsService.getSteamAchievements(Mockito.anyString(), Mockito.anyString())).thenReturn(new ArrayList<SteamUserAchievementInfo>());
	}
	
	@AfterEach
	public void tearDown() {
	}
	
	@Test
	private void injectedComponentsAreNotNull() {
		assertNotNull(mockMvc);
		assertNotNull(leaderboardsService);
	}
	
	@Test
	public void getSteamProfile() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/leaderboards/76561198089525491/profiles")).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void getSteamStats() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/leaderboards/76561198089525491/statistics/games/252490")).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
	}
	
	@Test
	public void getSteamAchievements() throws Exception {
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/leaderboards/76561198089525491/achievements/games/252490")).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
	}
}
