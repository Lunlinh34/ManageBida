package com.example.Bida.Bida.Bida.Model;

import com.example.Bida.Bida.Bida.Enum.CategoryTable;
import com.example.Bida.Bida.Bida.Enum.StatusTable;
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
@Table(name = "table_bia")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "nvarchar(100)", unique = true)
    private String name;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "is_vip", nullable = false, columnDefinition = "bit default 0")
    private boolean isVip;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusTable status = StatusTable.AVAILABLE;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_table", nullable = false)
    private CategoryTable categoryTable;

    @Column(name = "hourly_rate", nullable = false, columnDefinition = "decimal(10,2)")
    private double hourlyRate;

    @OneToMany(mappedBy = "table")
    private List<ReservationEntity> reservations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
