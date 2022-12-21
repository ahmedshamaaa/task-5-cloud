package com.bezkoder.spring.hibernate.manytomany.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.bezkoder.spring.hibernate.manytomany.model.Course;
import com.bezkoder.spring.hibernate.manytomany.model.Student;
import com.bezkoder.spring.hibernate.manytomany.repository.CourseRepository;
import com.bezkoder.spring.hibernate.manytomany.repository.StudentRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class CourseController {

  @Autowired
  private StudentRepository studentRepository;

  @Autowired
  private CourseRepository CourseRepository;

  @GetMapping("/course")
  public List<Course> getAllCourses() {
    List<Course> courses = new ArrayList<Course>();

    CourseRepository.findAll().forEach(courses::add);

    if (courses.isEmpty()) {
    	throw new  IllegalStateException(" NO CONTENT ");
    }
    return courses;
  }
  
  @GetMapping("/students/{studentId}/courses")
  public List<Course> getAllCoursesByStudentId(@PathVariable(value = "studentId") int studentId) {
    if (!studentRepository.existsById(studentId)) {
      throw new IllegalStateException("Not found Student with id = " + studentId);
    }
    List<Course> courses = CourseRepository.findCoursesByStudentsId(studentId);
    return courses;
  }

  @GetMapping("/courses/{id}")
  public Course getCoursesById(@PathVariable(value = "id") int id) {
    Course course = CourseRepository.findById(id)
    		.orElseThrow(() -> new IllegalStateException("Not found Course with id = " + id));
    return course ;
  }
  
  @GetMapping("/courses/{courseId}/students")
  public List<Student> getAllStudentsByCourseId(@PathVariable(value = "courseId") int courseId) {
    if (!CourseRepository.existsById(courseId)) {
      throw new IllegalStateException("Not found Course  with id = " + courseId);
    }
    List<Student> students = studentRepository.findStudentsByCoursesId(courseId);
    return  students;
  }

  @PostMapping("/students/{studentId}/courses")
  public String addCourse(@PathVariable(value = "studentId") int studentId, @RequestBody Course courseRequest) {
	 Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Not found Student with id = " + studentId));
      int courseId = courseRequest.getId();
        
      //  existed course 
      if (courseId != 0L) {
        Course _course = CourseRepository.findById(courseId)
            .orElseThrow(() -> new IllegalStateException("Not found Course with id = " + courseId));
        student.addCourse(_course);
        studentRepository.save(student);
        return "added";
      }
      
      // create new Course and add  
      student.addCourse(courseRequest);
      CourseRepository.save(courseRequest);
    

    return "created and added";
  }

  @PutMapping("/course")
  public String updateCourse(@RequestBody Course courseRequest) {
    Course course = CourseRepository.findById(courseRequest.getId())
        .orElseThrow(() -> new IllegalStateException("CourseId " + courseRequest.getId() + "not found"));

    course.setName(courseRequest.getName());

    CourseRepository.save(course);
    return "saved";
  }
 
  @DeleteMapping("/students/{studentId}/courses/{courseId}")
  public String deleteCourseFromStudent(@PathVariable(value = "studentId") int studentId, @PathVariable(value = "courseId") int courseId) {
    Student student = studentRepository.findById(studentId)
        .orElseThrow(() -> new IllegalStateException("Not found Student with id = " + studentId));
    
    student.removeCourse(courseId);
    studentRepository.save(student);
    
    return "deleted ";
  }
  
  @DeleteMapping("/course/{id}")
  public void deleteCourse(@PathVariable("id") int id) {
    boolean isExisted = CourseRepository.existsById(id);

    if (!isExisted)
    {
      throw new IllegalStateException("student with id " + id +" is not existed");
    }

    CourseRepository.deleteById(id);
  }

  @PostMapping("/course")
  public void addCourse( @RequestBody Course courseRequest) {

    Optional<Course> courseByName = CourseRepository.findCourseByName(courseRequest.getName());

    if (courseByName.isPresent())
    {
      throw new IllegalStateException("Name Course taken");
    }

    CourseRepository.save(courseRequest);

  }

}
