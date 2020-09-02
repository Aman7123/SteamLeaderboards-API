package com.aaronrenner.SteamAPI.services;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.aaronrenner.SteamAPI.exceptions.AuthorizationError;
import com.aaronrenner.SteamAPI.exceptions.LoginError;
import com.aaronrenner.SteamAPI.models.Token;
import com.aaronrenner.SteamAPI.models.User;
import com.aaronrenner.SteamAPI.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class LoginServiceIMPL implements LoginService {
	
	@Value("${com.aaronrenner.tokenkey}")
	private String tokenkey = "";
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public Token createToken(User bufferUser) {
		Optional<User> registeredUser = userRepository.findByUsername(bufferUser.getUsername());
		if(registeredUser.isPresent()) {
			User perminantUser = registeredUser.get();
			if(bufferUser.getPassword() != null && bufferUser.getPassword().equals(perminantUser.getPassword())) {
				try {
					String username = perminantUser.getUsername();
					String steamID64 = perminantUser.getSteamID64();
					String role = perminantUser.getRole();
					// Create new token here
					String tokenString = JWT.create().withClaim("username", username).withClaim("steamID64", steamID64).withClaim("role", role).sign(Algorithm.HMAC256(tokenkey));
					
					Token newToken = new Token(tokenString);
					return newToken;
					
				} catch (Exception e) {
					throw new LoginError();
				}
			}
		}
		throw new LoginError();
	}

	@Override
	public User validateToken(String token) {
		User bufferUser = new User();
		try {
			Algorithm nA = Algorithm.HMAC256(tokenkey);
			JWTVerifier verifier = JWT.require(nA).build();
			DecodedJWT jwt = verifier.verify(token);
			Map<String, Claim> maps = jwt.getClaims();
			for(Map.Entry me : maps.entrySet()) {
				String key = (String)me.getKey();
				switch(key) {
				case "username":
					Claim usernameClaim = (Claim) me.getValue();
					String username = usernameClaim.asString();
					Optional<User> userByUsername = userRepository.findByUsername(username);
					if(userByUsername.isPresent()) {
						bufferUser = userByUsername.get();
					} else {
						throw new AuthorizationError();
					}
					break;
				case "steamID64":
					Claim steamID64Claim = (Claim) me.getValue();
					String steamID64 = steamID64Claim.asString();
					Optional<User> userBySteamID = userRepository.findBySteamID64(steamID64);
					if(userBySteamID.isPresent()) {
						bufferUser = userBySteamID.get();
					} else {
						throw new AuthorizationError();
					}
				}
				
			}
			
		} catch (JWTDecodeException | IllegalArgumentException | SignatureVerificationException | UnsupportedEncodingException e) {
			throw new AuthorizationError();
		}	
		
		return bufferUser;
			
		}

}
