# Evidencia de pruebas manuales

Ejecución local el 17 de octubre de 2026 en Windows, Java 25 instalado (el proyecto compila para Java 17), Spring Boot 3.5.16 y MySQL 8.0.37. La base `vetturno` se creó al iniciar la aplicación. No se incluyen credenciales, contraseñas ni tokens.

| # | Caso | Estado observado | Resultado |
| --- | --- | --- | --- |
| 1 | Aplicación conectada a MySQL | Aplicación inició en 8080 | Aprobado |
| 2 | Registro válido | 200, respuesta con JWT; login posterior 200 | Aprobado; BCrypt está configurado en el servicio |
| 3 | Email inválido y contraseña corta | 400 | Aprobado |
| 4 | Login válido | 200 | Aprobado |
| 5 | GET agenda sin JWT | 401 | Aprobado |
| 6 | USER intenta registrar veterinario | 403 | Aprobado |
| 7 | ADMIN registra veterinario | 201 | Aprobado |
| 8 | Crear propietario | 201, DTO con id y campos públicos | Aprobado |
| 9 | Mascota asociada al propietario | 201, devuelve propietarioId y propietarioNombre | Aprobado |
| 10 | Mascota con propietario inexistente | 400, sin guardar | Aprobado |
| 11 | Cita futura válida | 201; DTO muestra mascota, responsable y veterinario | Aprobado |
| 12 | Cita pasada | 400 | Aprobado |
| 13 | Cita duplicada por veterinario y fecha/hora | 400 | Aprobado |
| 14 | Filtro por veterinario | 200; devolvió una cita | Aprobado |
| 15 | Reinicio y persistencia | Reinicio correcto; login ADMIN 200; agenda conservó una cita; OpenAPI 200 | Aprobado |

Comprobación de almacenamiento de contraseñas: `SELECT COUNT(*) FROM usuarios WHERE password LIKE '$2%';` devolvió 3. Esto confirma que las tres contraseñas guardadas tienen prefijo BCrypt; no se exportaron ni publicaron sus hashes.

Para repetir, inicia sesión como USER/ADMIN, ejecuta las rutas en el orden descrito en el README y prueba las mismas IDs de los registros recién creados. La contraseña nunca aparece en DTO de usuario; la aplicación codifica con `BCryptPasswordEncoder`. Guarda capturas de Swagger y Workbench durante la defensa.
