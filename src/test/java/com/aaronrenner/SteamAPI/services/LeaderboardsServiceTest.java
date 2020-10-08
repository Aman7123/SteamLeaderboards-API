package com.aaronrenner.SteamAPI.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import com.aaronrenner.SteamAPI.repositories.GameRepository;
import com.aaronrenner.SteamAPI.repositories.UserRepository;

@SpringBootTest
public class LeaderboardsServiceTest {
	
	@InjectMocks
	private LeaderboardServiceIMPL leaderboardService;
	
	@Mock
	private GameRepository gameRepository;
	
	@Mock
	private UserRepository userRespository;
	
	@BeforeEach
	public void setUp() {

	}
	
	@AfterEach
	public void tearDown() {

	}
}
