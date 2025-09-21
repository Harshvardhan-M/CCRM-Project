package edu.ccrm.service;

import edu.ccrm.domain.Transcript;
import edu.ccrm.domain.exceptions.EntityNotFoundException;

/**
 * Service interface for transcript operations
 */
public interface TranscriptService {
    
    Transcript generateTranscript(String studentId) throws EntityNotFoundException;
    
    String generateTranscriptReport(String studentId) throws EntityNotFoundException;
    
    void exportTranscriptToPDF(String studentId, String filePath) throws EntityNotFoundException;
}