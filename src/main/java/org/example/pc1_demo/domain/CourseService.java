package org.example.pc1_demo.domain;

//implementar los imports y paquete correspondiente
import org.example.pc1_demo.infrastructure.CourseRepository;
import org.example.pc1_demo.infrastructure.StudentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> findCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(Long id, Course course) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isPresent()) {
            course.setId(id);
            return courseRepository.save(course);
        }
        return null;
    }

    public Course partialUpdateCourse(Long id, Course course) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isPresent()) {
            BeanUtils.copyProperties(course, existingCourse.get(), "id");
            return courseRepository.save(existingCourse.get());
        }
        return null;
    }

    public boolean hasStudents(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course course = courseOptional.get();
            return !course.getStudents().isEmpty();
        }
        return false;
    }

    //Implementar deleteCourseById
    public void deleteCourseById(Long id) {
        Optional<Course> courseOptional = courseRepository.findById(id);
        if (courseOptional.isPresent()) {
            Course courseUpdate = courseOptional.get();
            Set<Student> students = courseUpdate.getStudents();
            for (Student student : students) {
                student.getCourses().remove(courseUpdate);
                studentRepository.save(student);
            }
            courseRepository.deleteById(id);
        } 
    }
}