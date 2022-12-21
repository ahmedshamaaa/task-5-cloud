package com.bezkoder.spring.hibernate.manytomany.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.hibernate.manytomany.model.Course;
import org.springframework.data.jpa.repository.Query;

public interface CourseRepository extends JpaRepository<Course, Integer> {
  List<Course> findCoursesByStudentsId(Integer studentId);

  @Query("SELECT s FROM Course s WHERE s.name=?1")
  Optional<Course> findCourseByName(String courseName);
}
