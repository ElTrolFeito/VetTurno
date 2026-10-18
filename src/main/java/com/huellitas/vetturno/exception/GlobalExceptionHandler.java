package com.huellitas.vetturno.exception;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
@RestControllerAdvice public class GlobalExceptionHandler {
 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<ApiError> validacion(MethodArgumentNotValidException ex) {
  Map<String, String> errores = new TreeMap<>();
  ex.getBindingResult().getFieldErrors().forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));
  return respuesta(HttpStatus.BAD_REQUEST, "La solicitud contiene campos inválidos.", errores);
 }
 @ExceptionHandler(ReglaNegocioException.class)
 public ResponseEntity<ApiError> regla(ReglaNegocioException ex) { return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage(), Map.of()); }
 @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
 public ResponseEntity<ApiError> solicitudIlegible(Exception ex) { return respuesta(HttpStatus.BAD_REQUEST, "La solicitud tiene un formato o valor inválido.", Map.of()); }
 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<ApiError> integridad(DataIntegrityViolationException ex) { return respuesta(HttpStatus.BAD_REQUEST, "La operación contradice una regla de datos o un registro ya existente.", Map.of()); }
 @ExceptionHandler(Exception.class)
 public ResponseEntity<ApiError> inesperado(Exception ex) { return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error interno. Intenta nuevamente.", Map.of()); }
 private ResponseEntity<ApiError> respuesta(HttpStatus status, String mensaje, Map<String, String> errores) {
  return ResponseEntity.status(status).body(new ApiError(status.value(), mensaje, errores, Instant.now()));
 }
}
