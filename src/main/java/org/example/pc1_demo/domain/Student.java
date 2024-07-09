package org.example.pc1_demo.domain;

//colocar los imports y paquete correspondiente
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Course> courses = new HashSet<>();

    //Crea un constructor vacío y uno con todos los atributos menos el id
    public Student() {
    }

    public Student(String name, Set<Course> courses) {
        this.name = name;
        this.courses = courses;
    }

    //Crea los getters y setters necesarios
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    //Funcion la cual agrega un curso al estudiante
    public void enrollInCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }

}
