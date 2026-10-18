package com.huellitas.vetturno.controller;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.service.VeterinarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/veterinarios")
public class VeterinarioController {
 private final VeterinarioService service;
 public VeterinarioController(VeterinarioService service) { this.service = service; }
 @PostMapping public ResponseEntity<VeterinarioDTO> crear(@Valid @RequestBody VeterinarioRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
 @GetMapping public List<VeterinarioDTO> listar() { return service.listar(); }
}
