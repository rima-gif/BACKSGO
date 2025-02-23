package com.rima.ryma_prj.application.service;

import com.rima.ryma_prj.domain.model.Role;
import com.rima.ryma_prj.domain.model.User;
import com.rima.ryma_prj.domain.repository.UserRepository;
import com.rima.ryma_prj.infrastructure.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    private JavaMailSender mailSender;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String signup(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already in use");
        }

        // Utilisation de la méthode createNewUser si elle est préférable
        User user = User.createNewUser(
                username,
                email,
                passwordEncoder.encode(password), // Encodage du mot de passe
                Collections.singleton(Role.ROLE_USER)
        );

        userRepository.save(user);
        return "User registered successfully!"; // Message de confirmation

    }

    public Map<String, Object> signin(String email, String password) {
        Map<String, Object> response = new HashMap<>();

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            response.put("error", "Email or password is incorrect");
            return response;
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            response.put("error", "Email or password is incorrect");
            return response;
        }

        String token = jwtUtil.generateToken(user);

        response.put("token", token);
        response.put("id", user.getId());
        response.put("name", user.getUsername());
        response.put("role", user.getRole());

        return response;
    }


 // public void forgotPassword(String email) {
   //     User user = userRepository.findByEmail(email)
     //          .orElseThrow(() -> new RuntimeException("User not found"));

       // String resetToken = jwtUtil.generatePasswordResetToken(email);
         //TODO: Envoyer le token par email à l'utilisateur
       //System.out.println("Reset token for " + email + ": " + resetToken);
    //}

    public String forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return "Email non enregistré"; // Si l'email n'existe pas
        }

        User user = userOptional.get();
        String resetToken = jwtUtil.generatePasswordResetToken(email); // Générer un token JWT
        String resetLink = "http://localhost:8080/auth/reset-password?token=" + resetToken;

       sendResetPasswordEmail(email,resetLink);

        return "Email trouvé, lien envoyé";
    }
    private  void sendResetPasswordEmail(String toEmail,String resetLink){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Réinitialisation de votre mot de passe");
        message.setText("Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink);
        mailSender.send(message);
    }

    public String resetPasswordWithToken(String token, String newPassword, String confirmPassword) {
        String email = jwtUtil.validatePasswordResetToken(token);

        if (email == null) {
            return "Token invalide ou expiré";
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!newPassword.equals(confirmPassword)) {
            return "Les nouveaux mots de passe ne correspondent pas";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return "Mot de passe mis à jour avec succès!";
    }}