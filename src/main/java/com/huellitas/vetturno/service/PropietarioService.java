package com.huellitas.vetturno.service;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.Propietario;
import com.huellitas.vetturno.repository.PropietarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class PropietarioService {
 private final PropietarioRepository repository;
 public PropietarioService(PropietarioRepository repository) { this.repository = repository; }
 @Transactional public PropietarioDTO crear(PropietarioRequest request) {
  if (repository.existsByEmailIgnoreCase(request.email())) throw new ReglaNegocioException("Ya existe un propietario con ese email.");
  return toDto(repository.save(new Propietario(request.nombre().trim(), request.telefono().trim(), request.email().trim().toLowerCase())));
 }
 @Transactional(readOnly = true) public List<PropietarioDTO> listar() { return repository.findAll().stream().map(PropietarioService::toDto).toList(); }
 private static PropietarioDTO toDto(Propietario p) { return new PropietarioDTO(p.getId(), p.getNombre(), p.getTelefono(), p.getEmail()); }
}
