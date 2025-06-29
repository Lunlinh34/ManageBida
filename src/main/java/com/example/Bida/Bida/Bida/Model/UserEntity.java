package com.example.Bida.Bida.Bida.Model;

import com.example.Bida.Bida.Bida.Enum.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role = UserRole.MEMBER;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private LocalDate birthDate;

    @Column(nullable = true, length = 255)
    private String address;

    @Column(nullable = false, unique = true, length = 15)
    private String phoneNumber;

    @Column(nullable = true, length = 500)
    private String avatar;

    @OneToMany(mappedBy = "user")
    private List<ReservationEntity> reservations = new ArrayList<>();
}
