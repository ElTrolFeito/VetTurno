package com.huellitas.vetturno.service;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.*;
import com.huellitas.vetturno.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class MascotaService {
 private final MascotaRepository mascotas; private final PropietarioRepository propietarios;
 public MascotaService(MascotaRepository mascotas, PropietarioRepository propietarios) { this.mascotas = mascotas; this.propietarios = propietarios; }
 @Transactional public MascotaDTO crear(MascotaRequest request) {
  Propietario p = propietarios.findById(request.propietarioId()).orElseThrow(() -> new ReglaNegocioException("No existe el propietario indicado."));
  return toDto(mascotas.save(new Mascota(request.nombre().trim(), request.especie().trim(), normalizar(request.raza()), p)));
 }
 @Transactional(readOnly = true) public List<MascotaDTO> listar() { return mascotas.findAll().stream().map(MascotaService::toDto).toList(); }
 private static String normalizar(String v) { return v == null || v.isBlank() ? null : v.trim(); }
 private static MascotaDTO toDto(Mascota m) { return new MascotaDTO(m.getId(), m.getNombre(), m.getEspecie(), m.getRaza(), m.getPropietario().getId(), m.getPropietario().getNombre()); }
}
