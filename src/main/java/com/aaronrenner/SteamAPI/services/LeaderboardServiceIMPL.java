package com.aaronrenner.SteamAPI.services;

import java.net.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.aaronrenner.SteamAPI.models.SteamAPIEndpoints;

@Service
/**
 * 
 * @author aaron renner
 * @version 3.0
 */
public class LeaderboardServiceIMPL implements LeaderboardService {

	@Value("${com.aaronrenner.apikey}")
	private String apikey;
	private String steamProfileEndpoint;
	private String steamGameStatsEndpoint;
	private String steamRecentlyPlayedEndpoint;
	private String steamOwnedGamesEndpoint;
	private String steamProfileLevelEndpoint;
	private String steamProfileBadgeEndpoint;
	private RestTemplate restTemplate = new RestTemplate();
	private ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	UserRepository userRepository;

	@Autowired
	GameRepository gameRepository;

	private List<Game> masterGameList = new ArrayList<>();
	private Thread gameSavingThread = new Thread();

	private static final Logger LOGGER = LoggerFactory.getLogger(LeaderboardServiceIMPL.class);

	@Override
	public List<SteamProfile> getSteamProfile(String SteamID64) {
		// This code line must exist for jUnit tests, there are cases that the key would not get populated
		populateSecurityKey();

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
			int iteratorNumber = x;
			/**
			 * TODO figure out how to name the threads to keep them in order :D
			 * 
			 * This thread will call an @Async thread to create a new SteamProfile for a new loop
			 * of the for loop around line 82.
			 * 
			 */
			Thread newSteamProfile = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						CompletableFuture<SteamProfile> newProfile = completeSteamProfile(playerData.get(iteratorNumber).toString());
						
						while(true) {
							if(newProfile.isDone()) {
								LOGGER.info("Finished mapping a user and added to master");
								profile.add(newProfile.join());
								break;
							}
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			});
			newSteamProfile.start();
		}
		
		/**
		 * After all of the treads have been created we wait for them all to finish before moving on.
		 */
		while(true) {
			if(profile.size() == arraySize) {
				LOGGER.info("Finally done and returning");
				break;
			}
			
			/**
			 * This Thread.sleep(1000) is necessary to allow the main thread line to take a chill pill real quick and catch up.
			 * If you remove this you will no longer get responses on the end point.
			 */
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		saveGames(this.masterGameList);
		return profile;
	}

	@Override
	public List<SteamUserStatInfo> getSteamStats(String SteamID64, String appID) {
		// This code line must exist for jUnit tests, there are cases that the key wouldnt get populated
		populateSecurityKey();
		//Master variable
		List<SteamUserStatInfo> gameStats = new ArrayList<>();

		List<FriendID> masterLoopList = compileUserSearch(SteamID64);

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

		List<FriendID> masterLoopList = compileUserSearch(SteamID64);

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
	
	
	@Async
	private  CompletableFuture<SteamUserStatInfo> completeSteamStats(String steamID64, String appID) {
		SteamGameInfo newFriendData = getSteamPersonalGameProgressEndpoint(steamID64, appID);
		
		Optional<Game> gameInfo = searchGame(appID);
		String gameName = "";
		
		// This code exists to populate the game name on api call always, regardless of what is returned by steam
		if(gameInfo.isPresent()) {
			gameName = gameInfo.get().getTitle();
		} else {
			gameName = newFriendData.getGameName();
		}
		SteamUserStatInfo newFriendStats = new SteamUserStatInfo(newFriendData.getSteamID(), gameName, newFriendData.getStats());
		return CompletableFuture.completedFuture(newFriendStats);
	}
	/**
	 * This method contains every processing piece to parse an ass load of data from Steam. 
	 * Look at the other methods to get an idea for how much data this pulls together to deliver a result to the controller.
	 * 
	 * This method contains a lot of tests to ensure that if any data is missing it will just not be noticed to the end user, 
	 * however if you have console access you might be able to read some of the text there.
	 * 
	 * TODO Eventually this method should be able to parse data in an async manner to not bog down to processing line.
	 * 
	 * @implNote From my tests when I run with the {@code @Async} annotation it does not fully execute the code in the return statement.
	 * 
	 * For future work I will be implementing
	 * {@link https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/integration.html#scheduling-task-executor}
	 * 
	 * @param playerData
	 * @return
	 * @throws InterruptedException
	 * @since 2.5
	 */
	@Async
	private CompletableFuture<SteamProfile> completeSteamProfile(String playerData) throws InterruptedException {
		final long start = System.currentTimeMillis();
		
		SteamProfile profile = null;
		JSONObject recentGames = null;
		JSONObject ownedGames = null;
		JSONObject profileLevel = null;
		JSONArray badgeList = null;
		JSONArray recentlyPlayedGameList = null;
		JSONArray ownedGamesList = null;

		int recentlyPlayedListSize = 0;
		int ownedGamesListSize = 0;

		int recentPlaytimeCount = 0;
		int foreverPlaytime = 0;

		JSONObject bufferJSONObject = null;
		String bufferString = null;
		long bufferLong = 0;

		try {
			profile = objectMapper.readValue(playerData, SteamProfile.class);
			
			try {
				recentGames = getSteamEndpoint(this.steamRecentlyPlayedEndpoint, profile.getSteamid());
				ownedGames = getSteamEndpoint(this.steamOwnedGamesEndpoint, profile.getSteamid());
				profileLevel = getSteamEndpoint(this.steamProfileLevelEndpoint, profile.getSteamid());
				badgeList = (JSONArray) getSteamEndpoint(this.steamProfileBadgeEndpoint, profile.getSteamid()).get("badges");
			} catch (Exception e) {
				LOGGER.error("An endpoint is returning null");
			}
			// Before diving into the "games" array (below) we must gather data from the parent object
			try {
				bufferLong = (long) recentGames.getAsNumber("total_count");
				profile.setRecentlyPlayed_Count(bufferLong);
			} catch (Exception e) {
				profile.setRecentlyPlayed_Count(0);
			}

			try {
				bufferLong = (long) ownedGames.getAsNumber("game_count");
				profile.setGamesOwned_Count(bufferLong);
			} catch(Exception e) {
				profile.setGamesOwned_Count(0);
				LOGGER.error("Steam is returning null SteamProfile for owned games endpoint - completeSteamProfile()");
			}

			try {
				bufferLong = (long) profileLevel.getAsNumber("player_level");
				profile.setLevel(bufferLong);
			} catch(Exception e) {
				profile.setLevel(0);
			}

			try {
				bufferLong = (long) badgeList.size();
				profile.setBadge_Count(bufferLong);
			} catch(Exception e) {
				profile.setBadge_Count(0);
			}

			// TODO Create sort and log for games in profile data
			try {
				recentlyPlayedGameList = (JSONArray) recentGames.get("games");
			} catch(Exception e) {
				// TODO complete
			}

			try {
				ownedGamesList = (JSONArray) ownedGames.get("games");
			} catch(Exception e) {
				//TODO complete block-statement
			}

			try {
				recentlyPlayedListSize = recentlyPlayedGameList.size();
				ownedGamesListSize = ownedGamesList.size();
			} catch (Exception e) {
				// TODO Damn steam
			}

			for(int t=0; t<recentlyPlayedListSize; t++) {
				try {
					bufferJSONObject = (JSONObject) recentlyPlayedGameList.get(t);
					bufferString = bufferJSONObject.getAsString("playtime_2weeks");
					recentPlaytimeCount += Integer.parseInt(bufferString);
					bufferJSONObject = null;
					bufferString = null;
				} catch (Exception e) {
					//TODO complete block-statement
				}
				bufferJSONObject = null;
				bufferString = null;
			}

			profile.setRecentlyPlayed_Playtime(recentPlaytimeCount);

			for(int g=0; g<ownedGamesListSize; g++) {
				try {
					bufferJSONObject = (JSONObject) ownedGamesList.get(g);
					bufferString = bufferJSONObject.getAsString("playtime_forever");
					foreverPlaytime+= Integer.parseInt(bufferString);

					bufferString = null;

					bufferString = bufferJSONObject.getAsString("appid");
					Optional<String> name = Optional.of(bufferJSONObject.getAsString("name"));

					Game newGameEntry = new Game(Long.valueOf(bufferString), name.get());

					if(!masterGameList.contains(newGameEntry)) {
						masterGameList.add(newGameEntry);
					}
				} catch (Exception e) {
					//TODO complete block-statement
				}
				bufferJSONObject = null;
				bufferString = null;
			}
			profile.setTotalPlaytime(foreverPlaytime);

		} catch(Exception jMapError) {
			jMapError.printStackTrace();
		}
		
		LOGGER.info("Elapsed time: {}", (System.currentTimeMillis() - start));
		return CompletableFuture.completedFuture(profile);
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
	 * @since 1.0
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
	 * @since 1.0
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
	 * @since 1.0
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
	 * Using a user in the Steam system you can create an array of their friends from our databases.
	 * 
	 * More to this however!
	 * The return will include the root user provided because it is considered a 'master' user list.
	 * 
	 * @param steamID64 user from Steam
	 * @return master user list
	 */
	public List<FriendID> compileUserSearch(String steamID64) {
		//Check if user has friends (haha probably not)
		Optional<List<FriendID>> oLFID = getUserFriends(steamID64);

		List<FriendID> masterLoopList = new ArrayList<>();
		FriendID masterUser = new FriendID();
		masterUser.setSteamID64(steamID64);
		masterLoopList.add(masterUser);

		if(oLFID.isPresent()) {
			for(FriendID fID : oLFID.get()) {
				masterLoopList.add(fID);
			}
		}
		return masterLoopList;
	}

	/**
	 * This method will take a list of games which will then create a new threaded 
	 * process to manage the check and saving of all the games.
	 * 
	 * @param gameList
	 * @version 2
	 * @since 2.5
	 * 
	 */
	@Async
	private void saveGames(List<Game> gameList) {
		gameSavingThread = new Thread(new Runnable() {
			List<Game> gameRecordList = gameList;

			@Value("${com.aaronrenner.apikey}")
			private String apikey;

			public void run() {
				try {
					for(Game game : gameRecordList) {
						if(!gameRepository.existsById(game.getId())) {
							LOGGER.info("Saving a new game to DB");
							LOGGER.info(game.toString());

							gameRepository.save(game);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		gameSavingThread.start();
	}

	/**
	 * This is a helper method, in this method you can provide a String of the URL for 
	 * requesting and it will return a JSONObject of the response from Steam.
	 * 
	 * @param getURL Pass in a String of the API request URL.
	 * @return A json-smart object of the
	 * @since 1.0
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

	/**
	 * This helper method is specific for a URL that required the extension of steamID onto it, 
	 * suppling the appropriate data will append '&steamid=' and the supplied steamid to the string.
	 * 
	 * After this small modification to the endpoint it will reach out to the endpoint using {@code getRequest()} and return the JSONObject of data
	 * 
	 * @param endpoint The steam endpoint to modify, one of the variables in this class
	 * @param steamID64 A string of 17-digit length
	 * @return The JSON data in a proprietary data format
	 * @since 1.0
	 */
	private JSONObject getSteamEndpoint(String endpoint, String steamID64) {
		String steamEndpoint = endpoint + "&steamid=" + steamID64;
		// Fetch data from URL
		JSONObject getRequest = getRequest(steamEndpoint);
		return (JSONObject) getRequest.get("response");	
	}

	/**
	 * This method is void but should always be run at least once per public interface call
	 */
	private void populateSecurityKey() {
		steamProfileEndpoint = SteamAPIEndpoints.steamProfileEndpoint.getURL() + this.apikey;
		steamGameStatsEndpoint = SteamAPIEndpoints.steamGameStatsEndpoint.getURL() + this.apikey;
		steamRecentlyPlayedEndpoint = SteamAPIEndpoints.steamRecentlyPlayedEndpoint.getURL() + this.apikey;
		steamOwnedGamesEndpoint = SteamAPIEndpoints.steamOwnedGamesEndpoint.getURL() + this.apikey;
		steamProfileLevelEndpoint = SteamAPIEndpoints.steamProfileLevelEndpoint.getURL() + this.apikey;
		steamProfileBadgeEndpoint = SteamAPIEndpoints.steamProfileBadgeEndpoint.getURL() + this.apikey;
	}

	/**
	 * This method was created for the jUnit tests, there is a bug with loading the @Value spring annotation 
	 * and reading the api key from application.properties
	 * 
	 * @param key the API key
	 * @since 1.5
	 */
	public void setAPIKey(String key) {
		this.apikey = key;
	}
}
