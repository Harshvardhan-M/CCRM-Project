package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.domain.exceptions.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.function.Predicate;

/**
 * Implementation of CourseService interface
 */
public class CourseServiceImpl implements CourseService {
    
    private final Map<String, Course> courses = new ConcurrentHashMap<>();
    
    @Override
    public void addCourse(Course course) throws DuplicateEntityException {
        Objects.requireNonNull(course, "Course cannot be null");
        Objects.requireNonNull(course.getCode(), "Course code cannot be null");
        
        if (courses.containsKey(course.getCode())) {
            throw new DuplicateEntityException("Course", course.getCode());
        }
        
        courses.put(course.getCode(), course);
    }
    
    @Override
    public Optional<Course> getCourseByCode(String code) {
        Objects.requireNonNull(code, "Course code cannot be null");
        return Optional.ofNullable(courses.get(code));
    }
    
    @Override
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public List<Course> getActiveCourses() {
        return courses.values().stream()
            .filter(Course::isActive)
            .sorted((c1, c2) -> c1.getCode().compareToIgnoreCase(c2.getCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public void updateCourse(Course course) throws EntityNotFoundException {
        Objects.requireNonNull(course, "Course cannot be null");
        Objects.requireNonNull(course.getCode(), "Course code cannot be null");
        
        if (!courses.containsKey(course.getCode())) {
            throw new EntityNotFoundException("Course", course.getCode());
        }
        
        courses.put(course.getCode(), course);
    }
    
    @Override
    public void deactivateCourse(String code) throws EntityNotFoundException {
        Course course = getCourseByCode(code)
            .orElseThrow(() -> new EntityNotFoundException("Course", code));
        
        course.setActive(false);
        courses.put(code, course);
    }
    
    @Override
    public boolean deleteCourse(String code) throws EntityNotFoundException {
        if (!courses.containsKey(code)) {
            throw new EntityNotFoundException("Course", code);
        }
        
        return courses.remove(code) != null;
    }
    
    @Override
    public List<Course> getCoursesByDepartment(String department) {
        Objects.requireNonNull(department, "Department cannot be null");
        
        return courses.values().stream()
            .filter(course -> course.getDepartment().equalsIgnoreCase(department))
            .sorted((c1, c2) -> c1.getCode().compareToIgnoreCase(c2.getCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> getCoursesBySemester(Semester semester) {
        Objects.requireNonNull(semester, "Semester cannot be null");
        
        return courses.values().stream()
            .filter(course -> course.getSemester() == semester)
            .sorted((c1, c2) -> c1.getCode().compareToIgnoreCase(c2.getCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> getCoursesByInstructor(String instructor) {
        if (instructor == null || instructor.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        String searchTerm = instructor.toLowerCase().trim();
        
        return courses.values().stream()
            .filter(course -> course.getInstructor() != null && 
                course.getInstructor().toLowerCase().contains(searchTerm))
            .sorted((c1, c2) -> c1.getCode().compareToIgnoreCase(c2.getCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> getCoursesByCredits(int credits) {
        return courses.values().stream()
            .filter(course -> course.getCredits() == credits)
            .sorted((c1, c2) -> c1.getCode().compareToIgnoreCase(c2.getCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> searchCourses(Predicate<Course> criteria) {
        Objects.requireNonNull(criteria, "Search criteria cannot be null");
        
        return courses.values().stream()
            .filter(criteria)
            .sorted((c1, c2) -> c1.getCode().compareToIgnoreCase(c2.getCode()))
            .collect(Collectors.toList());
    }
    
    @Override
    public void assignInstructor(String courseCode, String instructor) throws EntityNotFoundException {
        Course course = getCourseByCode(courseCode)
            .orElseThrow(() -> new EntityNotFoundException("Course", courseCode));
        
        course.setInstructor(instructor);
        courses.put(courseCode, course);
    }
    
    @Override
    public boolean isCourseFull(String courseCode) {
        // This would check against enrollment capacity
        // For now, returning false (no course is full)
        return false;
    }
    
    @Override
    public int getEnrollmentCount(String courseCode) {
        // This would count actual enrollments
        // For now, returning 0
        return 0;
    }
    
    /**
     * Get course statistics by department
     */
    public Map<String, Map<String, Object>> getDepartmentStatistics() {
        return courses.values().stream()
            .collect(Collectors.groupingBy(
                Course::getDepartment,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    coursesInDept -> {
                        Map<String, Object> stats = new HashMap<>();
                        stats.put("totalCourses", coursesInDept.size());
                        stats.put("activeCourses", coursesInDept.stream().filter(Course::isActive).count());
                        stats.put("totalCredits", coursesInDept.stream().mapToInt(Course::getCredits).sum());
                        stats.put("avgCredits", coursesInDept.stream().mapToInt(Course::getCredits).average().orElse(0));
                        return stats;
                    }
                )
            ));
    }
}