package com.itmo.web.lab3;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity(name = "results")
@Table(name = "results")
@NamedQueries({
        @NamedQuery(name = "fetchAllResults", query = "select r from results r"),
        @NamedQuery(name = "dropAllResults", query = "delete from results r")
})
public class Result {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "x")
    private Double x;
    @Column(name = "y")
    private Double y;
    @Column(name = "r")
    private Double r;
    @Column(name = "hit")
    private Boolean hit;
    @Column(name = "execution_time")
    private Long executionTime; //in nanoseconds
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    public Result() {}

    public Result(Double x, Double y, Double r) {
        this.x = x;
        this.y = y;
        this.r = r;
    }

    public Result(
            Double x,
            Double y,
            Double r,
            Boolean hit,
            Long executionTime,
            LocalDateTime timestamp
    ) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.executionTime = executionTime;
        this.timestamp = timestamp;
    }

    public UUID getId() {
        return id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getR() {
        return r;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Boolean getHit() {
        return hit;
    }

    public void setHit(Boolean hit) {
        this.hit = hit;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getHumanReadableTimestamp() {
        String pattern = "hh:mm:ss dd.MM.yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return timestamp.format(formatter);
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", r=" + r +
                ", hit=" + hit +
                ", executionTime=" + executionTime +
                ", timestamp=" + timestamp +
                '}';
    }
}
