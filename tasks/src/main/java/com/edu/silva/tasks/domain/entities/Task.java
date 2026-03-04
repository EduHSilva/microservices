package com.edu.silva.tasks.domain.entities;

import com.edu.silva.tasks.domain.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@SQLDelete(sql = "UPDATE tasks SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
@Filter(
        name = "userFilter",
        condition = "user_id = :userId"
)
@FilterDef(
        name = "userFilter",
        parameters = @ParamDef(name = "userId", type = UUID.class)
)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    private String title;
    private Date dueDate;
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "user_id")
    private UUID userId;
}
