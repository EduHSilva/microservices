package com.edu.silva.crm.domain.entities;

import com.edu.silva.crm.domain.enums.ServicePriority;
import com.edu.silva.crm.domain.enums.ServiceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "services")
@SQLDelete(sql = "UPDATE services SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
public class Service implements Serializable {
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

    @ManyToOne
    private Client client;
    @OneToOne
    private Budget budget;
}
