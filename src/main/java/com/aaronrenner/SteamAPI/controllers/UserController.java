package com.aaronrenner.SteamAPI.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.aaronrenner.SteamAPI.exceptions.AuthorizationError;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.LoginService;
import com.aaronrenner.SteamAPI.services.UserService;

@RestController
public class UserController {
	
	final private String BASEURL = "/users";
	final private String SELECTUSERURL = BASEURL + "/{steamID64}";
	final private String FRIENDBASEURL = SELECTUSERURL + "/friendslist";
	final private String SELECTUSERFRIENDURL =  FRIENDBASEURL + "/{friendSteamID64}";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginService loginService;
	
	// Root URL
	@GetMapping(BASEURL)
	public List<User> getUserList(@RequestHeader("Authorization") Optional<String> oAuthToken) {
		if(oAuthToken.isPresent()) { // if authorization
			User approvedeUser = getUserFromAuth(oAuthToken.get());
			if(userLogin.getRole().equals("admin")) {

			} else {
				// TODO create error
				throw new AuthorizationError();
			}
			return this.userService.getUserList();
		}
		// Catch no included Auth header
		throw new AuthorizationError();
	}
	
	@PostMapping(BASEURL)
	@ResponseStatus(value= HttpStatus.CREATED)
	public void createUser(@RequestBody User newUser) {
		this.userService.createUser(newUser);
	}
	
	// USER SPECIFIC ENDPOINT
	@GetMapping(SELECTUSERURL)
	public User getUser(@RequestHeader("Authorization") String oAuthToken, @PathVariable String steamID64) {
		return this.userService.getUser(steamID64);
	}
	
	@PatchMapping(SELECTUSERURL)
	@ResponseStatus(value= HttpStatus.ACCEPTED)
	public User updateUser(@RequestHeader("Authorization") String oAuthToken, @PathVariable String steamID64, @RequestBody User userModel) {
		return this.userService.updateUser(steamID64, userModel);
	}
	
	@DeleteMapping(SELECTUSERURL)
	public void deleteUser(@RequestHeader("Authorization") String oAuthToken, @PathVariable String steamID64) {
		this.userService.deleteUser(steamID64);
	}
	
	// USER FRIENDS LIST COMMANDS
	@GetMapping(FRIENDBASEURL)
	public List<FriendID> getFriend(@RequestHeader("Authorization") String oAuthToken, @PathVariable String steamID64) {
		return this.userService.getFriend(steamID64);
	}
	
	@PostMapping(SELECTUSERFRIENDURL)
	@ResponseStatus(value= HttpStatus.CREATED)
	public void createFriend(@RequestHeader("Authorization") String oAuthToken, @PathVariable String steamID64, @PathVariable String friendSteamID64) {
		this.userService.createFriend(steamID64, friendSteamID64);
	}
	
	@DeleteMapping(SELECTUSERFRIENDURL)
	public void deleteFriend(@RequestHeader("Authorization") String oAuthToken, @PathVariable String steamID64, @PathVariable String friendSteamID64) {
		this.userService.deleteFriend(steamID64, friendSteamID64);
	}
	
	private User getUserFromAuth(String bearerPass) {
		String[] parseToken = bearerPass.split(" "); // split [0]="bearer", [1]="xxx"
		if(parseToken[0].equals("Bearer")) { // if Bearer is Bearer
			return loginService.validateToken(parseToken[1]);
		}
		// Catch wrong auth format
		throw new AuthorizationError();

	}
}
