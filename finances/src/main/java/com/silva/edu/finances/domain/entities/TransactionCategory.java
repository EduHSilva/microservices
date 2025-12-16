package com.silva.edu.finances.domain.entities;

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
@Table(name = "transaction_category")
@SQLDelete(sql = "UPDATE transaction_category SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
public class TransactionCategory implements Serializable {
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

}
