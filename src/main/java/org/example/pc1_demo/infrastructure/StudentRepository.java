package org.example.pc1_demo.infrastructure;

//implementar los imports y paquete correspondiente
import org.example.pc1_demo.domain.Course;
import org.example.pc1_demo.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCoursesContaining(Course course);
}
