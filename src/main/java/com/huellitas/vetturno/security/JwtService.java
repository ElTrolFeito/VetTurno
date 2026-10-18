package com.huellitas.vetturno.security;
import com.huellitas.vetturno.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
@Service public class JwtService {
 private final SecretKey key; private final long expirationMs;
 public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-ms}") long expirationMs) {
  try { key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)); }
  catch (RuntimeException ex) { throw new IllegalStateException("JWT_SECRET debe ser Base64 válido de al menos 32 bytes."); }
  this.expirationMs = expirationMs;
 }
 public String generar(Usuario usuario) { Date ahora = new Date(); return Jwts.builder().subject(usuario.getEmail()).claim("rol", usuario.getRol().name()).issuedAt(ahora).expiration(new Date(ahora.getTime() + expirationMs)).signWith(key).compact(); }
 public String extraerEmail(String token) { return claims(token).getSubject(); }
 public boolean esValido(String token) { try { claims(token); return true; } catch (JwtException | IllegalArgumentException ex) { return false; } }
 public long getExpirationMs() { return expirationMs; }
 private Claims claims(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
}
