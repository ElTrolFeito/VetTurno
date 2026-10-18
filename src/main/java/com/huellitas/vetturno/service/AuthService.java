package com.huellitas.vetturno.service;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.*;
import com.huellitas.vetturno.repository.UsuarioRepository;
import com.huellitas.vetturno.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service public class AuthService {
 private final UsuarioRepository usuarios; private final PasswordEncoder encoder; private final AuthenticationManager authManager; private final JwtService jwtService;
 public AuthService(UsuarioRepository usuarios, PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwtService) { this.usuarios = usuarios; this.encoder = encoder; this.authManager = authManager; this.jwtService = jwtService; }
 @Transactional public AuthResponse registrar(RegistroRequest request) {
  String email = request.email().trim().toLowerCase();
  if (usuarios.existsByEmailIgnoreCase(email)) throw new ReglaNegocioException("Ya existe una cuenta con ese email.");
  Usuario user = usuarios.save(new Usuario(email, encoder.encode(request.password()), Rol.USER));
  return response(user);
 }
 public AuthResponse login(LoginRequest request) {
  try { authManager.authenticate(new UsernamePasswordAuthenticationToken(request.email().trim().toLowerCase(), request.password())); }
  catch (AuthenticationException ex) { throw new ReglaNegocioException("Credenciales inválidas."); }
  Usuario user = usuarios.findByEmailIgnoreCase(request.email().trim()).orElseThrow(() -> new ReglaNegocioException("Credenciales inválidas."));
  return response(user);
 }
 private AuthResponse response(Usuario user) { return new AuthResponse(jwtService.generar(user), "Bearer", jwtService.getExpirationMs()); }
}
