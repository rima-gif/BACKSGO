package com.rima.ryma_prj.infrastructure.controller;

import com.rima.ryma_prj.application.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username,
                         @RequestParam String email,
                         @RequestParam String password) {
        return authService.signup(username, email, password);
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> signin(@RequestParam String email,
                                                      @RequestParam String password) {
        Map<String, Object> result = authService.signin(email, password);

        if (result.containsKey("error")) {
            return ResponseEntity.status(401).body(result);
        }

        return ResponseEntity.ok(result);
    }


    //@PostMapping("/forgot-password")
    //public void forgotPassword(@RequestParam String email) {
      //  authService.forgotPassword(email);
    //}
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        String response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }





    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword
    ) {
        String response = authService.resetPasswordWithToken(token, newPassword, confirmPassword);
        return ResponseEntity.ok(response);
    }


}

