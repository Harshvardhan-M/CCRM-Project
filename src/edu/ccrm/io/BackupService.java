package edu.ccrm.io;

import edu.ccrm.config.AppConfig;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service for backup operations using NIO.2
 * Demonstrates recursive file operations and modern I/O
 */
public class BackupService {
    
    private final AppConfig config = AppConfig.getInstance();
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    
    /**
     * Create a complete backup of all data
     * Demonstrates Path manipulation and directory operations
     */
    public String createBackup() throws IOException {
        String timestamp = LocalDateTime.now().format(timestampFormatter);
        String backupName = "backup_" + timestamp;
        Path backupPath = Paths.get(config.getBackupDirectory(), backupName);
        
        // Create backup directory
        Files.createDirectories(backupPath);
        
        // Copy data files (this would copy actual data in real implementation)
        createBackupFiles(backupPath);
        
        // Create backup manifest
        createBackupManifest(backupPath);
        
        return backupPath.toString();
    }
    
    /**
     * List all available backups
     */
    public List<String> listBackups() throws IOException {
        Path backupDir = Paths.get(config.getBackupDirectory());
        
        if (!Files.exists(backupDir)) {
            Files.createDirectories(backupDir);
            return List.of();
        }
        
        try (Stream<Path> paths = Files.list(backupDir)) {
            return paths
                .filter(Files::isDirectory)
                .map(path -> path.getFileName().toString())
                .filter(name -> name.startsWith("backup_"))
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Calculate directory size recursively
     * Demonstrates recursive file tree walking
     */
    public long calculateDirectorySize(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        
        if (!Files.exists(path)) {
            return 0;
        }
        
        if (Files.isRegularFile(path)) {
            return Files.size(path);
        }
        
        // Recursive calculation using Files.walk()
        try (Stream<Path> paths = Files.walk(path)) {
            return paths
                .filter(Files::isRegularFile)
                .mapToLong(file -> {
                    try {
                        return Files.size(file);
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .sum();
        }
    }
    
    /**
     * Clean old backups, keeping only the most recent N backups
     */
    public int cleanOldBackups(int keepCount) throws IOException {
        List<String> backups = listBackups();
        
        if (backups.size() <= keepCount) {
            return 0;
        }
        
        // Sort backups by name (which includes timestamp)
        List<String> sortedBackups = backups.stream()
            .sorted()
            .collect(Collectors.toList());
        
        // Delete old backups
        int deletedCount = 0;
        int toDelete = sortedBackups.size() - keepCount;
        
        for (int i = 0; i < toDelete; i++) {
            String backupToDelete = sortedBackups.get(i);
            Path backupPath = Paths.get(config.getBackupDirectory(), backupToDelete);
            
            try {
                deleteDirectoryRecursively(backupPath);
                deletedCount++;
            } catch (IOException e) {
                System.err.println("Failed to delete backup " + backupToDelete + ": " + e.getMessage());
            }
        }
        
        return deletedCount;
    }
    
    /**
     * Restore from backup
     */
    public void restoreFromBackup(String backupName) throws IOException {
        Path backupPath = Paths.get(config.getBackupDirectory(), backupName);
        
        if (!Files.exists(backupPath)) {
            throw new IOException("Backup not found: " + backupName);
        }
        
        // This would restore data from backup
        // For now, just validate the backup exists and is readable
        Path manifestPath = backupPath.resolve("backup_manifest.txt");
        if (!Files.exists(manifestPath)) {
            throw new IOException("Invalid backup: missing manifest file");
        }
        
        System.out.println("Backup validation successful for: " + backupName);
    }
    
    /**
     * Get backup information
     */
    public BackupInfo getBackupInfo(String backupName) throws IOException {
        Path backupPath = Paths.get(config.getBackupDirectory(), backupName);
        
        if (!Files.exists(backupPath)) {
            throw new IOException("Backup not found: " + backupName);
        }
        
        long size = calculateDirectorySize(backupPath.toString());
        LocalDateTime createdDate = LocalDateTime.parse(
            backupName.replace("backup_", ""), 
            timestampFormatter
        );
        
        return new BackupInfo(backupName, createdDate, size);
    }
    
    // Private helper methods
    private void createBackupFiles(Path backupPath) throws IOException {
        // Create sample backup files
        Files.write(backupPath.resolve("students.csv"), 
            List.of("ID,RegNo,Name,Email,Status", 
                   "STU001,REG001,John Doe,john@example.com,ACTIVE"));
        
        Files.write(backupPath.resolve("courses.csv"), 
            List.of("Code,Title,Credits,Department", 
                   "CS101,Intro to CS,3,Computer Science"));
        
        Files.write(backupPath.resolve("enrollments.csv"), 
            List.of("StudentID,CourseCode,Date", 
                   "STU001,CS101,2024-01-15"));
        
        Files.write(backupPath.resolve("grades.csv"), 
            List.of("StudentID,CourseCode,Marks,Grade", 
                   "STU001,CS101,85,B"));
    }
    
    private void createBackupManifest(Path backupPath) throws IOException {
        List<String> manifestLines = List.of(
            "CCRM Backup Manifest",
            "Created: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            "Version: " + config.getVersion(),
            "Files:",
            "- students.csv",
            "- courses.csv", 
            "- enrollments.csv",
            "- grades.csv"
        );
        
        Files.write(backupPath.resolve("backup_manifest.txt"), manifestLines);
    }
    
    /**
     * Recursively delete directory and all contents
     */
    private void deleteDirectoryRecursively(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            return;
        }
        
        try (Stream<Path> paths = Files.walk(directory)) {
            paths.sorted((path1, path2) -> path2.compareTo(path1)) // Reverse order for deletion
                 .forEach(path -> {
                     try {
                         Files.delete(path);
                     } catch (IOException e) {
                         System.err.println("Could not delete " + path + ": " + e.getMessage());
                     }
                 });
        }
    }
    
    /**
     * Inner class for backup information
     */
    public static class BackupInfo {
        private final String name;
        private final LocalDateTime createdDate;
        private final long size;
        
        public BackupInfo(String name, LocalDateTime createdDate, long size) {
            this.name = name;
            this.createdDate = createdDate;
            this.size = size;
        }
        
        public String getName() { return name; }
        public LocalDateTime getCreatedDate() { return createdDate; }
        public long getSize() { return size; }
        
        @Override
        public String toString() {
            return String.format("BackupInfo{name='%s', created=%s, size=%d bytes}", 
                name, createdDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), size);
        }
    }
}