package com.aaronrenner.SteamAPI.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.models.Token;
import com.aaronrenner.SteamAPI.services.LoginServiceIMPL;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {
	
	ObjectMapper om = new ObjectMapper();
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LoginServiceIMPL loginService;
	
	@BeforeEach
	public void SetUp() {
		Mockito.when(loginService.createToken(Mockito.any(User.class))).thenReturn(new Token(""));
	}
	
	@AfterEach
	public void tearDown() {

	}
	
	@Test
	private void injectedComponentsAreNotNull() {
		assertNotNull(mockMvc);
		assertNotNull(loginService);
	}
	
	@Test
	private void runLoginTest( ) throws Exception {
		String loginUserString = "{\"username\":\"test_user\",\"password\":\"1234\"}";
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/login").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(loginUserString )).andDo(MockMvcResultHandlers.print()).andReturn();
		
		assert(200 == mvcResult.getResponse().getStatus());
		
	}
}
