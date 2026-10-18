package com.huellitas.vetturno.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MascotaRequest(@NotBlank @Size(max = 100) String nombre,
                             @NotBlank @Size(max = 60) String especie,
                             @Size(max = 80) String raza,
                             @NotNull Long propietarioId) { }
