package com.huellitas.vetturno.controller;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/citas")
public class CitaController {
 private final CitaService service;
 public CitaController(CitaService service) { this.service = service; }
 @PostMapping public ResponseEntity<CitaDTO> crear(@Valid @RequestBody CitaRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
 @GetMapping public List<CitaDTO> listar() { return service.listar(); }
 @GetMapping("/veterinario/{id}") public List<CitaDTO> porVeterinario(@PathVariable Long id) { return service.porVeterinario(id); }
}
