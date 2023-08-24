package com.example.demo.student_id_card;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
public class StudentIdCardRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentIdCardRepository studentIdCardRepository;

    @Test
    public void findStudentIdCardByUuid() {
        StudentIdCard studentIdCard = new StudentIdCard(UUID.randomUUID());
        entityManager.persist(studentIdCard);
        entityManager.flush();
        Optional<StudentIdCard> optional = studentIdCardRepository.findStudentIdCardByUuid(studentIdCard.getUuid());
        Assertions.assertFalse(optional.isEmpty());
    }
}
