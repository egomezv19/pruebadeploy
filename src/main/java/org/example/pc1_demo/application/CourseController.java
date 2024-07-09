package org.example.pc1_demo.application;

import org.example.pc1_demo.application.dto.CourseDTO;
import org.example.pc1_demo.application.exception.CourseNotFoundException;
import org.example.pc1_demo.domain.Course;
import org.example.pc1_demo.domain.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//implementar los imports y paquete correspondiente


@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // POST: Crea un nuevo curso
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return new ResponseEntity<>(courseService.saveCourse(course), HttpStatus.CREATED);
    }

    // GET: Recupera un curso por id
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> course(@PathVariable Long id) {
        Optional<Course> course = courseService.findCourseById(id);
        if (course.isEmpty()) {
            throw new CourseNotFoundException("Course not found");
        }
        CourseDTO courseDTO = new CourseDTO(course.get().getTitle());
        return ResponseEntity.status(200).body(courseDTO);
    }

    // GET: Recupera todos los cursos
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        return new ResponseEntity<>(courseService.findAllCourses(), HttpStatus.OK);
    }

    // PUT: Actualiza un curso por id
    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        return new ResponseEntity<>(courseService.updateCourse(id, updatedCourse), HttpStatus.OK);
    }

    // PATCH: Actualiza parcialmente un curso por id
    @PatchMapping("/{id}")
    public ResponseEntity<Course> partialUpdateCourse(@PathVariable Long id, @RequestBody Course updatedFields) {
        return new ResponseEntity<>(courseService.partialUpdateCourse(id, updatedFields), HttpStatus.OK);
    }

    // DELETE: Borra un curso por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourseById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
