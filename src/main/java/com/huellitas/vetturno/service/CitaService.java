package com.huellitas.vetturno.service;
import com.huellitas.vetturno.dto.*;
import com.huellitas.vetturno.exception.ReglaNegocioException;
import com.huellitas.vetturno.model.*;
import com.huellitas.vetturno.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Service public class CitaService {
 private final CitaRepository citas; private final MascotaRepository mascotas; private final VeterinarioRepository veterinarios;
 public CitaService(CitaRepository citas, MascotaRepository mascotas, VeterinarioRepository veterinarios) { this.citas = citas; this.mascotas = mascotas; this.veterinarios = veterinarios; }
 @Transactional public CitaDTO crear(CitaRequest request) {
  if (!request.fechaHora().isAfter(LocalDateTime.now())) throw new ReglaNegocioException("La fecha de la cita debe estar en el futuro.");
  Mascota mascota = mascotas.findById(request.mascotaId()).orElseThrow(() -> new ReglaNegocioException("No existe la mascota indicada."));
  Veterinario veterinario = veterinarios.findById(request.veterinarioId()).orElseThrow(() -> new ReglaNegocioException("No existe el veterinario indicado."));
  if (citas.existsByVeterinarioIdAndFechaHora(veterinario.getId(), request.fechaHora())) throw new ReglaNegocioException("El veterinario ya tiene una cita en ese horario.");
  return toDto(citas.save(new Cita(request.fechaHora(), request.motivo().trim(), mascota, veterinario)));
 }
 @Transactional(readOnly = true) public List<CitaDTO> listar() { return citas.findAllByOrderByFechaHoraAsc().stream().map(CitaService::toDto).toList(); }
 @Transactional(readOnly = true) public List<CitaDTO> porVeterinario(Long id) {
  if (!veterinarios.existsById(id)) throw new ReglaNegocioException("No existe el veterinario indicado.");
  return citas.findAllByVeterinarioIdOrderByFechaHoraAsc(id).stream().map(CitaService::toDto).toList();
 }
 private static CitaDTO toDto(Cita c) { return new CitaDTO(c.getId(), c.getFechaHora(), c.getMotivo(), c.getMascota().getNombre(), c.getMascota().getPropietario().getNombre(), c.getVeterinario().getNombre()); }
}
