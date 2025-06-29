package com.example.Bida.Bida.Bida.Repository;

import com.example.Bida.Bida.Bida.Enum.UserRole;
import com.example.Bida.Bida.Bida.Model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByRole(UserRole role);
}
