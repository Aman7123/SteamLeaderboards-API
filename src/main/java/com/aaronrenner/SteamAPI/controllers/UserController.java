package com.aaronrenner.SteamAPI.controllers;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.aaronrenner.SteamAPI.exceptions.AuthorizationError;
import com.aaronrenner.SteamAPI.exceptions.BadRequestError;
import com.aaronrenner.SteamAPI.models.FriendID;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.LoginService;
import com.aaronrenner.SteamAPI.services.UserService;

@RestController
public class UserController {

	final private String BASEURL = "/users";
	final private String SELECTUSERURL = BASEURL + "/{steamID64}";
	final private String FRIENDBASEURL = SELECTUSERURL + "/friends-list";
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
			if(approvedeUser.getRole().equals("admin")) {
				return this.userService.getUserList();
			} else {
				throw new AuthorizationError("Permission missing, try /users/<yourSteamID>");
			}
		}
		// Catch no included OAuth header
		throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");
	}

	@PostMapping(BASEURL)
	@ResponseStatus(value= HttpStatus.CREATED)
	public void createUser(@RequestBody Optional<User> newUser) {
		if(newUser.isEmpty()) {
			throw new BadRequestError("Missing JSON with \"username\", \"steamID64\" and \"password\"");
		}
		this.userService.createUser(newUser.get());
	}

	// USER SPECIFIC ENDPOINT
	@GetMapping(SELECTUSERURL)
	public User getUser(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable String steamID64) {
		if(oAuthToken.isPresent()) { // if authorization
			User approvedeUser = getUserFromAuth(oAuthToken.get());
			if(approvedeUser.getRole().equals("admin") || approvedeUser.getSteamID64().equals(steamID64)) {
				return this.userService.getUser(steamID64);
			} else {
				throw new AuthorizationError("Permission missing, try /users/<yourSteamID>");
			}
		}
		// Catch no included OAuth header
		throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");
	}

	@PatchMapping(SELECTUSERURL)
	@ResponseStatus(value= HttpStatus.ACCEPTED)
	public User updateUser(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable String steamID64, @RequestBody User userModel) {
		if(oAuthToken.isPresent()) { // if authorization
			User approvedeUser = getUserFromAuth(oAuthToken.get());
			if(approvedeUser.getRole().equals("admin") || approvedeUser.getSteamID64().equals(steamID64)) {
				return this.userService.updateUser(steamID64, userModel);
			} else {
				throw new AuthorizationError("Permission missing, try /users/<yourSteamID>");
			}
		}
		// Catch no included OAuth header
		throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");
	}

	@DeleteMapping(SELECTUSERURL)
	public void deleteUser(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable String steamID64) {
		if(oAuthToken.isEmpty()) {
			throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");
		}

		User approvedeUser = getUserFromAuth(oAuthToken.get());
		if(approvedeUser.getRole().equals("admin") || approvedeUser.getSteamID64().equals(steamID64)) {
			this.userService.deleteUser(steamID64);
		} else {
			throw new AuthorizationError("Permission missing, try /users/<yourSteamID>");
		}
	}

	// USER FRIENDS LIST COMMANDS
	@GetMapping(FRIENDBASEURL)
	public List<FriendID> getFriend(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable String steamID64) {
		if(oAuthToken.isPresent()) { // if authorization
			User approvedeUser = getUserFromAuth(oAuthToken.get());
			if(approvedeUser.getRole().equals("admin") || approvedeUser.getSteamID64().equals(steamID64)) {
				return this.userService.getFriend(steamID64);
			} else {
				throw new AuthorizationError("Permission missing, try /users/<yourSteamID>");
			}
		}
		// Catch no included OAuth header
		throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");
	}

	@PostMapping(SELECTUSERFRIENDURL)
	@ResponseStatus(value= HttpStatus.CREATED)
	// TODO fix
	public void createFriend(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable String steamID64, @PathVariable String friendSteamID64) {
		if(oAuthToken.isEmpty()) {
			throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");
		}

		User approvedeUser = getUserFromAuth(oAuthToken.get());
		if(approvedeUser.getRole().equals("admin") || approvedeUser.getSteamID64().equals(steamID64)) {
			this.userService.createFriend(steamID64, friendSteamID64);
		} else {
			throw new AuthorizationError("Permission missing, try /users/<yourSteamID>");
		}
	}

	@DeleteMapping(SELECTUSERFRIENDURL)
	public void deleteFriend(@RequestHeader("Authorization") Optional<String> oAuthToken, @PathVariable String steamID64, @PathVariable String friendSteamID64) {
		if(oAuthToken.isEmpty()) {
			throw new AuthorizationError("Missing JWT token, POST \"/users\" or \"/login\" first");
		}

		User approvedeUser = getUserFromAuth(oAuthToken.get());
		if(approvedeUser.getRole().equals("admin") || approvedeUser.getSteamID64().equals(steamID64)) {
			this.userService.deleteFriend(steamID64, friendSteamID64);
		} else {
			throw new AuthorizationError("Permission missing, try /users/<yourSteamID>");
		}

	}

	private User getUserFromAuth(String bearerPass) {
		String[] parseToken = bearerPass.split(" "); // split [0]="bearer", [1]="xxx"
		if(parseToken[0].equals("Bearer")) { // if Bearer is Bearer
			return loginService.validateToken(parseToken[1]);
		}
		// Catch wrong auth format
		throw new AuthorizationError("JWT token bad format, check header");

	}
}
