package com.aaronrenner.SteamAPI.exceptions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.aaronrenner.SteamAPI.models.TokenTest;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ExceptionHandlerTest {

	private String adminToken = "Bearer " + new TokenTest().getToken();
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void testUserEndpoint() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/users")).andExpect(MockMvcResultMatchers.status().is(401));
		mockMvc.perform(MockMvcRequestBuilders.get("/users/12345678901234567").header("Authorization", adminToken)).andExpect(MockMvcResultMatchers.status().is(409));
		mockMvc.perform(MockMvcRequestBuilders.delete("/users/007/friends-list/12345678901234567").header("Authorization", adminToken)).andExpect(MockMvcResultMatchers.status().is(409));
		mockMvc.perform(MockMvcRequestBuilders.post("/users/007/friends-list/76561198089525491").header("Authorization", adminToken)).andExpect(MockMvcResultMatchers.status().is(409));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/games/0101")).andExpect(MockMvcResultMatchers.status().is(409));
		
		String gameString = "{\"id\":\"252490\",\"title\":\"Rust\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/games").header("Authorization", adminToken).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(gameString)).andExpect(MockMvcResultMatchers.status().is(409));
		
		//String loginUserString = "{\"fake_JSON_Key\":\"test_user\",\"fake_JSON_Key\":\"1234\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/login")).andExpect(MockMvcResultMatchers.status().is(400));
	}

}
