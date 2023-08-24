package com.example.demo.student;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void findStudentsByEmail() {
        Student student = new Student(UUID.randomUUID(), "test", LocalDate.of(2000, Month.APRIL, 21), "test@hotmail.com");
        entityManager.persist(student);
        entityManager.flush();
        Optional<Student> optional = studentRepository.findStudentsByEmail(student.getEmail());
        Assertions.assertFalse(optional.isEmpty());
        Assertions.assertEquals(student.getEmail(), optional.get().getEmail());
    }

    @Test
    void findStudentByUuid() {
        Student student = new Student(UUID.randomUUID(), "test", LocalDate.of(2000, Month.APRIL, 21), "test@hotmail.com");
        entityManager.persist(student);
        entityManager.flush();
        Optional<Student> optional = studentRepository.findStudentByUuid(student.getUuid());
        Assertions.assertFalse(optional.isEmpty());
        Assertions.assertEquals(student.getUuid(), optional.get().getUuid());
    }
}