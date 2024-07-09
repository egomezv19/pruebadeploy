package org.example.pc1_demo.application;

//implementar los imports y paquete correspondiente

import org.example.pc1_demo.application.dto.CourseDTO;
import org.example.pc1_demo.application.dto.StudentDTO;
import org.example.pc1_demo.application.exception.CourseNotFoundException;
import org.example.pc1_demo.application.exception.StudentNotFoundException;
import org.example.pc1_demo.domain.Student;
import org.example.pc1_demo.domain.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/students")
public class StudentController {

    //Implementar declaración de Servicio
    @Autowired
    private StudentService studentService;

    //Implementar metodo POST para crear un estudiante
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        return new ResponseEntity<>(studentService.saveStudent(student), HttpStatus.CREATED);
    }

    // GET: Recupera un estudiante por id
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.findStudentById(id);
        if (student.isEmpty()) {
            throw new StudentNotFoundException("Student not found");
        }
        StudentDTO studentDTO = new StudentDTO(student.get().getName());
        return new ResponseEntity<>(studentDTO, HttpStatus.OK);
    }

    //Implementar metodo GET, PATCH, PUT y DELETE
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentService.findAllStudents(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return new ResponseEntity<>(studentService.updateStudent(id, updatedStudent), HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> partialUpdateStudent(@PathVariable Long id, @RequestBody Student updatedFields) {
        return new ResponseEntity<>(studentService.partialUpdateStudent(id, updatedFields), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}