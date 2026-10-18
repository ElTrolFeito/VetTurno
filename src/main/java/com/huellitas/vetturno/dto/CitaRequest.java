package com.huellitas.vetturno.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record CitaRequest(@NotNull @Future LocalDateTime fechaHora,
                          @NotBlank @Size(max = 500) String motivo,
                          @NotNull Long mascotaId,
                          @NotNull Long veterinarioId) { }
