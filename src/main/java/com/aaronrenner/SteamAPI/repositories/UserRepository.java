package com.aaronrenner.SteamAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aaronrenner.SteamAPI.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findBySteamID64(String steamID64);
	User findByUsername(String username);

}
