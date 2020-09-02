package com.aaronrenner.SteamAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.aaronrenner.SteamAPI.models.Token;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.LoginService;

@RestController
public class LoginController {
	
	final private String LOGINURL = "/login";
	
	@Autowired
	private LoginService loginService;
	
	@PostMapping(LOGINURL)
	@ResponseStatus(value= HttpStatus.OK)
	public Token createLoginToken(@RequestBody User newLoginUser) {
		return this.loginService.createToken(newLoginUser);
	}

}
