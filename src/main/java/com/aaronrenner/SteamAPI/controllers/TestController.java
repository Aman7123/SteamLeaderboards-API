package com.aaronrenner.SteamAPI.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
public class TestController {
	
	final private String HELLOURL = "/hello";
	
	@GetMapping(HELLOURL)
	@ResponseStatus(value= HttpStatus.I_AM_A_TEAPOT)
	public void hello() {
	}

}
