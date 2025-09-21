package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.StudentStatus;
import edu.ccrm.domain.exceptions.*;

import java.util.List;
import java.util.Optional;

/**
 * Interface demonstrating abstraction and polymorphism
 * Service layer interface for student operations
 */
public interface StudentService {
    
    void addStudent(Student student) throws DuplicateEntityException;
    
    Optional<Student> getStudentById(String id);
    
    List<Student> getAllStudents();
    
    List<Student> getStudentsByStatus(StudentStatus status);
    
    void updateStudent(Student student) throws EntityNotFoundException;
    
    void deactivateStudent(String id) throws EntityNotFoundException;
    
    boolean deleteStudent(String id) throws EntityNotFoundException;
    
    // Search methods using functional programming
    List<Student> searchStudentsByName(String namePart);
    
    List<Student> searchStudentsByEmail(String emailPart);
    
    List<Student> searchStudents(java.util.function.Predicate<Student> criteria);
    
    // Business logic methods
    boolean canEnroll(String studentId, String courseCode);
    
    int getCurrentCredits(String studentId);
    
    void updateGPA(String studentId) throws EntityNotFoundException;
}