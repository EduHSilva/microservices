package com.edu.silva.crm.domain.entities;

import com.edu.silva.crm.domain.enums.ServicePriority;
import com.edu.silva.crm.domain.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "maintenances")
@SQLDelete(sql = "UPDATE services SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
@FilterDef(
        name = "userFilter",
        parameters = @ParamDef(name = "userId", type = UUID.class)
)
@Filter(
        name = "userFilter",
        condition = "user_id = :userId"
)
public class Maintenance implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    private String title;
    @Column(length = 1000)
    private String description;
    private Double value;
    private Date initialDate;
    private Date finalDate;
    @Enumerated(EnumType.STRING)
    private ServicePriority priority;
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
    @Column(length = 1000)
    private String observations;

    @Column(name = "user_id")
    private UUID userId;
    @OneToOne
    private Budget budget;
}
