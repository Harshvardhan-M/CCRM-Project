package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.exceptions.*;

import java.util.List;

/**
 * Service interface for enrollment operations
 */
public interface EnrollmentService {
    
    void enrollStudent(String studentId, String courseCode) 
        throws DuplicateEnrollmentException, MaxCreditLimitExceededException, EntityNotFoundException;
    
    void unenrollStudent(String studentId, String courseCode) throws EntityNotFoundException;
    
    List<Enrollment> getStudentEnrollments(String studentId) throws EntityNotFoundException;
    
    List<Enrollment> getCourseEnrollments(String courseCode) throws EntityNotFoundException;
    
    List<Enrollment> getAllEnrollments();
    
    boolean isStudentEnrolled(String studentId, String courseCode);
    
    int getStudentCreditCount(String studentId);
    
    void updateEnrollmentStatus(String studentId, String courseCode, 
                               Enrollment.EnrollmentStatus status) throws EntityNotFoundException;
}