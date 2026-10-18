package com.huellitas.vetturno.controller;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.service.MascotaService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/mascotas")
public class MascotaController {
 private final MascotaService service;
 public MascotaController(MascotaService service) { this.service = service; }
 @PostMapping public ResponseEntity<MascotaDTO> crear(@Valid @RequestBody MascotaRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
 @GetMapping public List<MascotaDTO> listar() { return service.listar(); }
}
