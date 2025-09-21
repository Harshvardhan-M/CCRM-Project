package edu.ccrm.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;

/**
 * Singleton configuration class for CCRM application
 * Demonstrates Singleton design pattern with thread safety
 */
public final class AppConfig {
    
    // Singleton instance with volatile for thread safety
    private static volatile AppConfig instance;
    
    // Configuration properties
    private final Properties properties;
    private final String applicationName;
    private final String version;
    private final LocalDateTime startupTime;
    private final String dataDirectory;
    private final String backupDirectory;
    private final String exportDirectory;
    
    /**
     * Private constructor to prevent external instantiation
     */
    private AppConfig() {
        this.properties = new Properties();
        this.startupTime = LocalDateTime.now();
        
        // Load configuration from properties file
        loadDefaultConfiguration();
        
        this.applicationName = getProperty("app.name", "Campus Course & Records Manager");
        this.version = getProperty("app.version", "1.0.0");
        this.dataDirectory = getProperty("app.data.directory", "data");
        this.backupDirectory = getProperty("app.backup.directory", "backups");
        this.exportDirectory = getProperty("app.export.directory", "exports");
        
        // Create necessary directories
        createDirectories();
    }
    
    /**
     * Thread-safe singleton instance retrieval using double-checked locking
     */
    public static AppConfig getInstance() {
        if (instance == null) {
            synchronized (AppConfig.class) {
                if (instance == null) {
                    instance = new AppConfig();
                }
            }
        }
        return instance;
    }
    
    /**
     * Load default configuration settings
     */
    private void loadDefaultConfiguration() {
        // Try to load from external properties file first
        try (InputStream inputStream = getClass().getResourceAsStream("/config.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            System.out.println("Could not load external configuration, using defaults.");
        }
        
        // Set default values if not loaded from file
        setDefaultProperty("app.name", "Campus Course & Records Manager");
        setDefaultProperty("app.version", "1.0.0");
        setDefaultProperty("app.data.directory", "data");
        setDefaultProperty("app.backup.directory", "backups");
        setDefaultProperty("app.export.directory", "exports");
        setDefaultProperty("app.max.credits.per.semester", "18");
        setDefaultProperty("app.backup.retention.days", "30");
        setDefaultProperty("app.auto.backup.enabled", "true");
    }
    
    private void setDefaultProperty(String key, String defaultValue) {
        if (!properties.containsKey(key)) {
            properties.setProperty(key, defaultValue);
        }
    }
    
    private void createDirectories() {
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(dataDirectory));
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(backupDirectory));
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(exportDirectory));
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get("test-data"));
        } catch (IOException e) {
            System.err.println("Warning: Could not create directories - " + e.getMessage());
        }
    }
    
    // Getter methods
    public String getApplicationName() {
        return applicationName;
    }
    
    public String getVersion() {
        return version;
    }
    
    public LocalDateTime getStartupTime() {
        return startupTime;
    }
    
    public String getDataDirectory() {
        return dataDirectory;
    }
    
    public String getBackupDirectory() {
        return backupDirectory;
    }
    
    public String getExportDirectory() {
        return exportDirectory;
    }
    
    public int getMaxCreditsPerSemester() {
        return Integer.parseInt(getProperty("app.max.credits.per.semester", "18"));
    }
    
    public int getBackupRetentionDays() {
        return Integer.parseInt(getProperty("app.backup.retention.days", "30"));
    }
    
    public boolean isAutoBackupEnabled() {
        return Boolean.parseBoolean(getProperty("app.auto.backup.enabled", "true"));
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }
    
    /**
     * Get application info string for display
     */
    public String getApplicationInfo() {
        return String.format("%s v%s - Started: %s", 
            applicationName, 
            version,
            startupTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
    
    /**
     * Get formatted uptime
     */
    public String getUptime() {
        java.time.Duration uptime = java.time.Duration.between(startupTime, LocalDateTime.now());
        long hours = uptime.toHours();
        long minutes = uptime.toMinutesPart();
        long seconds = uptime.toSecondsPart();
        
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
    
    /**
     * Prevent cloning of singleton
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Cannot clone singleton instance");
    }
    
    @Override
    public String toString() {
        return String.format("AppConfig{name='%s', version='%s', uptime='%s'}", 
            applicationName, version, getUptime());
    }
}