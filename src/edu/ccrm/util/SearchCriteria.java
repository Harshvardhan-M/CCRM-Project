package edu.ccrm.util;

import edu.ccrm.domain.*;

import java.util.function.Predicate;

/**
 * Utility class for creating search predicates using functional interfaces
 * Demonstrates functional programming with predicates and lambdas
 */
public final class SearchCriteria {
    
    private SearchCriteria() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    // Student search criteria
    public static class Students {
        
        public static Predicate<Student> nameContains(String searchTerm) {
            return student -> student.getFullName().toLowerCase()
                .contains(searchTerm.toLowerCase());
        }
        
        public static Predicate<Student> emailContains(String searchTerm) {
            return student -> student.getEmail().toLowerCase()
                .contains(searchTerm.toLowerCase());
        }
        
        public static Predicate<Student> hasStatus(StudentStatus status) {
            return student -> student.getStatus() == status;
        }
        
        public static Predicate<Student> gpaGreaterThan(double minGPA) {
            return student -> student.getGpa() > minGPA;
        }
        
        public static Predicate<Student> gpaLessThan(double maxGPA) {
            return student -> student.getGpa() < maxGPA;
        }
        
        public static Predicate<Student> gpaBetween(double minGPA, double maxGPA) {
            return student -> student.getGpa() >= minGPA && student.getGpa() <= maxGPA;
        }
        
        public static Predicate<Student> creditsGreaterThan(int minCredits) {
            return student -> student.getTotalCredits() > minCredits;
        }
        
        public static Predicate<Student> enrolledInCourse(String courseCode) {
            return student -> student.getEnrolledCourses().contains(courseCode);
        }
        
        public static Predicate<Student> enrolledInCoursesCount(int minCount) {
            return student -> student.getEnrolledCourses().size() >= minCount;
        }
        
        public static Predicate<Student> isActive() {
            return student -> student.getStatus() == StudentStatus.ACTIVE;
        }
        
        public static Predicate<Student> isEligibleForEnrollment() {
            return Student::isEligibleForEnrollment;
        }
    }
    
    // Course search criteria
    public static class Courses {
        
        public static Predicate<Course> codeContains(String searchTerm) {
            return course -> course.getCode().toLowerCase()
                .contains(searchTerm.toLowerCase());
        }
        
        public static Predicate<Course> titleContains(String searchTerm) {
            return course -> course.getTitle().toLowerCase()
                .contains(searchTerm.toLowerCase());
        }
        
        public static Predicate<Course> inDepartment(String department) {
            return course -> course.getDepartment().equalsIgnoreCase(department);
        }
        
        public static Predicate<Course> hasSemester(Semester semester) {
            return course -> course.getSemester() == semester;
        }
        
        public static Predicate<Course> hasInstructor(String instructor) {
            return course -> course.getInstructor() != null && 
                course.getInstructor().toLowerCase().contains(instructor.toLowerCase());
        }
        
        public static Predicate<Course> hasCredits(int credits) {
            return course -> course.getCredits() == credits;
        }
        
        public static Predicate<Course> creditsGreaterThan(int minCredits) {
            return course -> course.getCredits() > minCredits;
        }
        
        public static Predicate<Course> creditsLessThan(int maxCredits) {
            return course -> course.getCredits() < maxCredits;
        }
        
        public static Predicate<Course> isActive() {
            return Course::isActive;
        }
        
        public static Predicate<Course> hasAssignedInstructor() {
            return course -> course.getInstructor() != null && 
                !course.getInstructor().trim().isEmpty();
        }
    }
    
    // Grade search criteria
    public static class Grades {
        
        public static Predicate<Grade> forStudent(String studentId) {
            return grade -> grade.getStudentId().equals(studentId);
        }
        
        public static Predicate<Grade> forCourse(String courseCode) {
            return grade -> grade.getCourseCode().equals(courseCode);
        }
        
        public static Predicate<Grade> marksGreaterThan(double minMarks) {
            return grade -> grade.getMarks() > minMarks;
        }
        
        public static Predicate<Grade> marksLessThan(double maxMarks) {
            return grade -> grade.getMarks() < maxMarks;
        }
        
        public static Predicate<Grade> marksBetween(double minMarks, double maxMarks) {
            return grade -> grade.getMarks() >= minMarks && grade.getMarks() <= maxMarks;
        }
        
        public static Predicate<Grade> hasLetterGrade(LetterGrade letterGrade) {
            return grade -> grade.getLetterGrade() == letterGrade;
        }
        
        public static Predicate<Grade> isPassing() {
            return grade -> grade.getLetterGrade().isPassing();
        }
        
        public static Predicate<Grade> isHonorLevel() {
            return grade -> grade.getLetterGrade().isHonorLevel();
        }
        
        public static Predicate<Grade> gradePointsGreaterThan(double minPoints) {
            return grade -> grade.getGradePoints() > minPoints;
        }
    }
    
    // Enrollment search criteria
    public static class Enrollments {
        
        public static Predicate<Enrollment> forStudent(String studentId) {
            return enrollment -> enrollment.getStudentId().equals(studentId);
        }
        
        public static Predicate<Enrollment> forCourse(String courseCode) {
            return enrollment -> enrollment.getCourseCode().equals(courseCode);
        }
        
        public static Predicate<Enrollment> hasStatus(Enrollment.EnrollmentStatus status) {
            return enrollment -> enrollment.getStatus() == status;
        }
        
        public static Predicate<Enrollment> isActive() {
            return enrollment -> enrollment.getStatus() == Enrollment.EnrollmentStatus.ENROLLED;
        }
        
        public static Predicate<Enrollment> enrolledAfter(java.time.LocalDateTime date) {
            return enrollment -> enrollment.getEnrollmentDate().isAfter(date);
        }
        
        public static Predicate<Enrollment> enrolledBefore(java.time.LocalDateTime date) {
            return enrollment -> enrollment.getEnrollmentDate().isBefore(date);
        }
    }
    
    // Utility methods for combining predicates
    public static <T> Predicate<T> and(Predicate<T>... predicates) {
        if (predicates.length == 0) {
            return t -> true;
        }
        
        Predicate<T> result = predicates[0];
        for (int i = 1; i < predicates.length; i++) {
            result = result.and(predicates[i]);
        }
        
        return result;
    }
    
    public static <T> Predicate<T> or(Predicate<T>... predicates) {
        if (predicates.length == 0) {
            return t -> false;
        }
        
        Predicate<T> result = predicates[0];
        for (int i = 1; i < predicates.length; i++) {
            result = result.or(predicates[i]);
        }
        
        return result;
    }
    
    public static <T> Predicate<T> not(Predicate<T> predicate) {
        return predicate.negate();
    }
}