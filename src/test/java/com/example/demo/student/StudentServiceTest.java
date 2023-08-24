package com.example.demo.student;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    void allStudents() {

    }

    @Test
    void getStudent() {
        Mockito.when(studentRepository.findStudentByUuid(UUID.fromString("a505e6fd-8504-47c7-876d-03947e3bbcaf"))).thenReturn(java.util.Optional.of(new Student(UUID.fromString("a505e6fd-8504-47c7-876d-03947e3bbcaf"), "student", LocalDate.of(2000, Month.JULY, 11), "student@msn.com")));
        Student student3 = studentService.getStudent(UUID.fromString("a505e6fd-8504-47c7-876d-03947e3bbcaf"));
        Mockito.verify(studentRepository, Mockito.times(1)).findStudentByUuid(UUID.fromString("a505e6fd-8504-47c7-876d-03947e3bbcaf"));
        Assertions.assertNotNull(student3);
        Assertions.assertEquals(student3.getUuid(), UUID.fromString("a505e6fd-8504-47c7-876d-03947e3bbcaf"));
    }

    @Test
    void postStudent() {
        Student student = new Student(UUID.randomUUID(), "pokemon", LocalDate.now(), "son.wiza@hotmail.com");
        Mockito.when(studentRepository.save(student)).thenReturn(student);
        Mockito.when(studentRepository.findStudentsByEmail(student.getEmail())).thenReturn(Optional.empty());
        var student3 = studentService.postStudent(student);
        Assertions.assertNotNull(student3);
        Assertions.assertEquals(student.getName(), Objects.requireNonNull(student3.getBody()).getName());
        Mockito.verify(studentRepository, Mockito.times(1)).save(student);
        Mockito.verify(studentRepository, Mockito.times(1)).findStudentsByEmail(student.getEmail());
    }

    @Test
    void deleteStudent() {
        Student student = new Student(UUID.randomUUID(), "test", LocalDate.now(), "email@ok.com");
        Mockito.when(studentRepository.findStudentByUuid(student.getUuid())).thenReturn(java.util.Optional.of(student));
        ResponseEntity<String> dede = studentService.deleteStudent(student.getUuid());
        Assertions.assertEquals(dede.getBody(), "student successfully deleted");
        Assertions.assertTrue(dede.getStatusCode().is2xxSuccessful());
        Mockito.verify(studentRepository, Mockito.times(1)).delete(student);
    }

    @Test
    void updateStudent() {
        var uuid = UUID.randomUUID();
        var studentRetrieved = new Student(uuid, "first_iteration", LocalDate.now(), "first_iteration@hotmail.com");
        var studentUpdates = new Student(uuid, "second_iteration", LocalDate.now(), "second_iteration@hotmail.com");
        Mockito.when(studentRepository.findStudentByUuid(studentRetrieved.getUuid())).thenReturn(java.util.Optional.of(studentRetrieved));
        ResponseEntity<Student> responseEntity = studentService.updateStudent(studentRetrieved.getUuid(), studentUpdates);
        Assertions.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        Assertions.assertSame(responseEntity.getBody(), studentRetrieved);
        Assertions.assertEquals(responseEntity.getBody(), studentRetrieved);
        Assertions.assertEquals(studentRetrieved.getName(), studentUpdates.getName());
        Assertions.assertEquals(studentRetrieved.getEmail(), studentUpdates.getEmail());
        Assertions.assertNotEquals(studentRetrieved.getName(), "first_iteration");
        Assertions.assertNotEquals(studentRetrieved.getEmail(), "first_iteration@hotmail.com");
        Mockito.verify(studentRepository, Mockito.times(1)).save(studentRetrieved);
    }
}