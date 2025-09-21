package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.exceptions.*;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for course operations
 */
public interface CourseService {
    
    void addCourse(Course course) throws DuplicateEntityException;
    
    Optional<Course> getCourseByCode(String code);
    
    List<Course> getAllCourses();
    
    List<Course> getActiveCourses();
    
    void updateCourse(Course course) throws EntityNotFoundException;
    
    void deactivateCourse(String code) throws EntityNotFoundException;
    
    boolean deleteCourse(String code) throws EntityNotFoundException;
    
    // Search and filter methods
    List<Course> getCoursesByDepartment(String department);
    
    List<Course> getCoursesBySemester(Semester semester);
    
    List<Course> getCoursesByInstructor(String instructor);
    
    List<Course> getCoursesByCredits(int credits);
    
    List<Course> searchCourses(java.util.function.Predicate<Course> criteria);
    
    // Business logic
    void assignInstructor(String courseCode, String instructor) throws EntityNotFoundException;
    
    boolean isCourseFull(String courseCode);
    
    int getEnrollmentCount(String courseCode);
}