package com.edu.silva.users.domain.entities;

import com.edu.silva.common.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "companies")
@SQLDelete(sql = "UPDATE companies SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    private String name;
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "company")
    private List<User> users = new ArrayList<>();
    @OneToOne
    private User owner;
}
