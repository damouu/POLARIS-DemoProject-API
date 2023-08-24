package com.example.demo.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    @Query("SELECT p FROM users p WHERE p.id = :usersId")
    Optional<Users> findUserById(Integer usersId);

}
