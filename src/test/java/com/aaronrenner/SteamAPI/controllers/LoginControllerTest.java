package com.aaronrenner.SteamAPI.controllers;

import java.util.*;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.aaronrenner.SteamAPI.models.Game;
import com.aaronrenner.SteamAPI.models.TokenTest;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.services.GameService;
import com.aaronrenner.SteamAPI.services.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	private LoginService loginService;
	
	private ObjectMapper om = new ObjectMapper();
	
	@Test
	public void getLoginToken() throws Exception {
		User userLogin = new User();
		userLogin.setUsername("Aaron");
		userLogin.setPassword("1234");
		String userLogin_JSON = om.writeValueAsString(userLogin);
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/login").contentType(MediaType.APPLICATION_JSON).content(userLogin_JSON)).andDo(print()).andReturn();
	}

}
