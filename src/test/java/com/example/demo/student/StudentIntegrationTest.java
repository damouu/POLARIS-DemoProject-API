package com.example.demo.student;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class StudentIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    void allStudents() {
        ResponseEntity<List> responseEntity =
                this.restTemplate.getForEntity("http://localhost:" + port + "/api/student", List.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).includes(MediaType.APPLICATION_JSON));
        Assertions.assertFalse(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    void getStudent() {
        Student student = new Student(UUID.randomUUID(), "test",
                LocalDate.of(2000, Month.JANUARY, 21), "test.finalfantasy@hotmail.com");
        studentRepository.save(student);
        Optional<Student> student1 = studentRepository.findStudentsByEmail(student.getEmail());
        ResponseEntity<Student> responseEntity =
                this.restTemplate.getForEntity("http://localhost:" + port + "/api/student/" + student1.get().getUuid(), Student.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).includes(MediaType.APPLICATION_JSON));
        Assertions.assertEquals(student1.get().getUuid(), Objects.requireNonNull(responseEntity.getBody()).getUuid());
        Assertions.assertEquals(student1.get().getName(), Objects.requireNonNull(responseEntity.getBody()).getName());
        Assertions.assertEquals(student1.get().getDob(), Objects.requireNonNull(responseEntity.getBody()).getDob());
        Assertions.assertEquals(student1.get().getEmail(), Objects.requireNonNull(responseEntity.getBody()).getEmail());
    }

    @Test
    void postStudent() throws Exception {
        Student student = new Student(UUID.randomUUID(), "test", LocalDate.of(2000, Month.APRIL, 21), "test@hotmail.com");
        var responseEntity = restTemplate.postForEntity("http://localhost:" + port + "/api/student", student, Student.class);
        Assertions.assertTrue(Objects.requireNonNull(responseEntity.getHeaders().getContentType()).isCompatibleWith(MediaType.APPLICATION_JSON));
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertEquals(URI.create("http://localhost:8083/api/student/" + student.getUuid()), responseEntity.getHeaders().getLocation());
        Assertions.assertEquals(student.getUuid(), Objects.requireNonNull(responseEntity.getBody()).getUuid());
        Assertions.assertEquals(student.getEmail(), Objects.requireNonNull(responseEntity.getBody()).getEmail());
    }

    @Test
    void deleteStudent() throws Exception {
        Optional<Student> student = studentRepository.findById(1);
        restTemplate.delete("http://localhost:" + port + "/api/student/" + student.get().getUuid());
        Optional<Student> optionalStudent = studentRepository.findById(1);
        Assertions.assertTrue(optionalStudent.isEmpty());
    }

    @Test
    void updateStudent() throws Exception {
        var studentUpdates = new Student(null, "second_iteration", LocalDate.now(), "second_iteration@hotmail.com");
        HttpEntity<Student> entity = new HttpEntity<Student>(studentUpdates);
        ResponseEntity<String> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/student/" + studentRepository.findById(2).get().getUuid(),
                HttpMethod.PUT, entity, String.class);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
    }
}