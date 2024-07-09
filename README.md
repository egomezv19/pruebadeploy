# PC1

## Indicaciones :page_facing_up:

- La evaluación se realizará de manera individual.
- La duración de la prueba será de 120 minutos.
- Son libres de utilizar cualquier material de consulta excepto AI (Ej. Bert, ChatGPT, Github Copilot, etc.).
- No pueden comunicarse con algún compañero o persona ajena a la prueba (por ningun medio).


## Objetivos :dart:

- Crear un API utilizando el framework **[Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/)** de Java.
- Implementar las operaciones básicas de un CRUD (crear, listar, actualizar y eliminar).
- Agregar una capa de presistencia utilizando PostgreSQL.
- Implementar 1 DTO (Data Transfer Object) para transferir datos.
- Implementar 1 caso de *error handling* para el manejo de excepciones.

## Tools (extenciones vs code) :wrench:

* Utilizar [Thunder Client](https://www.thunderclient.com/) para realizar los requests HTTP.
* Utilizar [Spring Initializr Java Support](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-spring-initializr) para realizar la configuración inicial del proyecto.

## Requisitos Previos :memo:

* Tener instalado y configurado **Java** (v.17 LTS) Development Kit (JDK) instalado en tu sistema. Recomendamos utilizar la distribución de Amazon (*JDK Corretto*): [Linux](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/generic-linux-install.html) | [macOS](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/macos-install.html) | [Windows](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/windows-7-install.html)
* Tener instalado [Docker](https://www.docker.com/products/docker-desktop/) desktop o PostgreSQL directamente en la computadora.
* Tener instalado y configurado **Visual Studio Code**.

## Configuración de archivos

```
DEMO
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── demo  
│   │   │               ├── DemoApplication.java  
│   │   │               ├── application
│   │   │               │   ├── CourseController.java
│   │   │               │   ├── StudentController.java
│   │   │               │   ├── EnrollmentController.java
│   │   │               │   ├── exception
│   │   │               │   │   ├── CourseNotFoundException.java
│   │   │               │   │   └── <tu excepción>.java
│   │   │               │   └── dto
│   │   │               │       ├── CourseDTO.java
│   │   │               │       └── <tu DTO>.java
│   │   │               ├── domain
│   │   │               │   ├── Course.java
│   │   │               │   ├── CourseService.java
│   │   │               │   ├── EnrollmentService.java
│   │   │               │   ├── Student.java
│   │   │               │   └── StudentService.java
│   │   │               └── infrastructure
│   │   │                   ├── CourseRepository.java
│   │   │                   └── StudentRepository.java
│   │   └── resources
│   │       └── application.properties 
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── demo
│                       └── DemoApplicationTests.java  
├── .gitignore 
├── pom.xml  
└── mvnw  
```

## Tareas :mega:

### Tarea 01: Implementación de clases (4pts)

* Implementaremos una API para estudiantes y cursos. Tendremos 2 recursos: Course y Student.

* Asegurate de agregar las siguientes dependencias: `Spring Web`, `Spring Data JPA`, `PostgreSQL Driver`, `jackson-databind` y `h2database`.

#### [Course]

| Atributo  | Tipo de dato           | Descripción                                                  |
|-----------|------------------------|--------------------------------------------------------------|
| id        | Long                   | Identificador único generado automáticamente para el curso. |
| title     | String                 | Título del curso.                                            |
| students  | Set<Student>           | Conjunto de estudiantes asociados con este curso.           |

```java
//colocar los imports y paquete correspondiente
TODO

@Entity
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String title;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "courses")
    private Set<Student> students = new HashSet<>();

    public Course() {
    }

    public Course(String title, Set<Student> students) {
        this.title = title;
        this.students = students;
    }

    //Crea los getters y setters necesarios
    TODO
}
```


#### [Student]

| Atributo   | Tipo de dato  | Descripción                                                  |
|------------|---------------|--------------------------------------------------------------|
| id         | Long          | Identificador único generado automáticamente para el estudiante. |
| name       | String        | Nombre del estudiante.                                       |
| courses    | Set<Course>   | Conjunto de cursos en los que está inscrito el estudiante.   |

```java
//colocar los imports y paquete correspondiente
TODO

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
    @JoinTable(name = "student_courses",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")})
    private Set<Course> courses = new HashSet<>();

    //Crea un constructor vacío y uno con todos los atributos menos el id
    TODO

    //Crea los getters y setters necesarios
    TODO

    //Funcion la cual agrega un curso al estudiante
    public void enrollInCourse(Course course) {
        courses.add(course);
        course.getStudents().add(this);
    }
    
}

```


  
### Tarea 02: Implementación de controladores (4pts)

#### [CourseController]

```java
//implementar los imports y paquete correspondiente
TODO

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
        return new ResponseEntity<>(course.get(), HttpStatus.OK);
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
```

#### [StudentController]

```java
//implementar los imports y paquete correspondiente
TODO

@RestController
@RequestMapping("/students")
public class StudentController {

    //Implementar declaración de Servicio 
    TODO

    //Implementar metodo POST para crear un estudiante
    TODO

    // GET: Recupera un estudiante por id
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        Optional<Student> student = studentService.findStudentById(id);
        return new ResponseEntity<>(student.get(), HttpStatus.OK);
    }

    //Implementar metodo GET, PATCH, PUT y DELETE
    TODO

}
```

### [EnrollmentController]

Enrollment se encarga de inscribir un estudiante en un curso.

```java
//implementar los imports y paquete correspondiente
TODO

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping
    public ResponseEntity<String> enrollStudentInCourse(@RequestParam Long studentId, @RequestParam Long courseId) {
        try {
            enrollmentService.enrollStudentInCourse(studentId, courseId);
            return ResponseEntity.ok("Student successfully enrolled in course.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

```


### Tarea 03: Implementación de servicios (4pts)

#### [CourseService]

```java
//implementar los imports y paquete correspondiente
TODO

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
    TODO

}
```

#### [StudentService]

```java
//implementar los imports y paquete correspondiente
TODO

@Service
public class StudentService {

    //Implementar declaración de Repositorio
    TODO
    
    //Implementar metodo saveStudent
    TODO

    //Implementar metodo findStudentById
    TODO

    //Implementar metodo findAllStudents
    TODO

    //Implementar metodo updateStudent
    TODO

    //Implementar metodo partialUpdateStudent
    TODO

    //Implementar metodo isEnrolledInAnyCourse
    TODO

    public void deleteStudentById(Long id) {
        studentRepository.deleteById(id);
    }

}
```

`Verifica que puedas eliminar un estudiante que está dentro de un curso. De la misma manera, verifica que puedas eliminar un curso que tiene estudiantes.`

#### [EnrollmentService]

Puedes utilizar el método `enrollInCourse` de la clase `Student` para inscribir un estudiante en un curso.

```java
//implementar los imports y paquete correspondiente
TODO

@Service
public class EnrollmentService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    //implementar metodo enrollStudentInCourse
    TODO

}
```

### Tarea 04: Implementación de repositorios (2pts)

#### [CourseRepository]

```java
//implementar los imports y paquete correspondiente
TODO

public interface CourseRepository extends JpaRepository<Course, Long> {}
```

#### [StudentRepository]

```java
//implementar los imports y paquete correspondiente
TODO

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCoursesContaining(Course course);
}
```

### Tarea 05: Implementación de DTOs (2pts)

#### [CourseDTO]

```java
//implementar el paquete correspondiente
TODO

public class CourseDTO {
    private String title;

    public CourseDTO() {
    }

    public CourseDTO(String title) {
        this.title = title;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
```

En el archivo [CourseController] modifica el método `course` para que retorne un `ResponseEntity<CourseDTO>`.

```java
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> course(@PathVariable Long id) {
        Optional<Course> course = courseService.findCourseById(id);
        CourseDTO courseDTO = new CourseDTO(course.get().getTitle());
        return ResponseEntity.status(200).body(courseDTO);
    }
```

Ahora implementa tu propio DTO!

### Tarea 06: Implementación de excepciones (2pts)

#### [CourseNotFoundException]

```java
//implementar los imports y paquete correspondiente
TODO

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Course not found")
public class CourseNotFoundException extends RuntimeException {
    
    public CourseNotFoundException(String message) {
        super(message);
    }
    
}
```

En el archivo [CourseController] modifica el método `course` para aplicar la excepción.

```java
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> course(@PathVariable Long id) {
        Optional<Course> course = courseService.findCourseById(id);
        if (course.isEmpty()) {
            throw new CourseNotFoundException("Course not found");
        }
        CourseDTO courseDTO = new CourseDTO(course.get().getTitle());
        return ResponseEntity.status(200).body(courseDTO);
    }
```

Ahora implementa tu propia excepción!

### Tarea 07: Implementación de capa de persistencia (2pts)
> Para esta sección, si tiene PostgreSQL en su computadora y deseas utilizarla sin Docker, no hay ningún inconveniente.
#### [application.properties]

Este es un ejemplo de cómo debería verse el archivo `application.properties` para la configuración con la base de datos.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=mypassword
spring.jpa.hibernate.ddl-auto=update
```

`Asegurece de que el contenedor de Docker esté corriendo o que el servidor de la aplicación de PostgreSQL se está ejecutando en su computador. Adicionalmente, verifique que la base de datos esté creada.`

```bash
docker run --name postgres -e POSTGRES_PASSWORD=mypassword -p 5432:5432 -d postgres
```

### Fin

Si llegaste hasta aquí, ¡felicidades! :tada:

Asegúrate de que tu proyecto funcione correctamente y que no tengas errores de compilación. Recuerda subir a Canvas un documento PDF con los puntos señalados en la entrega.
