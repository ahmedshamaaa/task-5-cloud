package com.bezkoder.spring.hibernate.manytomany.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.bezkoder.spring.hibernate.manytomany.repository.StudentRepository;

@Entity
@Table(name = "students")
public class Student {
	
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name = "name")
  private String name;
  
  @Column(name = "password")
  private String password;
  

  public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

@ManyToMany(fetch = FetchType.LAZY,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
      })
  @JoinTable(name = "student_courses",
        joinColumns = { @JoinColumn(name = "student_id") },
        inverseJoinColumns = { @JoinColumn(name = "course_id") })
  private List<Course> courses = new ArrayList<Course>();
  
  public Student() {

  }

  public Student(String name,String password) {
	  this.password=password;
    this.name = name;
    
  }
  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Course> getCourses() {
    return courses;
  }

  public void setCourses(List<Course> courses) {
    this.courses = courses;
  }
  
  public void addCourse(Course course) {
    this.courses.add(course);
    course.getStudents().add(this);
  }
  
  public void removeCourse(int courseId) {
    Course course = this.courses.stream().filter(t -> t.getId() == courseId).findFirst().orElse(null);
    if (course != null) {
      this.courses.remove(course);
      course.getStudents().remove(this);
    }
  }
  
  @Override
  public String toString() {
    return "Student [id=" + id + ", name=" + name + "]";
  }

}

//
//////List<Student> students;
//////StudentRepository studentRepository;
//////  
//////try {
//////	students = studentRepository.findAll();
//////} catch (Exception e) {
//////	// TODO Auto-generated catch block
//////	throw new IllegalStateException("error in addstudent");
//////}
//////  for (int i=0;i<students.size();i++)
//////  {
//////      if (students.get(i).getName().equals(student.getName()))
//////      {
//////    	  this.students.remove(student);
//////      }
//////  }
////
