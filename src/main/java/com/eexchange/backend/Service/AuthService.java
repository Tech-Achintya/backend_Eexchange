package com.eexchange.backend.Service;

import com.eexchange.backend.DTO.LoginRequest;
import com.eexchange.backend.DTO.SignupRequest;
import com.eexchange.backend.Entity.User;
import com.eexchange.backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AuthService {
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserRepository userRepository;
    @Autowired
    public AuthService(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    public User signup(SignupRequest signupRequest){
        Optional<User> existing = userRepository.findByEmail(signupRequest.getEmail());
        if(existing.isPresent()){
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        return userRepository.save(user);
    }
    public User login(LoginRequest loginRequest){
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        if(!passwordEncoder.matches(loginRequest.getPassword(),user.getPassword())){
            throw new RuntimeException("Invalid User");
        }
        user.setPassword(null);
        return user;
    }

    public User updateUser(String email, String newName) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(newName);
        return userRepository.save(user);
    }
}
