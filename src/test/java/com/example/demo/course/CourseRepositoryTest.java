package com.example.demo.course;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;
import java.util.UUID;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void findByUuid() {
        Course course = new Course(UUID.randomUUID(), "course_test", "campus_test", "university_test");
        entityManager.persist(course);
        entityManager.flush();
        Optional<Course> optional = courseRepository.findByUuid(course.getUuid());
        Assertions.assertNotNull(optional);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(course.getUuid(), optional.get().getUuid());
        Assertions.assertEquals(course.getName(), optional.get().getName());
        Assertions.assertEquals(course.getCampus(), optional.get().getCampus());
        Assertions.assertEquals(course.getUniversity(), optional.get().getUniversity());
    }

    @Test
    void findByCampusContaining() {
    }

    @Test
    void findByUniversityContaining() {
    }

    @Test
    void findByNameContaining() {
    }
}