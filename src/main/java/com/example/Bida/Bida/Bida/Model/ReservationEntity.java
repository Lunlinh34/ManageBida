package com.example.Bida.Bida.Bida.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

import javax.validation.constraints.AssertTrue;

import com.example.Bida.Bida.Bida.Enum.StatusOrder;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_id", nullable = false)
    private TableEntity table;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusOrder status = StatusOrder.PENDING;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    private LocalDateTime createdAt;


    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private TransactionEntity transaction;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @AssertTrue(message = "Thời gian đặt bàn phải sau thời điểm hiện tại")
    private boolean isAppointmentTimeValid() {
        return appointmentTime != null && appointmentTime.isAfter(LocalDateTime.now());
    }

}
