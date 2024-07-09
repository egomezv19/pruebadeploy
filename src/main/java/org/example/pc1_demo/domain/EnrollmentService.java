package org.example.pc1_demo.domain;

//implementar los imports y paquete correspondiente
import org.example.pc1_demo.infrastructure.CourseRepository;
import org.example.pc1_demo.infrastructure.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnrollmentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    //implementar metodo enrollStudentInCourse
    public void enrollStudentInCourse(Long studentId, Long courseId){
        Course course = courseRepository.findById(courseId).get();
        Student student = studentRepository.findById(studentId).get();

        student.enrollInCourse(course);
    }

}