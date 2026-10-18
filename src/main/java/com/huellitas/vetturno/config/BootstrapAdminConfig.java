package com.huellitas.vetturno.config;
import com.huellitas.vetturno.model.Rol;
import com.huellitas.vetturno.model.Usuario;
import com.huellitas.vetturno.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration public class BootstrapAdminConfig {
 @Bean ApplicationRunner bootstrapAdmin(UsuarioRepository usuarios, PasswordEncoder encoder,
     @Value("${app.bootstrap-admin.enabled:false}") boolean enabled,
     @Value("${app.bootstrap-admin.email:}") String email,
     @Value("${app.bootstrap-admin.password:}") String password) {
  return args -> {
   if (!enabled) return;
   if (email.isBlank() || password.length() < 12) throw new IllegalStateException("Para habilitar el ADMIN inicial, configura un email y una contraseña de al menos 12 caracteres.");
   if (usuarios.existsByEmailIgnoreCase(email)) return;
   usuarios.save(new Usuario(email.trim().toLowerCase(), encoder.encode(password), Rol.ADMIN));
   System.out.println("Cuenta ADMIN inicial creada. Inicia sesión y luego desactiva el bootstrap.");
  };
 }
}
