package com.aaronrenner.SteamAPI.services;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.SteamProfile;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;
import com.aaronrenner.SteamAPI.models.User;
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
	
	private User fakeUser;
	private Game fakeGame;
	
	@Value("${com.aaronrenner.apikey}")
	private String apikey;
	
	@BeforeEach
	public void setUp() {
		fakeUser = createFakeUser();
		fakeGame = fakeGame();
		
		Optional<Game> fakeGame_opt = Optional.of(fakeGame);
		Mockito.when(gameRepository.findById(Mockito.anyLong())).thenReturn(fakeGame_opt);
		Mockito.when(gameRepository.save(Mockito.any(Game.class))).thenReturn(fakeGame);
		
		Optional<User> fakeUser_opt = Optional.of(fakeUser);
		Mockito.when(userRespository.findBySteamID64(Mockito.anyString())).thenReturn(fakeUser_opt);
		
		leaderboardService.setAPIKey(apikey);
		
	}
	
	@AfterEach
	public void tearDown() {
		fakeUser = null;
		fakeGame = null;
	}
	
	@Test
	public void getProfileData_Success() {
		List<SteamProfile> profileData = leaderboardService.getSteamProfile(fakeUser.getSteamID64());
		
		assert(profileData.size() == 2);
	}
	
	@Test
	public void getStatsData_Success() {
		List<SteamUserStatInfo> statsData = leaderboardService.getSteamStats(fakeUser.getSteamID64(), String.valueOf(fakeGame.getId()));
		
		assert(statsData.size() == 2);
	}
	
	@Test
	public void getAchievementsData_Success() {
		List<SteamUserAchievementInfo> achievementsData = leaderboardService.getSteamAchievements(fakeUser.getSteamID64(), String.valueOf(fakeGame.getId()));
		
		assert(achievementsData.size() == 2);
		
	}
	
	private User createFakeUser() {
		User newUser = new User();
		newUser.setId(1);
		newUser.setSteamID64("76561198089525491"); //me
		newUser.setUsername("test_user");
		newUser.getFriendList().add(fakeFriend("76561198440364879")); //cBizz
		return newUser;
	}
	
	private FriendID fakeFriend(String IDToHave) {
		FriendID newFriend = new FriendID();
		newFriend.setSteamID64(IDToHave);
		return newFriend;
	}
	
	private Game fakeGame() {
		return new Game(10, "test_game");
	}
	
	
}
