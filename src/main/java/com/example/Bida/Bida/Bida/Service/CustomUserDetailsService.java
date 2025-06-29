package com.example.Bida.Bida.Bida.Service;

import com.example.Bida.Bida.Bida.Model.UserEntity;
import com.example.Bida.Bida.Bida.Repository.UserRepository;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserRepository userRepository, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Đang tìm user: " + username);
        
        UserEntity user = userRepository.findByUsername(username)
            .orElseThrow(() -> {
                System.out.println("Không tìm thấy user: " + username);
                return new UsernameNotFoundException("User not found: " + username);
            });

        System.out.println("Tìm thấy user: " + user.getUsername());
        System.out.println("Pass: " + user.getPassword());

        String password = "a"; 
        String encodedPassword = passwordEncoder.encode(password);
        System.out.println("Encoded Password: " + encodedPassword);

        String rawPassword = "a";  
        encodedPassword = passwordEncoder.encode(rawPassword); 

        boolean isPasswordMatch = passwordEncoder.matches(rawPassword, encodedPassword);
        if (isPasswordMatch) {
            System.out.println("Mật khẩu đúng");
        } else {
            System.out.println("Mật khẩu sai");
        }


        return org.springframework.security.core.userdetails.User
            .withUsername(user.getUsername())
            .password(user.getPassword())
            .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            .build();
    }

}
