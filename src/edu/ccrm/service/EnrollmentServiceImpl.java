package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.domain.exceptions.*;
import edu.ccrm.config.AppConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of EnrollmentService
 */
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final Map<String, Enrollment> enrollments = new ConcurrentHashMap<>();
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final AppConfig config = AppConfig.getInstance();
    
    @Override
    public void enrollStudent(String studentId, String courseCode) 
            throws DuplicateEnrollmentException, MaxCreditLimitExceededException, EntityNotFoundException {
        
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        // Check if student exists and is eligible
        Student student = studentService.getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        if (!student.isEligibleForEnrollment()) {
            throw new EntityNotFoundException("Student not eligible for enrollment", "Student", studentId);
        }
        
        // Check if course exists
        Course course = courseService.getCourseByCode(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        String enrollmentKey = studentId + "_" + courseCode;
        
        // Check for duplicate enrollment
        if (enrollments.containsKey(enrollmentKey)) {
            throw new DuplicateEnrollmentException(studentId, courseCode);
        }
        
        // Check credit limit
        int currentCredits = getStudentCreditCount(studentId);
        if (currentCredits + course.getCredits() > config.getMaxCreditsPerSemester()) {
            throw new MaxCreditLimitExceededException(studentId, currentCredits, 
                course.getCredits(), config.getMaxCreditsPerSemester());
        }
        
        // Create enrollment
        Enrollment enrollment = new Enrollment(studentId, courseCode);
        enrollments.put(enrollmentKey, enrollment);
        
        // Update student's enrolled courses
        student.addEnrolledCourse(courseCode);
        student.setTotalCredits(currentCredits + course.getCredits());
        
        try {
            studentService.updateStudent(student);
        } catch (EntityNotFoundException e) {
            // Rollback enrollment if student update fails
            enrollments.remove(enrollmentKey);
            throw e;
        }
    }
    
    @Override
    public void unenrollStudent(String studentId, String courseCode) throws EntityNotFoundException {
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        String enrollmentKey = studentId + "_" + courseCode;
        
        if (!enrollments.containsKey(enrollmentKey)) {
            throw new EntityNotFoundException("Enrollment not found for student " + studentId + 
                " in course " + courseCode, "Enrollment", enrollmentKey);
        }
        
        // Remove enrollment
        enrollments.remove(enrollmentKey);
        
        // Update student
        Student student = studentService.getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        Course course = courseService.getCourseByCode(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        student.removeEnrolledCourse(courseCode);
        student.setTotalCredits(student.getTotalCredits() - course.getCredits());
        
        studentService.updateStudent(student);
    }
    
    @Override
    public List<Enrollment> getStudentEnrollments(String studentId) throws EntityNotFoundException {
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        
        // Verify student exists
        studentService.getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        return enrollments.values().stream()
            .filter(enrollment -> enrollment.getStudentId().equals(studentId))
            .sorted((e1, e2) -> e1.getCourseCode().compareToIgnoreCase(e2.getCourseCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Enrollment> getCourseEnrollments(String courseCode) throws EntityNotFoundException {
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        // Verify course exists
        courseService.getCourseByCode(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        return enrollments.values().stream()
            .filter(enrollment -> enrollment.getCourseCode().equals(courseCode))
            .sorted((e1, e2) -> e1.getStudentId().compareToIgnoreCase(e2.getStudentId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollments.values());
    }
    
    @Override
    public boolean isStudentEnrolled(String studentId, String courseCode) {
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        String enrollmentKey = studentId + "_" + courseCode;
        return enrollments.containsKey(enrollmentKey);
    }
    
    @Override
    public int getStudentCreditCount(String studentId) {
        try {
            List<Enrollment> studentEnrollments = getStudentEnrollments(studentId);
            
            return studentEnrollments.stream()
                .mapToInt(enrollment -> {
                    try {
                        Optional<Course> course = courseService.getCourseByCode(enrollment.getCourseCode());
                        return course.map(Course::getCredits).orElse(0);
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .sum();
        } catch (EntityNotFoundException e) {
            return 0;
        }
    }
    
    @Override
    public void updateEnrollmentStatus(String studentId, String courseCode, 
                                     Enrollment.EnrollmentStatus status) throws EntityNotFoundException {
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        Objects.requireNonNull(status, "Status cannot be null");
        
        String enrollmentKey = studentId + "_" + courseCode;
        
        Enrollment enrollment = enrollments.get(enrollmentKey);
        if (enrollment == null) {
            throw new EntityNotFoundException("Enrollment not found for student " + studentId + 
                " in course " + courseCode, "Enrollment", enrollmentKey);
        }
        
        enrollment.setStatus(status);
        enrollments.put(enrollmentKey, enrollment);
    }
    
    /**
     * Get enrollment statistics
     */
    public Map<String, Object> getEnrollmentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Enrollment> allEnrollments = getAllEnrollments();
        stats.put("totalEnrollments", allEnrollments.size());
        
        // Group by status
        Map<Enrollment.EnrollmentStatus, Long> statusCounts = allEnrollments.stream()
            .collect(Collectors.groupingBy(
                Enrollment::getStatus,
                Collectors.counting()
            ));
        stats.put("enrollmentsByStatus", statusCounts);
        
        // Most popular courses
        Map<String, Long> courseCounts = allEnrollments.stream()
            .collect(Collectors.groupingBy(
                Enrollment::getCourseCode,
                Collectors.counting()
            ));
        stats.put("enrollmentsByCourse", courseCounts);
        
        return stats;
    }
}