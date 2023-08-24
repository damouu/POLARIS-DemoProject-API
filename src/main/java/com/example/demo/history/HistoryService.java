package com.example.demo.history;

import com.example.demo.users.Users;
import com.example.demo.users.UsersRepository;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final UsersRepository usersRepository;

    public HistoryService(UsersRepository usersRepository, HistoryRepository historyRepository) {
        this.usersRepository = usersRepository;
        this.historyRepository = historyRepository;
    }

    public ResponseEntity<History> postHistory(boolean result, Users users) {
        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedUTC = localDateTime.atZone(ZoneId.of("UTC"));
        ZonedDateTime zonedIST = zonedUTC.withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
        History history = new History();
        history.setLogTime(zonedIST.toLocalDateTime().withNano(0));
        history.setResult(history.getResult());
        history.setUsers(users);
        history.setResult(result);
        historyRepository.save(history);
        return ResponseEntity.status(201).body(history);
    }

    public Optional<List<History>> getUserHistory(Optional<Users> users) {
        return historyRepository.getUserHistory(users.get().getId());

    }
}
