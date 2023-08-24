package com.example.demo.users;

import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Data
@Validated
@RestController
@RequestMapping("api/users")
public class UsersController {

    private final UsersService usersService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postUser(@Valid @RequestBody Users users) throws NoSuchAlgorithmException {
        return usersService.postUsers(users);
    }

    @GetMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logUser(@RequestHeader("Authorization") String basic) throws Exception {
        return usersService.logUser(basic);
    }

}
