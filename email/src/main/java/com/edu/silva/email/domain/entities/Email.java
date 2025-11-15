package com.edu.silva.email.domain.entities;

import com.edu.silva.email.domain.enums.StatusEmail;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="email")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID userID;
    private String emailFrom;
    private String emailTo;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String content;
    private LocalDateTime sentDate;
    @Enumerated(EnumType.STRING)
    private StatusEmail status;
}
