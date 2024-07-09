package org.example.pc1_demo.infrastructure;

//implementar los imports y paquete correspondiente
import org.example.pc1_demo.domain.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {}