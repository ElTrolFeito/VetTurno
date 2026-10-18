package com.huellitas.vetturno.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PropietarioRequest(@NotBlank @Size(max = 120) String nombre,
                                 @NotBlank @Size(max = 30) String telefono,
                                 @NotBlank @Email @Size(max = 160) String email) { }
