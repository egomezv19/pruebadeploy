package org.example.pc1_demo.domain;

//implementar los imports y paquete correspondiente
import org.example.pc1_demo.infrastructure.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    //Implementar declaración de Repositorio
    @Autowired
    StudentRepository studentRepository;


    //Implementar metodo saveStudent
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    //Implementar metodo findStudentById
    public Optional<Student> findStudentById(Long id) {
        return studentRepository.findById(id);
    }

    //Implementar metodo findAllStudents
    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    //Implementar metodo updateStudent
    public Student updateStudent(Long id, Student student) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            student.setId(id);
            return studentRepository.save(student);
        }
        return null;
    }

    //Implementar metodo partialUpdateStudent
    public Student partialUpdateStudent(Long id, Student student) {
        Optional<Student> existingStudent = studentRepository.findById(id);
        if (existingStudent.isPresent()) {
            BeanUtils.copyProperties(student, existingStudent.get(), "id");
            return studentRepository.save(existingStudent.get());
        }
        return null;
    }

    //Implementar metodo isEnrolledInAnyCourse
    public boolean isEnrolledInAnyCourse(Long id) {
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            return !student.getCourses().isEmpty();
        }
        return false;
    }

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

}