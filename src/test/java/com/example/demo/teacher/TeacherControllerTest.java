package com.example.demo.teacher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// this, tells to the spring core that this class, is a test class for the controller class. an MvcTest.
@WebMvcTest(TeacherController.class)
public class TeacherControllerTest {

    @Autowired
    // the autowire annotations, tells to the spring framework, that an existing bean of the instance of this class already exists
    // and automatically wire it to this object.
    private MockMvc mockMvc;

    //those annotations need to be implemented because, when we are running unit tests, the whole programme is not starting,
    // just a fraction of the program is only booting up. so, all the objects, resources needed are loaded.
    @MockBean
    private TeacherService teacherService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTeacherUuid() throws Exception {
        Teacher teacher = new Teacher(UUID.randomUUID(), "teacher_name", LocalDate.parse("2022-10-01"), "male", "teacher@email.com");
        Mockito.when(teacherService.getTeacherUuid(teacher.getUuid())).thenReturn(teacher);
        mockMvc.perform(get("/api/teacher/" + teacher.getUuid())).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"uuid\":\"" + teacher.getUuid() + "\",\"name\":\"" + "teacher_name" + "\", \"dob\":\"" + "2022-10-01" + "\", \"gender\": \"" + "male" + "\" ,\"email\":\"" + "teacher@email.com" + "\"}"));
        Mockito.verify(teacherService, Mockito.times(1)).getTeacherUuid(teacher.getUuid());
    }

    @Test
    void getTeacherEmail() throws Exception {
        Teacher teacher = new Teacher(UUID.randomUUID(), "teacher_name", LocalDate.parse("2022-10-01"), "male", "teacher@email.com");
        Mockito.when(teacherService.getTeacherEmail(teacher.getEmail())).thenReturn(Optional.of(teacher));
        mockMvc.perform(get("/api/teacher/email/teacher@email.com")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().json("{\"uuid\":\"" + teacher.getUuid() + "\",\"name\":\"" + "teacher_name" + "\", \"dob\":\"" + "2022-10-01" + "\", \"gender\": \"" + "male" + "\" ,\"email\":\"" + "teacher@email.com" + "\"}"));
        Mockito.verify(teacherService, Mockito.times(1)).getTeacherEmail(teacher.getEmail());
    }

    @Test
    void deleteTeacher() throws Exception {
        Teacher teacher = new Teacher(UUID.randomUUID(), "teacher_name", LocalDate.parse("2022-10-01"), "male", "teacher@email.com");
        ResponseEntity<String> responseEntity = ResponseEntity.status(204).contentType(MediaType.APPLICATION_JSON).body("teacher successfully deleted");
        Mockito.when(teacherService.deleteTeacher(teacher.getUuid())).thenReturn(responseEntity);
        mockMvc.perform(delete("/api/teacher/" + teacher.getUuid())).andExpect(status().is(204)).andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(content().string("teacher successfully deleted"));
        Mockito.verify(teacherService, Mockito.times(1)).deleteTeacher(teacher.getUuid());
    }

    @Test
    void postTeacher() throws Exception {
        Teacher teacher = new Teacher(null, "teacher_name", LocalDate.parse("1970-10-01"), "male", "teacher@contactinfo.com");
        ResponseEntity<Teacher> responseEntity = ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(teacher);
        Mockito.when(teacherService.postTeacher(teacher)).thenReturn(responseEntity);
        mockMvc.perform(post("/api/teacher").contentType(MediaType.APPLICATION_JSON_VALUE).content(objectMapper.writeValueAsString(teacher))).andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateTeacher() throws Exception {
        Teacher teacher = new Teacher(UUID.randomUUID(), "previous_name", LocalDate.parse("2022-10-01"), "male", "previous_teacher@email.com");
        Teacher teacher1 = new Teacher(teacher.getUuid(), "next_name", LocalDate.parse("2022-10-01"), "male", "next_teacher@email.com");
        ResponseEntity<Teacher> responseEntity = ResponseEntity.status(204).contentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE)).body(teacher1);
        HashMap<String, String> map = new HashMap();
        map.put("name", "next_name");
        map.put("email", "next_teacher@email.com");
        Mockito.when(teacherService.updateTeacher(teacher.getUuid(), map)).thenReturn(responseEntity);
        mockMvc.perform(put("/api/teacher/" + teacher.getUuid()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(map))).andExpect(status().is(204)).andExpect(content().contentType(MediaType.APPLICATION_JSON));
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(teacher1.getName(), responseEntity.getBody().getName());
        Assertions.assertEquals(teacher1.getEmail(), responseEntity.getBody().getEmail());
        Assertions.assertEquals(204, responseEntity.getStatusCode().value());
        Mockito.verify(teacherService, Mockito.times(1)).updateTeacher(teacher.getUuid(), map);
    }
}
