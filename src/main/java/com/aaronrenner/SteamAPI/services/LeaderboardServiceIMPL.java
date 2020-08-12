package com.aaronrenner.SteamAPI.services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.aaronrenner.SteamAPI.models.SteamUserAchievementInfo;
import com.aaronrenner.SteamAPI.models.SteamUserGameInfo;
import com.aaronrenner.SteamAPI.models.SteamUserProfileInfo;
import com.aaronrenner.SteamAPI.models.SteamUserStatInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@Service
public class LeaderboardServiceIMPL implements LeaderboardService {
	
	private String steamProfileEndpoint = "http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?";
	private String steamGameStatsEndpoint = "http://api.steampowered.com/ISteamUserStats/GetUserStatsForGame/v2/?";
	private String steamKey = "E7F6470D0BAFE99CED3362CB2DB5F25B";
	// String cbizz = "76561198440364879";

	@Override
	public List<SteamUserProfileInfo> getSteamProfile(String SteamID64) {
		List<SteamUserProfileInfo> profile = new ArrayList<>();
		
		RestTemplate restTemplate = new RestTemplate();
		// TODO Add functionality for friends search
		String steamSearchURL = steamProfileEndpoint + "key=" + steamKey + "&steamids=" + SteamID64;
		
		URI steamURI = null;
		try {
			steamURI = new URI(steamSearchURL);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResponseEntity<String> result = restTemplate.getForEntity(steamURI, String.class);
		JSONObject responseBody = (JSONObject) parseJSON(result.getBody());
		JSONObject responseData = (JSONObject) responseBody.get("response");
		JSONArray playerData = (JSONArray) responseData.get("players");

		int arraySize = playerData.size();
		ObjectMapper objectMapper = new ObjectMapper();
		for(int x=0; x<arraySize; x++) {
			try {
				SteamUserProfileInfo newEntry = objectMapper.readValue(playerData.get(x).toString(), SteamUserProfileInfo.class);
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
		List<SteamUserStatInfo> gameStats = new ArrayList<>();
		
		RestTemplate restTemplate = new RestTemplate();
		// TODO Add functionality for friends search
		String steamSearchURL = steamGameStatsEndpoint + "key=" + steamKey + "&steamid=" + SteamID64 + "&appid=" + appID;
		
		URI steamURI = null;
		try {
			steamURI = new URI(steamSearchURL);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResponseEntity<String> result = restTemplate.getForEntity(steamURI, String.class);
		JSONObject responseBody = (JSONObject) parseJSON(result.getBody());
		JSONObject responseData = (JSONObject) responseBody.get("playerstats");

		ObjectMapper objectMapper = new ObjectMapper();
		try {
			SteamUserGameInfo newEntry = objectMapper.readValue(responseData.toJSONString(), SteamUserGameInfo.class);
			SteamUserStatInfo newStats = new SteamUserStatInfo();
			newStats.setSteamID64(newEntry.getSteamID());
			newStats.setGameName(newEntry.getGameName());
			newStats.setStats(newEntry.getStats());
			System.out.println(newStats.toString());
			gameStats.add(newStats);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return gameStats;

	}

	@Override
	public List<SteamUserAchievementInfo> getSteamAchievements(String SteamID64, String appID) {
		//TODO code this, it is basically the same as the method for getSteamStats line 65
		
		return null;
	}
	
	private JSONObject parseJSON(String data) {
		JSONObject jsonData = null;
		try {
			JSONParser jsonParser = new JSONParser(JSONParser.MODE_JSON_SIMPLE);
			Object bufferObject = jsonParser.parse(data);
			jsonData = (JSONObject) bufferObject;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonData;
	}

}
