package com.aaronrenner.SteamAPI.services;

import java.net.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.SteamProfile;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamGameInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.*;
import net.minidev.json.parser.*;

@Service
public class LeaderboardServiceIMPL implements LeaderboardService {
	
	final private String steamKey = "E7F6470D0BAFE99CED3362CB2DB5F25B";
	final private String steamProfileEndpoint = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=" + this.steamKey;
	final private String steamGameStatsEndpoint = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v2/?key=" + this.steamKey;
	final private String steamGameInfoEndpoint = "http://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/?key=" + this.steamKey;
	final private String steamRecentlyPlayedEndpoint = "http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?key=" + this.steamKey;
	final private String steamOwnedGamesEndpoint = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key=" + this.steamKey;
	final private String steamProfileLevelEndpoint = "https://api.steampowered.com/IPlayerService/GetSteamLevel/v1/?key=" + this.steamKey;
	final private String steamProfileBadgeEndpoint = "https://api.steampowered.com/IPlayerService/GetBadges/v1/?key=" + this.steamKey;
	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper objectMapper = new ObjectMapper();
	// String cbizz = "76561198440364879"; Christian Burn's SteamID
	
	@Autowired
	private UserService userService;
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	GameService gameService;

	@Override
	public List<SteamProfile> getSteamProfile(String SteamID64) {
		// TODO List<String> friendsList = getUserFriends(SteamID64); fix friends search
		List<SteamProfile> profile = new ArrayList<>();
		
		String steamSearchURL = this.steamProfileEndpoint + "&steamids=" + SteamID64;
		JSONObject getRequest = getRequest(steamSearchURL);
		JSONObject responseData = (JSONObject) getRequest.get("response");
		JSONArray playerData = (JSONArray) responseData.get("players");
		int arraySize = playerData.size();
		// Loop player search
		for(int x=0; x<arraySize; x++) {
			try {
				SteamProfile userProfile = objectMapper.readValue(playerData.get(x).toString(), SteamProfile.class);
				JSONObject recentGames = getSteamEndpoint(this.steamRecentlyPlayedEndpoint, SteamID64);
				JSONObject ownedGames = getSteamEndpoint(this.steamOwnedGamesEndpoint, SteamID64);
				JSONObject profileLevel = getSteamEndpoint(this.steamProfileLevelEndpoint, SteamID64);
				JSONArray badgeList = (JSONArray) getSteamEndpoint(this.steamProfileBadgeEndpoint, SteamID64).get("badges");
				// Before diving into the "games" array (below) we must gather data from the parent object
				long recentlyPlayedCount = (long) recentGames.getAsNumber("total_count");
				userProfile.setRecentlyPlayed_Count(recentlyPlayedCount);
				
				long totalGamesOwned = (long) ownedGames.getAsNumber("game_count");
				userProfile.setGamesOwned_Count(totalGamesOwned);
				
				long playerLevel = (long) profileLevel.getAsNumber("player_level");
				userProfile.setLevel(playerLevel);
				
				long badgeCount = (long) badgeList.size();
				userProfile.setBadge_Count(badgeCount);

				JSONArray recentlyPlayedGameList = (JSONArray) recentGames.get("games");
				JSONArray ownedGamesList = (JSONArray) ownedGames.get("games");
				int recentlyPlayedListSize = recentlyPlayedGameList.size();
				int ownedGamesListSize = ownedGamesList.size();
				
				/** This is faster if I do not re-lookup the game
				for(int t=0; t<ownedGamesListSize; t++) {
					SteamProfileGameInfo newGameEntry = objectMapper.readValue(ownedGamesList.get(t).toString(), SteamProfileGameInfo.class);
					System.out.println(newGameEntry.getName());
				}
				*/
				
				// Loop recent games
				/** These methods need to be redesigned for speed these loops are outrageous
				for(int i=0; i<recentlyPlayedListSize; i++) {
					SteamProfileGameInfo newGameEntry = objectMapper.readValue(recentlyPlayedGameList.get(i).toString(), SteamProfileGameInfo.class);
					saveGame(String.valueOf(newGameEntry.getAppid()));
				}
				for(int t=0; t<ownedGamesListSize; t++) {
					SteamProfileGameInfo newGameEntry = objectMapper.readValue(ownedGamesList.get(t).toString(), SteamProfileGameInfo.class);
					saveGame(String.valueOf(newGameEntry.getAppid()));
				}
				*/
				
				profile.add(userProfile);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return profile;
	}

	@Override
	public List<SteamUserStatInfo> getSteamStats(String SteamID64, String appID) {
		// First: Save the game in our DB
		saveGame(appID);
		
		// Continue
		List<SteamUserStatInfo> gameStats = new ArrayList<>();
		
		// TODO Add functionality for friends search
		String steamSearchURL = this.steamGameStatsEndpoint + "&steamid=" + SteamID64 + "&appid=" + appID;

		JSONObject getRequest = getRequest(steamSearchURL);
		JSONObject responseData = (JSONObject) getRequest.get("playerstats");

		try {
			SteamGameInfo newEntry = objectMapper.readValue(responseData.toJSONString(), SteamGameInfo.class);
			SteamUserStatInfo newStats = new SteamUserStatInfo();
			newStats.setSteamID64(newEntry.getSteamID());
			newStats.setGameName(newEntry.getGameName());
			newStats.setStats(newEntry.getStats());
			gameStats.add(newStats);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return gameStats;

	}

	@Override
	public List<SteamUserAchievementInfo> getSteamAchievements(String SteamID64, String appID) {
		//TODO code this, it is basically the same as the method for getSteamStats
		
		return null;
	}
	
	/**
	 * This is a helper method, in this method you can send it any form of VALID json string and receive back a JSONObject from the library json-smart.
	 * @param data A string of proper JSON data
	 * @return A json-smart, JSONObject
	 */
	private JSONObject parseJSON(String data) {
		JSONObject jsonData = null;
		try {
			JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			Object bufferObject = jsonParser.parse(data);
			jsonData = (JSONObject) bufferObject;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return jsonData;
	}
	
	/**
	 * This is a helper method, in this method you can supply a valid String of a web address and receive a Java URI for other methods.
	 * @param string A String containing a web address
	 * @return A java.net URI for all your web needs
	 */
	private URI getURIFromString(String string) {
		URI newURI = null;
		try {
			newURI = new URI(string);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return newURI;
	}
	
	@Deprecated
	private List<String> getUserFriends(String SteamID64) {
		User activeUser = userService.getUser(SteamID64);
		List<String> friendsList = new ArrayList<>();
		
		if(activeUser != null) {
			List<FriendID> userFriends = activeUser.getFriendList();
			for(FriendID friendLoop: userFriends) {
				friendsList.add(friendLoop.getSteamID64());
			}
		}
		
		return friendsList;
		
	}
	
	/**
	 * This method will test our DB for a game, if it does not exist we create and save a new game.
	 * @param appID The games id # in Steams systems
	 */
	private void saveGame(String appID) {
		long game_appID = Long.parseLong(appID);
		Optional<Game> gameSearch = gameRepository.findById(game_appID);
		
		if(!gameSearch.isPresent()) {
			String steamGameInfo = this.steamGameInfoEndpoint + "&appid=" + appID;

			JSONObject getRequest = getRequest(steamGameInfo);
			JSONObject gameData = (JSONObject) getRequest.get("game");
			
			String gameName = gameData.getAsString("gameName");
			
			Game newGame = new Game();
			newGame.setId(Long.parseLong(appID));
			newGame.setTitle(gameName);
			gameRepository.save(newGame);
		}
	}
	
	/**
	 * This is a helper method, in this method you can provide a String of the URL for requesting and it will return a JSONObject of the response from Steam.
	 * @param getURL Pass in a String of the API request URL.
	 * @return A json-smart object of the 
	 */
	private JSONObject getRequest(String getURL) {
		URI createURI = getURIFromString(getURL);
		ResponseEntity<String> result = restTemplate.getForEntity(createURI, String.class);
		return parseJSON(result.getBody());
		
	}
	
	private JSONObject getSteamEndpoint(String endpoint, String steamID64) {
		String steamEndpoint = endpoint + "&steamid=" + steamID64;
		if(endpoint.equals(steamOwnedGamesEndpoint)) {
			steamEndpoint = steamEndpoint + "&include_appinfo=true";
		}
		// Fetch data from URL
		JSONObject getRequest = getRequest(steamEndpoint);
		return (JSONObject) getRequest.get("response");	
	}
}
