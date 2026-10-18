package com.huellitas.vetturno.service;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.model.Veterinario;
import com.huellitas.vetturno.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class VeterinarioService {
 private final VeterinarioRepository repository;
 public VeterinarioService(VeterinarioRepository repository) { this.repository = repository; }
 @Transactional public VeterinarioDTO crear(VeterinarioRequest request) { return toDto(repository.save(new Veterinario(request.nombre().trim(), request.especialidad().trim()))); }
 @Transactional(readOnly = true) public List<VeterinarioDTO> listar() { return repository.findAll().stream().map(VeterinarioService::toDto).toList(); }
 private static VeterinarioDTO toDto(Veterinario v) { return new VeterinarioDTO(v.getId(), v.getNombre(), v.getEspecialidad()); }
}
