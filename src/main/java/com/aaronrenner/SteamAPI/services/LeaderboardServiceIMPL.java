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
import com.aaronrenner.SteamAPI.models.SteamProfileGameInfo;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamGameInfo;
import com.aaronrenner.SteamAPI.models.SteamUserProfileInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.GameRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.*;
import net.minidev.json.parser.*;

@Service
public class LeaderboardServiceIMPL implements LeaderboardService {
	
	final private String steamProfileEndpoint = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?";
	final private String steamGameStatsEndpoint = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v2/?";
	final private String steamGameInfoEndpoint = "http://api.steampowered.com/ISteamUserStats/GetSchemaForGame/v2/?";
	final private String steamRecentlyPlayed = "http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?";
	final private String steamOwnedGames = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?";
	final private String steamKey = "E7F6470D0BAFE99CED3362CB2DB5F25B";
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
	public List<SteamUserProfileInfo> getSteamProfile(String SteamID64) {
		// TODO List<String> friendsList = getUserFriends(SteamID64); fix friends search
		List<SteamUserProfileInfo> profile = new ArrayList<>();

		String steamSearchURL = this.steamProfileEndpoint + "key=" + steamKey + "&steamids=" + SteamID64;
		JSONObject getRequest = getRequest(steamSearchURL);
		JSONObject responseData = (JSONObject) getRequest.get("response");
		JSONArray playerData = (JSONArray) responseData.get("players");
		int arraySize = playerData.size();
		// Loop player search
		for(int x=0; x<arraySize; x++) {
			try {
				SteamUserProfileInfo newEntry = objectMapper.readValue(playerData.get(x).toString(), SteamUserProfileInfo.class);
				JSONObject recentGames = getRecentlyPlayed(SteamID64);
				JSONObject ownedGames = getOwnedGames(SteamID64);

				// TODO get recently played total
				JSONArray recentlyPlayedGameList = (JSONArray) recentGames.get("games");
				// TODO get owned games total
				JSONArray ownedGamesList = (JSONArray) ownedGames.get("games");
				int recentlyPlayedListSize = recentlyPlayedGameList.size();
				// TODO loop owned games list for info
				int ownedGamesListSize = ownedGamesList.size();
				
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
				
				profile.add(newEntry);
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
		String steamSearchURL = this.steamGameStatsEndpoint + "key=" + steamKey + "&steamid=" + SteamID64 + "&appid=" + appID;

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
		
		if(gameSearch.isPresent()) {
			String steamGameInfo = this.steamGameInfoEndpoint + "key=" + steamKey + "&appid=" + appID;

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
	
	private JSONObject getRecentlyPlayed(String steamID64) {
		String steamRecentyPlayed = this.steamRecentlyPlayed + "key=" + steamKey + "&steamid=" + steamID64;
		
		// Fetch data from URL
		JSONObject getRequest = getRequest(steamRecentyPlayed);
		return (JSONObject) getRequest.get("response");
		
	}
	
	private JSONObject getOwnedGames(String steamID64) {
		String steamOwnedGames = this.steamOwnedGames + "key=" + steamKey + "&steamid=" + steamID64;
		
		// Fetch data from URL
		JSONObject getRequest = getRequest(steamOwnedGames);
		return (JSONObject) getRequest.get("response");		
	}
}
