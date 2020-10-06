package com.aaronrenner.SteamAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aaronrenner.SteamAPI.models.FriendID;

@Repository
public interface FriendRepository extends JpaRepository<FriendID, Long> {
}
