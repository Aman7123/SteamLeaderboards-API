package com.aaronrenner.SteamAPI.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aaronrenner.SteamAPI.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findBySteamID64(String steamID64);
	Optional<User> findByUsername(String Username);

}
