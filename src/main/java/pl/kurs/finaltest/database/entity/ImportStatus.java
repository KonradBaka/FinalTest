package pl.kurs.finaltest.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class ImportStatus implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private long recordsProcessed;

    public ImportStatus() {
    }

    public ImportStatus(Long id, LocalDateTime startTime, LocalDateTime endTime, String status, long recordsProcessed) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.recordsProcessed = recordsProcessed;
    }

    public void incrementRecordsProcessed(long count) {
        this.recordsProcessed = count;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getRecordsProcessed() {
        return recordsProcessed;
    }

    public void setRecordsProcessed(long recordsProcessed) {
        this.recordsProcessed = recordsProcessed;
    }
}
