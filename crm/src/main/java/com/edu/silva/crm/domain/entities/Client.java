package com.edu.silva.crm.domain.entities;


import com.edu.silva.crm.domain.enums.ClientStatus;
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
@Table(name = "clients")
@SQLDelete(sql = "UPDATE clients SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    private String name;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private ClientStatus status;
    @Column(length = 1000)
    private String observations;
    @OneToMany(mappedBy = "client")
    private List<Service> services;

    public Client(String username, String email, String password, String observations) {
        this.name = username;
        this.email = email;
        this.phone = password;
        this.observations = observations;
    }
}
