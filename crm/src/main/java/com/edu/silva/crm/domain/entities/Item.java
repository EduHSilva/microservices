package com.edu.silva.crm.domain.entities;

import com.edu.silva.common.enums.Status;
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
@Table(name = "item")
@SQLDelete(sql = "UPDATE item SET status = 'DELETED' WHERE id=?")
@SQLRestriction("status <> 'DELETED'")
@FilterDef(
        name = "userFilter",
        parameters = @ParamDef(name = "userId", type = UUID.class)
)
@Filter(
        name = "userFilter",
        condition = "user_id = :userId"
)
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @CreationTimestamp
    private Date createdDate;
    @UpdateTimestamp
    private Date updatedDate;
    private String name;
    @Column(length = 1000)
    private String description;
    private Integer quantity;
    private Double priceUn;
    private Status status;
    @Column(name = "user_id")
    private UUID userId;
}
