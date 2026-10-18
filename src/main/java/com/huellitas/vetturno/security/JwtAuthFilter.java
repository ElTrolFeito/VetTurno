package com.huellitas.vetturno.security;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
@Component public class JwtAuthFilter extends OncePerRequestFilter {
 private final JwtService jwtService; private final UsuarioDetailsService usuarioDetailsService;
 public JwtAuthFilter(JwtService jwtService, UsuarioDetailsService usuarioDetailsService) { this.jwtService = jwtService; this.usuarioDetailsService = usuarioDetailsService; }
 @Override protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
  String header = request.getHeader("Authorization");
  if (header != null && header.startsWith("Bearer ") && SecurityContextHolder.getContext().getAuthentication() == null) {
   String token = header.substring(7);
   if (jwtService.esValido(token)) try {
    UserDetails user = usuarioDetailsService.loadUserByUsername(jwtService.extraerEmail(token));
    var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); SecurityContextHolder.getContext().setAuthentication(auth);
   } catch (RuntimeException ignored) { SecurityContextHolder.clearContext(); }
  }
  chain.doFilter(request, response);
 }
}
