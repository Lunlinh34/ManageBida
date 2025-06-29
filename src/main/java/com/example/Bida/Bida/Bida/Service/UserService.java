package com.example.Bida.Bida.Bida.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Bida.Bida.Bida.Enum.UserRole;
import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class UserService {

    private final UserRepository userRepository;
    @SuppressWarnings("unused")
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(String username, String password, String fullname, String email, String phoneNumber) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Username already taken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password); 
        user.setFullname(fullname);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);

        return userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<UserEntity> findUserByRoleMember() {
        return userRepository.findByRole(UserRole.MEMBER);
    }

    public Optional<UserEntity> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username).orElse(null);
    }
}
