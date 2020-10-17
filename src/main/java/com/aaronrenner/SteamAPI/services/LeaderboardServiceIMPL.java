package com.aaronrenner.SteamAPI.services;

import java.net.*;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;
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
	
	@Value("${com.aaronrenner.apikey}")
	private String apikey;
	private String steamProfileEndpoint = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=";
	private String steamGameStatsEndpoint = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v2/?key=";
	private String steamRecentlyPlayedEndpoint = "http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?key=";
	private String steamOwnedGamesEndpoint = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?key=";
	private String steamProfileLevelEndpoint = "https://api.steampowered.com/IPlayerService/GetSteamLevel/v1/?key=";
	private String steamProfileBadgeEndpoint = "https://api.steampowered.com/IPlayerService/GetBadges/v1/?key=";
	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	GameRepository gameRepository;

	@Override
	// TODO fix for user search 76561198079513535
	public List<SteamProfile> getSteamProfile(String SteamID64) {
		// This code line must exist for jUnit tests, there are cases that the key wouldnt get populated
		populateSecurityKey();
		List<Game> masterGameList = new ArrayList<>();
		
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
				JSONArray recentlyPlayedGameList = (JSONArray) recentGames.get("games");
				JSONArray ownedGamesList = (JSONArray) ownedGames.get("games");
				
				int recentlyPlayedListSize = recentlyPlayedGameList.size();
				int ownedGamesListSize = ownedGamesList.size();

				int recentPlaytimeCount = 0;
				int foreverPlaytime = 0;
				
				JSONObject bufferJSONObject;
				String bufferString;
				
				for(int t=0; t<recentlyPlayedListSize; t++) {

					bufferJSONObject = (JSONObject) recentlyPlayedGameList.get(t);
					bufferString = bufferJSONObject.getAsString("playtime_2weeks");
					recentPlaytimeCount += Integer.parseInt(bufferString);
					bufferJSONObject = null;
					bufferString = null;
				}
				
				userProfile.setRecentlyPlayed_Playtime(recentPlaytimeCount);
				
				
				for(int g=0; g<ownedGamesListSize; g++) {
					bufferJSONObject = (JSONObject) ownedGamesList.get(g);
					bufferString = bufferJSONObject.getAsString("playtime_forever");
					foreverPlaytime+= Integer.parseInt(bufferString);
					
					bufferString = null;
					
					bufferString = bufferJSONObject.getAsString("appid");
					Optional<String> name = Optional.of(bufferJSONObject.getAsString("name"));
					
					// TODO testing fix for new game saving
					Game newGameEntry = new Game(Long.valueOf(bufferString), name.get());
					if(!masterGameList.contains(newGameEntry)) {
						masterGameList.add(newGameEntry);
					}
					
					bufferJSONObject = null;
					bufferString = null;
				}
				userProfile.setTotalPlaytime(foreverPlaytime);
				
				profile.add(userProfile);
				
				
			} catch (NullPointerException nP) {
				SteamProfile userProfile = new SteamProfile();
				try {
					userProfile = objectMapper.readValue(playerData.get(x).toString(), SteamProfile.class);
				} catch (Exception jsonError) {
					jsonError.printStackTrace();
				}
				profile.add(userProfile);
			} catch(Exception jMapError) {
				jMapError.printStackTrace();
			}
		}
		saveGames(masterGameList);
		return profile;
	}

	@Override
	public List<SteamUserStatInfo> getSteamStats(String SteamID64, String appID) {
		// This code line must exist for jUnit tests, there are cases that the key wouldnt get populated
		populateSecurityKey();
		//Master variable
		List<SteamUserStatInfo> gameStats = new ArrayList<>();
		
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
			// This code exists to populate the game name on api call always, regardless of what is returned by steam
			if(gameInfo.isPresent()) {
				gameName = gameInfo.get().getTitle();
			} else {
				gameName = newFriendData.getGameName();
			}
			SteamUserStatInfo newFriendStats = new SteamUserStatInfo(newFriendData.getSteamID(), gameName, newFriendData.getStats());
			gameStats.add(newFriendStats);
		}
		return gameStats;

	}

	@Override
	public List<SteamUserAchievementInfo> getSteamAchievements(String SteamID64, String appID) {
		// This code line must exist for jUnit tests, there are cases that the key wouldnt get populated
		populateSecurityKey();
		//Master variable
		List<SteamUserAchievementInfo> gameAchievements = new ArrayList<>();
		
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
		String gameName;
		
		for(FriendID fID : masterLoopList) {
			SteamGameInfo newFriendData = getSteamPersonalGameProgressEndpoint(fID.getSteamID64(), appID);
			// This code exists to populate the game name on api call always, regardless of what is returned by steam
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
	 * This is a helper method, in this method you can send it any form of VALID json 
	 * string and receive back a JSONObject from the library json-smart.
	 * 
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
	 * This is a helper method, in this method you can supply a valid 
	 * String of a web address and receive a Java URI for other methods.
	 * 
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
	
	/**
	 * This method implements the user database that would be linked to the code. 
	 * Throughout this class there are times you may need to access the friends 
	 * of a user while only having access to the steam ID.
	 * 
	 * @param SteamID64
	 * @return
	 */
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
	 * This method will take a list of games which will then create a new threaded 
	 * process to manage the check and saving of all the games.
	 * 
	 * @param gameList
	 * @version 2
	 * 
	 */
	@Async
	private void saveGames(List<Game> gameList) {
		new Thread(new Runnable() {
			List<Game> gameRecordList = gameList;
			
			@Value("${com.aaronrenner.apikey}")
			private String apikey;
			
		    public void run() {
		    	for(Game game : gameRecordList) {
					if(!gameRepository.existsById(game.getId())) {
						gameRepository.save(game);
					}
		    	}
		    }
		}).start();
	}
	
	/**
	 * This is a helper method, in this method you can provide a String of the URL for 
	 * requesting and it will return a JSONObject of the response from Steam.
	 * 
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
			// TODO Understand code block more
			//System.out.println(new SteamError(hCEE.getRawStatusCode()) + " - Safe to ignore?");
		} catch (HttpServerErrorException hSEE) {
			// TODO Understand and make code block better
			//System.out.println(new SteamError(hSEE.getRawStatusCode()) + " - Safe to ignore?");
		}
		return newResponse;
		
	}
	
	private JSONObject getSteamEndpoint(String endpoint, String steamID64) {
		String steamEndpoint = endpoint + "&steamid=" + steamID64;
		// Fetch data from URL
		JSONObject getRequest = getRequest(steamEndpoint);
		return (JSONObject) getRequest.get("response");	
	}
	
	
	private void populateSecurityKey() {
		steamProfileEndpoint = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v2/?key=" + this.apikey;
		steamGameStatsEndpoint = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v2/?key=" + this.apikey;
		steamRecentlyPlayedEndpoint = "http://api.steampowered.com/IPlayerService/GetRecentlyPlayedGames/v1/?key=" + this.apikey;
		steamOwnedGamesEndpoint = "http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/?&include_appinfo=true&key=" + this.apikey;
		steamProfileLevelEndpoint = "https://api.steampowered.com/IPlayerService/GetSteamLevel/v1/?key=" + this.apikey;
		steamProfileBadgeEndpoint = "https://api.steampowered.com/IPlayerService/GetBadges/v1/?key=" + this.apikey;
	}
	
	/**
	 * This method was created for the jUnit tests, there is a bug with loading the @Value spring annotation 
	 * and reading the api key from application.properties
	 * 
	 * @param key the API key
	 */
	public void setAPIKey(String key) {
		this.apikey = key;
	}
}
