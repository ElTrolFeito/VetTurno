package com.huellitas.vetturno.controller;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.service.PropietarioService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/propietarios")
public class PropietarioController {
 private final PropietarioService service;
 public PropietarioController(PropietarioService service) { this.service = service; }
 @PostMapping public ResponseEntity<PropietarioDTO> crear(@Valid @RequestBody PropietarioRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request)); }
 @GetMapping public List<PropietarioDTO> listar() { return service.listar(); }
}
