package com.example.demo.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Validated
@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    @Query(nativeQuery = true, value = "SELECT * FROM test.public.history h WHERE user_id = :userid AND h.logtime >= (SELECT current_timestamp AT TIME ZONE 'Asia/Tokyo' - INTERVAL '5 minutes') AND h.logtime <= (SELECT current_timestamp AT TIME ZONE 'Asia/Tokyo')ORDER BY h.logTime desc limit 3")
    Optional<List<History>> getUserHistory(int userid);

    @Query("SELECT max (h.logTime) FROM history h WHERE h.users.id = :userid ")
    LocalDateTime getLatestLogIn(Integer userid);

}
