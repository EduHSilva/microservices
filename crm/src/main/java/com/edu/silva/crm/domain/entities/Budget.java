package com.edu.silva.crm.domain.entities;

import com.edu.silva.crm.domain.enums.BudgetStatus;
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
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "budgets")
@SQLDelete(sql = "UPDATE budgets SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
public class Budget implements Serializable {
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
    @Column(length = 1000)
    private String terms;
    @Column(length = 1000)
    private String observations;
    private BudgetStatus status;

    @ManyToMany
    private List<Item> items;
}
