package edu.ccrm.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Transcript class for student academic records
 * Demonstrates composition and inner classes
 */
public class Transcript {
    
    private final String studentId;
    private final String studentName;
    private final LocalDateTime generatedDate;
    private final List<GradeEntry> gradeEntries;
    private final TranscriptSummary summary;
    
    public Transcript(String studentId, String studentName, List<Grade> grades, List<Course> courses) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.generatedDate = LocalDateTime.now();
        
        // Create grade entries by combining grades and courses
        this.gradeEntries = grades.stream()
            .map(grade -> {
                Course course = courses.stream()
                    .filter(c -> c.getCode().equals(grade.getCourseCode()))
                    .findFirst()
                    .orElse(null);
                return new GradeEntry(grade, course);
            })
            .collect(Collectors.toList());
        
        this.summary = new TranscriptSummary(gradeEntries);
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public LocalDateTime getGeneratedDate() { return generatedDate; }
    public List<GradeEntry> getGradeEntries() { return Collections.unmodifiableList(gradeEntries); }
    public TranscriptSummary getSummary() { return summary; }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OFFICIAL TRANSCRIPT\n");
        sb.append("=".repeat(60)).append("\n");
        sb.append(String.format("Student: %s (ID: %s)\n", studentName, studentId));
        sb.append(String.format("Generated: %s\n", generatedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))));
        sb.append("=".repeat(60)).append("\n\n");
        
        // Group by semester
        Map<Semester, List<GradeEntry>> entriesBySemester = gradeEntries.stream()
            .filter(entry -> entry.getCourse() != null)
            .collect(Collectors.groupingBy(entry -> entry.getCourse().getSemester()));
        
        entriesBySemester.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                sb.append(String.format("%s SEMESTER\n", entry.getKey().getDisplayName().toUpperCase()));
                sb.append("-".repeat(40)).append("\n");
                sb.append(String.format("%-10s %-25s %7s %5s %6s\n", 
                    "Code", "Title", "Credits", "Grade", "Points"));
                sb.append("-".repeat(40)).append("\n");
                
                entry.getValue().forEach(gradeEntry -> {
                    Course course = gradeEntry.getCourse();
                    Grade grade = gradeEntry.getGrade();
                    sb.append(String.format("%-10s %-25s %7d %5s %6.2f\n",
                        course.getCode(),
                        course.getTitle().length() > 25 ? course.getTitle().substring(0, 25) : course.getTitle(),
                        course.getCredits(),
                        grade.getLetterGrade(),
                        grade.getGradePoints()));
                });
                sb.append("\n");
            });
        
        sb.append("SUMMARY\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append(summary.toString());
        
        return sb.toString();
    }
    
    /**
     * Inner class for individual grade entries
     * Demonstrates inner class with access to outer class
     */
    public class GradeEntry {
        private final Grade grade;
        private final Course course;
        
        public GradeEntry(Grade grade, Course course) {
            this.grade = grade;
            this.course = course;
        }
        
        public Grade getGrade() { return grade; }
        public Course getCourse() { return course; }
        
        public int getCredits() {
            return course != null ? course.getCredits() : 0;
        }
        
        public double getQualityPoints() {
            return getCredits() * grade.getGradePoints();
        }
        
        public boolean isPassing() {
            return grade.getLetterGrade().isPassing();
        }
        
        @Override
        public String toString() {
            return String.format("%s: %s - %s (%.2f points)", 
                grade.getCourseCode(),
                course != null ? course.getTitle() : "Unknown Course",
                grade.getLetterGrade(),
                grade.getGradePoints());
        }
    }
    
    /**
     * Static nested class for transcript summary
     * Demonstrates static nested class
     */
    public static class TranscriptSummary {
        private final int totalCreditsAttempted;
        private final int totalCreditsEarned;
        private final double totalQualityPoints;
        private final double cumulativeGPA;
        private final Map<LetterGrade, Integer> gradeDistribution;
        
        public TranscriptSummary(List<GradeEntry> entries) {
            this.totalCreditsAttempted = entries.stream()
                .mapToInt(GradeEntry::getCredits)
                .sum();
            
            this.totalCreditsEarned = entries.stream()
                .filter(GradeEntry::isPassing)
                .mapToInt(GradeEntry::getCredits)
                .sum();
            
            this.totalQualityPoints = entries.stream()
                .mapToDouble(GradeEntry::getQualityPoints)
                .sum();
            
            this.cumulativeGPA = totalCreditsAttempted > 0 ? 
                totalQualityPoints / totalCreditsAttempted : 0.0;
            
            this.gradeDistribution = entries.stream()
                .collect(Collectors.groupingBy(
                    entry -> entry.getGrade().getLetterGrade(),
                    Collectors.summingInt(entry -> 1)
                ));
        }
        
        // Getters
        public int getTotalCreditsAttempted() { return totalCreditsAttempted; }
        public int getTotalCreditsEarned() { return totalCreditsEarned; }
        public double getTotalQualityPoints() { return totalQualityPoints; }
        public double getCumulativeGPA() { return cumulativeGPA; }
        public Map<LetterGrade, Integer> getGradeDistribution() { return Collections.unmodifiableMap(gradeDistribution); }
        
        public String getAcademicStanding() {
            if (cumulativeGPA >= 3.5) return "Dean's List";
            if (cumulativeGPA >= 3.0) return "Good Standing";
            if (cumulativeGPA >= 2.0) return "Satisfactory";
            if (cumulativeGPA >= 1.0) return "Academic Warning";
            return "Academic Probation";
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Total Credits Attempted: %d\n", totalCreditsAttempted));
            sb.append(String.format("Total Credits Earned: %d\n", totalCreditsEarned));
            sb.append(String.format("Cumulative GPA: %.2f\n", cumulativeGPA));
            sb.append(String.format("Academic Standing: %s\n", getAcademicStanding()));
            
            if (!gradeDistribution.isEmpty()) {
                sb.append("\nGrade Distribution:\n");
                gradeDistribution.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        sb.append(String.format("  %s: %d courses\n", 
                            entry.getKey(), entry.getValue()));
                    });
            }
            
            return sb.toString();
        }
    }
}