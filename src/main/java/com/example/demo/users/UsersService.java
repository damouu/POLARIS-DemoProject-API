package com.example.demo.users;

import com.example.demo.history.History;
import com.example.demo.history.HistoryRepository;
import com.example.demo.history.HistoryService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@Data
public class UsersService {

    private final UsersRepository usersRepository;
    private final HistoryRepository historyRepository;
    private final HistoryService historyService;

    public UsersService(HistoryRepository historyRepository, UsersRepository usersRepository, HistoryService historyService) {
        this.historyRepository = historyRepository;
        this.usersRepository = usersRepository;
        this.historyService = historyService;
    }

    public ResponseEntity<String> postUsers(Users users) {
        String encoded = Base64.getEncoder().encodeToString(users.getPwd().getBytes());
        users.setPwd(encoded);
        usersRepository.save(users);
        return ResponseEntity.status(201).body("登録が完了しました");
    }

    public ResponseEntity<String> logUser(String s) throws Exception {
        int countSuccessLog = 0;
        byte[] decodedBytes = Base64.getDecoder().decode(s.replace("Basic", "").strip());
        String[] split = new String(decodedBytes).split(":");
        String userId = split[0], userPassword = split[1];
        ZoneId zid = ZoneId.of("Asia/Tokyo");
        Duration duration;
        ResponseEntity<String> response = null;
        String bodyResponse = null;
        Optional<Users> user;
        try {
            user = usersRepository.findUserById(Integer.valueOf(userId));
            String decoded = new String(Base64.getDecoder().decode(user.get().getPwd()));
            Optional<List<History>> userHistory = historyService.getUserHistory(user);
            if (userHistory.isEmpty() || userHistory.get().size() < 3) {
                if (decoded.equals(userPassword)) {
                    countSuccessLog++;
                    bodyResponse = "ログイン出来ました";
                    historyService.postHistory(true, user.get());
                } else {
                    bodyResponse = userHistory.map(histories -> "クレデンシャルが間違っています, 五分以内に" + (userHistory.get().size() + 1) + "回をお間違いました.").orElse("クレデンシャルが違いです, 五分の内に今回は1回目の間違いのです.");
                    historyService.postHistory(false, user.get());
                    if (historyService.getUserHistory(user).get().size() == 3) {
                        duration = Duration.ofMinutes(Duration.between(LocalDateTime.now(zid), userHistory.get().get(0).getLogTime().plusMinutes(6)).toMinutes());
                        bodyResponse = "五分の内に３回が間違って" + duration.toMinutes() + "分" + "をお待ちください.";
                    }
                }
            } else {
                for (History history : userHistory.get()) {
                    if (decoded.equals(userPassword) && history.getResult()) {
                        countSuccessLog++;
                        bodyResponse = "ログイン出来ました";
                    }
                }
                if (countSuccessLog < 1) {
                    duration = Duration.ofMinutes(Duration.between(LocalDateTime.now(zid).withNano(0), userHistory.get().get(0).getLogTime().withNano(0).plusMinutes(6)).toMinutes());
                    bodyResponse = "五分の内に３回が間違って" + duration.toMinutes() + "分" + "をお待ちください.";
                }
            }

        } catch (EntityNotFoundException exception) {
            throw new Exception(exception);
        }
        if (countSuccessLog > 0) {
            response = ResponseEntity.status(200).body(bodyResponse);

        } else {
            response = ResponseEntity.status(401).body(bodyResponse);
        }
        return response;
    }
}
