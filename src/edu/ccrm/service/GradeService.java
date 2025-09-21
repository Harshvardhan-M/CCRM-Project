package edu.ccrm.service;

import edu.ccrm.domain.Grade;
import edu.ccrm.domain.exceptions.*;

import java.util.List;

/**
 * Service interface for grade operations
 */
public interface GradeService {
    
    Grade recordGrade(String studentId, String courseCode, double marks) 
        throws InvalidGradeException, EntityNotFoundException;
    
    Grade updateGrade(String studentId, String courseCode, double newMarks)
        throws InvalidGradeException, EntityNotFoundException;
    
    List<Grade> getStudentGrades(String studentId) throws EntityNotFoundException;
    
    List<Grade> getCourseGrades(String courseCode) throws EntityNotFoundException;
    
    List<Grade> getAllGrades();
    
    double calculateGPA(String studentId) throws EntityNotFoundException;
    
    double calculateCourseAverage(String courseCode) throws EntityNotFoundException;
    
    boolean hasGrade(String studentId, String courseCode);
    
    void deleteGrade(String studentId, String courseCode) throws EntityNotFoundException;
}