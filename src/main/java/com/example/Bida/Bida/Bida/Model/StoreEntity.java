package com.example.Bida.Bida.Bida.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "store")
public class StoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(100)", unique = true)
    private String name;

    @Column(name = "address", nullable = false, columnDefinition = "nvarchar(255)")
    private String address;

    @Column(name = "phone", nullable = false, columnDefinition = "nvarchar(10)")
    private String phone;

    @Column(name = "email", nullable = false, columnDefinition = "nvarchar(100)")
    private String email;

    @Column(name = "image", nullable = false)
    private String image;

    @OneToMany(mappedBy = "store")
    private List<TableEntity> tables = new ArrayList<>();

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
