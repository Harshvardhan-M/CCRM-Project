package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.domain.exceptions.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of GradeService
 */
public class GradeServiceImpl implements GradeService {
    
    private final Map<String, Grade> grades = new ConcurrentHashMap<>();
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final EnrollmentService enrollmentService = new EnrollmentServiceImpl();
    
    @Override
    public Grade recordGrade(String studentId, String courseCode, double marks) 
            throws InvalidGradeException, EntityNotFoundException {
        
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        // Validate marks
        if (marks < 0 || marks > 100) {
            throw new InvalidGradeException("Marks must be between 0 and 100", 
                studentId, courseCode, marks);
        }
        
        // Verify student exists
        studentService.getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        // Verify course exists
        courseService.getCourseByCode(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        // Verify student is enrolled in course
        if (!enrollmentService.isStudentEnrolled(studentId, courseCode)) {
            throw new EntityNotFoundException("Student " + studentId + 
                " is not enrolled in course " + courseCode, "Enrollment", 
                studentId + "_" + courseCode);
        }
        
        String gradeKey = studentId + "_" + courseCode;
        
        // Check if grade already exists
        if (grades.containsKey(gradeKey)) {
            throw new InvalidGradeException("Grade already exists for student " + 
                studentId + " in course " + courseCode);
        }
        
        Grade grade = new Grade(studentId, courseCode, marks);
        grades.put(gradeKey, grade);
        
        // Update student GPA
        try {
            updateStudentGPA(studentId);
        } catch (Exception e) {
            // Log but don't fail grade recording
            System.err.println("Warning: Could not update GPA for student " + studentId + ": " + e.getMessage());
        }
        
        return grade;
    }
    
    @Override
    public Grade updateGrade(String studentId, String courseCode, double newMarks)
            throws InvalidGradeException, EntityNotFoundException {
        
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        // Validate marks
        if (newMarks < 0 || newMarks > 100) {
            throw new InvalidGradeException("Marks must be between 0 and 100", 
                studentId, courseCode, newMarks);
        }
        
        String gradeKey = studentId + "_" + courseCode;
        
        Grade existingGrade = grades.get(gradeKey);
        if (existingGrade == null) {
            throw new EntityNotFoundException("Grade not found for student " + 
                studentId + " in course " + courseCode, "Grade", gradeKey);
        }
        
        existingGrade.setMarks(newMarks);
        grades.put(gradeKey, existingGrade);
        
        // Update student GPA
        try {
            updateStudentGPA(studentId);
        } catch (Exception e) {
            System.err.println("Warning: Could not update GPA for student " + studentId + ": " + e.getMessage());
        }
        
        return existingGrade;
    }
    
    @Override
    public List<Grade> getStudentGrades(String studentId) throws EntityNotFoundException {
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        
        // Verify student exists
        studentService.getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        return grades.values().stream()
            .filter(grade -> grade.getStudentId().equals(studentId))
            .sorted((g1, g2) -> g1.getCourseCode().compareToIgnoreCase(g2.getCourseCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Grade> getCourseGrades(String courseCode) throws EntityNotFoundException {
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        // Verify course exists
        courseService.getCourseByCode(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        return grades.values().stream()
            .filter(grade -> grade.getCourseCode().equals(courseCode))
            .sorted((g1, g2) -> g1.getStudentId().compareToIgnoreCase(g2.getStudentId()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Grade> getAllGrades() {
        return new ArrayList<>(grades.values());
    }
    
    @Override
    public double calculateGPA(String studentId) throws EntityNotFoundException {
        List<Grade> studentGrades = getStudentGrades(studentId);
        
        if (studentGrades.isEmpty()) {
            return 0.0;
        }
        
        // Calculate GPA using quality points
        double totalQualityPoints = 0.0;
        int totalCredits = 0;
        
        for (Grade grade : studentGrades) {
            try {
                Optional<Course> courseOpt = courseService.getCourseByCode(grade.getCourseCode());
                if (courseOpt.isPresent()) {
                    Course course = courseOpt.get();
                    int credits = course.getCredits();
                    totalQualityPoints += grade.getGradePoints() * credits;
                    totalCredits += credits;
                }
            } catch (Exception e) {
                // Skip this grade if course not found
                System.err.println("Warning: Course " + grade.getCourseCode() + " not found for GPA calculation");
            }
        }
        
        return totalCredits > 0 ? totalQualityPoints / totalCredits : 0.0;
    }
    
    @Override
    public double calculateCourseAverage(String courseCode) throws EntityNotFoundException {
        List<Grade> courseGrades = getCourseGrades(courseCode);
        
        if (courseGrades.isEmpty()) {
            return 0.0;
        }
        
        return courseGrades.stream()
            .mapToDouble(Grade::getMarks)
            .average()
            .orElse(0.0);
    }
    
    @Override
    public boolean hasGrade(String studentId, String courseCode) {
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        String gradeKey = studentId + "_" + courseCode;
        return grades.containsKey(gradeKey);
    }
    
    @Override
    public void deleteGrade(String studentId, String courseCode) throws EntityNotFoundException {
        Objects.requireNonNull(studentId, "Student ID cannot be null");
        Objects.requireNonNull(courseCode, "Course code cannot be null");
        
        String gradeKey = studentId + "_" + courseCode;
        
        if (!grades.containsKey(gradeKey)) {
            throw new EntityNotFoundException("Grade not found for student " + 
                studentId + " in course " + courseCode, "Grade", gradeKey);
        }
        
        grades.remove(gradeKey);
        
        // Update student GPA
        try {
            updateStudentGPA(studentId);
        } catch (Exception e) {
            System.err.println("Warning: Could not update GPA for student " + studentId + ": " + e.getMessage());
        }
    }
    
    private void updateStudentGPA(String studentId) throws EntityNotFoundException {
        double newGPA = calculateGPA(studentId);
        
        Student student = studentService.getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        student.setGpa(newGPA);
        studentService.updateStudent(student);
    }
    
    /**
     * Get grade distribution statistics for a course
     */
    public Map<LetterGrade, Long> getCourseGradeDistribution(String courseCode) throws EntityNotFoundException {
        List<Grade> courseGrades = getCourseGrades(courseCode);
        
        return courseGrades.stream()
            .collect(Collectors.groupingBy(
                Grade::getLetterGrade,
                Collectors.counting()
            ));
    }
    
    /**
     * Get overall grade statistics
     */
    public Map<String, Object> getGradeStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        List<Grade> allGrades = getAllGrades();
        stats.put("totalGrades", allGrades.size());
        
        if (!allGrades.isEmpty()) {
            // Average marks
            double avgMarks = allGrades.stream()
                .mapToDouble(Grade::getMarks)
                .average()
                .orElse(0.0);
            stats.put("averageMarks", avgMarks);
            
            // Grade distribution
            Map<LetterGrade, Long> distribution = allGrades.stream()
                .collect(Collectors.groupingBy(
                    Grade::getLetterGrade,
                    Collectors.counting()
                ));
            stats.put("gradeDistribution", distribution);
            
            // Pass rate
            long passingGrades = allGrades.stream()
                .filter(grade -> grade.getLetterGrade().isPassing())
                .count();
            double passRate = (passingGrades * 100.0) / allGrades.size();
            stats.put("passRate", passRate);
        }
        
        return stats;
    }
}