package edu.ccrm.util;

import edu.ccrm.domain.*;

import java.util.Comparator;
import java.util.function.Function;

/**
 * Utility class for common comparators using lambdas and method references
 * Demonstrates functional programming concepts
 */
public final class Comparators {
    
    // Private constructor to prevent instantiation
    private Comparators() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    // Student comparators using lambda expressions
    public static final Comparator<Student> BY_NAME = 
        (s1, s2) -> s1.getFullName().compareToIgnoreCase(s2.getFullName());
    
    public static final Comparator<Student> BY_ID = 
        Comparator.comparing(Student::getId);
    
    public static final Comparator<Student> BY_GPA_DESC = 
        (s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa());
    
    public static final Comparator<Student> BY_ENROLLMENT_DATE = 
        Comparator.comparing(Student::getEnrollmentDate);
    
    public static final Comparator<Student> BY_STATUS_THEN_NAME = 
        Comparator.comparing(Student::getStatus).thenComparing(BY_NAME);
    
    // Course comparators
    public static final Comparator<Course> BY_CODE = 
        Comparator.comparing(Course::getCode);
    
    public static final Comparator<Course> BY_TITLE = 
        (c1, c2) -> c1.getTitle().compareToIgnoreCase(c2.getTitle());
    
    public static final Comparator<Course> BY_DEPARTMENT_THEN_CODE = 
        Comparator.comparing(Course::getDepartment).thenComparing(BY_CODE);
    
    public static final Comparator<Course> BY_CREDITS_DESC = 
        (c1, c2) -> Integer.compare(c2.getCredits(), c1.getCredits());
    
    public static final Comparator<Course> BY_SEMESTER = 
        Comparator.comparing(Course::getSemester, Comparator.nullsLast(Comparator.naturalOrder()));
    
    // Grade comparators
    public static final Comparator<Grade> BY_MARKS_DESC = 
        (g1, g2) -> Double.compare(g2.getMarks(), g1.getMarks());
    
    public static final Comparator<Grade> BY_LETTER_GRADE = 
        Comparator.comparing(Grade::getLetterGrade);
    
    public static final Comparator<Grade> BY_STUDENT_THEN_COURSE = 
        Comparator.comparing(Grade::getStudentId).thenComparing(Grade::getCourseCode);
    
    public static final Comparator<Grade> BY_RECORDED_DATE = 
        Comparator.comparing(Grade::getRecordedDate);
    
    // Enrollment comparators
    public static final Comparator<Enrollment> BY_ENROLLMENT_DATE = 
        Comparator.comparing(Enrollment::getEnrollmentDate);
    
    public static final Comparator<Enrollment> BY_STUDENT_ID = 
        Comparator.comparing(Enrollment::getStudentId);
    
    public static final Comparator<Enrollment> BY_COURSE_CODE = 
        Comparator.comparing(Enrollment::getCourseCode);
    
    // Generic utility methods for creating comparators
    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<T, U> keyExtractor) {
        return Comparator.comparing(keyExtractor);
    }
    
    public static <T, U extends Comparable<? super U>> Comparator<T> comparingReverse(Function<T, U> keyExtractor) {
        return Comparator.comparing(keyExtractor).reversed();
    }
    
    public static <T> Comparator<T> nullsFirst(Comparator<T> comparator) {
        return Comparator.nullsFirst(comparator);
    }
    
    public static <T> Comparator<T> nullsLast(Comparator<T> comparator) {
        return Comparator.nullsLast(comparator);
    }
    
    /**
     * Create a case-insensitive string comparator
     */
    public static <T> Comparator<T> comparingIgnoreCase(Function<T, String> keyExtractor) {
        return (o1, o2) -> {
            String s1 = keyExtractor.apply(o1);
            String s2 = keyExtractor.apply(o2);
            
            if (s1 == null && s2 == null) return 0;
            if (s1 == null) return -1;
            if (s2 == null) return 1;
            
            return s1.compareToIgnoreCase(s2);
        };
    }
    
    /**
     * Create a multi-level comparator
     */
    @SafeVarargs
    public static <T> Comparator<T> multiLevel(Comparator<T>... comparators) {
        if (comparators.length == 0) {
            throw new IllegalArgumentException("At least one comparator is required");
        }
        
        Comparator<T> result = comparators[0];
        
        for (int i = 1; i < comparators.length; i++) {
            result = result.thenComparing(comparators[i]);
        }
        
        return result;
    }
    
    /**
     * Create a comparator that reverses the natural order
     */
    public static <T extends Comparable<T>> Comparator<T> reverseOrder() {
        return Comparator.reverseOrder();
    }
    
    /**
     * Create a comparator for natural order
     */
    public static <T extends Comparable<T>> Comparator<T> naturalOrder() {
        return Comparator.naturalOrder();
    }
}