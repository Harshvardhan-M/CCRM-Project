package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.domain.exceptions.*;
import edu.ccrm.config.AppConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * Implementation of StudentService interface
 * Demonstrates interface implementation and functional programming
 */
public class StudentServiceImpl implements StudentService {
    
    // In-memory storage using concurrent collections for thread safety
    private final Map<String, Student> students = new ConcurrentHashMap<>();
    private final AppConfig config = AppConfig.getInstance();
    
    @Override
    public void addStudent(Student student) throws DuplicateEntityException {
        Objects.requireNonNull(student, "Student cannot be null");
        Objects.requireNonNull(student.getId(), "Student ID cannot be null");
        
        if (students.containsKey(student.getId())) {
            throw new DuplicateEntityException("Student", student.getId());
        }
        
        students.put(student.getId(), student);
    }
    
    @Override
    public Optional<Student> getStudentById(String id) {
        Objects.requireNonNull(id, "Student ID cannot be null");
        return Optional.ofNullable(students.get(id));
    }
    
    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }
    
    @Override
    public List<Student> getStudentsByStatus(StudentStatus status) {
        Objects.requireNonNull(status, "Status cannot be null");
        
        return students.values().stream()
            .filter(student -> student.getStatus() == status)
            .sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()))
            .collect(Collectors.toList());
    }
    
    @Override
    public void updateStudent(Student student) throws EntityNotFoundException {
        Objects.requireNonNull(student, "Student cannot be null");
        Objects.requireNonNull(student.getId(), "Student ID cannot be null");
        
        if (!students.containsKey(student.getId())) {
            throw new EntityNotFoundException("Student", student.getId());
        }
        
        students.put(student.getId(), student);
    }
    
    @Override
    public void deactivateStudent(String id) throws EntityNotFoundException {
        Student student = getStudentById(id)
            .orElseThrow(() -> new EntityNotFoundException("Student", id));
        
        student.setStatus(StudentStatus.INACTIVE);
        students.put(id, student);
    }
    
    @Override
    public boolean deleteStudent(String id) throws EntityNotFoundException {
        if (!students.containsKey(id)) {
            throw new EntityNotFoundException("Student", id);
        }
        
        return students.remove(id) != null;
    }
    
    @Override
    public List<Student> searchStudentsByName(String namePart) {
        Objects.requireNonNull(namePart, "Name part cannot be null");
        
        String searchTerm = namePart.toLowerCase().trim();
        
        return students.values().stream()
            .filter(student -> student.getFullName().toLowerCase().contains(searchTerm))
            .sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Student> searchStudentsByEmail(String emailPart) {
        Objects.requireNonNull(emailPart, "Email part cannot be null");
        
        String searchTerm = emailPart.toLowerCase().trim();
        
        return students.values().stream()
            .filter(student -> student.getEmail().toLowerCase().contains(searchTerm))
            .sorted((s1, s2) -> s1.getEmail().compareToIgnoreCase(s2.getEmail()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Student> searchStudents(Predicate<Student> criteria) {
        Objects.requireNonNull(criteria, "Search criteria cannot be null");
        
        return students.values().stream()
            .filter(criteria)
            .sorted((s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName()))
            .collect(Collectors.toList());
    }
    
    @Override
    public boolean canEnroll(String studentId, String courseCode) {
        Optional<Student> studentOpt = getStudentById(studentId);
        if (studentOpt.isEmpty()) {
            return false;
        }
        
        Student student = studentOpt.get();
        
        // Check if student is active
        if (!student.isEligibleForEnrollment()) {
            return false;
        }
        
        // Check if already enrolled
        if (student.getEnrolledCourses().contains(courseCode)) {
            return false;
        }
        
        // Check credit limit (would need CourseService to get course credits)
        // This is simplified - in real implementation would check actual credit limit
        return student.getTotalCredits() < config.getMaxCreditsPerSemester();
    }
    
    @Override
    public int getCurrentCredits(String studentId) {
        return getStudentById(studentId)
            .map(Student::getTotalCredits)
            .orElse(0);
    }
    
    @Override
    public void updateGPA(String studentId) throws EntityNotFoundException {
        Student student = getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        // This would typically calculate GPA from grades
        // For now, we'll just update the last modified timestamp
        student.setGpa(student.getGpa()); // Triggers timestamp update
    }
    
    /**
     * Utility method demonstrating streams and collectors
     */
    public Map<StudentStatus, Long> getStudentStatistics() {
        return students.values().stream()
            .collect(Collectors.groupingBy(
                Student::getStatus,
                Collectors.counting()
            ));
    }
    
    /**
     * Advanced search with multiple criteria
     */
    public List<Student> advancedSearch(String name, String email, StudentStatus status, 
                                       Double minGPA, Double maxGPA) {
        return students.values().stream()
            .filter(student -> name == null || 
                student.getFullName().toLowerCase().contains(name.toLowerCase()))
            .filter(student -> email == null || 
                student.getEmail().toLowerCase().contains(email.toLowerCase()))
            .filter(student -> status == null || student.getStatus() == status)
            .filter(student -> minGPA == null || student.getGpa() >= minGPA)
            .filter(student -> maxGPA == null || student.getGpa() <= maxGPA)
            .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa())) // Sort by GPA desc
            .collect(Collectors.toList());
    }
}