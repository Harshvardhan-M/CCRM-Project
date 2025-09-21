package edu.ccrm.service;

import edu.ccrm.domain.*;
import edu.ccrm.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of TranscriptService
 */
public class TranscriptServiceImpl implements TranscriptService {
    
    private final StudentService studentService = new StudentServiceImpl();
    private final GradeService gradeService = new GradeServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    
    @Override
    public Transcript generateTranscript(String studentId) throws EntityNotFoundException {
        // Get student
        Student student = studentService.getStudentById(studentId)
            .orElseThrow(() -> new EntityNotFoundException("Student", studentId));
        
        // Get student's grades
        List<Grade> grades = gradeService.getStudentGrades(studentId);
        
        // Get corresponding courses
        List<Course> courses = grades.stream()
            .map(grade -> courseService.getCourseByCode(grade.getCourseCode()))
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(Collectors.toList());
        
        return new Transcript(studentId, student.getFullName(), grades, courses);
    }
    
    @Override
    public String generateTranscriptReport(String studentId) throws EntityNotFoundException {
        Transcript transcript = generateTranscript(studentId);
        return transcript.toString();
    }
    
    @Override
    public void exportTranscriptToPDF(String studentId, String filePath) throws EntityNotFoundException {
        // This would generate a PDF file
        // For now, just create a text file with transcript content
        String transcriptContent = generateTranscriptReport(studentId);
        
        try {
            java.nio.file.Files.write(
                java.nio.file.Paths.get(filePath), 
                transcriptContent.getBytes()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to export transcript: " + e.getMessage(), e);
        }
    }
}