package com.silva.edu.finances.domain.entities;

import com.silva.edu.finances.domain.enums.CategoryClassification;
import com.silva.edu.finances.domain.enums.CategoryStatus;
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
@Table(name = "category")
@SQLDelete(sql = "UPDATE category SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    private String title;
    @Enumerated(EnumType.STRING)
    private CategoryStatus status;
    private String color;
    private int goal;
    @Enumerated(EnumType.STRING)
    private CategoryClassification classification;
}
