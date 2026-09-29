package com.seatplan.config;

import com.seatplan.dto.ExamRequest;
import com.seatplan.dto.HallRequest;
import com.seatplan.dto.StudentRequest;
import com.seatplan.entity.Student;
import com.seatplan.repository.ExamSessionRepository;
import com.seatplan.repository.HallRepository;
import com.seatplan.repository.StudentRepository;
import com.seatplan.service.ExamSessionService;
import com.seatplan.service.HallService;
import com.seatplan.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final HallService hallService;
    private final StudentService studentService;
    private final ExamSessionService examSessionService;
    private final HallRepository hallRepository;
    private final StudentRepository studentRepository;
    private final ExamSessionRepository examSessionRepository;

    public DataInitializer(HallService hallService,
                           StudentService studentService,
                           ExamSessionService examSessionService,
                           HallRepository hallRepository,
                           StudentRepository studentRepository,
                           ExamSessionRepository examSessionRepository) {
        this.hallService = hallService;
        this.studentService = studentService;
        this.examSessionService = examSessionService;
        this.hallRepository = hallRepository;
        this.studentRepository = studentRepository;
        this.examSessionRepository = examSessionRepository;
    }

    @Override
    public void run(String... args) {
        if (hallRepository.count() == 0) {
            log.info("Populating sample examination halls...");
            hallService.createHall(new HallRequest("H101", "Main Examination Hall", 5, 6, 30));
            hallService.createHall(new HallRequest("H102", "Science Block Hall 1", 4, 5, 20));
            hallService.createHall(new HallRequest("H103", "Engineering Block Hall 2", 5, 5, 25));
        }

        if (studentRepository.count() == 0) {
            log.info("Populating sample students...");
            List<StudentRequest> sampleStudents = List.of(
                    // CSE-A
                    new StudentRequest("23CS101", "Arun Kumar", "CSE", "A"),
                    new StudentRequest("23CS102", "Bala Krishna", "CSE", "A"),
                    new StudentRequest("23CS103", "Chitra Devi", "CSE", "A"),
                    new StudentRequest("23CS104", "Deepak Raj", "CSE", "A"),
                    new StudentRequest("23CS105", "Ezhil Kumar", "CSE", "A"),

                    // CSE-B
                    new StudentRequest("23CS201", "Farooq Ahmed", "CSE", "B"),
                    new StudentRequest("23CS202", "Gowtham V", "CSE", "B"),
                    new StudentRequest("23CS203", "Hari Prasad", "CSE", "B"),
                    new StudentRequest("23CS204", "Indira Priyadarshini", "CSE", "B"),
                    new StudentRequest("23CS205", "Jaya Surya", "CSE", "B"),

                    // ECE-A
                    new StudentRequest("23EC101", "Karthik Raja", "ECE", "A"),
                    new StudentRequest("23EC102", "Latha M", "ECE", "A"),
                    new StudentRequest("23EC103", "Manish Kumar", "ECE", "A"),
                    new StudentRequest("23EC104", "Naveen Kumar", "ECE", "A"),
                    new StudentRequest("23EC105", "Om Prakash", "ECE", "A"),

                    // ECE-B
                    new StudentRequest("23EC201", "Pooja Sharma", "ECE", "B"),
                    new StudentRequest("23EC202", "Qadir Khan", "ECE", "B"),
                    new StudentRequest("23EC203", "Rahul Ramesh", "ECE", "B"),
                    new StudentRequest("23EC204", "Sangeetha K", "ECE", "B"),
                    new StudentRequest("23EC205", "Tarun V", "ECE", "B"),

                    // EEE-A
                    new StudentRequest("23EE101", "Uma Shankar", "EEE", "A"),
                    new StudentRequest("23EE102", "Vijay Kumar", "EEE", "A"),
                    new StudentRequest("23EE103", "Wasim Akram", "EEE", "A"),
                    new StudentRequest("23EE104", "Xavier S", "EEE", "A"),
                    new StudentRequest("23EE105", "Yogesh R", "EEE", "A")
            );

            for (StudentRequest req : sampleStudents) {
                studentService.createStudent(req);
            }
        }

        if (examSessionRepository.count() == 0) {
            log.info("Populating sample exam session and registrations...");
            ExamRequest examRequest = ExamRequest.builder()
                    .examName("Semester Examination")
                    .subject("Database Management Systems")
                    .examDate(LocalDate.of(2026, 10, 15))
                    .startTime(LocalTime.of(9, 30))
                    .endTime(LocalTime.of(12, 30))
                    .build();

            var examResponse = examSessionService.createExam(examRequest);

            List<Student> allStudents = studentRepository.findAll();
            List<Long> studentIds = allStudents.stream().map(Student::getId).toList();

            examSessionService.registerStudentsForExam(examResponse.getId(), studentIds);
            log.info("Registered {} students for sample exam ID: {}", studentIds.size(), examResponse.getId());
        }
    }
}
