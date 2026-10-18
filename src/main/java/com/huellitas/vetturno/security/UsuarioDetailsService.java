package com.huellitas.vetturno.security;
import com.huellitas.vetturno.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
@Service public class UsuarioDetailsService implements UserDetailsService {
 private final UsuarioRepository repository;
 public UsuarioDetailsService(UsuarioRepository repository) { this.repository = repository; }
 @Override public UserDetails loadUserByUsername(String email) {
  var usuario = repository.findByEmailIgnoreCase(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
  return User.withUsername(usuario.getEmail()).password(usuario.getPassword()).roles(usuario.getRol().name()).build();
 }
}
