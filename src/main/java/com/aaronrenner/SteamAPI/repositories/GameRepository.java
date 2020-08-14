package com.aaronrenner.SteamAPI.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aaronrenner.SteamAPI.models.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
	
	List<Game> findByTitle(String title);
	List<Game> findByTitleIgnoreCase(String title);
	

}
