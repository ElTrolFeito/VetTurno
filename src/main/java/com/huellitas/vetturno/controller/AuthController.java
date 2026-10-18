package com.huellitas.vetturno.controller;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth")
public class AuthController {
 private final AuthService service;
 public AuthController(AuthService service) { this.service = service; }
 @PostMapping("/register") public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) { return ResponseEntity.ok(service.registrar(request)); }
 @PostMapping("/login") public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) { return ResponseEntity.ok(service.login(request)); }
}
