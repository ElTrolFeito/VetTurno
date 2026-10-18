package com.huellitas.vetturno.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration public class OpenApiConfig {
 @Bean OpenAPI vetTurnoApi() {
  final String scheme = "bearerAuth";
  return new OpenAPI().info(new Info().title("VetTurno API").version("1.0.0")
   .description("API de agenda y citas de Veterinaria Huellitas. Registra responsables, mascotas, veterinarios y citas con control de acceso por roles."))
   .addSecurityItem(new SecurityRequirement().addList(scheme))
   .components(new io.swagger.v3.oas.models.Components().addSecuritySchemes(scheme,
    new SecurityScheme().name(scheme).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
 }
}
