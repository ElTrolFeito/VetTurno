# VetTurno

API REST para organizar la agenda de Veterinaria Huellitas. VetTurno permite a recepción registrar responsables y mascotas, consultar veterinarios y agendar citas con validación de disponibilidad. Doña Marta administra el alta de veterinarios.

## Alcance

Incluye autenticación JWT con roles `USER` y `ADMIN`, propietarios, mascotas, veterinarios, citas, persistencia MySQL, validaciones, errores consistentes y Swagger UI. No incluye interfaz web, historias clínicas, pagos, notificaciones, despliegue en la nube ni Docker (actividad extra del taller).

## Tecnologías

- Java 17 y Spring Boot 3.5.16
- Maven, Spring Web, Spring Data JPA/Hibernate, MySQL Connector/J
- Spring Security, BCrypt, JWT (JJWT)
- Jakarta Bean Validation y springdoc OpenAPI/Swagger UI

## Estructura

El código vive bajo `com.huellitas.vetturno`, dentro de `src/main/java`, siguiendo el layout Maven que IntelliJ IDEA reconoce automáticamente.

| Paquete | Responsabilidad |
| --- | --- |
| `model` | Entidades JPA y rol de cuenta |
| `repository` | Interfaces JpaRepository y consultas derivadas |
| `service` | Reglas del negocio y resolución de relaciones |
| `controller` | Endpoints HTTP y estados de respuesta |
| `dto` | Contratos de entrada y salida; evita exponer entidades |
| `security` | JWT, filtro, autenticación y autorización |
| `config` | OpenAPI y alta inicial controlada de ADMIN |
| `exception` | Errores de API y traducción global |

Las mascotas apuntan a su propietario y las citas apuntan a mascota y veterinario. Las relaciones se mantienen unidireccionales en las entidades para evitar ciclos JSON; los DTO devuelven nombres y referencias breves. Las citas se ordenan por fecha. Una restricción única en MySQL refuerza la regla de un veterinario por horario, además de la validación del servicio.

## Requisitos

- JDK 17 o superior y Maven 3.6.3 o superior.
- MySQL local.

## Cómo ejecutar

1. Crea la base vacía `vetturno` en MySQL, o deja que el URL local la cree si tu usuario tiene permisos.
2. Copia `src/main/resources/application-local.example.properties` como `src/main/resources/application-local.properties`. Configura usuario, contraseña y una clave JWT Base64 aleatoria de al menos 32 bytes. El archivo local está excluido de Git.
3. Inicia la API desde la raíz:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   En macOS/Linux: `./mvnw spring-boot:run`. También puedes importar `pom.xml` en IntelliJ IDEA y ejecutar `VetTurnoApplication`.

Hibernate usa `ddl-auto=update` para este taller. En producción conviene sustituirlo por migraciones versionadas. No publiques `application-local.properties`, claves, tokens ni contraseñas.

## Crear la primera cuenta ADMIN

El registro público siempre asigna `USER`; ignora cualquier rol que se intente enviar. Para habilitar el primer ADMIN de forma controlada, configura temporalmente en `application-local.properties` `app.bootstrap-admin.enabled=true`, un email y una contraseña de al menos 12 caracteres. Al iniciar se crea esa cuenta solo si el email aún no existe. Inicia sesión, comprueba el acceso y vuelve a poner la opción en `false`. No uses una contraseña de ejemplo en un entorno compartido.

## Flujo y endpoints

Swagger UI está disponible en `http://localhost:8080/swagger-ui.html`; OpenAPI JSON en `/v3/api-docs`. Usa **Authorize** con el JWT recibido en registro/login para probar rutas protegidas.

| Método | Ruta | Acceso | Resultado |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | Público | Registro USER y JWT, 200 |
| POST | `/api/auth/login` | Público | Verifica credenciales y entrega JWT, 200 |
| POST / GET | `/api/propietarios` | USER / ADMIN | Crear (201) / listar (200) |
| POST / GET | `/api/mascotas` | USER / ADMIN | Crear (201) / listar (200) |
| POST | `/api/veterinarios` | ADMIN | Crear (201); USER recibe 403 |
| GET | `/api/veterinarios` | USER / ADMIN | Listar (200) |
| POST / GET | `/api/citas` | USER / ADMIN | Agendar (201) / listar (200) |
| GET | `/api/citas/veterinario/{id}` | USER / ADMIN | Filtrar agenda (200) |

Orden sugerido: registrar/iniciar sesión, crear propietario, crear mascota con `propietarioId`, crear veterinario como ADMIN y crear cita con `mascotaId`, `veterinarioId`, `fechaHora` futura y motivo. Un horario duplicado para el mismo veterinario, una referencia inexistente o una fecha pasada responde 400. El JSON de cita incluye los nombres de la mascota, su propietario y el veterinario, sin anidar entidades.

## Matriz de pruebas manuales

El resultado se debe completar después de ejecutar el escenario en la versión que se entrega; esta tabla no afirma pruebas todavía no realizadas.

| # | Escenario | Esperado | Resultado real / evidencia |
| --- | --- | --- | --- |
| 1 | Inicio con MySQL disponible | Servidor activo y esquema accesible | OK; MySQL 8.0.37, servidor en 8080 |
| 2 | Registro válido de Paula | 200, token y contraseña BCrypt | OK; 200 y token; BCrypt configurado |
| 3 | Registro con email inválido y clave corta | 400 con errores de campo | OK; 400 |
| 4 | Login válido | 200 y JWT vigente | OK; 200 |
| 5 | GET `/api/citas` sin token | 401 | OK; 401 |
| 6 | POST veterinario con USER | 403 | OK; 403 |
| 7 | POST veterinario con ADMIN | 201 y persistencia | OK; 201 |
| 8 | Crear propietario válido | 201 y DTO plano | OK; 201 |
| 9 | Crear mascota con propietario existente | 201 y relación correcta | OK; 201 |
| 10 | Mascota con propietario inexistente | 400; no se inserta | OK; 400 |
| 11 | Cita futura con referencias válidas | 201 y persistencia | OK; 201 |
| 12 | Cita con fecha pasada | 400 | OK; 400 |
| 13 | Mismo veterinario y horario repetidos | 400; queda una cita | OK; 400 |
| 14 | Filtrar citas por veterinario | 200, solo coincidencias | OK; 200, una coincidencia |
| 15 | Reinicio y consulta protegida en Swagger | Datos persistentes y flujo válido | OK; reinicio, login ADMIN 200, agenda con una cita; OpenAPI 200 |

El detalle reproducible de esta ejecución está en [`docs/evidencias/pruebas-manuales.md`](docs/evidencias/pruebas-manuales.md). Una consulta a MySQL confirmó que las tres cuentas registradas tienen hash BCrypt; no se copiaron hashes ni contraseñas al repositorio. Conserva una captura de Workbench para la defensa.

## Errores

Las entradas inválidas y reglas de negocio producen 400 con `status`, `mensaje`, `errores` y `timestamp`. La falta de autenticación produce 401; un rol insuficiente, 403. Los fallos inesperados responden 500 con un mensaje genérico, sin trazas internas.

## Nota de uso de IA

Se usó asistencia de IA para revisar la estructura, proponer la separación por capas y diagnosticar la compilación. Las decisiones principales se reflejan arriba: los servicios resuelven IDs y aplican reglas, los controllers expresan estados HTTP y los DTO limitan la forma pública de los datos. Completa la matriz con resultados observados y conserva evidencia propia antes de entregar.
