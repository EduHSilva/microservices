package com.edu.silva.crm.domain.entities;

import com.edu.silva.crm.domain.enums.BudgetStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

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
@FilterDef(
        name = "userFilter",
        parameters = @ParamDef(name = "userId", type = UUID.class)
)
@Filter(
        name = "userFilter",
        condition = "user_id = :userId"
)
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
    @ManyToOne
    private Client client;
    @Column(name = "user_id")
    private UUID userId;
    @OneToOne(mappedBy = "budget")
    private Maintenance service;

    public Budget(String title, String description, String terms, String observations) {
        this.title = title;
        this.description = description;
        this.terms = terms;
        this.observations = observations;
    }

    @ManyToMany
    private List<Item> items;
}
