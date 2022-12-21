package com.bezkoder.spring.hibernate.manytomany.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bezkoder.spring.hibernate.manytomany.model.Course;
import com.bezkoder.spring.hibernate.manytomany.model.Student;
import com.bezkoder.spring.hibernate.manytomany.repository.CourseRepository;
import com.bezkoder.spring.hibernate.manytomany.repository.StudentRepository;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class StudentController {

  @Autowired
  StudentRepository studentRepository;

  
  
  public ResponseEntity login(String userName , String password)
  {
      boolean flag=false;
      List<Student> students;
	try {
		students = studentRepository.findAll();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		throw new IllegalStateException("error in login");
	}
      for (int i=0;i<students.size();i++)
      {
          if (students.get(i).getName().equals(userName)  && students.get(i).getPassword().equals(password))
          {
              flag=true;

          }
      }

      if (flag == true)
      {
          return ResponseEntity.ok().build();
      }

      else
      {
          return (ResponseEntity) ResponseEntity.internalServerError();
      }


  }
  
  @PostMapping(path = "/login")
	public ResponseEntity adminLogin(@RequestBody Student student)
	{
		return login(student.getName(), student.getPassword());
	}
  
  
  
  
  
  @GetMapping("/students")
  public  List<Student> getAllStudents(@RequestParam(required = false) String name) {
    List<Student> students = new ArrayList<Student>();

    if (name == null)
      studentRepository.findAll().forEach(students::add);
    else
      studentRepository.findByNameContaining(name).forEach(students::add);

    if (students.isEmpty()) {
      throw new IllegalThreadStateException("NO_CONTENT");
    }
    return students ;
  }

  @GetMapping("/students/{id}")
  public Student getStudentById(@PathVariable("id") int id) {
    Student student = studentRepository.findById(id)
        .orElseThrow(() -> new IllegalStateException("Not found Student with id = " + id));

    return student ;
  }

  @PostMapping("/students")
  public String createStudent(@RequestBody Student student) {
     studentRepository.save(new Student(student.getName(),student.getPassword()));
    return"register";
  }

  @PutMapping("/students")
  public String updateStudent(@RequestBody Student student) {
    Student _student = studentRepository.findById(student.getId())
        .orElseThrow(() -> new IllegalStateException("Not found Student with id = " + student.getId()));

    _student.setName(student.getName());
    
    
    studentRepository.save(_student);
    return "updated";
  }

  @DeleteMapping("/students/{id}")
  public String deleteStudent(@PathVariable("id") int id) {
    studentRepository.deleteById(id);
    
    return "deleted";
  }
  
  

  @DeleteMapping("/students")
  public String deleteAllStudents() {
    studentRepository.deleteAll();
    
    return "delete all completed";
  }
  
  @Autowired
  private CourseRepository courseRepository;
  
  @PostMapping("/courses/{courseId}/students")
  public String addCourse(@PathVariable(value = "courseId") int courseId, @RequestBody Student studentRequest) {
	  Course course = courseRepository.findById(courseId).orElseThrow(() -> new IllegalStateException("Not found Student with id = " + courseId));
      int studentId = studentRequest.getId();
        
      //  existed course 
      if (studentId != 0L) {
    	  Student _student = studentRepository.findById(studentId)
            .orElseThrow(() -> new IllegalStateException("Not found Course with id = " + studentId));
        course.addStudent(_student);
        courseRepository.save(course);
        return "added";
      }
      
      // create new Course and add  
      course.addStudent(studentRequest);
      studentRepository.save(studentRequest);
    

    return "created and added";
  }


}
