package com.example.demo.student_id_card;

import com.example.demo.course.Course;
import com.example.demo.student.Student;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentIdCardController.class)
@AutoConfigureMockMvc
class StudentIdClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentIdCardService studentIdCardService;


    @Test
    public void getStudentIdCards() throws Exception {
        List<StudentIdCard> studentIdCardCollection = Arrays.asList(new StudentIdCard(UUID.randomUUID()), new StudentIdCard(UUID.randomUUID()), new StudentIdCard(UUID.randomUUID()));
        Mockito.when(studentIdCardService.getStudentIdCards(0, 5)).thenReturn(ResponseEntity.ok(studentIdCardCollection));
        mockMvc.perform(get("/api/studentCard"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"uuid\":\"" + studentIdCardCollection.get(0).getUuid() + "\"},{\"uuid\":\"" + studentIdCardCollection.get(1).getUuid() + "\"},{\"uuid\":\"" + studentIdCardCollection.get(2).getUuid() + "\"}]"));
        Mockito.verify(studentIdCardService, Mockito.times(1)).getStudentIdCards(0, 5);
    }

    @Test
    public void getStudentIdCard() throws Exception {
        StudentIdCard studentIdCard = new StudentIdCard(UUID.randomUUID());
        Mockito.when(studentIdCardService.getStudentIdCard(studentIdCard.getUuid())).thenReturn(studentIdCard);
        mockMvc.perform(get("/api/studentCard/" + studentIdCard.getUuid()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"uuid\":\"" + studentIdCard.getUuid() + "\"}"));
        Mockito.verify(studentIdCardService, Mockito.times(1)).getStudentIdCard(studentIdCard.getUuid());
    }

    @Test
    public void deleteStudentIdCard() throws Exception {
        StudentIdCard studentIdCard = new StudentIdCard(UUID.randomUUID());
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.valueOf(204));
        Mockito.when(studentIdCardService.deleteStudentIdCard(studentIdCard.getUuid())).thenReturn(responseEntity);
        mockMvc.perform(delete("/api/studentCard/" + studentIdCard.getUuid()))
                .andExpect(status().is(204));
        Mockito.verify(studentIdCardService, Mockito.times(1)).deleteStudentIdCard(studentIdCard.getUuid());
    }

    @Test
    public void postStudentIdCard() throws Exception {
        Student student = new Student(UUID.randomUUID(), "test", LocalDate.of(2000, Month.JANUARY, 1), "test@gmail.com");
        StudentIdCard studentIdCard = new StudentIdCard(UUID.randomUUID());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setLocation(URI.create("http://localhost:8083/api/studentCard/" + studentIdCard.getUuid()));
        ResponseEntity<StudentIdCard> responseEntity = new ResponseEntity<StudentIdCard>(studentIdCard, responseHeaders, HttpStatus.CREATED);
        Mockito.when(studentIdCardService.postStudentIdCard(student.getUuid())).thenReturn(responseEntity);
        mockMvc.perform(post("/api/studentCard/student/" + student.getUuid()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string("Location", "http://localhost:8083/api/studentCard/" + studentIdCard.getUuid()))
                .andExpect(content().json("{\"uuid\":" + studentIdCard.getUuid() + "}"));
        Mockito.verify(studentIdCardService, Mockito.times(1)).postStudentIdCard(student.getUuid());
    }

    @Test
    public void getStudentIdCardCourse() throws Exception {
        Course course = new Course(UUID.randomUUID(), "Test", "Test", "Test");
        Course course1 = new Course(UUID.randomUUID(), "Test1", "Test1", "Test1");
        StudentIdCard studentIdCard = new StudentIdCard(UUID.randomUUID());
        Collection<Course> courseCollection = new ArrayList<>();
        courseCollection.add(course);
        courseCollection.add(course1);
        ResponseEntity<Collection<Course>> responseEntity = new ResponseEntity<>(courseCollection, HttpStatus.OK);
        Mockito.when(studentIdCardService.getStudentIdCardCourse(studentIdCard.getUuid())).thenReturn(responseEntity);
        mockMvc.perform(get("/api/studentCard/" + studentIdCard.getUuid() + "/course"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[{\"uuid\":\"" + course.getUuid() + "\"},{\"uuid\":\"" + course1.getUuid() + "\"}]"));
        Mockito.verify(studentIdCardService, Mockito.times(1)).getStudentIdCardCourse(studentIdCard.getUuid());
    }

    @Test
    void getStudentStudentIdCard() throws Exception {
        Student student = new Student(UUID.randomUUID(), "dede", LocalDate.of(2000, Month.AUGUST, 11), "dedeUnit@msn.com");
        StudentIdCard studentIdCard = new StudentIdCard(UUID.randomUUID());
        studentIdCard.setStudent(student);
        ResponseEntity<Student> studentResponseEntity = new ResponseEntity<>(studentIdCard.getStudent(), HttpStatus.OK);
        Mockito.when(studentIdCardService.getStudentStudentIdCard(studentIdCard.getUuid())).thenReturn(studentResponseEntity);
        mockMvc.perform(get("/api/studentCard/" + studentIdCard.getUuid() + "/student"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{\"name\":\"dede\",\"dob\":\"2000-08-11\",\"email\":\"dedeUnit@msn.com\"}"));
        Mockito.verify(studentIdCardService, Mockito.times(1)).getStudentStudentIdCard(studentIdCard.getUuid());
    }

}
