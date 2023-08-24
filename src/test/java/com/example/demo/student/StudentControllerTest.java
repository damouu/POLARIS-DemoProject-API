package com.example.demo.student;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void allStudents() throws Exception {
        List<Student> studentList = new ArrayList<>();
        Student student = new Student(UUID.randomUUID(), "student", LocalDate.of(2000, Month.NOVEMBER, 1), "student@email.com");
        Student student1 = new Student(UUID.randomUUID(), "student1", LocalDate.of(2000, Month.NOVEMBER, 1), "student1@email.com");
        studentList.add(student);
        studentList.add(student1);
        Mockito.when(studentService.allStudents(0, 5)).thenReturn(studentList);
        mockMvc.perform(get("/api/student"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"name\":\"student\",\"dob\":\"2000-11-01\",\"email\":\"student@email.com\"},{\"name\":\"student1\",\"dob\":\"2000-11-01\",\"email\":\"student1@email.com\"}]"));
        Mockito.verify(studentService, Mockito.times(1)).allStudents(0, 5);
    }

    @Test
    void findById() throws Exception {
        UUID uuid = UUID.randomUUID();
        Student student = new Student(uuid, "student", LocalDate.of(2000, Month.NOVEMBER, 1), "student@email.com");
        Mockito.when(studentService.getStudent(uuid)).thenReturn(student);
        mockMvc.perform(get("/api/student/" + student.getUuid()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\":\"student\",\"dob\":\"2000-11-01\",\"email\":\"student@email.com\"}"));
        Mockito.verify(studentService, Mockito.times(1)).getStudent(uuid);
    }

    @Test
    void inertNewStudent() throws Exception {
        UUID uuid = UUID.randomUUID();
        Student student = new Student(uuid, "student", LocalDate.of(2000, Month.NOVEMBER, 1), "student@email.com");
        Mockito.when(studentService.postStudent(student)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(student));
        mockMvc.perform(post("/api/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteStudent() throws Exception {
        Mockito.when(studentService.deleteStudent(UUID.fromString("da37914b-d612-4903-b833-5ed83f1a1dc0"))).thenReturn(ResponseEntity.status(204).body("student successfully deleted"));
        mockMvc.perform(delete("/api/student/" + "da37914b-d612-4903-b833-5ed83f1a1dc0"))
                .andExpect(status().is(204))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("student successfully deleted"));
        Mockito.verify(studentService, Mockito.times(1)).deleteStudent(UUID.fromString("da37914b-d612-4903-b833-5ed83f1a1dc0"));
    }

    @Test
    void updateStudent() throws Exception {
        Student student = new Student(UUID.randomUUID(), "student", LocalDate.of(2000, Month.NOVEMBER, 1), "student@email.com");
        Mockito.when(studentService.updateStudent(student.getUuid(), student)).thenReturn(ResponseEntity.status(HttpStatus.valueOf(204)).body(student));
        mockMvc.perform(put("/api/student/" + student.getUuid())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().is2xxSuccessful());
    }
}