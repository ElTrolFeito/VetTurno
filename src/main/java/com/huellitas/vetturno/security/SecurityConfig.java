package com.huellitas.vetturno.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huellitas.vetturno.exception.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.time.Instant;
import java.util.Map;
@Configuration @EnableWebSecurity @EnableMethodSecurity
public class SecurityConfig {
 @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
 @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception { return config.getAuthenticationManager(); }
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthFilter jwtFilter, ObjectMapper mapper) throws Exception {
  http.csrf(csrf -> csrf.disable()).sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(auth -> auth
    .requestMatchers("/api/auth/**", "/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
    .requestMatchers(HttpMethod.POST, "/api/veterinarios").hasRole("ADMIN")
    .anyRequest().authenticated())
   .exceptionHandling(errors -> errors
    .authenticationEntryPoint((request, response, ex) -> escribirError(response, mapper, 401, "Autenticación requerida."))
    .accessDeniedHandler((request, response, ex) -> escribirError(response, mapper, 403, "No tienes permiso para realizar esta operación.")))
   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
  return http.build();
 }
 private static void escribirError(HttpServletResponse response, ObjectMapper mapper, int status, String message) throws java.io.IOException {
  response.setStatus(status); response.setContentType("application/json"); response.setCharacterEncoding("UTF-8");
  mapper.writeValue(response.getOutputStream(), new ApiError(status, message, Map.of(), Instant.now()));
 }
}
