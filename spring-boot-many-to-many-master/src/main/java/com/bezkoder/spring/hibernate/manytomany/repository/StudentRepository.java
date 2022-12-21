package com.bezkoder.spring.hibernate.manytomany.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bezkoder.spring.hibernate.manytomany.model.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
  

    @Query("SELECT a FROM Student a WHERE a.name=?1")
    Optional<Student> findAdminByName(String name); 
    
  List<Student> findByNameContaining(String name);
  
  List<Student> findStudentsByCoursesId(int courseId);
}
