package org.example.expert.domain.log.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ActionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String actionMethod;

    private Boolean success;

    private String request_uri;

    private Long actorUserId;

    private String errorMessage;

    @Builder
    public ActionLog(LocalDateTime timestamp, String actionMethod, String request_uri, Boolean success, Long initiatorId, String errorMessage) {
        this.timestamp = timestamp;
        this.actionMethod = actionMethod;
        this.request_uri = request_uri;
        this.success = success;
        this.actorUserId = initiatorId;
        this.errorMessage = errorMessage;
    }
}
