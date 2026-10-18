package com.huellitas.vetturno.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VeterinarioRequest(@NotBlank @Size(max = 120) String nombre,
                                 @NotBlank @Size(max = 120) String especialidad) { }
