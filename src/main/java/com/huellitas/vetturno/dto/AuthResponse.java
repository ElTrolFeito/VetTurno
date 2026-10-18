package com.huellitas.vetturno.dto;

public record AuthResponse(String token, String tipo, long expiraEnMs) { }
