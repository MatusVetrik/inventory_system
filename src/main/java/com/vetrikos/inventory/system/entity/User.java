package com.vetrikos.inventory.system.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.EAGER)
    private List<Order> orders;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

}
