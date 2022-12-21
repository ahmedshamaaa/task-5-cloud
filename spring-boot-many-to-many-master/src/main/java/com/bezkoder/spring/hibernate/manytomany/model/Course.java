package com.bezkoder.spring.hibernate.manytomany.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "courses")
public class Course {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  public Course(String name) {
    this.name = name;
  }

  @Column(name = "name")
  private String name;

  @ManyToMany(fetch = FetchType.LAZY,
      cascade = {
          CascadeType.PERSIST,
          CascadeType.MERGE
      },
      mappedBy = "courses")
  @JsonIgnore
  private List<Student> students = new ArrayList<Student>();

  public Course() {

  }
  
  public int getId() {
    return id;
  }

  public void addStudent(Student student) {
	  
	    this.students.add(student);
	    student.getCourses().add(this);
	  }
	  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Student> getStudents() {
    return students;
  }

  public void setStudents(List<Student> students) {
    this.students = students;
  }  
  
}
