package com.aaronrenner.SteamAPI.services;

import java.net.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.SteamProfile;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamGameInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.GameRepository;
import com.aaronrenner.SteamAPI.repositories.UserRepository;
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
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GameRepository gameRepository;
	
	@Autowired
	GameService gameService;

	@Override
	public List<SteamProfile> getSteamProfile(String SteamID64) {
		Optional<List<FriendID>> userFriendList = getUserFriends(SteamID64);
		String friendIDList = "";
		if(userFriendList.isPresent()) {
			for(FriendID friendEntry : userFriendList.get()) {
				friendIDList = friendIDList + "," + friendEntry.getSteamID64();
			}
		}
		List<SteamProfile> profile = new ArrayList<>();
		
		String steamSearchURL = this.steamProfileEndpoint + "&steamids=" + SteamID64 + friendIDList;
		JSONObject getRequest = getRequest(steamSearchURL);
		JSONObject responseData = (JSONObject) getRequest.get("response");
		JSONArray playerData = (JSONArray) responseData.get("players");
		int arraySize = playerData.size();
		// Loop player search
		for(int x=0; x<arraySize; x++) {
			try {
				SteamProfile userProfile = objectMapper.readValue(playerData.get(x).toString(), SteamProfile.class);
				JSONObject recentGames = getSteamEndpoint(this.steamRecentlyPlayedEndpoint, userProfile.getSteamid());
				JSONObject ownedGames = getSteamEndpoint(this.steamOwnedGamesEndpoint, userProfile.getSteamid());
				JSONObject profileLevel = getSteamEndpoint(this.steamProfileLevelEndpoint, userProfile.getSteamid());
				JSONArray badgeList = (JSONArray) getSteamEndpoint(this.steamProfileBadgeEndpoint, userProfile.getSteamid()).get("badges");
				// Before diving into the "games" array (below) we must gather data from the parent object
				long recentlyPlayedCount = (long) recentGames.getAsNumber("total_count");
				userProfile.setRecentlyPlayed_Count(recentlyPlayedCount);
				
				long totalGamesOwned = (long) ownedGames.getAsNumber("game_count");
				userProfile.setGamesOwned_Count(totalGamesOwned);
				
				long playerLevel = (long) profileLevel.getAsNumber("player_level");
				userProfile.setLevel(playerLevel);
				
				long badgeCount = (long) badgeList.size();
				userProfile.setBadge_Count(badgeCount);

				// TODO Create sort and log for games in profile data
				/** This is faster if I do not re-lookup the game
				JSONArray recentlyPlayedGameList = (JSONArray) recentGames.get("games");
				JSONArray ownedGamesList = (JSONArray) ownedGames.get("games");
				
				int recentlyPlayedListSize = recentlyPlayedGameList.size();
				int ownedGamesListSize = ownedGamesList.size();
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
		//Master variable
		List<SteamUserStatInfo> gameStats = new ArrayList<>();
		
		// First: Save the game in our DB
		saveGame(appID);
		
		//Check if user has friends (haha probably not)
		Optional<List<FriendID>> oLFID = getUserFriends(SteamID64);
		
		List<FriendID> masterLoopList = new ArrayList<>();
		FriendID masterUser = new FriendID();
		masterUser.setSteamID64(SteamID64);
		masterLoopList.add(masterUser);
		
		if(oLFID.isPresent()) {
			for(FriendID fID : oLFID.get()) {
				masterLoopList.add(fID);
			}
		}
		
		Optional<Game> gameInfo = searchGame(appID);
		String gameName = "";
		
		for(FriendID fID : masterLoopList) {
			SteamGameInfo newFriendData = getSteamPersonalGameProgressEndpoint(fID.getSteamID64(), appID);
			if(gameInfo.isPresent()) {
				gameName = gameInfo.get().getTitle();
			} else {
				gameName = newFriendData.getGameName();
			}
			SteamUserStatInfo newFriendStats = new SteamUserStatInfo(newFriendData.getSteamID(), gameName, newFriendData.getStats());
			gameStats.add(newFriendStats);
		}
		
		/**
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
			e.printStackTrace();
		} 
		*/
		return gameStats;

	}

	@Override
	public List<SteamUserAchievementInfo> getSteamAchievements(String SteamID64, String appID) {
		//Master variable
		List<SteamUserAchievementInfo> gameAchievements = new ArrayList<>();
		
		// First: Save the game in our DB
		saveGame(appID);
		
		//Check if user has friends (haha probably not)
		Optional<List<FriendID>> oLFID = getUserFriends(SteamID64);
		
		List<FriendID> masterLoopList = new ArrayList<>();
		FriendID masterUser = new FriendID();
		masterUser.setSteamID64(SteamID64);
		masterLoopList.add(masterUser);
		
		if(oLFID.isPresent()) {
			for(FriendID fID : oLFID.get()) {
				masterLoopList.add(fID);
			}
		}
		
		Optional<Game> gameInfo = searchGame(appID);
		String gameName = "";
		
		for(FriendID fID : masterLoopList) {
			SteamGameInfo newFriendData = getSteamPersonalGameProgressEndpoint(fID.getSteamID64(), appID);
			if(gameInfo.isPresent()) {
				gameName = gameInfo.get().getTitle();
			} else {
				gameName = newFriendData.getGameName();
			}
			SteamUserAchievementInfo newFriendAchievements = new SteamUserAchievementInfo(newFriendData.getSteamID(), gameName, newFriendData.getAchievements());
			gameAchievements.add(newFriendAchievements);
		}
		
		return gameAchievements;
	}
	
	private SteamGameInfo getSteamPersonalGameProgressEndpoint(String SteamID64, String appID) {
		SteamGameInfo sGI = null;
		String steamSearchURL = this.steamGameStatsEndpoint + "&steamid=" + SteamID64 + "&appid=" + appID;
		JSONObject getRequest = getRequest(steamSearchURL);
		JSONObject responseData = (JSONObject) getRequest.get("playerstats");
		try {
			sGI = objectMapper.readValue(responseData.toJSONString(), SteamGameInfo.class);
		} catch (Exception e) {
			sGI = new SteamGameInfo();
			sGI.setSteamID(SteamID64);
		}
		return sGI;
		
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
	
	// TODO Current work
	private Optional<List<FriendID>> getUserFriends(String SteamID64) {
		Optional<User> activeUser = userRepository.findBySteamID64(SteamID64);
		Optional<List<FriendID>> oLFID = Optional.empty();
		
		if(activeUser.isPresent()) {
			oLFID = Optional.of(activeUser.get().getFriendList());
		}
		
		return oLFID;
		
	}
	
	private Optional<Game> searchGame(String appID) {
		long game_appID = Long.parseLong(appID);
		return gameRepository.findById(game_appID);
	}
	
	/**
	 * This method will test our DB for a game, if it does not exist we create and save a new game.
	 * @param appID The games id # in Steams systems
	 */
	private void saveGame(String appID) {
		Optional<Game> gameSearch = searchGame(appID);
		
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
		ResponseEntity<String> result = null;
		JSONObject newResponse = new JSONObject();
		try {
			result = restTemplate.getForEntity(createURI, String.class);
			newResponse = parseJSON(result.getBody());
		} catch (HttpClientErrorException hCEE) {
			// TODO throw new SteamError(hCEE.getRawStatusCode());
		} catch (HttpServerErrorException hSEE) {
			// TODO throw new SteamError(hSEE.getRawStatusCode());
		}
		return newResponse;
		
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
